package org.ject.momentia.api.artwork.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.ject.momentia.api.artwork.repository.ArtworkLikeRepository;
import org.ject.momentia.api.artwork.repository.ArtworkPostRepository;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.common.domain.artwork.ArtworkLike;
import org.ject.momentia.common.domain.artwork.ArtworkLikeId;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArtworkLikeService {

    private final ArtworkPostRepository artworkPostRepository;
    private final ArtworkLikeRepository artworkLikeRepository;

    @Transactional
    public void like(User user, Long postId) {
        ArtworkPost post = artworkPostRepository.findById(postId).orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);
        ArtworkLikeId id = new ArtworkLikeId(user,post);
        if(artworkLikeRepository.findById(id).isPresent()) throw ErrorCd.ALREADY_lIKE.serviceException();
        artworkLikeRepository.save(new ArtworkLike(id));
        post.increaseLikeCount();
    }

    @Transactional
    public void unlike(User user, Long postId) {
        ArtworkPost post = artworkPostRepository.findById(postId).orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);
        ArtworkLikeId id = new ArtworkLikeId(user,post);
        ArtworkLike like = artworkLikeRepository.findById(id).orElseThrow(ErrorCd.LIKE_ALREADY_REMOVED::serviceException);
        artworkLikeRepository.delete(like);
        post.decreaseLikeCount();
    }
}
