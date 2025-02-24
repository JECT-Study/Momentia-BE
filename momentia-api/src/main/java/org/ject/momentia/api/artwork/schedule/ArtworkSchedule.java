package org.ject.momentia.api.artwork.schedule;

import org.ject.momentia.api.artwork.service.ArtworkScheduleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArtworkSchedule {

	private final ArtworkScheduleService artworkScheduleService;

	@Scheduled(fixedRate = 120000)
	public void migrateLikesAndViewToDB() {
		artworkScheduleService.migrateLikesToDB();
		artworkScheduleService.migrateViewToDB();
		artworkScheduleService.deleteAllPostIds();
	}

}
