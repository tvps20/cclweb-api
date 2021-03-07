package br.com.santiago.ccl.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.santiago.ccl.domain.Theme;
import br.com.santiago.ccl.dtos.ThemeRequestDto;
import br.com.santiago.ccl.endpoints.enuns.TipoEndPoint;
import br.com.santiago.ccl.services.ThemeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(TipoEndPoint.THEME)
public class ThemeController extends AbstractBaseController<Theme, ThemeRequestDto> {

	@Autowired
	public ThemeController(ThemeService service) {
		super(service);
		log.debug("[{}] [PieceController] [Info] - Endpoint made available for requests in: {}.", this.simpleClassName,
				TipoEndPoint.THEME);
		this.simpleClassName = this.getClass().getSimpleName();
	}

	@Override
	public Theme parseToEntity(ThemeRequestDto request) {
		return this.baseService.parseToEntity(request);
	}

	@Override
	public ThemeRequestDto parseToDto(Theme response) {
		return (ThemeRequestDto) this.baseService.parteToDto(response);
	}

}
