package com.mongodb.jee.jaxrs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.DBObject;
import com.mongodb.jee.PageResult;
import com.mongodb.jee.jaxrs.providers.BSONObjectProvider;
import com.mongodb.jee.jaxrs.providers.DBObjectIterableProvider;
import com.mongodb.jee.jaxrs.providers.PageResultProvider;
import com.mongodb.jee.service.ProductService;

import dojo.store.PageRangeRequest;

public class MockProductServiceTestCase {

	private static final int PORT = 8999;

	private static Server server;

	private static final String BASE_ADDRESS = "http://localhost:" + PORT
			+ "/jaxrs";

	@Before
	public void beforeEach() throws Exception {
		startJettyServer();
	}

	private void startJettyServer() throws Exception {
		server = new Server(PORT);

		WebAppContext webappcontext = new WebAppContext("src/test/resources",
				"/");

		ContextHandlerCollection servlet_contexts = new ContextHandlerCollection();
		webappcontext.setClassLoader(Thread.currentThread()
				.getContextClassLoader());
		HandlerCollection handlers = new HandlerCollection();
		handlers.setHandlers(new Handler[] { servlet_contexts, webappcontext,
				new DefaultHandler() });
		server.setHandler(handlers);

		server.start();

	}

	@After
	public void afterEach() throws Exception {
		if (server != null) {
			server.stop();
		}
	}

	@Test
	public void findOne() {
		ProductService client = createClient();
		DBObject person = client.findOne();
		System.err.println(person);
	}

	@Test
	public void find() {
		ProductService client = createClient();
		Iterator<DBObject> persons = client.find().iterator();
		while (persons.hasNext()) {
			DBObject p = persons.next();
			System.err.println(p);
		}

	}

	@Test
	public void findPage() {
		ProductService client = createClient();

		PageResult page = client.findPage(0, 9);
		Assert.assertEquals(0, page.getFromItemIndex());
		Assert.assertEquals(9, page.getToItemIndex());
		Assert.assertEquals(100, page.getTotalItems());

		Iterator<DBObject> persons = page.getItems().iterator();
		while (persons.hasNext()) {
			DBObject p = persons.next();
			System.err.println(p);
		}

		System.err.println(page.getTotalItems());
	}

	@Test
	public void findPageRange() {
		ProductService client = createClient();

		PageRangeRequest range = new PageRangeRequest(0, 9);
		PageResult page = client.findPage(range);

		Assert.assertEquals(0, page.getFromItemIndex());
		Assert.assertEquals(9, page.getToItemIndex());
		Assert.assertEquals(100, page.getTotalItems());

		Iterator<DBObject> persons = page.getItems().iterator();
		while (persons.hasNext()) {
			DBObject p = persons.next();
			System.err.println(p);
		}

		System.err.println(page.getTotalItems());
	}

	private ProductService createClient() {
		List providers = new ArrayList();
		providers.add(new DBObjectIterableProvider());
		providers.add(new BSONObjectProvider());
		providers.add(new PageResultProvider());

		ProductService client = JAXRSClientFactory.create(BASE_ADDRESS,
				ProductService.class, providers);

		WebClient.getConfig(client).getInInterceptors()
				.add(new LoggingInInterceptor());
		WebClient.getConfig(client).getOutInterceptors()
				.add(new LoggingOutInterceptor());
		return client;
	}

}
