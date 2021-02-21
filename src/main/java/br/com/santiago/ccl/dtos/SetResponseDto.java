package br.com.santiago.ccl.dtos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
public class SetResponseDto extends AbstractResponseBaseDto {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	private String setId;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	@Builder.Default
	private List<ThemeResponseDto> themes = new ArrayList<>();

	@Getter
	@Setter
	private Integer year;

	@Getter
	@Setter
	private BigDecimal price;

	@Getter
	@Setter
	@Builder.Default
	private List<SetPieceResponseDto> pcs = new ArrayList<>();

	@Getter
	@Setter
	@Builder.Default
	private List<String> figuresUrls = new ArrayList<>();

	@Getter
	@Setter
	@Builder.Default
	private List<String> instructionsUrls = new ArrayList<>();

}
