package br.com.santiago.ccl.dtos;

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
public class PieceRequestDto extends AbstractBaseDto {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@NotNull(message = "The qtd field is not null")
	private Integer qtd;

	@Getter
	@Setter
	@NotNull(message = "The partNum field is not null")
	@NotEmpty(message = "The partNum field is not empty")
	private String partNum;

	@Getter
	@Setter
	@NotNull(message = "The color field is not null")
	@NotEmpty(message = "The color field is not empty")
	private String color;

	@Getter
	@Setter
	@NotNull(message = "The description field is not null")
	@NotEmpty(message = "The description field is not empty")
	private String description;

	@Getter
	@Setter
	private String pictureUrl;

	@Getter
	@Setter
	@Length(min = 5, max = 255, message = "The note field name be between 5 and 255 characters")
	private String note;

//	@Getter
//	@Setter
//	@NotNull(message = "The setId field is not null")
//	private Long setId;

}
