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
package com.mongodb.jee.servlet;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoURI;

public class MongoProvider {

	private static final ConcurrentMap<String, Mongo> _mongos = new ConcurrentHashMap<String, Mongo>();
	private static Mongo mongo;

	private MongoProvider() {
	}

	public static Mongo connect() {
		if (mongo == null) {
			throw new RuntimeException("call MongoProvider#connect at firsts.");
		}
		return mongo;
	}

	public static Mongo connect(MongoURI mongoURI) throws UnknownHostException {
		return connect(mongoURI, false);
	}

	public static Mongo connect(MongoURI mongoURI, boolean defaultMongo)
			throws UnknownHostException {

		Mongo mongo = internalConnect(mongoURI);
		if (defaultMongo) {
			MongoProvider.mongo = mongo;
		}
		return mongo;
	}

	public static void disconnect() {
		if (mongo != null) {
			mongo.close();
		}
	}
	
	public static void dispose() {
		Collection<Mongo> m = _mongos.values();
		for (Mongo mongo : m) {
			mongo.close();
		}
		_mongos.clear();
	}

	/**
	 * Attempts to find an existing Mongo instance matching that URI in the
	 * holder, and returns it if exists. Otherwise creates a new Mongo instance
	 * based on this URI and adds it to the holder.
	 * 
	 * @param uri
	 *            the Mongo URI
	 * @return
	 * @throws MongoException
	 * @throws UnknownHostException
	 */
	public static Mongo internalConnect(MongoURI uri)
			throws UnknownHostException {

		String key = _toKey(uri);

		Mongo m = _mongos.get(key);
		if (m != null)
			return m;

		m = new Mongo(uri);

		Mongo temp = _mongos.putIfAbsent(key, m);
		if (temp == null) {
			// ours got in
			return m;
		}

		// there was a race and we lost
		// close ours and return the other one
		m.close();
		return temp;
	}

	private static String _toKey(MongoURI uri) {
		StringBuilder buf = new StringBuilder();
		for (String h : uri.getHosts())
			buf.append(h).append(",");
		buf.append(uri.getOptions());
		buf.append(uri.getUsername());
		return buf.toString();
	}

	public static void disconnect(MongoURI mongoURI) {
		String key = _toKey(mongoURI);
		Mongo m = _mongos.get(key);
		if (m != null) {
			m.close();
		}
		_mongos.remove(m);
		
	}
}
