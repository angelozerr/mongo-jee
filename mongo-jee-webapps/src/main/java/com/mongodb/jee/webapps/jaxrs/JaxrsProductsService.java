package com.mongodb.jee.webapps.jaxrs;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.jee.PageResult;
import com.mongodb.jee.servlet.MongoProvider;

import dojo.store.PageRangeRequest;

@Path("/")
public class JaxrsProductsService {

	@GET
	@Path("/init")
	public void initializeData() {
		DB db = MongoProvider.connect().getDB("ecommerce");
		DBCollection products = db.getCollection("products");
		DBObject product = null;
		for (int i = 0; i < 20000; i++) {
			product = new BasicDBObject();
			product.put("title", "Product title [" + i + "]");
			product.put("description", "bla bla bla [" + i + "]");
			products.insert(product);
		}
	}

	@GET
	@Path("/products")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PageResult findProducts(@HeaderParam("Range") PageRangeRequest range) {
		DB db = MongoProvider.connect().getDB("ecommerce");
		DBCollection products = db.getCollection("products");
		return new PageResult(products.find(), range.getFromIndex(),
				range.getToIndex());
	}

}
