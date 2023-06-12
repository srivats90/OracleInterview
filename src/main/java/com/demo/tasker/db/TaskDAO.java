package com.demo.tasker.db;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.demo.tasker.api.Task;

import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.hibernate.UnitOfWork;
import jakarta.persistence.criteria.CriteriaQuery;

public class TaskDAO extends AbstractDAO<Task> {

    /**
     * Constructor.
     *
     * @param sessionFactory Hibernate session factory.
     */
	public TaskDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Task create(Task task) {
		return persist(task);
	}

	public Task update(Task task) {
		return currentSession().merge(task);
	}

	public void delete(Task task) {
		currentSession().remove(task);
	}

	public Task findById(long id) {
		return (Task) currentSession().get(Task.class, id);
	}

	public List<Task> findAll() {
        return list(namedTypedQuery("com.demo.tasker.api.Task.findAll"));
	}

}
