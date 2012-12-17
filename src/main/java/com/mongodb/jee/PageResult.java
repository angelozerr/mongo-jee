package com.mongodb.jee;

import com.mongodb.DBCursor;

public class PageResult {

	private final DBCursor items;
	private final int fromItemIndex;
	private final int toItemIndex;
	private final int totalItems;

	public PageResult(DBCursor cursor, int fromItemIndex, int toItemIndex) {
		this(cursor.skip(fromItemIndex).limit(toItemIndex - fromItemIndex + 1),
				fromItemIndex, toItemIndex, cursor.count());
	}

	public PageResult(DBCursor items, int fromItemIndex, int toItemIndex,
			int totalItems) {
		this.items = items;
		this.fromItemIndex = fromItemIndex;
		this.toItemIndex = toItemIndex;
		this.totalItems = totalItems;
	}

	public DBCursor getItems() {
		return items;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public int getFromItemIndex() {
		return fromItemIndex;
	}

	public int getToItemIndex() {
		return toItemIndex;
	}
}
