package com.mongodb.jee.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.mongodb.DBObject;

@Path("/")
public interface PersonService {

	@GET
	@Path("/findOne")	
	public DBObject findOne();
}
