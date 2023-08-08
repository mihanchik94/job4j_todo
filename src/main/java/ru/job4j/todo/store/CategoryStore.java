package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.Map;


@ThreadSafe
@Repository
@AllArgsConstructor
public class CategoryStore implements CategoryRepository {
    private final CrudRepository crudRepository;

    @Override
    public Collection<Category> getAll() {
        return crudRepository.query("from Category", Category.class);
    }

    @Override
    public List<Category> getGroupOfCategories(List<Integer> idList) {
        return crudRepository.query("from Category c where c.id in :categoryIdList",
                Category.class, Map.of("categoryIdList", idList));
    }
}