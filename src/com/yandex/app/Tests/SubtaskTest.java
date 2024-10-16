package com.yandex.app.Tests;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubtaskTest {
    //Не могу понять какие тесты добавлять
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @Test
    void SubtasksAreEqualsById() {
        Epic epic = new Epic("epic 1", "description 1");
        manager.addNewEpic(epic);
        Subtask subtask = new Subtask("subtask 1", "description 1", TaskStatus.NEW, epic.getId());
        final int subtaskId = manager.addNewSubtask(subtask);

        final Subtask savedSubtask = manager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Подзадача не найдена");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают");
    }
}
