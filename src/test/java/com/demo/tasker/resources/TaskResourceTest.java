package com.demo.tasker.resources;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import com.demo.tasker.api.Task;
import com.demo.tasker.db.TaskDAO;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(DropwizardExtensionsSupport.class)
class TaskResourceTest {
	
    private static final TaskDAO TASK_DAO = mock(TaskDAO.class);
    public static final ResourceExtension RESOURCES = ResourceExtension.builder()
            .addResource(new TaskResource(TASK_DAO))
            .build();
    private final ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
    
    private Task task,updatedTask;
	private Task[] tasks;
	private String taskURL = "/task";
    
    @BeforeEach
    void setUp() {
        task = new Task(1,"Complete unit test",LocalDate.parse("2023-07-26"));        
        tasks = new Task[] {task,new Task(2, "Complete integration test",LocalDate.parse("2023-08-26"))};
        updatedTask = new Task(1, "Cleanup repo",LocalDate.parse("2023-07-26"));    
    }
    
    @AfterEach
    void tearDown() {
        reset(TASK_DAO);
    }
    
	@Test
	void testGetAllTasks() {
		when(TASK_DAO.findAll()).thenReturn(Arrays.asList(tasks));
		@SuppressWarnings("unchecked")
		List<Task> tasksReturned = (List<Task>) RESOURCES.target(taskURL)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(List.class);
        
		assertThat(tasksReturned).hasSize(2);
		verify(TASK_DAO).findAll();
	}
	

    
    @Test
    void createTaskSuccess() {
        when(TASK_DAO.create(any(Task.class))).thenReturn(task);
        
        final Response response = RESOURCES.target(taskURL)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(task, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CREATED);
        verify(TASK_DAO).create(taskCaptor.capture());
        assertThat(taskCaptor.getValue()).isEqualTo(task);
    }
    
    @Test
    void createTaskFailure() {
        final Response response1 = RESOURCES.target(taskURL)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity("Invalid Json", MediaType.APPLICATION_JSON_TYPE));

        assertThat(response1.getStatusInfo()).isEqualTo(Response.Status.BAD_REQUEST);
        
        final Response response2 = RESOURCES.target(taskURL)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(task, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response2.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);
    }
    
    
    @Test
    void updateTaskAndReturnOKwhenTaskExist() {
		when(TASK_DAO.findById(any(Long.class))).thenReturn(task);
        when(TASK_DAO.update(any(Task.class))).thenReturn(updatedTask);


        final Response response = RESOURCES.target(taskURL+"/"+task.getId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(updatedTask, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(TASK_DAO).update(taskCaptor.capture());
        assertThat(taskCaptor.getValue()).isEqualTo(updatedTask);
    }
    
    @Test
    void updateTaskAndReturnNotFoundwhenTaskDoesNotExist() {
		when(TASK_DAO.findById(any(Long.class))).thenReturn(null);

        final Response response = RESOURCES.target(taskURL+"/"+task.getId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(updatedTask, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);

    }
    
    @Test
    void deleteTaskAndReturnOKwhenTaskExist() {
		when(TASK_DAO.findById(any(Long.class))).thenReturn(task);

        final Response response = RESOURCES.target(taskURL+"/"+task.getId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(TASK_DAO).delete(taskCaptor.capture());
        assertThat(taskCaptor.getValue()).isEqualTo(task);
    }
    
    @Test
    void deleteTaskAndReturnNotFoundwhenTaskDoesNotExist() {
		when(TASK_DAO.findById(any(Long.class))).thenReturn(null);

        final Response response = RESOURCES.target(taskURL+"/"+task.getId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);

    }
    
}