/**
 *   Copyright (C) 2012 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.mongodb.jee.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.mongodb.DBObject;
import com.mongodb.jee.PageResult;
import com.mongodb.jee.util.BSONHelper;
import com.mongodb.jee.util.JSON;

import dojo.store.JsonRestHelper;
import dojo.store.PageRangeResponse;

/**
 * 
 * JAX-RS {@link PageResult} provider.
 * 
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PageResultProvider extends AbstractProvider<PageResult> {

	public void writeTo(PageResult page, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {

		// set HTTP Header response with paging info :
		// ContentRange : $from-$to/$total
		setContentRange(httpHeaders, page.getFromItemIndex(),
				page.getToItemIndex(), page.getTotalItems());

		// Write the Iterable<DBObject> (like DBCursor) as JSON array stream.
		Iterable<DBObject> cursor = page.getItems();
		JSON.serialize(cursor, entityStream);
	}

	/**
	 * Set HTTP Header response with paging info : ContentRange :
	 * $from-$to/$total
	 * 
	 * @param httpHeaders
	 * @param fromItemIndex
	 * @param toItemIndex
	 * @param totalItems
	 */
	protected void setContentRange(MultivaluedMap<String, Object> httpHeaders,
			int fromItemIndex, int toItemIndex, int totalItems) {
		String range = JsonRestHelper.getContentRange(fromItemIndex,
				toItemIndex, totalItems);
		httpHeaders.putSingle(JsonRestHelper.CONTENT_RANGE_RESPONSE_HEADER,
				range);
	}

	public PageResult readFrom(Class<PageResult> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		// Buildthe range from the HTTP Response Header ContentRange : $from-$to/$total 
		PageRangeResponse range = getContentRange(httpHeaders);

		Iterable<DBObject> items = BSONHelper.toIterable(entityStream);

		int fromItemIndex = range.getFromIndex();
		int toItemIndex = range.getToIndex();
		int totalItems = range.getTotalItems();
		return new PageResult(items, fromItemIndex, toItemIndex, totalItems);
	}

	protected PageRangeResponse getContentRange(
			MultivaluedMap<String, String> httpHeaders) {
		String contentRange = httpHeaders
				.getFirst(JsonRestHelper.CONTENT_RANGE_RESPONSE_HEADER);
		return JsonRestHelper.getResponseRange(contentRange);
	}

	/**
	 * Returns true if the given type is a {@link PageResult} and the given
	 * media type is "application/json".
	 * 
	 * @param type
	 *            clas type.
	 * @param mediaType
	 *            media type.
	 * @return
	 */
	@Override
	protected boolean isSupported(java.lang.Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return PageResult.class.isAssignableFrom(type)
				&& mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
	}

}
