package com.yandex.app.Tests;

import com.yandex.app.model.Task;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskTest {
    //Не могу понять какие тесты добавлять
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @Test
    void TasksAreEqualsById() {
        Task task = new Task("task 1", "description 1", TaskStatus.NEW);
        final int taskId = manager.addNewTask(task);

        final Task savedTask = manager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");
    }
}
