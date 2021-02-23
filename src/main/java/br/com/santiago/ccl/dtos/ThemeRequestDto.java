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
public class ThemeRequestDto extends AbstractBaseDto {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@NotNull(message = "The name field is not null")
	@NotEmpty(message = "The name field is not empty")
	@Length(min = 3, max = 50, message = "The qtd field name be between 3 and 50 characters")
	private String name;

}
