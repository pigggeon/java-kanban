package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    protected List<Task> historyList = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    @Override
    public void add(Task task) {
        historyList.add(task);
        checkHistoryLength();
    }

    private void checkHistoryLength() {
        if (historyList.size() > 10) {
            historyList.removeFirst();
        }
    }
}
