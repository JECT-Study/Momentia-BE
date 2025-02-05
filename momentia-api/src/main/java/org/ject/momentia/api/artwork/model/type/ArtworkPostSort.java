package org.ject.momentia.api.artwork.model.type;

public enum ArtworkPostSort {
	VIEW("viewCount"),
	POPULAR("likeCount"),
	RECENT("createdAt");

	private final String columnName;

	ArtworkPostSort(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnName() {
		return columnName;
	}
}
