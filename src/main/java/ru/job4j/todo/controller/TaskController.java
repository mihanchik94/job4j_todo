package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;
import static ru.job4j.todo.utils.UserUtils.getUserFromSession;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;



@ThreadSafe
@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final PriorityService priorityService;
    private final CategoryService categoryService;

    @GetMapping("/all")
    public String allTasksPage(Model model, HttpSession session) {
        User user = getUserFromSession(session);
        List<Task> tasks = taskService.findAll(user.getTimezone());
        model.addAttribute("tasks", tasks);
        return "tasks/tasks";
    }

    @GetMapping("/new")
    public String newTasksPage(Model model, HttpSession session) {
        User user = getUserFromSession(session);
        List<Task> tasks = taskService.findTasks(false, user.getTimezone());
        model.addAttribute("newTasks", tasks);
        return "tasks/newTasks";
    }

    @GetMapping("/done")
    public String doneTasksPage(Model model, HttpSession session) {
        User user = getUserFromSession(session);
        List<Task> tasks = taskService.findTasks(true, user.getTimezone());
        model.addAttribute("doneTasks", tasks);
        return "tasks/doneTasks";
    }

    @GetMapping("/createNewTask")
    public String creationOfTaskPage(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.getAll());
        return "tasks/create";
    }

    @PostMapping("/createNewTask")
    public String createTask(@ModelAttribute Task task, @RequestParam List<Integer> categoryList, @SessionAttribute User user) {
        task.setUser(user);
        task.setDone(false);
        task.getCategories().addAll(categoryService.getGroupOfCategories(categoryList));
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
        model.addAttribute("categories", categoryService.getAll());
        return "tasks/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model, @RequestParam List<Integer> categoryList, @SessionAttribute User user) {
        task.setUser(user);
        task.getCategories().addAll(categoryService.getGroupOfCategories(categoryList));
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