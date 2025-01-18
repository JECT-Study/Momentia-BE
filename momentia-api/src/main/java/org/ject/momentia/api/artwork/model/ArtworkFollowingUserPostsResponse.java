package org.ject.momentia.api.artwork.model;

import java.util.List;

public record ArtworkFollowingUserPostsResponse(
	List<FollowingUserModel> posts
) {
}
