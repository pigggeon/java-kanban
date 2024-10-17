package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    //добавление новых задач
    int addNewTask(Task task);

    int addNewSubtask(Subtask subtask);

    int addNewEpic(Epic epic);

    //удаление всех задач
    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    //получение задач по id
    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    //обновление тасков
    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    //удаление по id
    void deleteTaskById(int id);

    void deleteSubtaskById(int id);

    void deleteEpicById(int id);

    void printAllTasks(TaskManager manager);

    List<Task> getHistory();

    //получение списка всех подзадач эпика
    List<Subtask> getAllEpicSubtasks(int epicId);
}
