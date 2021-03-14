package br.com.santiago.ccl.services;

import java.util.List;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santiago.ccl.builders.PieceBuilder;
import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.dtos.PieceRequestDto;
import br.com.santiago.ccl.repositories.PieceRepository;
import br.com.santiago.ccl.services.interfaces.ICrudService;

class PieceServiceTest extends AbstractBaseServiceTest<Piece, PieceRequestDto> {

	@InjectMocks
	private PieceService pieceService;

	@Mock
	private PieceRepository pieceRepository;

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

}
