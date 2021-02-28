package br.com.santiago.ccl.endpoints;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.domain.Set;
import br.com.santiago.ccl.dtos.AbstractBaseDto;
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

	@Override
	public ResponseEntity<List<AbstractBaseDto>> listAll() {
		log.debug("ListAll {}s in endpoint [{}]", this.simpleClassName, this.routerName);
		List<Set> list = this.baseService.findAll();
		List<AbstractBaseDto> listDto = list.stream()
				.map(entity -> ((SetService) this.baseService).parseToSetListDto(entity)).collect(Collectors.toList());

		return ResponseEntity.ok().body(listDto);
	}

	@Override
	public ResponseEntity<Page<AbstractBaseDto>> listAllPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy) {
		log.debug("ListAllPage {}s in endpoint [{}]", this.simpleClassName, this.routerName);
		Page<Set> pages = this.baseService.findAllPage(page, linesPerPage, direction, orderBy);
		Page<AbstractBaseDto> pageDto = pages.map(entity -> ((SetService) this.baseService).parseToSetListDto(entity));

		return ResponseEntity.ok().body(pageDto);
	}

	@PostMapping(TipoEndPoint.SET_ID + TipoEndPoint.PIECE)
	public ResponseEntity<Void> insertPiece(@PathVariable Long setId, @Valid @RequestBody PieceRequestDto request) {
		log.debug("Insert Piece in {} in endpoint [{}]", this.simpleClassName, this.routerName + TipoEndPoint.PIECE);

		Piece piece = this.pieceService.parseToEntity(request);
		((SetService) this.baseService).insetPiece(setId, piece);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().replacePath(TipoEndPoint.SET + "/{id}")
				.buildAndExpand(setId).toUri();
		log.trace("uri variable [{}]", uri);

		return ResponseEntity.created(uri).build();
	}

	@DeleteMapping(TipoEndPoint.SET_ID + TipoEndPoint.PIECE + TipoEndPoint.PIECE_ID)
	public ResponseEntity<Void> deletePiece(@PathVariable Long pieceId) {
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
