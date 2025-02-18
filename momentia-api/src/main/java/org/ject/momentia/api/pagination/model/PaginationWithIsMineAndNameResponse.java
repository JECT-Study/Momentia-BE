package org.ject.momentia.api.pagination.model;

import java.util.List;

public record PaginationWithIsMineAndNameResponse<T>(
	Boolean isMine,
	String name,
	List<T> data,
	PaginationModel page
) {
}
