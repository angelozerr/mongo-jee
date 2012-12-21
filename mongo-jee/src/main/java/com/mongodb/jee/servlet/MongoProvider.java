package com.mongodb.jee.servlet;

import java.net.UnknownHostException;

import com.mongodb.Mongo;
import com.mongodb.MongoURI;

public class MongoProvider {

	private static Mongo mongo;

	private MongoProvider() {
	}

	public static Mongo getMongo() {
		if (mongo == null) {
			throw new RuntimeException("call MongoProvider#connect at firsts.");
		}
		return mongo;
	}

	public static void connect(MongoURI mongoURI) throws UnknownHostException {
		mongo = new Mongo(mongoURI);
	}

	public static void disconnect() {
		mongo.close();
	}
}
