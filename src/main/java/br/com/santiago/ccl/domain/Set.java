package br.com.santiago.ccl.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
public class Set extends AbstractBaseEntity {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String setId;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	@ManyToMany
	@Builder.Default
	private List<Theme> themes = new ArrayList<>();

	@Getter
	@Setter
	private Integer year;

	@Getter
	@Setter
	private BigDecimal price;

	@Getter
	@Setter
	@Builder.Default
	@OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "set")
	@OnDelete(action = OnDeleteAction.CASCADE) // Gera o “cascade delete” Evita várias querys para delete
	private List<Piece> pcs = new ArrayList<>();

	@Getter
	@Setter
	@Builder.Default
	@ElementCollection
	@CollectionTable(name = "set_figures_urls")
	private List<String> figuresUrls = new ArrayList<>();

	@Getter
	@Setter
	@Builder.Default
	@ElementCollection
	@CollectionTable(name = "set_instructions_urls")
	private List<String> instructionsUrls = new ArrayList<>();

}
