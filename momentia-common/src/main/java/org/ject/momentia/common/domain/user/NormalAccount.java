package org.ject.momentia.common.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
	@JoinColumn(name = "user_id", nullable = false)
	@OneToOne
	private User id;

	@Size(max = 50)
	@NotNull
	@Column(name = "password", nullable = false, length = 50)
	private String password;

}