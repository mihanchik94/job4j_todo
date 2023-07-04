package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskControllerTest {
    private TaskService taskService;
    private TaskController taskController;

    @BeforeEach
    public void initServices() {
        taskService = mock(TaskService.class);
        taskController = new TaskController(taskService);
    }

    @Test
    public void whenRequestAllTasksPageThenReturnPageWithAllTasks() {
        Task task1 = new Task(1, "task_1", "desc_1", true);
        Task task2 = new Task(2, "task_2", "desc_2", false);
        Task task3 = new Task(3, "task_3", "desc_3", false);
        List<Task> expectedTasks = List.of(task1, task2, task3);

        when(taskService.findAll()).thenReturn(expectedTasks);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.allTasksPage(model);
        Object actualTasks = model.getAttribute("tasks");

        assertThat(view).isEqualTo("tasks/tasks");
        assertThat(actualTasks).isEqualTo(expectedTasks);
    }

    @Test
    public void whenRequestNewTasksPageThenReturnPageWithNewTasks() {
        Task task1 = new Task(2, "task_2", "desc_2", false);
        Task task2 = new Task(3, "task_3", "desc_3", false);
        List<Task> expectedTasks = List.of(task1, task2);

        when(taskService.findTasks(false)).thenReturn(expectedTasks);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.newTasksPage(model);
        Object actualTasks = model.getAttribute("newTasks");

        assertThat(view).isEqualTo("tasks/newTasks");
        assertThat(actualTasks).isEqualTo(expectedTasks);
    }

    @Test
    public void whenRequestDoneTasksPageThenReturnPageWithDoneTasks() {
        Task task1 = new Task(1, "task_1", "desc_1", true);
        List<Task> expectedTasks = List.of(task1);

        when(taskService.findTasks(true)).thenReturn(expectedTasks);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.doneTasksPage(model);
        Object actualTasks = model.getAttribute("doneTasks");

        assertThat(view).isEqualTo("tasks/doneTasks");
        assertThat(actualTasks).isEqualTo(expectedTasks);
    }
}