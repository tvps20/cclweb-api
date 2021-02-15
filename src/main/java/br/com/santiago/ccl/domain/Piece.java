package br.com.santiago.ccl.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Piece extends AbstractBaseEntity {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private int qtd;

	@Getter
	@Setter
	private String partNum;

	@Getter
	@Setter
	private String color;

	@Getter
	@Setter
	private String description;

	@Getter
	@Setter
	@Builder.Default
	@ElementCollection
	@CollectionTable(name = "piece_pictures_urls")
	private List<String> picturesUrls = new ArrayList<>();

	@Getter
	@Setter
	private String note;

	@Getter
	@Setter
	@JoinColumn(name = "set_id", nullable = false)
	private Set set;

}
