package org.ject.momentia.api.pagination.model;


import lombok.Builder;

@Builder
public record PaginationModel(
        Long totalDataCnt,
        Integer totalPages,
        Boolean isLastPage,
        Boolean isFirstPage,
        Integer requestPage,
        Integer requestSize
) {

}
