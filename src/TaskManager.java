import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
public class TaskManager {
    private int generatorId = 0;
    private final HashMap<Integer, Task> allTasks = new HashMap<>();
    private final HashMap<Integer, Subtask> allSubtasks = new HashMap<>();
    private final HashMap<Integer, Epic> allEpics = new HashMap<>();

//получение списка всех задач

    protected ArrayList<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }

    protected ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(allEpics.values());
    }

    protected ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(allSubtasks.values());
    }

//добавление новых задач
    protected int addNewTask(Task task) {
        final int id = ++generatorId;
        task.setId(id);
        allTasks.put(id, task);
        return id;
    }

    protected int addNewSubtask(Subtask subtask, int epicId) {
        Epic epic = getEpicById(subtask.getEpicId());
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

    protected int addNewEpic(Epic epic) {
        final int id = ++generatorId;
        updateEpicStatus(epic);
        epic.setId(id);
        allEpics.put(id, epic);
        return id;
    }

//удаление всех задач
    protected void deleteAllTasks() {
        allTasks.clear();
    }

    protected void deleteAllSubtasks() {
        allSubtasks.clear();
    }

    protected void deleteAllEpics() {
        allEpics.clear();
    }

//получение задач по id
    protected Task getTaskById(int id) {
        return allTasks.get(id);
    }

    protected Subtask getSubtaskById(int id) {
        return allSubtasks.get(id);
    }

    protected Epic getEpicById(int id) {
        return allEpics.get(id);
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
        }
    }

//обновление тасков
    protected void updateTask(Task task) {
        int id  = task.getId();
        if (allTasks.get(id) == null) {
            System.out.println("No such task: " + id);
            return;
        }
        Task updatedTask = new Task(task.getName(), task.getDescription(), task.getStatus());
        allTasks.remove(id);
        updatedTask.setId(id);
        allTasks.put(id, updatedTask);
    }

    protected void updateSubtask(Subtask subtask) {
        int id  = subtask.getId();
        if (allSubtasks.get(id) == null) {
            System.out.println("No such subtask: " + id);
            return;
        }
        Subtask updatedSubtask = new Subtask(subtask.getName(), subtask.getDescription(), subtask.getStatus(), subtask.getEpicId());
        allSubtasks.remove(id);
        updatedSubtask.setId(id);
        allSubtasks.put(id, updatedSubtask);
        updateEpicStatus(allEpics.get(subtask.getEpicId()));
    }

    protected void updateEpic(Epic epic) {
        int id  = epic.getId();
        if (allEpics.get(id) == null) {
            System.out.println("No such epic: " + id);
            return;
        }
        Epic updatedEpic = new Epic(epic.getName(), epic.getDescription(), epic.getStatus());
        allEpics.remove(id);
        updatedEpic.setId(id);
        allEpics.put(id, updatedEpic);
        updateEpicStatus(epic);
    }

//удаление по id
    protected void deleteTaskById(int id) {
        if (allTasks.get(id) == null) {
            System.out.println("No such task: " + id);
            return;
        }
        allTasks.remove(id);
    }

    protected void deleteSubtaskById(int id) {
        if (allSubtasks.get(id) == null) {
            System.out.println("No such subtask: " + id);
            return;
        }
        int epicId = allSubtasks.get(id).getEpicId();
        Epic epic = allEpics.get(epicId);
        epic.deleteSubtaskId(id);
        allSubtasks.remove(id);
        updateEpicStatus(epic);
    }

    protected void deleteEpicById(int id) {
        Epic epic = allEpics.get(id);
        if (epic == null) {
            System.out.println("No such epic: " + id);
            return;
        }
        epic.clearSubtasksIds();
        allEpics.remove(id);
    }

//получение списка всех подзадач эпика
    protected ArrayList<Subtask> getAllEpicSubtasks(Epic epic){
        ArrayList<Integer> subsIds = new ArrayList<>(epic.getSubtasksId());
        ArrayList<Subtask> listOfEpicSubtask = new ArrayList<>();
        for (int subtaskId : subsIds) {
            listOfEpicSubtask.add(allSubtasks.get(subtaskId));
        }
        return listOfEpicSubtask;
    }
}
