package io.github.mat3e.todo;

import io.github.mat3e.HibernateUtil;
import io.github.mat3e.lang.Lang;

import java.util.List;
import java.util.Optional;

public class ToDoRepository {

    List<ToDo> findAll() {
        var session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        var result = (session.createQuery("from ToDo", ToDo.class).list());
        transaction.commit();
        session.close();
        return result;
    }

    ToDo toggleTodo(Integer id) {
        var session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();

        var optionalTodo = session.get(ToDo.class, id);
        optionalTodo.setDone(!optionalTodo.isDone());
        transaction.commit();

        session.close();
        return optionalTodo;
    }

    public Optional<ToDo> findById(Integer id) {
        var session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        var result = Optional.ofNullable(session.get(ToDo.class, id));
        transaction.commit();
        session.close();
        return result;
    }

    ToDo AddTodo(ToDo newTodo) {
        var session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();

        session.persist(newTodo);

        transaction.commit();
        session.close();
        return newTodo;
    }

}
