package br.com.santiago.ccl.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santiago.ccl.domain.AbstractBaseEntity;
import br.com.santiago.ccl.dtos.AbstractBaseDto;
import br.com.santiago.ccl.services.exceptions.DataIntegrityException;
import br.com.santiago.ccl.services.exceptions.ObjectNotFoundException;
import br.com.santiago.ccl.services.interfaces.ICrudService;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractBaseServiceTest<T extends AbstractBaseEntity, K extends AbstractBaseDto> {

	protected ICrudService<T, K> baseService;
	protected JpaRepository<T, Long> baseRepository;
	protected List<T> baseList;
	protected Optional<T> baseEntityOpt;
	protected T baseEntity;
	protected K baseDto;

	@BeforeEach
	public void init() {
		this.baseService = this.getService();
		this.baseRepository = this.getRepository();
		this.baseList = this.mockCollectionEntityListBuilder();
		this.baseEntityOpt = this.mockEntityOptBuilder();
		this.baseEntity = this.mockEntityBuilder();
		this.baseDto = this.mockDtoBuilder();
	}

	@Test
	public void mustReturnSuccess_WhenFindAll() {
		when(this.baseRepository.findAll()).thenReturn(this.baseList);

		List<T> result = this.baseService.findAll();

		assertFalse(result.isEmpty());
	}

	@Test
	public void mustReturnSuccess_WhenFindAllPage() {
		Page<T> listPage = new PageImpl<>(this.baseList);
		PageRequest pageRequest = PageRequest.of(0, 24, Direction.ASC, "id");

		when(this.baseRepository.findAll(pageRequest)).thenReturn(listPage);

		List<T> result = this.baseService.findAllPage(0, 24, "ASC", "id").getContent();

		assertFalse(result.isEmpty());
		assertEquals(1, result.get(0).getId());
	}

	@Test
	public void mustReturnSuccess_WhenFindById() {
		Long id = this.baseEntityOpt.get().getId();

		when(this.baseRepository.findById(id)).thenReturn(this.baseEntityOpt);

		T result = this.baseService.findById(id);

		assertEquals(this.baseEntityOpt.get(), result);
		assertEquals(this.baseEntityOpt.get().getId(), result.getId());
	}

	@Test()
	public void mustReturnException_WhenFindByIdNotFound() {
		Long id = this.baseEntityOpt.get().getId();

		when(this.baseRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(ObjectNotFoundException.class, () -> this.baseService.findById(1L));
		verify(this.baseRepository, Mockito.times(1)).findById(1L);
	}

	@Test
	public void mustReturnSuccess_WhenInsertEntity() {
		when(this.baseRepository.save(this.baseEntity)).thenReturn(this.baseEntity);

		T result = this.baseService.insert(this.baseEntity);

		assertEquals(this.baseEntity, result);
		assertEquals(this.baseEntity.getId(), result.getId());
	}

	@Test
	public void mustReturnException_WhenInsertEntity() {
		doThrow(DataIntegrityViolationException.class).when(this.baseRepository).save(this.baseEntity);

		assertThrows(DataIntegrityException.class, () -> this.baseService.insert(this.baseEntity));
		verify(this.baseRepository, Mockito.times(1)).save(this.baseEntity);
	}

	@Test
	public void mustReturnSuccess_WhenSaveAllEntities() {
		when(this.baseRepository.saveAll(this.baseList)).thenReturn(this.baseList);

		List<T> result = this.baseService.saveAll(this.baseList);

		assertEquals(this.baseList, result);
		assertEquals(this.baseList.size(), 10);
	}

	@Test
	public void mustReturnException_WhenSaveAllEntities() {
		doThrow(DataIntegrityViolationException.class).when(this.baseRepository).saveAll(this.baseList);

		assertThrows(DataIntegrityException.class, () -> this.baseService.saveAll(this.baseList));
		verify(this.baseRepository, Mockito.times(1)).saveAll(this.baseList);
	}

	@Test
	public void mustReturnSuccess_WhenUpdateEntity() {
		Long id = this.baseEntityOpt.get().getId();
		T entityUpdate = this.getEntityUpdate();

		when(this.baseRepository.findById(id)).thenReturn(this.baseEntityOpt);
		when(this.baseRepository.save(entityUpdate)).thenReturn(entityUpdate);

		T result = this.baseService.update(entityUpdate);

		assertEquals(entityUpdate, result);
	}

	@Test
	public void mustReturnException_WhenUpdateEntity() {
		Long id = this.baseEntityOpt.get().getId();

		when(this.baseRepository.findById(id)).thenReturn(this.baseEntityOpt);
		doThrow(DataIntegrityViolationException.class).when(this.baseRepository).save(this.baseEntity);

		assertThrows(DataIntegrityException.class, () -> this.baseService.update(this.baseEntity));
		verify(this.baseRepository, Mockito.times(1)).findById(id);
		verify(this.baseRepository, Mockito.times(1)).save(this.baseEntity);
	}

	@Test
	public void mustReturnException_WhenUpdateEntityNotFound() {
		Long id = this.baseEntityOpt.get().getId();

		when(this.baseRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(ObjectNotFoundException.class, () -> this.baseService.update(this.baseEntity));
	}

	@Test
	public void mustReturnSuccess_WhenDeleteEntity() {
		Long id = this.baseEntityOpt.get().getId();

		when(this.baseRepository.findById(id)).thenReturn(this.baseEntityOpt);

		this.baseService.deleteById(id);

		verify(this.baseRepository, Mockito.times(1)).findById(id);
		verify(this.baseRepository, Mockito.times(1)).deleteById(id);
	}

	@Test
	public void mustReturnException_WhenDeleteEntity() {
		Long id = this.baseEntityOpt.get().getId();

		when(this.baseRepository.findById(id)).thenReturn(this.baseEntityOpt);
		doThrow(DataIntegrityViolationException.class).when(this.baseRepository).deleteById(id);

		assertThrows(DataIntegrityException.class, () -> this.baseService.deleteById(id));
		verify(this.baseRepository, Mockito.times(1)).findById(id);
		verify(this.baseRepository, Mockito.times(1)).deleteById(id);
	}

	@Test
	public void mustReturnException_WhenDeleteEntityNotFound() {
		Long id = this.baseEntityOpt.get().getId();

		when(this.baseRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(ObjectNotFoundException.class, () -> this.baseService.deleteById(id));
	}

	@Test
	public void mustReturnSuccess_WhenParseToEntity() {
		K request = this.baseDto;

		T result = this.baseService.parseToEntity(request);

		assertNotNull(result);
	}

	@Test
	public void mustReturnSuccess_WhenParseToDto() {
		AbstractBaseDto result = this.baseService.parseToDto(this.baseEntity);

		assertNotNull(result);
	}

	public abstract K mockDtoBuilder();

	public abstract T mockEntityBuilder();

	public abstract Optional<T> mockEntityOptBuilder();

	public abstract List<T> mockCollectionEntityListBuilder();

	public abstract ICrudService<T, K> getService();

	public abstract JpaRepository<T, Long> getRepository();

	public abstract T getEntityUpdate();

}
