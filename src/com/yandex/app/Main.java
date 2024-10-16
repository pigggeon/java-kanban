package com.yandex.app;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;
import com.yandex.app.service.TaskStatus;
import com.yandex.app.service.InMemoryTaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("task 1", "description 1", TaskStatus.NEW);
        manager.addNewTask(task1);
        Task task2 = new Task("task 2", "description 2", TaskStatus.IN_PROGRESS);
        manager.addNewTask(task2);

        Epic epic1 = new Epic("epic 1", "description 1");
        manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("subtask 1", "description 1", TaskStatus.NEW, 3);
        manager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask 2", "description 2", TaskStatus.IN_PROGRESS, 3);
        manager.addNewSubtask(subtask2);


        Epic epic2 = new Epic("epic 2", "description 2");
        manager.addNewEpic(epic2);
        Subtask subtask3 = new Subtask("subtask 3", "description 3", TaskStatus.NEW, 6);
        manager.addNewSubtask(subtask3);

        System.out.println("(Добавление новых задач)");
        manager.printAllTasks(manager);

        task2 = new Task(2, "new task 2", "new description 2", TaskStatus.DONE);
        manager.updateTask(task2);

        subtask1 = new Subtask(4, "new subtask 1", "new description 1", TaskStatus.DONE, 3);
        subtask2 = new Subtask(5, "new subtask 2", "new description 2", TaskStatus.DONE, 3);
        subtask3 = new Subtask(7, "new subtask 3", "new description 3", TaskStatus.IN_PROGRESS, 6);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);
        manager.updateSubtask(subtask3);

        System.out.println("\n(Изменение статусов)");
        manager.printAllTasks(manager);

        manager.deleteTaskById(1);
        manager.deleteEpicById(3);

        System.out.println("\n(Удаление одной задачи и одного эпика)");
        manager.printAllTasks(manager);

        manager.getTaskById(2);
        manager.getSubtaskById(7);

        System.out.println("\n(Добавление истории)");
        manager.printAllTasks(manager);


    }
}
