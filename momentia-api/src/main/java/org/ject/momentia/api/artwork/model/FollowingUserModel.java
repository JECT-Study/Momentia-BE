package org.ject.momentia.api.artwork.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Builder
@Getter
@Setter
public class FollowingUserModel {
    Long userId;
    String nickname;
    String userImage;
    Boolean isFollow;
    String userField;
    List<ArtworkFollowingUserPostModel> posts;
}
