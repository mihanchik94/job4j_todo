package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
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
    private PriorityService priorityService;
    private CategoryService categoryService;
    private TaskController taskController;


    @BeforeEach
    public void initServices() {
        taskService = mock(TaskService.class);
        priorityService = mock(PriorityService.class);
        categoryService = mock(CategoryService.class);
        taskController = new TaskController(taskService, priorityService, categoryService);
    }

    @Test
    public void whenRequestAllTasksPageThenReturnPageWithAllTasks() {
        Task task1 = new Task(1, "task_1", "desc_1", true, new User());
        Task task2 = new Task(2, "task_2", "desc_2", false, new User());
        Task task3 = new Task(3, "task_3", "desc_3", false, new User());
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
        Task task1 = new Task(2, "task_2", "desc_2", false, new User());
        Task task2 = new Task(3, "task_3", "desc_3", false, new User());
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
        Task task1 = new Task(1, "task_1", "desc_1", true, new User());
        List<Task> expectedTasks = List.of(task1);

        when(taskService.findTasks(true)).thenReturn(expectedTasks);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.doneTasksPage(model);
        Object actualTasks = model.getAttribute("doneTasks");

        assertThat(view).isEqualTo("tasks/doneTasks");
        assertThat(actualTasks).isEqualTo(expectedTasks);
    }

    @Test
    public void whenPostCreateTaskSuccessfullyThenReturnPageWithAllTasks() {
        Task savedTask = new Task(1, "task_1", "desc_1", true, new User());

        when(taskService.save(savedTask)).thenReturn(savedTask);

        String view = taskController.createTask(savedTask, List.of(1), new User());

        assertThat(view).isEqualTo("redirect:/tasks/all");
    }

    @Test
    public void whenRequestGetByIdSuccessfullyThenReturnPageWithOneTasks() {
        Task task1 = new Task(1, "task_1", "desc_1", true, new User());

        when(taskService.findById(anyInt())).thenReturn(Optional.of(task1));

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.getById(model, task1.getId());
        Object actualTask = model.getAttribute("task");

        assertThat(view).isEqualTo("tasks/one");
        assertThat(actualTask).isEqualTo(task1);
    }

    @Test
    public void whenRequestGetByIdFailedThenReturnPageWith404Error() {
        Task task1 = new Task(1, "task_1", "desc_1", true, new User());

        when(taskService.findById(anyInt())).thenReturn(Optional.empty());

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.getById(model, task1.getId());

        assertThat(view).isEqualTo("redirect:/errors/404");
    }

    @Test
    public void whenPostUpdateTaskThenReturnPageWithAllTasks() {
        Task updatedTask = new Task(1, "task_1", "desc_1", true, new User());

        when(taskService.update(any())).thenReturn(true);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.update(updatedTask, model, List.of(1), new User());

        assertThat(view).isEqualTo("redirect:/tasks/all");
    }

    @Test
    public void whenPostUpdateTaskIdFailedThenReturnPageWith404Error() {
        Task updatedTask = new Task(1, "task_1", "desc_1", true, new User());
        String expectedMessage = "Задача с указанным id не найдена";

        when(taskService.update(any())).thenReturn(false);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.update(updatedTask, model, List.of(1), new User());
        Object actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenRequestDeleteByIdSuccessfullyThenReturnPageWithAllTasks() {
        Task deletedTask = new Task(1, "task_1", "desc_1", true, new User());

        when(taskService.deleteById(anyInt())).thenReturn(true);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.delete(deletedTask.getId(), model);

        assertThat(view).isEqualTo("redirect:/tasks/all");
    }

    @Test
    public void whenRequestDeleteByIdFailedThenReturnPageWith404Error() {
        Task deletedTask = new Task(1, "task_1", "desc_1", true, new User());
        String expectedMessage = "Задача с указанным id не найдена";

        when(taskService.deleteById(anyInt())).thenReturn(false);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.delete(deletedTask.getId(), model);
        Object actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenRequestChangeTaskStatusSuccessfullyThenReturnPageWithAllTasks() {
        Task changedStatusTask = new Task(1, "task_1", "desc_1", true, new User());

        when(taskService.changeDone(any())).thenReturn(true);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.changeTaskStatus(changedStatusTask, model);

        assertThat(view).isEqualTo("redirect:/tasks/all");
    }

    @Test
    public void whenRequestChangeTaskStatusFailedThenReturnPageWith404Error() {
        Task changedStatusTask = new Task(1, "task_1", "desc_1", true, new User());
        String expectedMessage = "Задача с указанным id не найдена";

        when(taskService.changeDone(any())).thenReturn(false);

        ConcurrentModel model = new ConcurrentModel();
        String view = taskController.changeTaskStatus(changedStatusTask, model);
        Object actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}