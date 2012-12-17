package com.mongodb.jee.service;

import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.mongodb.DBObject;
import com.mongodb.jee.PageResult;

import dojo.store.RequestPageRange;

@Path("/")
public interface PersonService {

	@GET
	@Path("/findOne")
	@Produces(MediaType.APPLICATION_JSON)
	DBObject findOne();

	@GET
	@Path("/find")
	@Produces(MediaType.APPLICATION_JSON)
	Iterator<DBObject> find();

	@GET
	@Path("/findPage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	PageResult findPage(@QueryParam("from") int fromItemIndex,
			@QueryParam("to") int toItemIndex);

	@GET
	@Path("/findPageRange")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	PageResult findPage(@QueryParam("range") RequestPageRange range);

}
