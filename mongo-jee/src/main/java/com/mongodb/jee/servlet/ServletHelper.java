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

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Helper to serialize object to JSON with {@link HttpServletResponse}.
 * 
 */
public class ServletHelper {

	private static final String UTF_8 = "UTF-8";
	private static final String APPLICATION_JSON = "application/json";

	/**
	 * Serializes the given object into its JSON form and write the result in
	 * the given HTTP response writer.
	 * 
	 * @param o
	 *            object to serialize.
	 * @param response
	 *            the HTTP response.
	 * @throws IOException
	 */
	public static void writeJson(Object o, HttpServletResponse response)
			throws IOException {
		writeJson(o, response, UTF_8);
	}

	/**
	 * Serializes the given object into its JSON form and write the result in
	 * the given HTTP response writer.
	 * 
	 * @param o
	 *            object to serialize.
	 * @param response
	 *            the HTTP response.
	 * @param encoding
	 *            the character encoding to use for the HTTP response.
	 * @throws IOException
	 */
	public static void writeJson(Object o, HttpServletResponse response,
			String encoding) throws IOException {
		// HTTP response is JSON
		response.setCharacterEncoding(encoding);
		response.setContentType(APPLICATION_JSON);

		// Loop for DB cursor and write each DB document in the HTTP writer
		Writer writer = response.getWriter();

		// Serialize object as JSON and write the result in the HTTP response
		// writer.
		com.mongodb.jee.util.JSON.serialize(o, writer);

		writer.flush();

	}
}
