package org.ject.momentia.api.collection.model.type;

public enum CollectionSort {
	RECENT("createdAt"),
	ALPHABETIC("name");

	private final String columnName;

	// 생성자
	CollectionSort(String columnName) {
		this.columnName = columnName;
	}

	// displayName을 반환하는 메서드
	public String getColumnName() {
		return columnName;
	}
}
