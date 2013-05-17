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
package com.mongodb.jee.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;

import org.bson.types.BasicBSONList;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.jee.util.org.apache.commons.io.IOUtils;

/**
 * Helper classes for BSON object.
 * 
 */
public class BSONHelper {

	/**
	 * Transform the JSON array stream to iterable DBObject
	 * 
	 * @param entityStream
	 *            the JSON array stream.
	 * @return
	 * @throws IOException
	 */
	public static Iterable<DBObject> toIterable(InputStream entityStream)
			throws IOException {

		// FIXME : JSON#parse should manage stream and not only String.
		StringWriter writer = new StringWriter();
		IOUtils.copy(entityStream, writer);
		String json = writer.toString();

		BasicDBList o = (BasicDBList) JSON.parse(json);
		return BSONHelper.toIterable(o);
	}

	/**
	 * Transform the BSON array to iterable DBObject.
	 * 
	 * @param list
	 * @return
	 */
	public static Iterable<DBObject> toIterable(BasicBSONList list) {
		final Iterator<DBObject> it = toIterator(list);
		return new Iterable<DBObject>() {

			public Iterator<DBObject> iterator() {
				return it;
			}
		};
	}

	/**
	 * Transform the BSON array to iterator DBObject.
	 * 
	 * @param list
	 * @return
	 */
	public static Iterator<DBObject> toIterator(BasicBSONList list) {
		return new BasicBSONListWrapper(list);
	}

	/**
	 * BasicBSONList wrapper.
	 * 
	 */
	static class BasicBSONListWrapper implements Iterator<DBObject> {

		private final Iterator<Object> it;

		public BasicBSONListWrapper(BasicBSONList list) {
			this.it = list.iterator();
		}

		public boolean hasNext() {
			return it.hasNext();
		}

		public DBObject next() {
			return (DBObject) it.next();
		}

		public void remove() {
			it.remove();
		}
	}
}
