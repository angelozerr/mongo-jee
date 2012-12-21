package com.mongodb.jee.servlet;

import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


import com.mongodb.MongoURI;

public class MongoServletContextListener implements ServletContextListener {

	private static final String MONGO_URI_PARAM = "mongoURI";
	private static final String DEFAULT_MONGO_URI = "mongodb://localhost:27017";

	public void contextInitialized(ServletContextEvent event) {
		try {
			MongoURI mongoURI = new MongoURI(getMongoURI(event));
			MongoProvider.connect(mongoURI);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
		MongoProvider.disconnect();
	}

	private String getMongoURI(ServletContextEvent event) {
		String mongoURI = null;
		ServletContext context = event.getServletContext();
		if (context != null) {
			mongoURI = context.getInitParameter(MONGO_URI_PARAM);
		}
		return mongoURI != null ? mongoURI : DEFAULT_MONGO_URI;
	}
}
