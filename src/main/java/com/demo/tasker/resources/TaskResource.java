package com.demo.tasker.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.tasker.api.Task;
import com.demo.tasker.db.TaskDAO;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/task")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class TaskResource {

	private static final Logger logger = LoggerFactory.getLogger(TaskResource.class);
	
	private final TaskDAO taskDAO;

	public TaskResource(TaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}
	
    @GET
    @UnitOfWork
    public List<Task> listTask() {
        return taskDAO.findAll();
    }

	@GET
	@Path("/{id}")
	@UnitOfWork
	public Response getTaskById(@PathParam("id") long id) {
		logger.info("Looking for task with id: " + id);
		Task task = taskDAO.findById(id);
		if (task != null)
			return Response.ok(task).build();
		else
			return Response.status(Status.NOT_FOUND).build();
	}
	
	@POST
	@UnitOfWork
	public Response createTask(Task newTask) throws URISyntaxException   {
		logger.info("Creating a new task ...");
		Task task = taskDAO.create(newTask);
		if (task != null) {
			return Response.created(new URI("/task/" + task.getId())).build();
		} else
			return Response.status(Status.NOT_FOUND).build();
	}
	
	@PUT
	@Path("/{id}")
	@UnitOfWork
	public Response updateTaskById(@PathParam("id") long id, Task task) {
		logger.info("Updating task with id: " + id);
		Task fetchedTask = taskDAO.findById(id);
		
		if (fetchedTask != null) {
			return Response.ok(taskDAO.update(task)).build();
		}
		else
			return Response.status(Status.NOT_FOUND).build();
		
		
	}
	
	@DELETE
	@Path("/{id}")
	@UnitOfWork
	public Response deleteTaskById(@PathParam("id") long id) {
		logger.info("Deleting task with id: " + id);
		Task fetchedTask = taskDAO.findById(id);
		
		if (fetchedTask != null) {
			taskDAO.delete(fetchedTask);	
			return Response.ok().build();
		}
		else
			return Response.status(Status.NOT_FOUND).build();
		
		
	}
	
}
