package com.mcredit.data.factory;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateFactory {
	public static SessionFactory factory;

	// to disallow creating objects by other classes.
	private HibernateFactory() {
	}

	// maling the Hibernate SessionFactory object as singleton
	public static synchronized SessionFactory getInstance() {
		if (factory == null) {
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
			System.out.println("ServiceRegistry created successfully");

			SessionFactory sessFactory = new Configuration().buildSessionFactory(serviceRegistry);
			System.out.println("SessionFactory created successfully");
			
			factory = sessFactory;
		}
		return factory;
	}
}
