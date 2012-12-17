package dojo.store;

public class ResponsePageRange extends RequestPageRange {

	private final int totalItems;

	public ResponsePageRange(int fromIndex, int toIndex, int totalItems) {
		super(fromIndex, toIndex);
		this.totalItems = totalItems;
	}

	public int getTotalItems() {
		return totalItems;
	}

}
