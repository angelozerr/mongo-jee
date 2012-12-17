package com.mongodb.jee.service;

import java.util.Iterator;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.jee.PageResult;
import com.mongodb.jee.servlet.MongoProvider;

import dojo.store.RequestPageRange;

public class PersonServiceImpl implements PersonService {

	public DBObject findOne() {
		DB db = MongoProvider.getMongo().getDB("contact");
		DBCollection col = db.getCollection("persons");
		return col.findOne();
	}

	public Iterator<DBObject> find() {
		DB db = MongoProvider.getMongo().getDB("contact");
		DBCollection col = db.getCollection("persons");
		return col.find();
	}

	public PageResult findPage(int fromItemIndex, int toItemIndex) {
		DB db = MongoProvider.getMongo().getDB("contact");
		DBCollection col = db.getCollection("persons");
		return new PageResult(col.find(), fromItemIndex, toItemIndex);
	}

	public PageResult findPage(RequestPageRange range) {
		DB db = MongoProvider.getMongo().getDB("contact");
		DBCollection col = db.getCollection("persons");
		return new PageResult(col.find(), range.getFromIndex(),
				range.getToIndex());
	}
}
