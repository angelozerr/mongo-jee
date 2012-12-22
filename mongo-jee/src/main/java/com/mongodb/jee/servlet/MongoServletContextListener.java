/**
 *   Copyright (C) 2012 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.mongodb.jee.servlet;

import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mongodb.MongoURI;
import com.mongodb.jee.MongoHolder;

public class MongoServletContextListener implements ServletContextListener {

	private static final String MONGO_URI_PARAM = "mongoURI";
	private static final String DEFAULT_MONGO_URI = "mongodb://localhost:27017";

	public void contextInitialized(ServletContextEvent event) {
		try {
			MongoURI mongoURI = new MongoURI(getMongoURI(event));
			MongoHolder.connect(mongoURI, true);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
		MongoHolder.dispose();
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
