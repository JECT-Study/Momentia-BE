package org.ject.momentia.api.artwork.model.type;

public enum ArtworkPostSort {
    VIEW("viewCount"),
    POPULAR("likeCount"),
    RECENT("createdAt");

    private final String columnName;

    // 생성자
    ArtworkPostSort(String columnName) {
        this.columnName = columnName;
    }

    // displayName을 반환하는 메서드
    public String getColumnName() {
        return columnName;
    }
}
