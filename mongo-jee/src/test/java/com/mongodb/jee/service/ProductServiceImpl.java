package com.mongodb.jee.service;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.jee.PageResult;
import com.mongodb.jee.servlet.MongoProvider;

import dojo.store.PageRangeRequest;

public class ProductServiceImpl implements ProductService {

	public DBObject findOne() {
		DB db = MongoProvider.connect().getDB("contact");
		DBCollection col = db.getCollection("persons");
		return col.findOne();
	}

	public Iterable<DBObject> find() {
		DB db = MongoProvider.connect().getDB("contact");
		DBCollection col = db.getCollection("persons");
		return col.find();
	}

	public PageResult findPage(int fromItemIndex, int toItemIndex) {
		DB db = MongoProvider.connect().getDB("contact");
		DBCollection col = db.getCollection("persons");
		return new PageResult(col.find(), fromItemIndex, toItemIndex);
	}

	public PageResult findPage(PageRangeRequest range) {
		DB db = MongoProvider.connect().getDB("contact");
		DBCollection col = db.getCollection("persons");
		return new PageResult(col.find(), range.getFromIndex(),
				range.getToIndex());
	}
}
