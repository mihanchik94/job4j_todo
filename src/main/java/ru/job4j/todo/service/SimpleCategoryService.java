package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.CategoryRepository;

import java.util.Collection;
import java.util.List;

@ThreadSafe
@Service
@AllArgsConstructor
public class SimpleCategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Collection<Category> getAll() {
        return categoryRepository.getAll();
    }

    @Override
    public List<Category> getGroupOfCategories(List<Integer> idList) {
        return categoryRepository.getGroupOfCategories(idList);
    }
}