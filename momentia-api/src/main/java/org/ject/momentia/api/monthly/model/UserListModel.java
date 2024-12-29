package org.ject.momentia.api.monthly.model;

import lombok.Builder;

@Builder
public record UserListModel (
        Long userId,
        String profileImage,
        String nickname,
        String userField,
        String introduction,
        Boolean isFollow,
        String artworkImage
){
}
