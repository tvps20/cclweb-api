package br.com.santiago.ccl.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.santiago.ccl.domain.Set;
import br.com.santiago.ccl.dtos.SetRequestDto;
import br.com.santiago.ccl.endpoints.enuns.TipoEndPoint;
import br.com.santiago.ccl.services.SetService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(TipoEndPoint.SET)
public class SetController extends AbstractBaseController<Set, SetRequestDto> {

	@Autowired
	public SetController(SetService service) {
		super(service);
		log.debug("Endpoint available on the route {}", TipoEndPoint.SET);
		this.simpleClassName = "Set";
		this.routerName = TipoEndPoint.SET;
	}

	@Override
	public Set parseToEntity(SetRequestDto request) {
		return this.baseService.parseToEntity(request);
	}

	@Override
	public SetRequestDto parseToDto(Set response) {
		return (SetRequestDto) this.baseService.parteToDto(response);
	}

}
