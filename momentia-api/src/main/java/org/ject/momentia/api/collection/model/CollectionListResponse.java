package org.ject.momentia.api.collection.model;

import java.util.List;

public record CollectionListResponse(
	List<CollectionListModel> collections
) {
}
