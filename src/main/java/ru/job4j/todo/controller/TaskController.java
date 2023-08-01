package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import java.util.Optional;

@ThreadSafe
@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final PriorityService priorityService;

    @GetMapping("/all")
    public String allTasksPage(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/tasks";
    }

    @GetMapping("/new")
    public String newTasksPage(Model model) {
        model.addAttribute("newTasks", taskService.findTasks(false));
        return "tasks/newTasks";
    }

    @GetMapping("/done")
    public String doneTasksPage(Model model) {
        model.addAttribute("doneTasks", taskService.findTasks(true));
        return "tasks/doneTasks";
    }

    @GetMapping("/createNewTask")
    public String creationOfTaskPage(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        return "tasks/create";
    }

    @PostMapping("/createNewTask")
    public String createTask(@ModelAttribute Task task, @SessionAttribute User user) {
        task.setUser(user);
        task.setDone(false);
        taskService.save(task);
        return "redirect:/tasks/all";
    }

    @GetMapping("{id}")
    public String getById(Model model, @PathVariable int id) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "заявка с указанным id не найдена");
            return "redirect:/errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        model.addAttribute("priorities", priorityService.findAll());
        return "tasks/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model, @SessionAttribute User user) {
        task.setUser(user);
        boolean isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("message", "Задача с указанным id не найдена");
            return "errors/404";
        }
        return "redirect:/tasks/all";
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

    @GetMapping("/changeTaskStatus/{id}")
    public String changeTaskStatus(@ModelAttribute Task task, Model model) {
        boolean isChanged = taskService.changeDone(task);
        if (!isChanged) {
            model.addAttribute("message", "Задача с указанным id не найдена");
            return "errors/404";
        }
        return "redirect:/tasks/all";
    }
}