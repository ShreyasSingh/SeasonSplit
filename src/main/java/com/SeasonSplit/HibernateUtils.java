package com.SeasonSplit;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
	private static SessionFactory sessionFactory = buildSessionFactory();

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	private static SessionFactory buildSessionFactory() {
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(HotelMSG.class);
		configuration.addAnnotatedClass(Season.class);
		return configuration.buildSessionFactory(new StandardServiceRegistryBuilder().build());

	}

}