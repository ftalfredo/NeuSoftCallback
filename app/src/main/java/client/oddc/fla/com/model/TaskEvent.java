package client.oddc.fla.com.model;

/**
 * Created by yzharchuk on 8/9/2017.
 */

public class TaskEvent
{
    private String taskId;
    private int EventType;

    public TaskEvent() {}

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getEventType() {
        return EventType;
    }

    public void setEventType(int eventType) {
        EventType = eventType;
    }
}
