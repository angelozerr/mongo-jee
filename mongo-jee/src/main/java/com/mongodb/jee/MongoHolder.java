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
package com.mongodb.jee;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoURI;

/**
 * Holder of {@link Mongo} instance which manages connect/disconnection of Mongo
 * and default connection.
 * 
 */
public class MongoHolder {

	private static final ConcurrentMap<MongoURI, Mongo> mongosCache = new ConcurrentHashMap<MongoURI, Mongo>();

	private static MongoURI defaultMongoURI;
	private static Mongo defaultMongo;

	private MongoHolder() {
	}

	/**
	 * Returns the default {@link Mongo} instance. To set the default instance,
	 * {@link MongoHolder#connect(MongoURI, boolean)} must be called before this
	 * method with true parameter.
	 * 
	 * @return
	 */
	public static Mongo connect() {
		if (defaultMongo == null) {
			throw new RuntimeException("call MongoHolder#connect at first.");
		}
		return defaultMongo;
	}

	/**
	 * Returns the default Mongo uri and null otherwise.
	 * 
	 * @return
	 */
	public static MongoURI getDefaultMongoURI() {
		return defaultMongoURI;
	}

	/**
	 * Returns the default Mongo and null otherwise.
	 * 
	 * @return
	 */
	public static Mongo getDefaultMongo() {
		return defaultMongo;
	}

	/**
	 * Connect to the MongoDB with given uri.
	 * 
	 * @param mongoURI
	 *            the mongo uri.
	 * @return
	 * @throws UnknownHostException
	 */
	public static Mongo connect(MongoURI mongoURI) throws UnknownHostException {
		return connect(mongoURI, false);
	}

	/**
	 * Connect to the MongoDB with given uri.
	 * 
	 * @param mongoURI
	 *            the mongo uri.
	 * @param defaultMongo
	 *            true if it's the default connection and false otherwise.
	 * @return
	 * @throws UnknownHostException
	 */
	public static Mongo connect(MongoURI mongoURI, boolean defaultMongo)
			throws UnknownHostException {

		Mongo mongo = internalConnect(mongoURI);
		if (defaultMongo) {
			MongoHolder.defaultMongo = mongo;
			MongoHolder.defaultMongoURI = mongoURI;
		}
		return mongo;
	}

	/**
	 * Disconnect the default mongo.
	 * 
	 */
	public static void disconnect() {
		disconnect(defaultMongoURI);
		defaultMongo = null;
		defaultMongoURI = null;
	}

	/**
	 * Disconnect the whole mongo instances.
	 */
	public static void dispose() {
		disconnect();
		Collection<Mongo> mongos = mongosCache.values();
		for (Mongo mongo : mongos) {
			mongo.close();
		}
		mongosCache.clear();
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

		Mongo m = mongosCache.get(uri);
		if (m != null)
			return m;

		m = new Mongo(uri);

		Mongo temp = mongosCache.putIfAbsent(uri, m);
		if (temp == null) {
			// ours got in
			return m;
		}

		// there was a race and we lost
		// close ours and return the other one
		m.close();
		return temp;
	}

	/**
	 * Disconnect the mongo instance with the given Mongo uri.
	 * 
	 * @param mongoURI
	 *            the Mongo URI
	 */
	public static void disconnect(MongoURI mongoURI) {
		if (mongoURI == null) {
			return;
		}
		Mongo mongo = mongosCache.get(mongoURI);
		if (mongo != null) {
			mongo.close();
			mongosCache.remove(mongoURI);
		}

	}

}
