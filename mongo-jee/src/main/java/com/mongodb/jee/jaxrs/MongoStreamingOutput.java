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
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.jee.util.JSON;

/**
 * JAX-RS {@link StreamingOutput} implementation to write Mongo object (
 * {@link DBObject}, {@link DBCursor}, etc) as JSON with the {@link JSON}.
 * 
 */
public class MongoStreamingOutput implements StreamingOutput {

	private final Object object;

	public MongoStreamingOutput(Object object) {
		this.object = object;
	}

	public void write(OutputStream out) throws IOException,
			WebApplicationException {
		com.mongodb.jee.util.JSON.serialize(object, out);
	}
}
