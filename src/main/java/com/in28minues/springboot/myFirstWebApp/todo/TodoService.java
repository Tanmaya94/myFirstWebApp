package com.in28minues.springboot.myFirstWebApp.todo;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
public class TodoService {

    private static List<Todo> todos = new ArrayList<>();
    private static int todoCount = 0;

    static {
        todos.add(
                new Todo(++todoCount, "in28minutes", "Learn Spring boot", LocalDate.now().plusYears(2), false)
        );
        todos.add(
                new Todo(++todoCount, "in28minutes", "Learn Docker", LocalDate.now().plusYears(3), false)
        );
        todos.add(
                new Todo(++todoCount, "in28minutes", "Learn AWS", LocalDate.now().plusYears(4), false)
        );
    }

    public static void addTodo(String username, String description, LocalDate date, boolean isDone) {
        todos.add(
                new Todo(++todoCount, username, description, date, isDone)
        );
    }

    public static void delete(int id) {
        Predicate<? super Todo> predicate = todo -> todo.getId() == id;
        todos.removeIf(predicate);
    }

    public static Todo findById(int id) {
        Predicate<? super Todo> predicate = todo -> todo.getId() == id;
        return todos.stream().filter(predicate).findFirst().get();
    }

    public static void updateTodo(Todo todo) {
        delete(todo.getId());
        todos.add(todo);
    }

    public List<Todo> findByUsername(String username) {
        Predicate<? super Todo> predicate = todo -> todo.getUsername().equals(username);
        return todos.stream().filter(predicate).toList();
    }
}
