package io.github.mat3e.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mat3e.lang.LangServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ToDo", urlPatterns = {"/api/todos/*"})
public class ToDoServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(LangServlet.class);
    private ObjectMapper jsonMapper;
    private ToDoRepository repository;

    /**
     * container needs it
     */
    @SuppressWarnings("unused")
    public ToDoServlet() {
        this(new ObjectMapper(), new ToDoRepository());
    }

    public ToDoServlet(ObjectMapper jsonMapper, ToDoRepository repository) {
        this.jsonMapper = jsonMapper;
        this.repository = repository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Got request with parameters: " + req.getParameterMap());
        resp.setContentType("application/json;charset=UTF-8");
        // mapping the response to JSON
        jsonMapper.writeValue(resp.getOutputStream(), repository.findAll());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathInfo = req.getPathInfo();
        try {
            var todoId = Integer.valueOf(pathInfo.substring(1));
            var todo = repository.toggleTodo(todoId);
            resp.setContentType("application/json;charset=UTF-8");
            jsonMapper.writeValue(resp.getOutputStream(), todo);
        } catch (NumberFormatException e) {
            logger.warn("Wrong path used:" + pathInfo);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var newTodo = jsonMapper.readValue(req.getInputStream(), ToDo.class);
        resp.setContentType("application/json;charset=UTF-8");
        jsonMapper.writeValue(resp.getOutputStream(), repository.AddTodo(newTodo));
    }
}
