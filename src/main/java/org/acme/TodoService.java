package org.acme;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TodoService
{
    long sequence = 1;
    Map<Long, Todo> todos = new HashMap<>();

    Todo create(Todo input)
    {
        long id = sequence++;
        final Todo todo = input.withId(id);
        todos.put(id, todo);
        return todo;
    }

    List<Todo> listAll()
    {
        return todos.values().stream().toList();
    }

    Todo findById(Long id)
    {
        return todos.get(id);
    }

    void update(Todo todo, Long id)
    {
        todos.put(id, todo);
    }

    void deleteCompleted()
    {
        todos.entrySet().removeIf(t -> t.getValue().completed());
    }

    void delete(Long id)
    {
        todos.remove(id);
    }
}
