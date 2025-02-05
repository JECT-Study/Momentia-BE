package org.ject.momentia.api.artwork.repository.cache.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@RedisHash(value = "PageIdsCacheModel", timeToLive = 1000L) // Redis에 저장될 키 이름
public class PageIdsCacheModel implements Serializable {

	@Id
	String key;

	//List<Long> ids;
	String ids; // 용량을 줄이기 위해 String 값으로 저장

	String pageInfo;  // totalDataCnt, totalPages, isLastPage, isFirstPage, requestPage, requestSize 를 저장하는 문자열

	public PageIdsCacheModel(String key, Page<Long> ids) {
		this.key = key;
		this.ids = ids.getContent().stream().map(String::valueOf).collect(Collectors.joining(","));

		// 모든 페이지 정보를 하나의 문자열로 저장: totalDataCnt, totalPages, isLastPage, isFirstPage, requestPage, requestSize
		this.pageInfo = String.join(",",
			String.valueOf(ids.getTotalElements()),  // totalDataCnt
			String.valueOf(ids.getTotalPages()),  // totalPages
			String.valueOf(ids.isLast()),  // isLastPage
			String.valueOf(ids.isFirst()),  // isFirstPage
			String.valueOf(ids.getNumber()),  // requestPage
			String.valueOf(ids.getSize())  // requestSize
		);
	}

	// 각 필드를 get 메서드를 통해 반환
	public Long getTotalDataCnt() {
		return Long.parseLong(pageInfo.split(",")[0]);
	}

	public Integer getTotalPages() {
		return Integer.parseInt(pageInfo.split(",")[1]);
	}

	public Boolean getIsLastPage() {
		return Boolean.parseBoolean(pageInfo.split(",")[2]);
	}

	public Boolean getIsFirstPage() {
		return Boolean.parseBoolean(pageInfo.split(",")[3]);
	}

	public Integer getRequestPage() {
		return Integer.parseInt(pageInfo.split(",")[4]);
	}

	public Integer getRequestSize() {
		return Integer.parseInt(pageInfo.split(",")[5]);
	}

	// IDs는 CSV 형식으로 저장되어 있는 값을 List<Long>으로 반환
	public List<Long> getIds() {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyList();
		}

		return Arrays.stream(ids.split(","))
			.map(Long::parseLong)
			.collect(Collectors.toList());
	}

	// Long totalDataCnt;
	// Integer totalPages;
	// Boolean isLastPage;
	// Boolean isFirstPage;
	// Integer requestPage;
	// Integer requestSize;
	//
	// public PageIdsCacheModel(String key, Page<Long> ids) {
	// 	this.key = key;
	// 	//this.ids = ids.getContent();
	// 	this.ids = ids.getContent().stream().map(String::valueOf).collect(Collectors.joining(","));
	// 	this.isFirstPage = ids.isFirst();
	// 	this.isLastPage = ids.isLast();
	// 	this.totalPages = ids.getTotalPages();
	// 	this.totalDataCnt = ids.getTotalElements();
	// 	this.requestPage = ids.getNumber();
	// 	this.requestSize = ids.getSize();
	// }
	//
	// public List<Long> getIds() {
	// 	// ids는 CSV 형식의 문자열
	// 	if (ids == null || ids.isEmpty()) {
	// 		return Collections.emptyList();
	// 	}
	//
	// 	return Arrays.stream(ids.split(","))   // CSV 형식의 문자열을 구분자(,)로 나누기
	// 		.map(Long::parseLong)      // 각 문자열을 Long으로 변환
	// 		.collect(Collectors.toList());  // List<Long>으로 수집
	// }
}
