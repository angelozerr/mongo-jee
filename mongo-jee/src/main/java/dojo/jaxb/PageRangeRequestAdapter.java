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
package dojo.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import dojo.store.JsonRestHelper;
import dojo.store.PageRangeRequest;

public class PageRangeRequestAdapter extends XmlAdapter<String, PageRangeRequest> {

	@Override
	public String marshal(PageRangeRequest range) throws Exception {
		return range.toString();
	}

	@Override
	public PageRangeRequest unmarshal(String range) throws Exception {
		return JsonRestHelper.getRange(range);
	}

}
