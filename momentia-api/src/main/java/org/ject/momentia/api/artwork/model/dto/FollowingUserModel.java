package org.ject.momentia.api.artwork.model.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
