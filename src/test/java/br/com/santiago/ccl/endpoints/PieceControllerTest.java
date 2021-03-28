package br.com.santiago.ccl.endpoints;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import br.com.santiago.ccl.builders.PieceBuilder;
import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.dtos.AbstractResponseBaseDto;
import br.com.santiago.ccl.dtos.PieceRequestDto;
import br.com.santiago.ccl.endpoints.exceptions.EndpointNotExistsException;
import br.com.santiago.ccl.services.PieceService;
import br.com.santiago.ccl.services.interfaces.ICrudService;

class PieceControllerTest extends AbstractBaseControllerTest<Piece, PieceRequestDto> {

	@InjectMocks
	private PieceController pieceController;

	@Mock
	private PieceService pieceService;

	@Test
	@Override
	public void mustReturnSuccess_WhenInsertEntity() {
		assertThrows(EndpointNotExistsException.class, () -> this.pieceController.insert(this.baseRequestDto));
	}

	@Test
	@Override
	public void mustReturnSuccess_WhenDeleteEntity() {
		assertThrows(EndpointNotExistsException.class, () -> this.pieceController.delete(this.baseEntity.getId()));
	}

	@Override
	public AbstractResponseBaseDto mockResponseDtoBuilder() {
		return PieceBuilder.createPieceResponseDto();
	}

	@Override
	public PieceRequestDto mockRequestDtoBuilder() {
		return PieceBuilder.createPieceRequestDto();
	}

	@Override
	public List<Piece> mockCollectionEntityListBuilder() {
		return PieceBuilder.createMockPiecesList();
	}

	@Override
	public Piece mockEntityBuilder() {
		return PieceBuilder.createMockPiece();
	}

	@Override
	public ICrudService<Piece, PieceRequestDto> getService() {
		return this.pieceService;
	}

	@Override
	public AbstractBaseController<Piece, PieceRequestDto> getController() {
		return this.pieceController;
	}

	@Override
	public Piece getEntityUpdate() {
		Piece pieceUpdate = this.baseEntity;
		pieceUpdate.setDescription("Piece update");

		return pieceUpdate;
	}

}
