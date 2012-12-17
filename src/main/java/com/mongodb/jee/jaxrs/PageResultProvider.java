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
import com.mongodb.jee.PageResult;
import com.mongodb.jee.util.JSON;

public class PageResultProvider implements MessageBodyWriter<PageResult> {

	
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return PageResult.class.isAssignableFrom(type);
	}

	
	public long getSize(PageResult page, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	
	public void writeTo(PageResult page, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {

		setContentRange(httpHeaders, page.getFromItemIndex(),
				page.getToItemIndex(), page.getTotalItems());

		DBCursor cursor = page.getItems();
		JSON.serialize(cursor, entityStream);
	}

	private void setContentRange(MultivaluedMap<String, Object> httpHeaders,
			int fromItemIndex, int toItemIndex, int totalItems) {
		String range = getContentRange(fromItemIndex, toItemIndex, totalItems);
		httpHeaders.putSingle("Content-Range", range);
	}

	protected String getContentRange(int fromItemIndex, int toItemIndex,
			int totalItems) {
		return new StringBuilder("items=").append(fromItemIndex).append("-")
				.append(toItemIndex).append("/").append(totalItems).toString();
	}

}
