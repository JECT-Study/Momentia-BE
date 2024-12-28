package org.ject.momentia.api.monthly.controller;

import lombok.RequiredArgsConstructor;
import org.ject.momentia.api.artwork.service.Temp;
import org.ject.momentia.api.monthly.model.MonthlyPostsResponse;
import org.ject.momentia.api.monthly.model.MonthlyUsersResponse;
import org.ject.momentia.api.monthly.service.MonthlyService;
import org.ject.momentia.common.domain.monthly.MonthlyUser;
import org.ject.momentia.common.domain.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class MonthlyController {

    private final Temp temp;
    private final MonthlyService monthlyService;

    @GetMapping("/artwork/posts/popular")
    MonthlyPostsResponse getMonthlyPosts() {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();

        return monthlyService.getMonthlyPosts(user);
    }

    @GetMapping("/users/top10")
    MonthlyUsersResponse getMonthlyUsers() {
        ///  Todo : user 이후 수정
        User user = temp.getUserObject();

        return monthlyService.getMonthlyUsers(user);
    }
}
