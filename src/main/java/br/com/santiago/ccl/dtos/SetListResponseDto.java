package br.com.santiago.ccl.dtos;

import java.math.BigDecimal;

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
public class SetListResponseDto extends AbstractResponseBaseDto {
	
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
	private String theme;

	@Getter
	@Setter
	private Integer year;

	@Getter
	@Setter
	private BigDecimal price;

	@Getter
	@Setter
	private Integer pcs;

	@Getter
	@Setter
	private boolean imgs;

	@Getter
	@Setter
	private boolean intructions;
	
}
