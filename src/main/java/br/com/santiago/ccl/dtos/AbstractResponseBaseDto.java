package br.com.santiago.ccl.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public abstract class AbstractResponseBaseDto extends AbstractBaseDto {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Long id;
}
