package br.com.santiago.ccl.endpoints;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.santiago.ccl.domain.AbstractBaseEntity;
import br.com.santiago.ccl.dtos.AbstractBaseDto;
import br.com.santiago.ccl.endpoints.enuns.TipoEndPoint;
import br.com.santiago.ccl.services.interfaces.ICrudService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractBaseController<T extends AbstractBaseEntity, K extends AbstractBaseDto> {

	protected ICrudService<T, K> baseService;
	protected String simpleClassName = this.getClass().getSimpleName();

	public AbstractBaseController(ICrudService<T, K> service) {
		this.baseService = service;
	}

	@GetMapping
	public ResponseEntity<List<AbstractBaseDto>> listAll() {
		log.debug("[{}] [listAll] [Info] - Started request for list all data.", this.simpleClassName);
		List<T> list = this.baseService.findAll();
		List<AbstractBaseDto> listDto = list.stream().map(entity -> this.baseService.parteToDto(entity))
				.collect(Collectors.toList());
		log.debug("[{}] [listAll] [Info] - Finished request for list all data.", this.simpleClassName);

		return ResponseEntity.ok().body(listDto);
	}

	@GetMapping(TipoEndPoint.PAGE)
	public ResponseEntity<Page<AbstractBaseDto>> listAllPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy) {
		log.debug("[{}] [listAllPage] [Info] - Started request for list all data.", this.simpleClassName);
		Page<T> pages = this.baseService.findAllPage(page, linesPerPage, direction, orderBy);
		Page<AbstractBaseDto> pageDto = pages.map(entity -> this.baseService.parteToDto(entity));
		log.debug("[{}] [listAllPage] [Info] - Finished request for list all data.", this.simpleClassName);

		return ResponseEntity.ok().body(pageDto);
	}

	@GetMapping(TipoEndPoint.ID)
	public ResponseEntity<AbstractBaseDto> findById(@PathVariable Long id) {
		log.debug("[{}] [findById] [Info] - Started request for find data by id.", this.simpleClassName);
		T entity = this.baseService.findById(id);
		AbstractBaseDto entityDto = this.baseService.parteToDto(entity);
		log.debug("[{}] [findById] [Info] - Finished request for find data by id.", this.simpleClassName);

		return ResponseEntity.ok().body(entityDto);
	}

	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody K request) throws Exception {
		log.debug("[{}] [insert] [Info] - Started request for insert new data.", this.simpleClassName);
		T entity = this.parseToEntity(request);
		T entitySaved = this.baseService.insert(entity);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entitySaved.getId())
				.toUri();
		log.debug("[{}] [insert] [Success] - Created uri for new data: {}.", this.simpleClassName, uri);
		log.debug("[{}] [insert] [Info] - Finished request for insert new data.", this.simpleClassName);

		return ResponseEntity.created(uri).build();
	}

	@PutMapping(TipoEndPoint.ID)
	public ResponseEntity<Void> update(@Valid @RequestBody K request, @PathVariable Long id) {
		log.debug("[{}] [update] [Info] - Started request for update data.", this.simpleClassName);
		T entity = this.parseToEntity(request);
		entity.setId(id);
		this.baseService.update(entity);
		log.debug("[{}] [update] [Info] - Finished request for update data.", this.simpleClassName);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(TipoEndPoint.ID)
	public ResponseEntity<Void> delete(@PathVariable Long id) throws Exception {
		log.debug("[{}] [delete] [Info] - Started request for delete data.", this.simpleClassName);
		this.baseService.deleteById(id);
		log.debug("[{}] [delete] [Info] - Finished request for delete data.", this.simpleClassName);

		return ResponseEntity.noContent().build();
	}

	public abstract T parseToEntity(K request);

	public abstract AbstractBaseDto parseToDto(T response);

}
