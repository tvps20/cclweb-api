package br.com.santiago.ccl.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santiago.ccl.builders.PieceBuilder;
import br.com.santiago.ccl.builders.SetBuilder;
import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.domain.Set;
import br.com.santiago.ccl.dtos.SetRequestDto;
import br.com.santiago.ccl.repositories.SetRepository;
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

}
