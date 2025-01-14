package org.ject.momentia.api.collection.model.type;

public enum CollectionSort {
	RECENT("createdAt"),
	ALPHABETIC("name");

	private final String columnName;

	CollectionSort(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnName() {
		return columnName;
	}
}
