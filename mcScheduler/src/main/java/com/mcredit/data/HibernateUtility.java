package com.mcredit.data;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtility {
    public static SessionFactory factory;
//to disallow creating objects by other classes.
    private HibernateUtility() {
    }
//maling the Hibernate SessionFactory object as singleton
    public static synchronized SessionFactory getSessionFactory() {
        if (factory == null) {
    		// Configuration configuration = new Configuration();
    		// configuration.configure("hibernate.cfg.xml");
    		// logger.info("Hibernate Configuration created successfully");

    		// ServiceRegistry serviceRegistry = new
    		// StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
    		// logger.info("ServiceRegistry created successfully");
    		// SessionFactory sessFactory =
    		// configuration.buildSessionFactory(serviceRegistry);
    		// logger.info("SessionFactory created successfully");

    		// servletContextEvent.getServletContext().setAttribute("SessionFactory",
    		// sessFactory);
    		// logger.info("Hibernate SessionFactory Configured successfully");
    		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
    				.configure("hibernate.cfg.xml").build();
    		System.out.println("ServiceRegistry created successfully");

    		SessionFactory sessFactory = new Configuration()
    				.buildSessionFactory(serviceRegistry);
    		System.out.println("SessionFactory created successfully");
    		factory = sessFactory;
        }
        return factory;
    }
}
