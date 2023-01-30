package com.in28minues.springboot.myFirstWebApp.todo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;
import java.util.List;

//@Controller
@SessionAttributes("name")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @RequestMapping("/list-todo")
    public String listTodos(ModelMap model) {
        String username = getUsername(model);
        List<Todo> todos = todoService.findByUsername(username);

        model.put("todoList", todos);
        return "todo-list";
    };

    @RequestMapping(value="/add-todo", method = RequestMethod.GET)
    public String showNewTodo(ModelMap model) {
        String username = getUsername(model);
        Todo todo = new Todo(0, username, "", LocalDate.now().plusYears(1), false);
        model.put("todo", todo);
        return "todo";
    }

    @RequestMapping(value="/add-todo", method = RequestMethod.POST)
    public String addNewTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "todo";
        }

        TodoService.addTodo(
                getUsername(model),
                todo.getDescription(),
                LocalDate.now().plusYears(1),
                false
        );
        return "redirect:list-todo";
    };

    @RequestMapping("/delete-todo")
    public String deleteTodo(@RequestParam int id) {
        TodoService.delete(id);
        return "redirect:list-todo";
    };

    @RequestMapping(value="/update-todo", method = RequestMethod.GET)
    public String showUpdateTodoPage(@RequestParam int id, ModelMap model) {
        Todo todo = TodoService.findById(id);
        model.addAttribute("todo", todo);

        return "todo";
    };

    @RequestMapping(value="/update-todo", method = RequestMethod.POST)
    public String updateTodoModelMap(ModelMap model, @Valid Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "todo";
        }

        todo.setUsername(getUsername(model));
        TodoService.updateTodo(todo);
        return "redirect:list-todo";
    };

    private static String getUsername(ModelMap model) {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

}
