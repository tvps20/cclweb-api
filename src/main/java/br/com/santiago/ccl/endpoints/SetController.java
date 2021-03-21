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
import br.com.santiago.ccl.domain.Theme;
import br.com.santiago.ccl.dtos.AbstractBaseDto;
import br.com.santiago.ccl.dtos.PieceRequestDto;
import br.com.santiago.ccl.dtos.SetRequestDto;
import br.com.santiago.ccl.dtos.ThemeRequestDto;
import br.com.santiago.ccl.endpoints.enuns.TipoEndPoint;
import br.com.santiago.ccl.services.PieceService;
import br.com.santiago.ccl.services.SetService;
import br.com.santiago.ccl.services.ThemeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(TipoEndPoint.SET)
public class SetController extends AbstractBaseController<Set, SetRequestDto> {

	@Autowired
	private PieceService pieceService;

	@Autowired
	private ThemeService themeService;

	@Autowired
	public SetController(SetService service) {
		super(service);
		log.debug("[{}] [SetController] [Info] - Endpoint made available for requests in: {}.", this.simpleClassName,
				TipoEndPoint.SET);
		this.simpleClassName = this.getClass().getSimpleName();
	}

	@Override
	public ResponseEntity<List<AbstractBaseDto>> listAll() {
		log.debug("[{}] [listAll] [Info] - Started request for list all data.", this.simpleClassName);
		List<Set> list = this.baseService.findAll();
		List<AbstractBaseDto> listDto = list.stream().map(entity -> this.getSetService().parseToSetListDto(entity))
				.collect(Collectors.toList());
		log.debug("[{}] [listAll] [Info] - Finished request for list all data.", this.simpleClassName);

		return ResponseEntity.ok().body(listDto);
	}

	@Override
	public ResponseEntity<Page<AbstractBaseDto>> listAllPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy) {
		log.debug("[{}] [listAllPage] [Info] - Started request for list all data.", this.simpleClassName);
		Page<Set> pages = this.baseService.findAllPage(page, linesPerPage, direction, orderBy);
		Page<AbstractBaseDto> pageDto = pages.map(entity -> this.getSetService().parseToSetListDto(entity));
		log.debug("[{}] [listAllPage] [Info] - Finished request for list all data.", this.simpleClassName);

		return ResponseEntity.ok().body(pageDto);
	}

	@PostMapping(TipoEndPoint.SET_ID + TipoEndPoint.PIECE)
	public ResponseEntity<Void> insertPiece(@PathVariable Long setId, @Valid @RequestBody PieceRequestDto request) {
		log.debug("[{}] [insertPiece] [Info] - Started request for insert new piece in the set.", this.simpleClassName);
		Piece piece = this.pieceService.parseToEntity(request);
		this.getSetService().insetPiece(setId, piece);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().replacePath(TipoEndPoint.SET + "/{id}")
				.buildAndExpand(setId).toUri();
		log.debug("[{}] [insertPiece] [Success] - Created uri for new data: {}.", this.simpleClassName, uri);
		log.debug("[{}] [insertPiece] [Info] - Finished request for insert new piece in the set.",
				this.simpleClassName);

		return ResponseEntity.created(uri).build();
	}

	@PostMapping(TipoEndPoint.SET_ID + TipoEndPoint.THEME)
	public ResponseEntity<Void> insertTheme(@PathVariable Long setId, @Valid @RequestBody ThemeRequestDto request) {
		log.debug("[{}] [insertTheme] [Info] - Started request for insert new theme in the set.", this.simpleClassName);
		Theme theme = this.themeService.parseToEntity(request);
		this.getSetService().insertTheme(setId, theme);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().replacePath(TipoEndPoint.THEME + "/{id}")
				.buildAndExpand(setId).toUri();
		log.debug("[{}] [insertTheme] [Success] - Created uri for new data: {}.", this.simpleClassName, uri);
		log.debug("[{}] [insertTheme] [Info] - Finished request for insert new theme in the set.",
				this.simpleClassName);

		return ResponseEntity.created(uri).build();
	}

	@DeleteMapping(TipoEndPoint.SET_ID + TipoEndPoint.PIECE + TipoEndPoint.PIECE_ID)
	public ResponseEntity<Void> deletePiece(@PathVariable Long pieceId) {
		log.debug("[{}] [insertTheme] [Info] - Started request for delete piece in the set.", this.simpleClassName);
		this.getSetService().deletePiece(pieceId);
		log.debug("[{}] [insertTheme] [Info] - Finished request for delete piece in the set.", this.simpleClassName);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(TipoEndPoint.SET_ID + TipoEndPoint.THEME + TipoEndPoint.THEME_ID)
	public ResponseEntity<Void> deleteTheme(@PathVariable Long setId, @PathVariable Long themeId) {
		log.debug("[{}] [deleteTheme] [Info] - Started request for insert theme in the set.", this.simpleClassName);
		this.getSetService().deleteTheme(setId, themeId);
		log.debug("[{}] [deleteTheme] [Info] - Finished request for delete theme in the set.", this.simpleClassName);

		return ResponseEntity.noContent().build();
	}

	@Override
	public Set parseToEntity(SetRequestDto request) {
		return this.baseService.parseToEntity(request);
	}

	@Override
	public SetRequestDto parseToDto(Set response) {
		return (SetRequestDto) this.baseService.parseToDto(response);
	}

	private SetService getSetService() {
		return ((SetService) this.baseService);
	}

}
