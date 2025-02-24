package org.ject.momentia.api.monthly.model;

import java.util.List;

import org.ject.momentia.api.artwork.model.dto.ArtworkPostModel;

public record MonthlyPostsResponse(
	List<ArtworkPostModel> posts
) {

}
