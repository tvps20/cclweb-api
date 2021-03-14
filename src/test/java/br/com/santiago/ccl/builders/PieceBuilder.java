package br.com.santiago.ccl.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.santiago.ccl.domain.Piece;

public class PieceBuilder {

	private Piece piece;

	private List<Piece> pieces;

	private PieceBuilder() {

	}

	public static Piece createMockPiece() {
		PieceBuilder builder = createBasePieceBuilder();

		return builder.piece;
	}

	public static Optional<Piece> createMockPieceOpt() {
		PieceBuilder builder = createBasePieceBuilder();

		return Optional.of(builder.piece);
	}

	public static List<Piece> createMockPiecesList() {
		PieceBuilder builder = new PieceBuilder();
		builder.pieces = new ArrayList<Piece>();

		for (long i = 1; i <= 10; i++) {
			Piece newPiece = Piece.builder().partNum("3003pe" + i).color("Yellow")
					.description("Brick 2 x 2 with id: " + i).build();
			newPiece.setId(i);

			builder.pieces.add(newPiece);
		}

		return builder.pieces;
	}

	private static PieceBuilder createBasePieceBuilder() {
		PieceBuilder builder = new PieceBuilder();
		builder.piece = Piece.builder().partNum("3003pe1").color("Yellow")
				.description("Brick 2 x 2 with Black Eye Pattern on Both Sides").build();
		builder.piece.setId(1L);

		return builder;
	}

}
