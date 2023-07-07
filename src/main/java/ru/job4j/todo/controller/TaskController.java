package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

@ThreadSafe
@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/all")
    public String allTasksPage(Model model) {
        model.addAttribute("tasks",taskService.findAll());
        return "tasks/tasks";
    }

    @GetMapping("/new")
    public String newTasksPage(Model model) {
        model.addAttribute("newTasks",taskService.findTasks(false));
        return "tasks/newTasks";
    }

    @GetMapping("/done")
    public String doneTasksPage(Model model) {
        model.addAttribute("doneTasks",taskService.findTasks(true));
        return "tasks/doneTasks";
    }

    @GetMapping("/createNewTask")
    public String creationOfTaskPage() {
        return "tasks/create";
    }

    @PostMapping("/createNewTask")
    public String createTask(@ModelAttribute Task task, Model model) {
        task.setDone(false);
        try {
            taskService.save(task);
            return "redirect:/tasks/all";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/404";
        }
    }
}