package org.ject.momentia.api.image.entity;

import org.ject.momentia.api.image.model.type.ImageStatus;
import org.ject.momentia.common.domain.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "temp_image", schema = "momentia")
public class TempImage extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id", nullable = false)
	private Long id;

	@Size(max = 150)
	@NotNull
	@Column(name = "presigned_url", nullable = false, length = 150)
	private String presignedUrl;

	@NotNull
	@Column(name = "status", nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private ImageStatus status;

	public boolean isUploaded() {
		return this.status == ImageStatus.COMPLETED;
	}

	public void completeUpload() {
		this.status = ImageStatus.COMPLETED;
	}
}