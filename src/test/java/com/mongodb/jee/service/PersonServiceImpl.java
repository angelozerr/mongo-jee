package com.mongodb.jee.service;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.jee.servlet.MongoProvider;

public class PersonServiceImpl implements PersonService {

	public DBObject findOne() {
		DB db = MongoProvider.getMongo().getDB("contact");
		DBCollection col = db.getCollection("persons");
		return col.findOne();
	}
}
