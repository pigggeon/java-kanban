package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
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
