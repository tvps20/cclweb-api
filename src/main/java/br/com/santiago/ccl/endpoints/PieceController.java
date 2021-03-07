package br.com.santiago.ccl.endpoints;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.dtos.PieceRequestDto;
import br.com.santiago.ccl.endpoints.enuns.TipoEndPoint;
import br.com.santiago.ccl.endpoints.exceptions.EndpointNotExistsException;
import br.com.santiago.ccl.services.PieceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(TipoEndPoint.PIECE)
public class PieceController extends AbstractBaseController<Piece, PieceRequestDto> {

	public PieceController(PieceService service) {
		super(service);
		log.debug("[{}] [PieceController] [Info] - Endpoint made available for requests in: {}.", this.simpleClassName,
				TipoEndPoint.PIECE);
		this.simpleClassName = this.getClass().getSimpleName();
	}

	@Override
	public ResponseEntity<Void> insert(PieceRequestDto request) throws Exception {
		throw new EndpointNotExistsException("Endpoint not implemented.");
	}

	@Override
	public ResponseEntity<Void> delete(Long id) throws Exception {
		throw new EndpointNotExistsException("Endpoint not implemented.");
	}

	@Override
	public Piece parseToEntity(PieceRequestDto request) {
		return this.baseService.parseToEntity(request);
	}

	@Override
	public PieceRequestDto parseToDto(Piece response) {
		return (PieceRequestDto) this.baseService.parteToDto(response);
	}

}
