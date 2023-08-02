package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.store.PriorityRepository;

import java.util.List;

@ThreadSafe
@Service
@AllArgsConstructor
public class SimplePriorityService implements PriorityService {
    private final PriorityRepository priorityRepository;


    @Override
    public List<Priority> findAll() {
        return priorityRepository.findAll();
    }
}