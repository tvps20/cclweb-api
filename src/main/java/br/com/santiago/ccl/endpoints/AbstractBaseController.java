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
	protected String simpleClassName = "Base";
	protected String routerName;

	public AbstractBaseController(ICrudService<T, K> service) {
		this.baseService = service;
		this.routerName = "routerName is not define";
	}

	@GetMapping
	public ResponseEntity<List<AbstractBaseDto>> listAll() {
		log.debug("ListAll {}s in endpoint [{}]", this.simpleClassName, this.routerName);
		List<T> list = this.baseService.findAll();
		List<AbstractBaseDto> listDto = list.stream().map(entity -> this.baseService.parteToDto(entity))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	@GetMapping(TipoEndPoint.PAGE)
	public ResponseEntity<Page<AbstractBaseDto>> listAllPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy) {
		log.debug("ListAllPage {}s in endpoint [{}]", this.simpleClassName, this.routerName);
		Page<T> pages = this.baseService.findAllPage(page, linesPerPage, direction, orderBy);
		Page<AbstractBaseDto> pageDto = pages.map(entity -> this.baseService.parteToDto(entity));
		return ResponseEntity.ok().body(pageDto);
	}

	@GetMapping(TipoEndPoint.ID)
	public ResponseEntity<AbstractBaseDto> findById(@PathVariable Long id) {
		log.debug("FindById {} in endpoint [{}]", this.simpleClassName, this.routerName);
		T entity = this.baseService.findById(id);
		AbstractBaseDto entityDto = this.baseService.parteToDto(entity);
		return ResponseEntity.ok().body(entityDto);
	}

	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody K request) {
		log.debug("Insert {} in endpoint [{}]", this.simpleClassName, this.routerName);
		T entity = this.parseToEntity(request);
		T entitySaved = this.baseService.insert(entity);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entitySaved.getId())
				.toUri();
		log.trace("uri variable [{}]", uri);
		return ResponseEntity.created(uri).build();
	}

	@PutMapping(TipoEndPoint.ID)
	public ResponseEntity<Void> update(@Valid @RequestBody K request, @PathVariable Long id) {
		log.debug("Update {} in endpoint [{}]", this.simpleClassName, this.routerName);
		log.trace("id parameter [{}]", id);
		T entity = this.parseToEntity(request);
		entity.setId(id);
		this.baseService.update(entity);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(TipoEndPoint.ID)
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		log.debug("Delete {} in endpoint [{}]", this.simpleClassName, this.routerName);
		this.baseService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	public abstract T parseToEntity(K request);

	public abstract AbstractBaseDto parseToDto(T response);

}
