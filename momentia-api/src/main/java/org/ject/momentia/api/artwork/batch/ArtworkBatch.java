package org.ject.momentia.api.artwork.batch;

import org.ject.momentia.api.artwork.batch.service.LikeBatchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArtworkBatch {

	private final LikeBatchService likeBatchService;

	@Scheduled(fixedRate = 60000) // 1분마다 실행
	public void migrateLikesToDB() {
		likeBatchService.migrateLikesToDB();
	}

}
