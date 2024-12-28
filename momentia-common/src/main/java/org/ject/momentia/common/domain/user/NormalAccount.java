package org.ject.momentia.common.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
	name = "normal_account",
	schema = "momentia"
)
public class NormalAccount {
	@Id
	@Column(name = "user_id", nullable = false)
	private Long id;

	@MapsId
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Size(max = 100)
	@NotNull
	@Column(name = "password", nullable = false, length = 100)
	private String password;
}