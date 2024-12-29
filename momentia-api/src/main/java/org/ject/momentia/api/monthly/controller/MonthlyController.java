package org.ject.momentia.api.monthly.controller;

import lombok.RequiredArgsConstructor;
import org.ject.momentia.api.monthly.model.MonthlyPostsResponse;
import org.ject.momentia.api.monthly.model.MonthlyUsersResponse;
import org.ject.momentia.api.monthly.service.MonthlyService;
import org.ject.momentia.api.mvc.annotation.MomentiaUser;
import org.ject.momentia.common.domain.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class MonthlyController {

    private final MonthlyService monthlyService;

    @GetMapping("/artwork/posts/popular")
    MonthlyPostsResponse getMonthlyPosts(@MomentiaUser User user) {
        return monthlyService.getMonthlyPosts(user);
    }

    @GetMapping("/users/top10")
    MonthlyUsersResponse getMonthlyUsers(@MomentiaUser User user) {
        return monthlyService.getMonthlyUsers(user);
    }
}
