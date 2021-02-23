package br.com.santiago.ccl.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
public class Piece extends AbstractBaseEntity {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(nullable = false)
	private Integer qtd;

	@Getter
	@Setter
	@Column(unique = true)
	private String partNum;

	@Getter
	@Setter
	@Column(nullable = false)
	private String color;

	@Getter
	@Setter
	@Column(nullable = false)
	private String description;

	@Getter
	@Setter
	private String pictureUrl;

	@Getter
	@Setter
	private String note;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "set_id", nullable = false)
	private Set set;

}
