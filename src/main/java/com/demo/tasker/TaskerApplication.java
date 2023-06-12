package com.demo.tasker;

import com.demo.tasker.resources.TaskResource;
import com.demo.tasker.db.TaskDAO;
import com.demo.tasker.api.Task;


import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;

public class TaskerApplication extends Application<TaskerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new TaskerApplication().run(args);
    }

    @Override
    public String getName() {
        return "tasker";
    }

    @Override
    public void initialize(final Bootstrap<TaskerConfiguration> bootstrap) {
		bootstrap.addBundle(hibernateBundle);
	}

    
	// Hibernate bundle used in the persistence layer
	private final HibernateBundle<TaskerConfiguration> hibernateBundle = new HibernateBundle<TaskerConfiguration>(
			Task.class) {
		@Override
		public DataSourceFactory getDataSourceFactory(TaskerConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};
	
    @Override
    public void run(final TaskerConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
		final TaskDAO taskDAO = new TaskDAO(hibernateBundle.getSessionFactory());
		
		final TaskResource resource = new TaskResource(taskDAO);
		
	
        environment.jersey().register(new TaskResource(taskDAO));
    }

}
