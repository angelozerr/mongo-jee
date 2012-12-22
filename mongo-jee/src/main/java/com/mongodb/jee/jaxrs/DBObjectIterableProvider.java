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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.mongodb.DBObject;
import com.mongodb.jee.util.BSONHelper;
import com.mongodb.jee.util.JSON;

/**
 * 
 * JAX-RS {@link Iterable<DBObject>} provider.
 * 
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DBObjectIterableProvider extends
		AbstractProvider<Iterable<DBObject>> {

	public void writeTo(Iterable<DBObject> cursor, Class<?> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		JSON.serialize(cursor, entityStream);
	}

	public Iterable<DBObject> readFrom(Class<Iterable<DBObject>> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		return BSONHelper.toIterable(entityStream);
	}

	@Override
	protected boolean isSupported(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		if (!Iterable.class.isAssignableFrom(type)) {
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

}
