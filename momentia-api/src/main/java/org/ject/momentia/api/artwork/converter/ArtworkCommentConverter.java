package org.ject.momentia.api.artwork.converter;

import org.ject.momentia.api.artwork.model.ArtworkCommentModel;
import org.ject.momentia.common.domain.artwork.ArtworkComment;
import org.ject.momentia.common.domain.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class ArtworkCommentConverter {
    // User, commentList 받아서 본인여부 체크
    public static List<ArtworkCommentModel> ArtworkCommentToArtworkCommentModel(List<ArtworkComment> commentList, User user) {
        return commentList.stream()
                .map(comment -> {
                    boolean isMine = false;
                    if (user != null) isMine = comment.getUser().getId().equals(user.getId());
                    return ArtworkCommentModel.builder()
                            .commentId(comment.getId())
                            .userId(comment.getUser().getId())
                            .content(comment.getContent())
                            .isMine(isMine)
                            /// todo : 이미지 처리
                            .profileImage("empty")
                            .createdTime(comment.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
