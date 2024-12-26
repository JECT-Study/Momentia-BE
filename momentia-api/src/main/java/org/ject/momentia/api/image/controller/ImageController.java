package org.ject.momentia.api.image.controller;

import org.ject.momentia.api.image.model.StartUploadRequest;
import org.ject.momentia.api.image.model.StartUploadResponse;
import org.ject.momentia.api.image.service.ImageService;
import org.ject.momentia.api.mvc.annotation.MomentiaUser;
import org.ject.momentia.common.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/image")
public class ImageController {
	private final ImageService imageService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public StartUploadResponse startUpload(@RequestBody @Valid StartUploadRequest request) {
		return imageService.startUpload(request);
	}

	@PutMapping("/{imageId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void completeUpload(@PathVariable @Positive Long imageId) {
		imageService.completeUpload(imageId);
	}
}
