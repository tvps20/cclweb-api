package br.com.santiago.ccl.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.santiago.ccl.builders.PieceBuilder;
import br.com.santiago.ccl.builders.SetBuilder;
import br.com.santiago.ccl.builders.ThemeBuilder;
import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.domain.Set;
import br.com.santiago.ccl.domain.Theme;
import br.com.santiago.ccl.dtos.AbstractBaseDto;
import br.com.santiago.ccl.dtos.AbstractResponseBaseDto;
import br.com.santiago.ccl.dtos.PieceRequestDto;
import br.com.santiago.ccl.dtos.SetListResponseDto;
import br.com.santiago.ccl.dtos.SetRequestDto;
import br.com.santiago.ccl.dtos.ThemeRequestDto;
import br.com.santiago.ccl.services.PieceService;
import br.com.santiago.ccl.services.SetService;
import br.com.santiago.ccl.services.ThemeService;
import br.com.santiago.ccl.services.interfaces.ICrudService;

class SetControllerTest extends AbstractBaseControllerTest<Set, SetRequestDto> {

	@InjectMocks
	private SetController setController;

	@Mock
	private SetService setService;

	@Mock
	private PieceService pieceService;

	@Mock
	private ThemeService themeService;

	@Test
	@Override
	public void mustReturnSuccess_WhenFindAll() throws Exception {
		SetListResponseDto setListReponse = SetBuilder.createMockSetListResponseDto();

		when(this.baseService.findAll()).thenReturn(this.baseList);
		when(this.setService.parseToSetListDto(Mockito.any())).thenReturn(setListReponse);
		ResponseEntity<List<AbstractBaseDto>> result = this.baseController.listAll();

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(10, result.getBody().size());
		assertFalse(result.getBody().isEmpty());
	}

	@Test
	@Override
	public void mustReturnSuccess_WhenFindAllPage() {
		SetListResponseDto setListReponse = SetBuilder.createMockSetListResponseDto();
		Page<Set> listPage = new PageImpl<>(this.baseList);

		when(this.baseService.findAllPage(0, 24, "ASC", "id")).thenReturn(listPage);
		when(this.setService.parseToSetListDto(Mockito.any())).thenReturn(setListReponse);

		ResponseEntity<Page<AbstractBaseDto>> result = this.baseController.listAllPage(0, 24, "ASC", "id");

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(10, result.getBody().getSize());
		assertFalse(result.getBody().isEmpty());
	}

	@Test
	public void mustReturnSuccess_WhenInsertPiece() {
		Long setId = this.baseEntity.getId();
		PieceRequestDto pieceRequest = PieceBuilder.createPieceRequestDto();
		Piece piece = PieceBuilder.createMockPiece();

		when(this.pieceService.parseToEntity(Mockito.any())).thenReturn(piece);
		when(this.setService.insetPiece(setId, piece)).thenReturn(piece);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ResponseEntity<Void> result = this.setController.insertPiece(setId, pieceRequest);

		assertEquals(HttpStatus.CREATED, result.getStatusCode());
		assertFalse(result.getHeaders().isEmpty());
	}

	@Test
	public void mustReturnSuccess_WhenInsertTheme() {
		Long setId = this.baseEntity.getId();
		ThemeRequestDto pieceRequest = ThemeBuilder.createMockthemeRequestDto();
		Theme theme = ThemeBuilder.createMockTheme();

		when(this.themeService.parseToEntity(Mockito.any())).thenReturn(theme);
		when(this.setService.insertTheme(setId, theme)).thenReturn(theme);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		ResponseEntity<Void> result = this.setController.insertTheme(setId, pieceRequest);

		assertEquals(HttpStatus.CREATED, result.getStatusCode());
		assertFalse(result.getHeaders().isEmpty());
	}

	@Test
	public void mustReturnSuccess_WhenDeletePiece() {
		Long pieceId = PieceBuilder.createMockPiece().getId();

		this.setController.deletePiece(pieceId);

		verify(this.setService, Mockito.times(1)).deletePiece(pieceId);
	}

	@Test
	public void mustReturnSuccess_WhenDeleteTheme() {
		Long setId = this.baseEntity.getId();
		Long themeId = ThemeBuilder.createMockTheme().getId();

		this.setController.deleteTheme(setId, themeId);

		verify(this.setService, Mockito.times(1)).deleteTheme(setId, themeId);
	}

	@Override
	public AbstractResponseBaseDto mockResponseDtoBuilder() {
		return SetBuilder.createMockSetDetailResponseDto();
	}

	@Override
	public SetRequestDto mockRequestDtoBuilder() {
		return SetBuilder.createMockSetRequestDto();
	}

	@Override
	public List<Set> mockCollectionEntityListBuilder() {
		return SetBuilder.createMockSetsList();
	}

	@Override
	public Set mockEntityBuilder() {
		return SetBuilder.createMockSet();
	}

	@Override
	public ICrudService<Set, SetRequestDto> getService() {
		return this.setService;
	}

	@Override
	public AbstractBaseController<Set, SetRequestDto> getController() {
		return this.setController;
	}

	@Override
	public Set getEntityUpdate() {
		Set setUpdate = this.baseEntity;
		setUpdate.setSetId("Set Update");

		return setUpdate;
	}

}
