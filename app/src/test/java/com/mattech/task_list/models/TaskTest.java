package com.mattech.task_list.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class TaskTest {

    @Test
    public void nextStatus() throws Exception {
        Task.TaskStatus[] expectedStatuses = {Task.TaskStatus.TRAVELLING, Task.TaskStatus.WORKING, Task.TaskStatus.OPEN};
        Task.TaskStatus[] actualStatuses = new Task.TaskStatus[3];
        Task task = new Task(123, "Task123", Task.TaskStatus.OPEN);
        for (int i = 0; i < 3; i++) {
            actualStatuses[i] = Task.nextStatus(task);
            task.setStatus(actualStatuses[i]);
        }
        assertArrayEquals(expectedStatuses, actualStatuses);
    }
}