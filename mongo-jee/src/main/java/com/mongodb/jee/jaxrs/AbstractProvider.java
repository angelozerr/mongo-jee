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

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

/**
 * Abstract class for JAX-RS Provider.
 * 
 * @param <T>
 */
public abstract class AbstractProvider<T> implements MessageBodyReader<T>,
		MessageBodyWriter<T> {

	public long getSize(T t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		// Return -1 if the content length cannot be determined
		return -1;
	}

	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return isSupported(type, genericType, annotations, mediaType);
	}

	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return isSupported(type, genericType, annotations, mediaType);
	}

	/**
	 * Returns true if the given class type, media type is supported and false
	 * otherwise.
	 * 
	 * @param type
	 * @param genericType
	 * @param annotations
	 * @param mediaType
	 * @return
	 */
	protected abstract boolean isSupported(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType);
}
