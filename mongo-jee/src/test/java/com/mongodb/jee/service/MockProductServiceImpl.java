package com.mongodb.jee.service;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.jee.PageResult;

import dojo.store.PageRangeRequest;

public class MockProductServiceImpl implements ProductService {

	private final List<DBObject> products;

	public MockProductServiceImpl() {
		this.products = new ArrayList<DBObject>();
		for (int i = 0; i < 100; i++) {
			BasicDBObject product = new BasicDBObject();
			product.put("title", "product [" + i + "]");
			products.add(product);
		}
	}

	public DBObject findOne() {
		// DB db = MongoProvider.getMongo().getDB("ecommerce");
		// DBCollection col = db.getCollection("products");
		// return col.findOne();
		return products.get(0);
	}

	public Iterable<DBObject> find() {
		// DB db = MongoProvider.getMongo().getDB("ecommerce");
		// DBCollection col = db.getCollection("products");
		// return col.find();
		return products;
	}

	public PageResult findPage(int fromItemIndex, int toItemIndex) {
		// DB db = MongoProvider.getMongo().getDB("ecommerce");
		// DBCollection col = db.getCollection("products");
		// return new PageResult(col.find(), fromItemIndex, toItemIndex);
		return new PageResult(products.subList(fromItemIndex, toItemIndex),
				fromItemIndex, toItemIndex, products.size());
	}

	public PageResult findPage(PageRangeRequest range) {
		// DB db = MongoProvider.getMongo().getDB("ecommerce");
		// DBCollection col = db.getCollection("products");
		// return new PageResult(col.find(), range.getFromIndex(),
		// range.getToIndex());
		int fromItemIndex = range.getFromIndex();
		int toItemIndex = range.getToIndex();
		return new PageResult(products.subList(fromItemIndex, toItemIndex),
				fromItemIndex, toItemIndex, products.size());
	}
}
