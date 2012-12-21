package com.mongodb.jee.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.bson.types.BasicBSONList;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class BSONHelper {

	public static Iterable<DBObject> toIterable(InputStream entityStream)
			throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(entityStream, writer);
		String json = writer.toString();

		BasicDBList o = (BasicDBList) JSON.parse(json);
		return BSONHelper.toIterable(o);
	}

	public static Iterable<DBObject> toIterable(BasicBSONList list) {
		final Iterator<DBObject> it = toIterator(list);
		return new Iterable<DBObject>() {

			public Iterator<DBObject> iterator() {
				return it;
			}
		};
	}

	public static Iterator<DBObject> toIterator(BasicBSONList list) {
		return new BasicBSONListWrapper(list);
	}

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
