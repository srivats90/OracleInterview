package com.demo.tasker;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.demo.tasker.api.Task;

import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;

// NOT COMPLETE AND NOT WORKING

@ExtendWith(DropwizardExtensionsSupport.class)
public class IntegrationTests {

    private static final String CONFIG = "tasker-testconfig.yml";
    
    static final DropwizardAppExtension<TaskerConfiguration> APP = new DropwizardAppExtension<>(
            TaskerApplication.class, CONFIG,
            new ResourceConfigurationSourceProvider());
    
    private Task postTask(Task task) {
        return APP.client().target("http://localhost:" + APP.getLocalPort() + "/task")
                .request()
                .post(Entity.entity(task, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Task.class);
    }
    
    @Test
    void testPostTask() {
        final Task task = new Task("Complete testing", LocalDate.parse("2023-07-03"));
        final Task newTask = postTask(task);
        assertThat(newTask.getName()).isEqualTo(task.getName());
    }
    
}
