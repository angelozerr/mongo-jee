package com.mongodb.jee.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.apache.commons.io.IOUtils;
import org.bson.BSONObject;

import com.mongodb.jee.util.JSON;

public class BSONObjectProvider implements MessageBodyReader<BSONObject>,
		MessageBodyWriter<BSONObject> {

	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return BSONObject.class.isAssignableFrom(type);
	}

	public long getSize(BSONObject t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	public void writeTo(BSONObject BSONObject, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {

		//StringBuilder buf = new StringBuilder();
		JSON.serialize(BSONObject, entityStream);
		//entityStream.write(buf.toString().getBytes());
	}

	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return BSONObject.class.isAssignableFrom(type);
	}

	public BSONObject readFrom(Class<BSONObject> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		StringWriter writer = new StringWriter();
		IOUtils.copy(entityStream, writer);
		String json = writer.toString();

		return (BSONObject) JSON.parse(json);
	}

}
