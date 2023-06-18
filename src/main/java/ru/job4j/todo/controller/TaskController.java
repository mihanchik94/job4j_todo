package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.Optional;

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
        model.addAttribute("newTasks",taskService.findNewTasks());
        return "tasks/newTasks";
    }

    @GetMapping("/done")
    public String doneTasksPage(Model model) {
        model.addAttribute("doneTasks",taskService.findDoneTasks());
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

    @GetMapping("{id}")
    public String getById(Model model, @PathVariable int id) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "заявка с указанным id не найдена");
            return "redirect:/errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model) {
        try {
            boolean isUpdated = taskService.update(task);
            if (!isUpdated) {
                model.addAttribute("message", "Задача с указанным id не найдена");
                return "errors/404";
            }
            return "redirect:/tasks/all";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        boolean isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Задача с указанным id не найдена");
            return "errors/404";
        }
        return "redirect:/tasks/all";
    }
}