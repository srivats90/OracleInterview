package com.demo.tasker.db;

import com.demo.tasker.api.Task;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.DAOTestExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(DropwizardExtensionsSupport.class)
public class TaskDAOTest {

    public DAOTestExtension database = DAOTestExtension.newBuilder().addEntityClass(Task.class).build();
    
    private Task task,newTask;
    private TaskDAO taskDAO;

    @BeforeEach
    public void setUp() {
        taskDAO = new TaskDAO(database.getSessionFactory());
        task = new Task();
		task.setName("Complete unit test");
		task.setTaskDate(LocalDate.parse("2023-07-26"));  
		newTask = new Task(1, "Cleanup repo",LocalDate.parse("2023-07-26"));    
    }

    @Test
    public void createsTask() {
        Task createdTask = database.inTransaction(() -> {
            return taskDAO.create(task);
        });
        assertThat(createdTask.getId()).isEqualTo(1);
        assertThat(createdTask.getName()).isEqualTo("Complete unit test");
        assertThat(createdTask.getTaskDate()).isEqualTo(LocalDate.parse("2023-07-26"));
    }
    

    @Test
    public void updateTask() {		
        Task createdTask = database.inTransaction(() -> {
        	return taskDAO.create(task);
        });
        
        assertThat(createdTask.getId()).isEqualTo(1);
        assertThat(createdTask.getName()).isEqualTo("Complete unit test");
        assertThat(createdTask.getTaskDate()).isEqualTo(LocalDate.parse("2023-07-26"));
        
        Task updatedTask = database.inTransaction(() -> {
        	return taskDAO.update(newTask);
        });
  
        assertThat(updatedTask.getId()).isEqualTo(1);
        assertThat(updatedTask.getName()).isEqualTo("Cleanup repo");
        assertThat(updatedTask.getTaskDate()).isEqualTo(LocalDate.parse("2023-07-26"));

    }
    
    @Test
    public void deleteTask() {
    	
        Task createdTask = database.inTransaction(() -> {
            return taskDAO.create(task);
        });  
        List<Task> tasks = taskDAO.findAll();
        assertThat(tasks).hasSize(1);

        database.inTransaction(() -> {
        	taskDAO.delete(task);
        });
        tasks = taskDAO.findAll();  
        assertThat(tasks).hasSize(0);
    }
    
    @Test
    void findById() {
        Task createdTask = database.inTransaction(() -> {
            return taskDAO.create(task);
        });
        
        long expectedId = createdTask.getId();
        Task taskFound = taskDAO.findById(expectedId);
        
        assertThat(taskFound.getId()).isEqualTo(1);
        assertThat(taskFound.getName()).isEqualTo("Complete unit test");
        assertThat(taskFound.getTaskDate()).isEqualTo(LocalDate.parse("2023-07-26"));
    }
    
    @Test
    void findAll() {
    	database.inTransaction(() -> {
    		taskDAO.create(new Task("Complete reviews", LocalDate.parse("2023-07-26")));
    		taskDAO.create(new Task("Write integration tests", LocalDate.parse("2023-08-22")));
    		taskDAO.create(new Task("Close open tickets", LocalDate.parse("2023-07-23")));
        });

        final List<Task> tasks = taskDAO.findAll();
        assertThat(tasks).extracting("name").containsOnly("Complete reviews","Write integration tests","Close open tickets");
        assertThat(tasks).extracting("taskDate").containsOnly(LocalDate.parse("2023-07-26"), LocalDate.parse("2023-08-22"), LocalDate.parse("2023-07-23"));
    }
    
}
    



