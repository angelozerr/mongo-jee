package dojo.store;

public class JsonRestHelper {

	public static final String RANGE_REQUEST_HEADER = "Range";

	public static final String CONTENT_RANGE_RESPONSE_HEADER = "Content-Range";

	public static PageRangeRequest getRange(String range) {
		if (range == null || range.length() < 1) {
			return null;
		}
		String items = range.substring("items=".length(), range.length());
		int index = items.indexOf("-");
		int fromIndex = Integer.parseInt(items.substring(0, index));
		int toIndex = Integer.parseInt(items.substring(index + 1,
				items.length()));
		return new PageRangeRequest(fromIndex, toIndex);
	}

	public static String getContentRange(int fromItemIndex, int toItemIndex,
			int totalItems) {
		return new StringBuilder("items=").append(fromItemIndex).append("-")
				.append(toItemIndex).append("/").append(totalItems).toString();
	}

	public static PageRangeResponse getResponseRange(String contentRange) {
		if (contentRange == null || contentRange.length() < 1) {
			return null;
		}
		int indexTotal = contentRange.lastIndexOf('/');

		String items = contentRange.substring("items=".length(), indexTotal);
		int index = items.indexOf("-");
		int fromIndex = Integer.parseInt(items.substring(0, index));
		int toIndex = Integer.parseInt(items.substring(index + 1,
				items.length()));

		int totalItems = Integer.parseInt(contentRange.substring(
				indexTotal + 1, contentRange.length()));

		return new PageRangeResponse(fromIndex, toIndex, totalItems);
	}

}
