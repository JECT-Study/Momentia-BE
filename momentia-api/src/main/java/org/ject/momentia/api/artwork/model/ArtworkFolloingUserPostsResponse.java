package org.ject.momentia.api.artwork.model;

import java.util.List;

public record ArtworkFolloingUserPostsResponse(
        List<FollowingUserModel> posts
) {
}
