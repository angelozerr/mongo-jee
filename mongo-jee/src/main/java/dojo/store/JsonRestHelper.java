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
package dojo.store;

/**
 * Dojo Helper for using Dojo JsonRest store on server side.
 * 
 * @see http 
 *      ://dojotoolkit.org/reference-guide/1.8/dojox/data/JsonRestStore.html#id9
 * 
 */
public class JsonRestHelper {

	public static final String RANGE_REQUEST_HEADER = "Range";

	public static final String CONTENT_RANGE_RESPONSE_HEADER = "Content-Range";

	private static final String SEPARATOR = "-";

	private static final String ITEMS_TOKEN = "items=";

	/**
	 * Parse the given range String (coming from the HTTP request) and returns
	 * an instance {@link PageRangeRequest}.
	 * 
	 * @param range
	 *            the page range which follows the given pattern items=$from-$to
	 *            (ex:items=0-9).
	 * @return
	 */
	public static PageRangeRequest getRange(String range) {
		if (range == null || range.length() < 1) {
			return null;
		}
		String items = range.substring(ITEMS_TOKEN.length(), range.length());
		int index = items.indexOf(SEPARATOR);
		int fromIndex = Integer.parseInt(items.substring(0, index));
		int toIndex = Integer.parseInt(items.substring(index + 1,
				items.length()));
		return new PageRangeRequest(fromIndex, toIndex);
	}

	/**
	 * Returns the String value of the Content-Range by using the given from,to
	 * and total (items=$from-$to/$total).
	 * 
	 * @param fromItemIndex
	 * @param toItemIndex
	 * @param totalItems
	 * @return
	 */
	public static String getContentRange(int fromItemIndex, int toItemIndex,
			int totalItems) {
		return new StringBuilder(ITEMS_TOKEN).append(fromItemIndex)
				.append(SEPARATOR).append(toItemIndex).append("/")
				.append(totalItems).toString();
	}

	/**
	 * Parse the given content range String (coming from the HTTP response) and
	 * returns an instance {@link PageRangeResponse}.
	 * 
	 * @param contentRange
	 *            the content range which follows the given pattern
	 *            items=$from-$to/$total (ex:items=0-9/100).
	 * @return
	 */
	public static PageRangeResponse getResponseRange(String contentRange) {
		if (contentRange == null || contentRange.length() < 1) {
			return null;
		}
		int indexTotal = contentRange.lastIndexOf('/');

		String items = contentRange.substring(ITEMS_TOKEN.length(), indexTotal);
		int index = items.indexOf(SEPARATOR);
		int fromIndex = Integer.parseInt(items.substring(0, index));
		int toIndex = Integer.parseInt(items.substring(index + 1,
				items.length()));

		int totalItems = Integer.parseInt(contentRange.substring(
				indexTotal + 1, contentRange.length()));

		return new PageRangeResponse(fromIndex, toIndex, totalItems);
	}

}
