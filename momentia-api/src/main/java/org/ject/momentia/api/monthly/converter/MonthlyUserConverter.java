package org.ject.momentia.api.monthly.converter;

import org.ject.momentia.api.monthly.model.UserListModel;
import org.ject.momentia.common.domain.user.User;

public class MonthlyUserConverter {
    public static UserListModel toUserListModel(User user,String profileImage,String artworkImage,Boolean isFollow){
        return UserListModel.builder()
                .userId(user.getId())
                .artworkImage(artworkImage)
                .nickname(user.getNickname())
                .userField(user.getField() == null ? null : user.getField().name())
                .introduction(user.getIntroduction())
                .isFollow(isFollow)
                .artworkImage(artworkImage)
                .build();
    }
}
