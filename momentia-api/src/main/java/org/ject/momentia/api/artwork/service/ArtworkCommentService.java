package org.ject.momentia.api.artwork.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.ject.momentia.api.artwork.converter.ArtworkCommentConverter;
import org.ject.momentia.api.artwork.converter.ArtworkPostConverter;
import org.ject.momentia.api.artwork.model.ArtworkCommentRequest;
import org.ject.momentia.api.artwork.model.ArtworkCommentIdResponse;
import org.ject.momentia.api.artwork.model.ArtworkCommentListResponse;
import org.ject.momentia.api.artwork.model.ArtworkCommentModel;
import org.ject.momentia.api.artwork.repository.ArtworkCommentRepository;
import org.ject.momentia.api.artwork.repository.ArtworkPostRepository;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.image.service.ImageService;
import org.ject.momentia.common.domain.artwork.ArtworkComment;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.image.type.ImageTargetType;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArtworkCommentService {

    private final ArtworkCommentRepository artworkCommentRepository;
    private final ArtworkPostRepository artworkPostRepository;
    private final ImageService imageService;

    @Transactional
    public ArtworkCommentIdResponse createComment(User user, Long postId, ArtworkCommentRequest artworkCommentCreateRequest) {
        ArtworkPost post = artworkPostRepository.findById(postId).orElseThrow(ErrorCd.ARTWORK_POST_NOT_FOUND::serviceException);
        ArtworkComment comment = ArtworkPostConverter.from(artworkCommentCreateRequest, post, user);
        comment = artworkCommentRepository.save(comment);

        post.increaseCommentCount();

        return new ArtworkCommentIdResponse(comment.getId());
    }

    @Transactional
    public void deleteComment(User user, Long commentId) {
        ArtworkComment comment = artworkCommentRepository.findById(commentId).orElseThrow(ErrorCd.ARTWORK_COMMENT_NOT_FOUND::serviceException);
        if (user == null || !comment.getUser().getId().equals(user.getId()))
            throw ErrorCd.NO_PERMISSION.serviceException();
        artworkCommentRepository.delete(comment);

        comment.getPost().decreaseCommentCount();
    }


    public ArtworkCommentListResponse getCommentList(User user, Long postId, Long skip, Long size) {
        if (!artworkPostRepository.existsById(postId)) throw ErrorCd.ARTWORK_POST_NOT_FOUND.serviceException();
        List<ArtworkComment> commentList =
                artworkCommentRepository.findByArtworkIdOrderByCreatedAtDescForInfiniteScrolling(postId, skip, size);
        List<ArtworkCommentModel> comments =
                commentList.stream()
                        .map(comment -> {
                            // 프로필 이미지
                            String imageUrl = null;
                            if(comment.getUser().getProfileImage()!=null) imageUrl = imageService.getImageUrl(ImageTargetType.PROFILE,user.getId());
                            return ArtworkCommentConverter.ArtworkCommentToArtworkCommentModel(
                                    comment,
                                    imageUrl,
                                    user
                            );
                        }).collect(Collectors.toList());
        return new ArtworkCommentListResponse(comments);
    }

    @Transactional
    public void updateComment(User user, Long commentId, ArtworkCommentRequest artworkCommentUpdateRequest) {
        ArtworkComment comment = artworkCommentRepository.findById(commentId).orElseThrow(ErrorCd.ARTWORK_COMMENT_NOT_FOUND::serviceException);
        if(user == null || comment.getUser().getId() != user.getId()) ErrorCd.NO_PERMISSION.serviceException();
        comment.update(artworkCommentUpdateRequest.content());
    }


}
