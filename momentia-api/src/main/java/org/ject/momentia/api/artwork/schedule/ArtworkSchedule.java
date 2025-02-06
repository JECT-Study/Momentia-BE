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

	/// 좋아요 데이터 캐시에서 db로 옮기는 작엄
	/// findAll,deleteAll 부분이 배포환경에서 동작안하는 것같아서..
	/// 좋아요 로직은 추후에 수정할 예정
	// @Scheduled(fixedRate = 60000) // 1분마다 실행
	// public void migrateLikesToDB() {
	// 	artworkScheduleService.migrateLikesToDB();
	// }
	@Scheduled(fixedRate = 21600000)
	public void migrateViewToDB() {
		artworkScheduleService.migrateViewToDB();
	}

}
