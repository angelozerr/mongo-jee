package com.mongodb.jee.jaxrs;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.jee.service.PersonService;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class JaxrsTest {

	private static final int PORT = 8999;

	private static Server server;

	private static final String BASE_ADDRESS = "http://localhost:" + PORT
			+ "/jaxrs";

	private static final String DATABASE_NAME = "contact";

	// https://github.com/flapdoodle-oss/embedmongo.flapdoodle.de
	private MongodExecutable mongodExe;
	private MongodProcess mongod;
	private Mongo mongo;

	@Before
	public void beforeEach() throws Exception {
		startMongo();
		startJettyServer();
	}

	private void startMongo() throws UnknownHostException, IOException {
		MongodStarter runtime = MongodStarter.getDefaultInstance();
		mongodExe = runtime.prepare(new MongodConfig(Version.V2_0_1, 12345,
				Network.localhostIsIPv6()));
		mongod = mongodExe.start();
		mongo = new Mongo("localhost", 12345);
		
		DB db = mongo.getDB(DATABASE_NAME);
		DBCollection col = db.getCollection("persons");
		
		DBObject person = new BasicDBObject();
		person.put("name", "xxx");
		col.insert(person);
		
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
		mongod.stop();
		mongodExe.stop();
		if (server != null) {
			server.stop();
		}
	}

	 @Test
	public void shouldCreateNewObjectInEmbeddedMongoDb() {
		// given
//		DB db = mongo.getDB(DATABASE_NAME);
//		DBCollection col = db.getCollection("persons");
//		
//		DBObject person = new BasicDBObject();
//		person.put("name", "xxx");
//		col.insert(person);
//		
		// DBCollection col = db.createCollection("testCollection", new
		// BasicDBObject());

		// when
		// col.save(new BasicDBObject("testDoc", new Date()));

		// then
	}

	@Test
	public void t() {

		List providers = new ArrayList();
		providers.add(new BSONObjectProvider());
		
		PersonService client = JAXRSClientFactory.create(BASE_ADDRESS,
				PersonService.class, providers);

		DBObject person = client.findOne();
		System.err.println(person);
	}
}
