package br.com.santiago.ccl.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santiago.ccl.builders.PieceBuilder;
import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.dtos.PieceRequestDto;
import br.com.santiago.ccl.repositories.PieceRepository;
import br.com.santiago.ccl.services.exceptions.ObjectNotFoundException;
import br.com.santiago.ccl.services.interfaces.ICrudService;

class PieceServiceTest extends AbstractBaseServiceTest<Piece, PieceRequestDto> {

	@InjectMocks
	private PieceService pieceService;

	@Mock
	private PieceRepository pieceRepository;

	@Test
	public void mustReturnSuccess_WhenFindByPartNum() {
		String partNum = this.baseEntity.getPartNum();

		when(this.pieceRepository.findByPartNum(partNum)).thenReturn(this.baseEntityOpt);

		Piece result = this.pieceService.findByPartNum(partNum);

		assertEquals(this.baseEntity, result);
	}

	@Test
	public void mustReturnException_WhenFindByPartNum() {
		String partNum = this.baseEntity.getPartNum();

		when(this.pieceRepository.findByPartNum(partNum)).thenReturn(Optional.empty());

		assertThrows(ObjectNotFoundException.class, () -> this.pieceService.findByPartNum(partNum));
	}

	@Override
	public Piece mockEntityBuilder() {
		return PieceBuilder.createMockPiece();
	}

	@Override
	public Optional<Piece> mockEntityOptBuilder() {
		return PieceBuilder.createMockPieceOpt();
	}

	@Override
	public List<Piece> mockCollectionEntityListBuilder() {
		return PieceBuilder.createMockPiecesList();
	}

	@Override
	public ICrudService<Piece, PieceRequestDto> getService() {
		return this.pieceService;
	}

	@Override
	public JpaRepository<Piece, Long> getRepository() {
		return this.pieceRepository;
	}

	@Override
	public Piece getEntityUpdate() {
		Piece pieceUpdate = this.baseEntity;
		pieceUpdate.setDescription("Piece update");

		return pieceUpdate;
	}

	@Override
	public PieceRequestDto mockDtoBuilder() {
		return PieceBuilder.createPieceRequestDto();
	}

}
