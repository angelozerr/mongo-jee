package dojo.store;

public class RequestPageRange {

	private final int fromIndex;
	private final int toIndex;

	public RequestPageRange(int fromIndex, int toIndex) {
		this.fromIndex = fromIndex;
		this.toIndex = toIndex;
	}

	public int getFromIndex() {
		return fromIndex;
	}

	public int getToIndex() {
		return toIndex;
	}
}
