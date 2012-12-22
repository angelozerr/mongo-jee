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
 * 
 * <cite> On your server, you should look at the header in the request to know
 * which items to return. The server should respond with a Content-Range: header
 * to indicate how many items are being returned and how many total items exist.
 * For example:
 * 
 * Content-Range: items 0-24/66
 * 
 * The returned total is available as a further promise on the returned promise
 * of data which returns the total number of available rows indicated in the
 * Content-Range: header as a number, so you can retrieve it like this:
 * 
 * </cite>
 * 
 * @see http://dojotoolkit.org/reference-guide/1.8/dojo/store/JsonRest.html#id7
 */
public class PageRangeResponse extends PageRangeRequest {

	private final int totalItems;

	public PageRangeResponse(int fromIndex, int toIndex, int totalItems) {
		super(fromIndex, toIndex);
		this.totalItems = totalItems;
	}

	public int getTotalItems() {
		return totalItems;
	}

}
