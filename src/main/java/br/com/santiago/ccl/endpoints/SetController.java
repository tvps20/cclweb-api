package br.com.santiago.ccl.endpoints;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.domain.Set;
import br.com.santiago.ccl.dtos.PieceRequestDto;
import br.com.santiago.ccl.dtos.SetRequestDto;
import br.com.santiago.ccl.endpoints.enuns.TipoEndPoint;
import br.com.santiago.ccl.services.PieceService;
import br.com.santiago.ccl.services.SetService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(TipoEndPoint.SET)
public class SetController extends AbstractBaseController<Set, SetRequestDto> {

	@Autowired
	private PieceService pieceService;

	@Autowired
	public SetController(SetService service) {
		super(service);
		log.debug("Endpoint available on the route {}", TipoEndPoint.SET);
		this.simpleClassName = "Set";
		this.routerName = TipoEndPoint.SET;
	}

	@PostMapping(TipoEndPoint.SET_ID + TipoEndPoint.PIECE)
	public ResponseEntity<Void> insertPiece(@PathVariable Long setId, @Valid @RequestBody PieceRequestDto request) {
		log.debug("Insert Piece in {} in endpoint [{}]", this.simpleClassName, this.routerName + TipoEndPoint.PIECE);

		Piece piece = this.pieceService.parseToEntity(request);
		((SetService) this.baseService).insetPiece(setId, piece);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().replacePath(TipoEndPoint.SET + "/{id}").buildAndExpand(setId)
				.toUri();
		log.trace("uri variable [{}]", uri);

		return ResponseEntity.created(uri).build();
	}
	
	@DeleteMapping(TipoEndPoint.SET_ID + TipoEndPoint.PIECE + TipoEndPoint.PIECE_ID)
	public ResponseEntity<Void> delete(@PathVariable Long pieceId) {
		log.debug("Delete Piece {} in endpoint [{}]", this.simpleClassName, this.routerName);
		this.pieceService.deleteById(pieceId);
		
		return ResponseEntity.noContent().build();
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
