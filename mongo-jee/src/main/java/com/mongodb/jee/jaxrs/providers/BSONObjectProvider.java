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
package com.mongodb.jee.jaxrs.providers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.bson.BSONObject;

import com.mongodb.jee.util.JSON;

/**
 * 
 * JAX-RS {@link BSONObject} provider.
 * 
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BSONObjectProvider extends AbstractProvider<BSONObject> {

	public void writeTo(BSONObject BSONObject, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {

		// Write the BSON object as JSON stream.
		JSON.serialize(BSONObject, entityStream);
	}

	public BSONObject readFrom(Class<BSONObject> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		// Parse the input JSON stream to BSONObject
		// FIXME : JSON#parse should manage stream and not only String.
		StringWriter writer = new StringWriter();
		IOUtils.copy(entityStream, writer);
		String json = writer.toString();

		return (BSONObject) JSON.parse(json);
	}

	/**
	 * Returns true if the given type is a {@link BSONObject} and the given
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
		return BSONObject.class.isAssignableFrom(type)
				&& mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
	}

}
