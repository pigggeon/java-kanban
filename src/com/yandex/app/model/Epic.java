package com.yandex.app.model;

import com.yandex.app.service.TaskStatus;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }

    public Epic(int id, String name, String description) {
        super(id, name, description, TaskStatus.NEW);
    }

    public void addSubtaskId(int id) {
        subtasksId.add(id);
    }

    public void deleteSubtaskId(Integer id) {
        subtasksId.remove(id);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void clearSubtasksIds() {
        subtasksId.clear();
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }



    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
