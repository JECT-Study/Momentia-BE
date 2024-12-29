package org.ject.momentia.common.domain.image;

import org.ject.momentia.common.domain.base.BaseEntity;
import org.ject.momentia.common.domain.image.type.ImageTargetType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "images", schema = "momentia")
public class Image extends BaseEntity {
	@Id
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "target_id")
	private Long targetId;

	@Column(name = "target_type", length = 10)
	@Enumerated(EnumType.STRING)
	private ImageTargetType targetType;

	@NotNull
	@Column(name = "image_src", nullable = false, length = 1000)
	private String imageSrc;
}