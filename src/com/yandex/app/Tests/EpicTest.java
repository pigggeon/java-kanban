package com.yandex.app.Tests;

import com.yandex.app.model.Epic;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    //Не могу понять какие тесты добавлять
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @Test
    void EpicsAreEqualsById() {
        Epic epic = new Epic("epic 1", "description 1");
        final int epicId = manager.addNewEpic(epic);

        final Epic savedEpic = manager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Эпики не совпадают");
    }
}