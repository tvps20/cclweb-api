package br.com.santiago.ccl.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santiago.ccl.builders.PieceBuilder;
import br.com.santiago.ccl.builders.SetBuilder;
import br.com.santiago.ccl.builders.ThemeBuilder;
import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.domain.Set;
import br.com.santiago.ccl.domain.Theme;
import br.com.santiago.ccl.dtos.SetListResponseDto;
import br.com.santiago.ccl.dtos.SetRequestDto;
import br.com.santiago.ccl.repositories.SetRepository;
import br.com.santiago.ccl.services.exceptions.DataIntegrityException;
import br.com.santiago.ccl.services.exceptions.ObjectNotFoundException;
import br.com.santiago.ccl.services.exceptions.ObjectUniqueException;
import br.com.santiago.ccl.services.interfaces.ICrudService;

class SetServiceTest extends AbstractBaseServiceTest<Set, SetRequestDto> {

	@InjectMocks
	private SetService setService;

	@Mock
	private SetRepository setRepository;

	@Mock
	private ThemeService themeServive;

	@Mock
	private PieceService pieceService;

	@Test
	public void mustReturnSuccess_WhenInsertPiece() {
		Long setId = this.baseEntity.getId();
		Piece piece = PieceBuilder.createMockPiece();

		when(this.baseRepository.findById(setId)).thenReturn(this.baseEntityOpt);
		when(this.pieceService.insert(piece)).thenReturn(piece);

		Piece result = this.setService.insetPiece(setId, piece);

		assertEquals(piece, result);
	}

	@Test
	public void mustReturnSuccess_WhenInsertTheme() {
		Long setId = this.baseEntity.getId();
		Theme theme = Theme.builder().name("New Theme").build();

		when(this.baseRepository.findById(setId)).thenReturn(this.baseEntityOpt);
		when(this.themeServive.saveAll(Mockito.anyList())).thenReturn(new ArrayList<Theme>());

		Theme result = this.setService.insertTheme(setId, theme);

		assertEquals(theme, result);
	}

	@Test
	public void mustReturnException_WhenInsertUniqueTheme() {
		Long setId = this.baseEntity.getId();
		Theme theme = ThemeBuilder.createMockTheme();

		when(this.baseRepository.findById(setId)).thenReturn(this.baseEntityOpt);

		assertThrows(ObjectUniqueException.class, () -> this.setService.insertTheme(setId, theme));
		verify(this.baseRepository, Mockito.times(1)).findById(setId);
	}

	@Test
	public void mustReturnException_WhenInsertTheme() {
		Long setId = this.baseEntity.getId();
		Theme theme = Theme.builder().name("New Theme").build();

		when(this.baseRepository.findById(setId)).thenReturn(this.baseEntityOpt);
		doThrow(DataIntegrityViolationException.class).when(this.themeServive).saveAll(Mockito.anyList());

		assertThrows(DataIntegrityException.class, () -> this.setService.insertTheme(setId, theme));
		verify(this.baseRepository, Mockito.times(1)).findById(setId);
	}

	@Test
	public void mustReturnSuccess_WhenDeletePiece() {
		Long pieceId = PieceBuilder.createMockPiece().getId();

		this.setService.deletePiece(pieceId);
		verify(this.pieceService, Mockito.times(1)).deleteById(pieceId);
	}

	@Test
	public void mustReturnSuccess_WhenDeleteTheme() {
		Long setId = this.baseEntity.getId();
		Theme theme = ThemeBuilder.createMockTheme();

		when(this.baseRepository.findById(setId)).thenReturn(this.baseEntityOpt);
		when(this.themeServive.findById(setId)).thenReturn(theme);

		this.setService.deleteTheme(setId, theme.getId());

		verify(this.baseRepository, Mockito.times(1)).findById(setId);
		verify(this.themeServive, Mockito.times(1)).findById(theme.getId());
		verify(this.baseRepository, Mockito.times(1)).save(this.baseEntity);
	}

	@Test
	public void mustReturnException_WhenDeleteThemeNotFound() {
		Long setId = this.baseEntity.getId();
		Long themeId = 1L;
		Theme theme = Theme.builder().name("Theme is not found").build();

		when(this.baseRepository.findById(setId)).thenReturn(this.baseEntityOpt);
		when(this.themeServive.findById(Mockito.anyLong())).thenReturn(theme);

		assertThrows(ObjectNotFoundException.class, () -> this.setService.deleteTheme(setId, themeId));
		verify(this.baseRepository, Mockito.times(1)).findById(setId);
		verify(this.themeServive, Mockito.times(1)).findById(themeId);
	}

	@Test
	public void mustReturnException_WhenDeleteTheme() {
		Long setId = this.baseEntity.getId();
		Theme theme = ThemeBuilder.createMockTheme();

		when(this.baseRepository.findById(setId)).thenReturn(this.baseEntityOpt);
		when(this.themeServive.findById(setId)).thenReturn(theme);
		doThrow(DataIntegrityViolationException.class).when(this.baseRepository).save(this.baseEntity);

		assertThrows(DataIntegrityException.class, () -> this.setService.deleteTheme(setId, theme.getId()));
		verify(this.baseRepository, Mockito.times(1)).findById(setId);
		verify(this.themeServive, Mockito.times(1)).findById(theme.getId());
	}

	@Test
	public void mustReturnSuccess_WhenParseToSetListDto() {
		SetListResponseDto result = this.setService.parseToSetListDto(this.baseEntity);

		assertNotNull(result);
	}

	@Override
	public Set mockEntityBuilder() {
		return SetBuilder.createMockSet();
	}

	@Override
	public Optional<Set> mockEntityOptBuilder() {
		return SetBuilder.createMockSetOpt();
	}

	@Override
	public List<Set> mockCollectionEntityListBuilder() {
		return SetBuilder.createMockSetsList();
	}

	@Override
	public ICrudService<Set, SetRequestDto> getService() {
		return this.setService;
	}

	@Override
	public JpaRepository<Set, Long> getRepository() {
		return this.setRepository;
	}

	@Override
	public Set getEntityUpdate() {
		Set setUpdate = this.baseEntity;
		setUpdate.setSetId("Set Update");

		return setUpdate;
	}

	@Override
	public SetRequestDto mockDtoBuilder() {
		return SetBuilder.createMockSetRequestDto();
	}

}
