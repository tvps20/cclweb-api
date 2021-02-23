package br.com.santiago.ccl.dtos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SetRequestDto extends AbstractBaseDto {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@NotNull(message = "The setId field is not null")
	@NotEmpty(message = "The setId field is not empty")
	private String setId;

	@Getter
	@Setter
	@NotNull(message = "The name field is not null")
	@NotEmpty(message = "The name field is not empty")
	@Length(min = 3, max = 50, message = "The qtd field name be between 3 and 50 characters")
	private String name;

	@Getter
	@Setter
	@Builder.Default
	@NotEmpty(message = "The themes field is not empty")
	private List<ThemeRequestDto> themes = new ArrayList<>();

	@Getter
	@Setter
	private Integer year;

	@Getter
	@Setter
	private BigDecimal price;

	@Getter
	@Setter
	@Builder.Default
	private List<PieceRequestDto> pcs = new ArrayList<>();

	@Getter
	@Setter
	@Builder.Default
	private List<String> figuresUrls = new ArrayList<>();

	@Getter
	@Setter
	@Builder.Default
	private List<String> instructionsUrls = new ArrayList<>();

}
