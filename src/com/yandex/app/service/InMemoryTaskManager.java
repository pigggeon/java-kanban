package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int generatorId = 0;
    private final Map<Integer, Task> allTasks = new HashMap<>();
    private final Map<Integer, Subtask> allSubtasks = new HashMap<>();
    private final Map<Integer, Epic> allEpics = new HashMap<>();

    InMemoryHistoryManager historyManager = Managers.getDefaultHistory();

//получение списка всех задач

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(allEpics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(allSubtasks.values());
    }

//добавление новых задач
    @Override
    public int addNewTask(Task task) {
        final int id = ++generatorId;
        task.setId(id);
        allTasks.put(id, task);
        return id;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        Epic epic = allEpics.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("No such epic: " + subtask.getEpicId());
            return -1;
        }
        final int id = ++generatorId;
        subtask.setId(id);
        epic.addSubtaskId(id);
        allSubtasks.put(id, subtask);
        updateEpicStatus(epic);
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id = ++generatorId;
        epic.setId(id);
        allEpics.put(id, epic);
        return id;
    }

//удаление всех задач
    @Override
    public void deleteAllTasks() {
        allTasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : allEpics.values()) {
            epic.clearSubtasksIds();
            updateEpicStatus(epic);
        }
        allSubtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        allEpics.clear();
        allSubtasks.clear();
    }

//получение задач по id
    @Override
    public Task getTaskById(int id) {
        historyManager.add(allTasks.get(id));
        return allTasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(allSubtasks.get(id));
        return allSubtasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(allEpics.get(id));
        return allEpics.get(id);
    }

//обновление тасков
    @Override
    public void updateTask(Task task) {
        int id  = task.getId();
        Task oldTask = allTasks.get(id);
        if (oldTask == null) {
            System.out.println("No such task: " + id);
            return;
        }
        oldTask.setName(task.getName());
        oldTask.setDescription(task.getDescription());
        oldTask.setStatus(task.getStatus());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id  = subtask.getId();
        Subtask oldSubtask = allSubtasks.get(id);
        if (oldSubtask == null) {
            System.out.println("No such subtask: " + id);
            return;
        }
        oldSubtask.setName(subtask.getName());
        oldSubtask.setDescription(subtask.getDescription());
        oldSubtask.setStatus(subtask.getStatus());
        updateEpicStatus(allEpics.get(subtask.getEpicId()));
    }

    @Override
    public void updateEpic(Epic epic) {
        int id  = epic.getId();
        Epic oldEpic = allEpics.get(id);
        if (oldEpic == null) {
            System.out.println("No such epic: " + id);
            return;
        }
        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
    }

//удаление по id
    @Override
    public void deleteTaskById(int id) {
        Task task = allTasks.remove(id);
        if (task == null) {
            System.out.println("No such task: " + id);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = allSubtasks.remove(id);
        if (subtask == null) {
            System.out.println("No such subtask: " + id);
            return;
        }
        int epicId = subtask.getEpicId();
        Epic epic = allEpics.get(epicId);
        epic.deleteSubtaskId(id);
        updateEpicStatus(epic);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = allEpics.remove(id);
        if (epic == null) {
            System.out.println("No such epic: " + id);
            return;
        }
        for (int subId : epic.getSubtasksId()) {
            allSubtasks.remove(subId);
        }
        epic.clearSubtasksIds();
    }

//получение списка всех подзадач эпика
    @Override
    public List<Subtask> getAllEpicSubtasks(int epicId){
        Epic epic = allEpics.get(epicId);
        ArrayList<Integer> subsIds = new ArrayList<>(epic.getSubtasksId());
        ArrayList<Subtask> listOfEpicSubtask = new ArrayList<>();
        for (int subtaskId : subsIds) {
            listOfEpicSubtask.add(allSubtasks.get(subtaskId));
        }
        return listOfEpicSubtask;
    }

    @Override
    public void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getAllEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //обновление статуса эпика
    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> subtasksIds = new ArrayList<>(epic.getSubtasksId());

        if (subtasksIds.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        int subsDone = 0;
        int subsNew = 0;
        for (Integer subtaskId: subtasksIds ) {
            Subtask sub = allSubtasks.get(subtaskId);
            if (sub.getStatus() == TaskStatus.IN_PROGRESS) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
            if (sub.getStatus() == TaskStatus.NEW) {
                subsNew++;
            } else {
                subsDone++;
            }
        }

        if (subsNew == 0) {
            epic.setStatus(TaskStatus.DONE);
        } else if (subsDone == 0) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
