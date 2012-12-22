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
