package com.mongodb.jee.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import com.mongodb.DBObject;
import com.mongodb.jee.PageResult;
import com.mongodb.jee.util.BSONHelper;
import com.mongodb.jee.util.JSON;

import dojo.store.JsonRestHelper;
import dojo.store.ResponsePageRange;

public class PageResultProvider implements MessageBodyReader<PageResult>,
		MessageBodyWriter<PageResult> {

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

		Iterator<DBObject> cursor = page.getItems();
		JSON.serialize(cursor, entityStream);
	}

	protected void setContentRange(MultivaluedMap<String, Object> httpHeaders,
			int fromItemIndex, int toItemIndex, int totalItems) {
		String range = JsonRestHelper.getContentRange(fromItemIndex,
				toItemIndex, totalItems);
		httpHeaders.putSingle(JsonRestHelper.CONTENT_RANGE_RESPONSE_HEADER,
				range);
	}

	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return PageResult.class.isAssignableFrom(type);
	}

	public PageResult readFrom(Class<PageResult> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		ResponsePageRange range = getContentRange(httpHeaders);

		Iterator<DBObject> items = BSONHelper.toIterator(entityStream);

		int fromItemIndex = range.getFromIndex();
		int toItemIndex = range.getToIndex();
		int totalItems = range.getTotalItems();
		return new PageResult(items, fromItemIndex, toItemIndex, totalItems);
	}

	protected ResponsePageRange getContentRange(
			MultivaluedMap<String, String> httpHeaders) {
		String contentRange = httpHeaders
				.getFirst(JsonRestHelper.CONTENT_RANGE_RESPONSE_HEADER);
		return JsonRestHelper.getResponseRange(contentRange);
	}

}
