package com.mongodb.jee.jaxrs;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import com.mongodb.DBCursor;
import com.mongodb.jee.util.JSON;

public class DBCursorProvider implements MessageBodyWriter<DBCursor> {

	
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return DBCursor.class.isAssignableFrom(type);
	}

	
	public long getSize(DBCursor t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	
	public void writeTo(DBCursor cursor, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		JSON.serialize(cursor, entityStream);
	}

}
