package org.ject.momentia.api.artwork.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.ject.momentia.api.artwork.converter.ArtworkCommentConverter;
import org.ject.momentia.api.artwork.converter.ArtworkPostConverter;
import org.ject.momentia.api.artwork.model.ArtworkCommentIdResponse;
import org.ject.momentia.api.artwork.model.ArtworkCommentListResponse;
import org.ject.momentia.api.artwork.model.ArtworkCommentModel;
import org.ject.momentia.api.artwork.model.ArtworkCommentRequest;
import org.ject.momentia.api.artwork.repository.ArtworkCommentRepository;
import org.ject.momentia.api.artwork.service.module.ArtworkPostModuleService;
import org.ject.momentia.api.exception.ErrorCd;
import org.ject.momentia.api.image.service.ImageService;
import org.ject.momentia.common.domain.artwork.ArtworkComment;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ArtworkCommentService {

	private final ArtworkCommentRepository artworkCommentRepository;

	private final ImageService imageService;
	private final ArtworkPostModuleService artworkService;

	@Transactional
	public ArtworkCommentIdResponse createComment(User user, Long postId,
		ArtworkCommentRequest artworkCommentCreateRequest) {
		ArtworkPost post = artworkService.findPostByIdElseThrowError(postId);
		ArtworkComment comment = ArtworkPostConverter.from(artworkCommentCreateRequest, post, user);
		comment = artworkCommentRepository.save(comment);

		post.increaseCommentCount();

		return new ArtworkCommentIdResponse(comment.getId());
	}

	@Transactional
	public void deleteComment(User user, Long commentId) {
		ArtworkComment comment = artworkCommentRepository.findById(commentId)
			.orElseThrow(ErrorCd.ARTWORK_COMMENT_NOT_FOUND::serviceException);
		if (user == null || !comment.getUser().getId().equals(user.getId()))
			throw ErrorCd.NO_PERMISSION.serviceException();
		artworkCommentRepository.delete(comment);

		comment.getPost().decreaseCommentCount();
	}

	@Transactional
	public ArtworkCommentListResponse getCommentList(User user, Long postId, Long skip, Long size) {
		artworkService.findPostByIdElseThrowError(postId);
		List<ArtworkComment> commentList =
			artworkCommentRepository.findByArtworkIdOrderByCreatedAtDescForInfiniteScrolling(postId, skip, size);
		List<ArtworkCommentModel> comments =
			commentList.stream()
				.map(comment -> {
					return ArtworkCommentConverter.ArtworkCommentToArtworkCommentModel(
						comment,
						user
					);
				}).collect(Collectors.toList());
		return new ArtworkCommentListResponse(comments);
	}

	@Transactional
	public void updateComment(User user, Long commentId, ArtworkCommentRequest artworkCommentUpdateRequest) {
		ArtworkComment comment = artworkCommentRepository.findById(commentId)
			.orElseThrow(ErrorCd.ARTWORK_COMMENT_NOT_FOUND::serviceException);
		if (user == null || !Objects.equals(comment.getUser().getId(), user.getId()))
			ErrorCd.NO_PERMISSION.serviceException();
		comment.update(artworkCommentUpdateRequest.content());
	}

}
