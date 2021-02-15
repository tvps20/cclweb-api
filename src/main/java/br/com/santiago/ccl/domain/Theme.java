package br.com.santiago.ccl.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Theme extends AbstractBaseEntity {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(unique = true, nullable = false)
	private String name;

}
