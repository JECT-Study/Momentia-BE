package org.ject.momentia.api.follow.model;

import java.util.List;

public record FollowResponse(
	List<FollowInfo> users
) {
}
