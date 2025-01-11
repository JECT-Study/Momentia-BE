package org.ject.momentia.api.pagination.model;

import java.util.List;

public record PaginationWithIsMineResponse<T>(
	Boolean isMine,
	List<T> data,
	PaginationModel page
) {
}
