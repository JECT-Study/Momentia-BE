package org.ject.momentia.api.pagination.converter;

import org.ject.momentia.api.pagination.model.PaginationModel;
import org.springframework.data.domain.Page;

public class PaginationConverter {
    public static PaginationModel pageToPaginationModel(Page<?> data){
        return PaginationModel.builder()
                .isFirstPage(data.isFirst())
                .isLastPage(data.isLast())
                .requestSize(data.getSize())
                .requestPage(data.getNumber())
                .totalDataCnt(data.getTotalElements())
                .totalPages(data.getTotalPages())
                .build();
    }
}
