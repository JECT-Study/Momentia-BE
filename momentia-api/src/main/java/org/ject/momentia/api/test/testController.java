package org.ject.momentia.api.test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/test")
public class testController {
	private final testCacheRepository testRepository;
	private final RedisTemplate<String, String> redisTemplate;

	/**
	 * 테스트 용 API - 추후 삭제
	 */
	@GetMapping("/redis/{num}")
	@ResponseStatus(HttpStatus.OK)
	public String getTestCache(@PathVariable Long num) {
		if (testRepository.findById(num).isPresent()) {
			return "기존 - " + testRepository.findById(num).get().getName();
		} else
			//redisTemplate.opsForSet().add("testCacheModel", String.valueOf(num));  // int -> String으로 변환하여 저장
			return "새로 생성 - " + testRepository.save(new testCacheModel(num, "name" + num)).getName();
	}

	/**
	 * 테스트 용 API - 추후 삭제
	 */
	@GetMapping("/redis/getAll")
	@ResponseStatus(HttpStatus.OK)
	public String getAllTestCache() {
		List<Long> idList = Optional.ofNullable(redisTemplate.opsForSet().members("testCacheModel"))
			.orElse(Collections.emptySet())  // null일 경우 빈 Set을 반환
			.stream()
			.map(Long::valueOf)  // String -> Long 변환
			.collect(Collectors.toList());
		StringBuilder st = new StringBuilder();
		for (Long id : idList) {
			if (testRepository.findById(id).isPresent()) {
				st.append(testRepository.findById(id).get().getName()).append(",");
			}
		}
		return st.toString();
	}

	/**
	 * 테스트 용 API - 추후 삭제
	 */
	@GetMapping("/redis/{num}/delete")
	@ResponseStatus(HttpStatus.OK)
	public String deleteTestCache(@PathVariable Long num) {
		if (testRepository.findById(num).isEmpty()) {
			return "없음";
		} else {
			testRepository.deleteById(num);
			return "삭제 완료";
		}
	}

	/**
	 * 테스트 용 API - 추후 삭제
	 */
	@GetMapping("/redis/deleteAll2")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> deleteTestCache2() {
		// List<Long> idList = Optional.ofNullable(redisTemplate.opsForSet().members("testCacheModel"))
		// 	.orElse(Collections.emptySet())  // null일 경우 빈 Set을 반환
		// 	.stream()
		// 	.map(Long::valueOf)  // String -> Long 변환
		// 	.toList();
		List<Long> idList = new java.util.ArrayList<>(redisTemplate.opsForSet().members("testCacheModel").stream()
			.map(Long::valueOf)  // String -> Long 변환
			.toList());
		idList.add(200L);
		for (Long aLong : idList) {
			// testRepository.findById(aLong).ifPresent(test -> {
			// });
			testRepository.deleteById(aLong);
			redisTemplate.opsForSet().remove("testCacheModel", String.valueOf(aLong));
		}

		return ResponseEntity.ok().body("삭제 완료");
	}

	/**
	 * 테스트 용 API - 추후 삭제
	 */
	@GetMapping("/redis/deleteAll")
	@ResponseStatus(HttpStatus.OK)
	public String deleteAllTestCache() {
		testRepository.deleteAll();
		return "모두 삭제";
	}

}
