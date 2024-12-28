package org.ject.momentia.api.collection.model;

import java.util.List;

public record CollecionListResponse (
        List<CollectionListModel> collections
){
}
