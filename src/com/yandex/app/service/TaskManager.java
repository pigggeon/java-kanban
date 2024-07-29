package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
public class TaskManager {
    private int generatorId = 0;
    private final HashMap<Integer, Task> allTasks = new HashMap<>();
    private final HashMap<Integer, Subtask> allSubtasks = new HashMap<>();
    private final HashMap<Integer, Epic> allEpics = new HashMap<>();

//получение списка всех задач

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(allEpics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(allSubtasks.values());
    }

//добавление новых задач
    public int addNewTask(Task task) {
        final int id = ++generatorId;
        task.setId(id);
        allTasks.put(id, task);
        return id;
    }

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

    public int addNewEpic(Epic epic) {
        final int id = ++generatorId;
        epic.setId(id);
        allEpics.put(id, epic);
        return id;
    }

//удаление всех задач
    public void deleteAllTasks() {
        allTasks.clear();
    }

    public void deleteAllSubtasks() {
        for (Epic epic : allEpics.values()) {
            epic.clearSubtasksIds();
            updateEpicStatus(epic);
        }
        allSubtasks.clear();
    }

    public void deleteAllEpics() {
        allEpics.clear();
        allSubtasks.clear();
    }

//получение задач по id
    public Task getTaskById(int id) {
        return allTasks.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return allSubtasks.get(id);
    }

    public Epic getEpicById(int id) {
        return allEpics.get(id);
    }

//обновление тасков
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
    public void deleteTaskById(int id) {
        Task task = allTasks.remove(id);
        if (task == null) {
            System.out.println("No such task: " + id);
        }
    }

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
    public ArrayList<Subtask> getAllEpicSubtasks(int epicId){
        Epic epic = allEpics.get(epicId);
        ArrayList<Integer> subsIds = new ArrayList<>(epic.getSubtasksId());
        ArrayList<Subtask> listOfEpicSubtask = new ArrayList<>();
        for (int subtaskId : subsIds) {
            listOfEpicSubtask.add(allSubtasks.get(subtaskId));
        }
        return listOfEpicSubtask;
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
