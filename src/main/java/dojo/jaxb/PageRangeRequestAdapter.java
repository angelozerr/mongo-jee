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
