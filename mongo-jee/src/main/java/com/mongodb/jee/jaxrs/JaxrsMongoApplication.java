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

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * Mongo JEE JAX-RS Application registers the Mongo JEE Provider. To use it,
 * extend this class and register your REST services with :
 * 
 * <ul>
 * <li> {@link JaxrsMongoApplication#addClass(Class)} if you wish to register
 * JAX-RS Service which is instantiated for each request. Ex :
 * 
 * <code>addClass(MyService.class);</code></li>
 * </li>
 * <li>{@link JaxrsMongoApplication#addSingleton(Object)} if you wish to
 * register JAX-RS Service as singleton. Ex :
 * 
 * <code>addSingleton(new MyService());</code></li>
 * </li>
 * </ul>
 * 
 */
public class JaxrsMongoApplication extends Application {

	private final Set<Class<?>> classes;
	private final Set<Object> singletons;

	public JaxrsMongoApplication() {
		this.classes = new HashSet<Class<?>>();
		addClass(BSONObjectProvider.class);
		addClass(DBObjectIterableProvider.class);
		addClass(PageResultProvider.class);
		this.singletons = new HashSet<Object>();
	}

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}

	/**
	 * Add root resource or provider classes.
	 * 
	 * @param clazz
	 */
	protected void addClass(Class<?> clazz) {
		classes.add(clazz);
	}

	/**
	 * Add singleton.
	 * 
	 * @param object
	 */
	protected void addSingleton(Object object) {
		singletons.add(object);
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
