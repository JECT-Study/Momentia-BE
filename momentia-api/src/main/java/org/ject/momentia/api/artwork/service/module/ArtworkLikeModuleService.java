package org.ject.momentia.api.artwork.service.module;

import lombok.AllArgsConstructor;
import org.ject.momentia.api.artwork.repository.ArtworkLikeRepository;
import org.ject.momentia.common.domain.artwork.ArtworkLikeId;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArtworkLikeModuleService {
    private final ArtworkLikeRepository artworkLikeRepository;

    public Boolean isLiked(User user, ArtworkPost post){
        return user != null && artworkLikeRepository.existsById(new ArtworkLikeId(user, post));
    }

    public Boolean isLikedByPostId(User user,Long postId){
        return user != null && artworkLikeRepository.existsByPostIdAndUserId(postId, user);
    }

    public void deleteAllByArtwork(ArtworkPost artworkPost){
        artworkLikeRepository.deleteAllByArtwork(artworkPost);
    }

}
