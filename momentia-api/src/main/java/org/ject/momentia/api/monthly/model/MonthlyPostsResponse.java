package org.ject.momentia.api.monthly.model;

import org.ject.momentia.api.artwork.model.ArtworkPostModel;

import java.util.List;

public record MonthlyPostsResponse (
        List<ArtworkPostModel> posts
){

}
