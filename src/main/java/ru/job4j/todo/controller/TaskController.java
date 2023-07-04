package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}