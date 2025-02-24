package org.ject.momentia.api.artwork.model.dto;

import java.util.List;

public record ArtworkFollowingUserPostsResponse(
	List<FollowingUserModel> posts
) {
}
