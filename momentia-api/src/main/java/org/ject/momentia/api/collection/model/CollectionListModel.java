package org.ject.momentia.api.collection.model;

import lombok.Builder;

@Builder
public record CollectionListModel(
	Long collectionId,
	String collectionImage,
	String name,
	String collectionStatus
) {
}
