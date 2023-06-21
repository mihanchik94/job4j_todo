package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
        Task task1 = new Task(1, "task_1", true);
        Task task2 = new Task(2, "task_2", false);
        Task task3 = new Task(3, "task_3", false);
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
        Task task1 = new Task(2, "task_2", false);
        Task task2 = new Task(3, "task_3", false);
        List<Task> expectedTasks = List.of(task1, task2);

        when(taskService.findNewTasks()).thenReturn(expectedTasks);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.newTasksPage(model);
        Object actualTasks = model.getAttribute("newTasks");

        assertThat(view).isEqualTo("tasks/newTasks");
        assertThat(actualTasks).isEqualTo(expectedTasks);
    }

    @Test
    public void whenRequestDoneTasksPageThenReturnPageWithDoneTasks() {
        Task task1 = new Task(1, "task_1", true);
        List<Task> expectedTasks = List.of(task1);

        when(taskService.findDoneTasks()).thenReturn(expectedTasks);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.doneTasksPage(model);
        Object actualTasks = model.getAttribute("doneTasks");

        assertThat(view).isEqualTo("tasks/doneTasks");
        assertThat(actualTasks).isEqualTo(expectedTasks);
    }

    @Test
    public void whenPostCreateTaskSuccessfullyThenReturnPageWithAllTasks() {
        Task savedTask = new Task(1, "task_1", true);

        when(taskService.save(savedTask)).thenReturn(savedTask);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.createTask(savedTask, model);

        assertThat(view).isEqualTo("redirect:/tasks/all");
    }

    @Test
    public void whenPostCreateTaskFailedThenReturnPageWith404Error() {
        Task savedTask = new Task(1, "task_1", true);
        Exception expectedException = new RuntimeException("Some exception");

        when(taskService.save(any())).thenThrow(expectedException);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.createTask(savedTask, model);
        Object message = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(message).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestGetByIdSuccessfullyThenReturnPageWithOneTasks() {
        Task task1 = new Task(1, "task_1", true);

        when(taskService.findById(anyInt())).thenReturn(Optional.of(task1));

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.getById(model, task1.getId());
        Object actualTask = model.getAttribute("task");

        assertThat(view).isEqualTo("tasks/one");
        assertThat(actualTask).isEqualTo(task1);
    }

    @Test
    public void whenRequestGetByIdFailedThenReturnPageWith404Error() {
        Task task1 = new Task(1, "task_1", true);

        when(taskService.findById(anyInt())).thenReturn(Optional.empty());

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.getById(model, task1.getId());

        assertThat(view).isEqualTo("redirect:/errors/404");
    }

    @Test
    public void whenPostUpdateTaskSuccessfullyThenReturnPageWithAllTasks() {
        Task updatedTask = new Task(1, "task_1", true);

        when(taskService.update(any())).thenReturn(true);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.update(updatedTask, model);

        assertThat(view).isEqualTo("redirect:/tasks/all");
    }

    @Test
    public void whenPostUpdateTaskFailedThenReturnPageWith404Error() {
        Task updatedTask = new Task(1, "task_1", true);
        String expectedMessage = "Задача с указанным id не найдена";

        when(taskService.update(any())).thenReturn(false);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.update(updatedTask, model);
        Object actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenPostUpdateTaskAndExceptionThenReturnPageWith404Error() {
        Task updatedTask = new Task(1, "task_1", true);
        Exception expectedException = new RuntimeException("Some exception");

        when(taskService.update(any())).thenThrow(expectedException);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.update(updatedTask, model);
        Object actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestDeleteByIdSuccessfullyThenReturnPageWithAllTasks() {
        Task deletedTask = new Task(1, "task_1", true);

        when(taskService.deleteById(anyInt())).thenReturn(true);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.delete(deletedTask.getId(), model);

        assertThat(view).isEqualTo("redirect:/tasks/all");
    }

    @Test
    public void whenRequestDeleteByIdFailedThenReturnPageWith404Error() {
        Task deletedTask = new Task(1, "task_1", true);
        String expectedMessage = "Задача с указанным id не найдена";

        when(taskService.deleteById(anyInt())).thenReturn(false);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.update(deletedTask, model);
        Object actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenRequestChangeTaskStatusSuccessfullyThenReturnPageWithAllTasks() {
        Task changedStatusTask = new Task(1, "task_1", true);

        when(taskService.changeDone(any())).thenReturn(true);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.changeTaskStatus(changedStatusTask, model);

        assertThat(view).isEqualTo("redirect:/tasks/all");
    }

    @Test
    public void whenRequestChangeTaskStatusFailedThenReturnPageWith404Error() {
        Task changedStatusTask = new Task(1, "task_1", true);
        String expectedMessage = "Задача с указанным id не найдена";

        when(taskService.changeDone(any())).thenReturn(false);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.changeTaskStatus(changedStatusTask, model);
        Object actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenRequestChangeTaskStatusAndExceptionThenReturnPageWith404Error() {
        Task changedStatusTask = new Task(1, "task_1", true);
        Exception expectedException = new RuntimeException("Some exception");

        when(taskService.changeDone(any())).thenThrow(expectedException);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.changeTaskStatus(changedStatusTask, model);
        Object actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedException.getMessage());
    }
}