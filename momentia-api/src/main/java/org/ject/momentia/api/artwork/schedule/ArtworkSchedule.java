package org.ject.momentia.api.artwork.schedule;

import org.ject.momentia.api.artwork.schedule.service.ArtworkScheduleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArtworkSchedule {

	private final ArtworkScheduleService artworkScheduleService;

	@Scheduled(fixedRate = 180000) // 1분마다 실행
	public void migrateLikesToDB() {
		artworkScheduleService.migrateLikesToDB();
	}

	@Scheduled(fixedRate = 180000)
	public void migrateViewToDB() {
		artworkScheduleService.migrateViewToDB();
	}

}
