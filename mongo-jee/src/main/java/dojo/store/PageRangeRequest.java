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

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dojo.jaxb.PageRangeRequestAdapter;

/**
 * 
 * <cite> The store uses HTTP's Range: header to perform paging. When a request
 * is made for a range of items, the store will include a Range: header with an
 * items range unit specifying the range. For example:
 * 
 * Range: items=0-24 
 * 
 * </cite>
 * 
 * @see http://dojotoolkit.org/reference-guide/1.8/dojo/store/JsonRest.html#id7
 */
@XmlJavaTypeAdapter(PageRangeRequestAdapter.class)
public class PageRangeRequest {

	private final int fromIndex;
	private final int toIndex;

	public PageRangeRequest(int fromIndex, int toIndex) {
		this.fromIndex = fromIndex;
		this.toIndex = toIndex;
	}

	public int getFromIndex() {
		return fromIndex;
	}

	public int getToIndex() {
		return toIndex;
	}

	@Override
	public String toString() {
		return new StringBuilder("items=").append(getFromIndex()).append("-")
				.append(getToIndex()).toString();
	}
}
