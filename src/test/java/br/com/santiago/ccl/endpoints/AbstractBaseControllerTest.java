package br.com.santiago.ccl.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.santiago.ccl.domain.AbstractBaseEntity;
import br.com.santiago.ccl.dtos.AbstractBaseDto;
import br.com.santiago.ccl.dtos.AbstractResponseBaseDto;
import br.com.santiago.ccl.services.interfaces.ICrudService;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractBaseControllerTest<T extends AbstractBaseEntity, K extends AbstractBaseDto> {

	protected AbstractBaseController<T, K> baseController;
	protected ICrudService<T, K> baseService;
	protected List<T> baseList;
	protected T baseEntity;
	protected AbstractResponseBaseDto baseResponseDto;
	protected K baseRequestDto;

	@BeforeEach
	public void init() {
		this.baseController = this.getController();
		this.baseService = this.getService();
		this.baseList = this.mockCollectionEntityListBuilder();
		this.baseEntity = this.mockEntityBuilder();
		this.baseResponseDto = this.mockResponseDtoBuilder();
		this.baseRequestDto = this.mockRequestDtoBuilder();
	}

	@Test
	public void mustReturnSuccess_WhenFindAll() throws Exception {
		when(this.baseService.findAll()).thenReturn(this.baseList);
		when(this.baseService.parseToDto(Mockito.any())).thenReturn(this.baseResponseDto);

		ResponseEntity<List<AbstractBaseDto>> result = this.baseController.listAll();

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(10, result.getBody().size());
		assertFalse(result.getBody().isEmpty());
	}

	@Test
	public void mustReturnSuccess_WhenFindAllPage() {
		Page<T> listPage = new PageImpl<>(this.baseList);

		when(this.baseService.findAllPage(0, 24, "ASC", "id")).thenReturn(listPage);
		when(this.baseService.parseToDto(Mockito.any())).thenReturn(this.baseResponseDto);

		ResponseEntity<Page<AbstractBaseDto>> result = this.baseController.listAllPage(0, 24, "ASC", "id");

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(10, result.getBody().getSize());
		assertFalse(result.getBody().isEmpty());
	}

	@Test
	public void mustReturnSuccess_WhenFindById() {
		Long id = this.baseEntity.getId();

		when(this.baseService.findById(id)).thenReturn(this.baseEntity);
		when(this.baseService.parseToDto(this.baseEntity)).thenReturn(this.baseResponseDto);

		ResponseEntity<AbstractBaseDto> result = this.baseController.findById(id);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(this.baseResponseDto, result.getBody());
	}

	@Test
	public void mustReturnSuccess_WhenInsertEntity() throws URISyntaxException {

		when(this.baseService.parseToEntity(this.baseRequestDto)).thenReturn(this.baseEntity);
		when(this.baseService.insert(this.baseEntity)).thenReturn(this.baseEntity);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ResponseEntity<Void> result = this.baseController.insert(this.baseRequestDto);

		assertEquals(HttpStatus.CREATED, result.getStatusCode());
		assertFalse(result.getHeaders().isEmpty());
	}

	@Test
	public void mustReturnSuccess_WhenUpdateEntity() {
		T entityupdate = this.getEntityUpdate();

		when(this.baseService.parseToEntity(this.baseRequestDto)).thenReturn(entityupdate);
		when(this.baseService.update(entityupdate)).thenReturn(entityupdate);

		ResponseEntity<Void> result = this.baseController.update(this.baseRequestDto, this.baseEntity.getId());

		assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
	}

	@Test
	public void mustReturnSuccess_WhenDeleteEntity() {
		Long id = this.baseEntity.getId();

		this.baseController.delete(id);

		verify(this.baseService, Mockito.times(1)).deleteById(id);
	}

	@Test
	public void mustReturnSuccess_WhenParseToDto() {
		when(this.baseService.parseToDto(Mockito.any())).thenReturn(this.baseResponseDto);

		AbstractResponseBaseDto result = (AbstractResponseBaseDto) this.baseController.parseToDto(this.baseEntity);

		assertEquals(this.baseRequestDto, result);
	}

	public abstract AbstractResponseBaseDto mockResponseDtoBuilder();

	public abstract K mockRequestDtoBuilder();

	public abstract List<T> mockCollectionEntityListBuilder();

	public abstract T mockEntityBuilder();

	public abstract ICrudService<T, K> getService();

	public abstract AbstractBaseController<T, K> getController();

	public abstract T getEntityUpdate();

}
