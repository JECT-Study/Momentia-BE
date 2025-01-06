package org.ject.momentia.api.pagination.model;

import java.util.List;

public record PaginationResponse<T> (
        List<T> data,
        PaginationModel page
){
}
