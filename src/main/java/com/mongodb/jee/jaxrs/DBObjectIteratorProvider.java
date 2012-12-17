package com.mongodb.jee.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.apache.commons.io.IOUtils;
import org.bson.types.BasicBSONList;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.jee.util.BSONHelper;
import com.mongodb.jee.util.JSON;

public class DBObjectIteratorProvider implements
		MessageBodyReader<Iterator<DBObject>>,
		MessageBodyWriter<Iterator<DBObject>> {

	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		if (!Iterator.class.isAssignableFrom(type)) {
			return false;
		}
		if (genericType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) genericType;		
			Type[] actualTypeArguments = parameterizedType
					.getActualTypeArguments();
			for (int i = 0; i < actualTypeArguments.length; i++) {
				Class<?> a = (Class<?>) actualTypeArguments[i];
				if (DBObject.class.isAssignableFrom(a)) {
					return true;
				}
			}
		}
		return false;
	}

	public long getSize(Iterator<DBObject> t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	public void writeTo(Iterator<DBObject> cursor, Class<?> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		JSON.serialize(cursor, entityStream);
	}

	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return isWriteable(type, genericType, annotations, mediaType);
	}

	public Iterator<DBObject> readFrom(Class<Iterator<DBObject>> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		return BSONHelper.toIterator(entityStream);
	}

}
