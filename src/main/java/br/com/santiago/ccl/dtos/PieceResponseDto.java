package br.com.santiago.ccl.dtos;

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
public class PieceResponseDto extends AbstractResponseBaseDto {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	private Integer qtd;

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
	private String pictureUrl;

	@Getter
	@Setter
	private String note;

	@Getter
	@Setter
	private Long setId;

}
