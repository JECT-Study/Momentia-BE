package org.ject.momentia.api.monthly.service;

import lombok.AllArgsConstructor;
import org.ject.momentia.api.artwork.converter.ArtworkPostConverter;
import org.ject.momentia.api.artwork.model.ArtworkPostModel;
import org.ject.momentia.api.artwork.service.ArtworkService;
import org.ject.momentia.api.follow.service.FollowService;
import org.ject.momentia.api.image.service.ImageService;
import org.ject.momentia.api.monthly.converter.MonthlyUserConverter;
import org.ject.momentia.api.monthly.model.MonthlyPostsResponse;
import org.ject.momentia.api.monthly.model.MonthlyUsersResponse;
import org.ject.momentia.api.monthly.model.UserListModel;
import org.ject.momentia.api.monthly.repository.MonthlyPostRepository;
import org.ject.momentia.api.monthly.repository.MonthlyUserRepository;
import org.ject.momentia.api.user.repository.UserRepository;
import org.ject.momentia.common.domain.artwork.ArtworkPost;
import org.ject.momentia.common.domain.image.type.ImageTargetType;
import org.ject.momentia.common.domain.monthly.MonthlyArtwork;
import org.ject.momentia.common.domain.monthly.MonthlyUser;
import org.ject.momentia.common.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MonthlyService {

    private final MonthlyPostRepository monthlyPostRepository;
    private final MonthlyUserRepository monthlyUserRepository;
    private final UserRepository userRepository;

    private final ArtworkService artworkService;
    private final FollowService followService;
    private final ImageService imageService;

    public MonthlyPostsResponse getMonthlyPosts(User user) {
        List<MonthlyArtwork> artworkList = monthlyPostRepository.findAllByMonthAndYearOrderByRankAsc(12, 2024);

        List<Long> ids = artworkList.stream().map((a) -> a.getPost().getId()).toList();

        // id들 보내면, post List 반환 하는 service 함수
        List<ArtworkPost> posts = artworkService.getPostsByIds(ids);

        // id 값으로 post 찾을 수 있도록 map 생성  ( artworkList 순서 유지를 위함 )
        Map<Long, ArtworkPost> idToPostMap = posts.stream().filter(post -> ids.contains(post.getId()))
                .collect(Collectors.toMap(ArtworkPost::getId, post -> post
                ));

        List<ArtworkPostModel> postModelList = artworkList.stream()
                .map((p) -> {
                            ArtworkPost post = idToPostMap.get(p.getPost().getId());
                            Boolean isLiked = user != null && artworkService.isLiked(user, post);
                            String imageUrl = imageService.getImageUrl(ImageTargetType.ARTWORK,post.getId());
                            return ArtworkPostConverter.toArtworkPostModel(post, isLiked, imageUrl);
                        }
                )
                .toList();

        return new MonthlyPostsResponse(postModelList);
    }

    public MonthlyUsersResponse getMonthlyUsers(User user) {

        List<MonthlyUser> monthlyUsers = monthlyUserRepository.findAllByMonthAndYearOrderByRankAsc(12, 2024);

        List<Long> ids = monthlyUsers.stream()
                .map((a) -> a.getUser().getId())
                .toList();

        Map<Long, User> userMap = userRepository.findAllByIdIn(ids).stream()
                .collect(Collectors.toMap(User::getId, User -> User));

        // monthlyUsers의 순서대로 UserListModel을 생성합니다.
        List<UserListModel> userListModels = monthlyUsers.stream().map((monthlyUser) -> {
            User u = userMap.get(monthlyUser.getUser().getId());

            // 대표작
            ArtworkPost artwork = artworkService.getPopularArtworkByUser(u);

            /// todo: 대표작 이미지
            String postImage = imageService.getImageUrl(ImageTargetType.ARTWORK,artwork.getId());

            // todo : 프로필 이미지
            String profileImage = u.getProfileImage()!=null ? imageService.getImageUrl(ImageTargetType.PROFILE,u.getId()) : null;

            // 좋아요 여부
            Boolean isFollow = followService.isFollowing(user, u);

            return MonthlyUserConverter.toUserListModel(u, profileImage, postImage, isFollow);
        }).toList();
        return new MonthlyUsersResponse(userListModels);
    }

}
