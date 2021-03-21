package br.com.santiago.ccl.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.domain.Set;
import br.com.santiago.ccl.domain.Theme;
import br.com.santiago.ccl.dtos.SetRequestDto;

public class SetBuilder {

	private Set set;

	private SetRequestDto setRequestDto;

	private List<Set> sets;

	private SetBuilder() {

	}

	public static Set createMockSet() {
		SetBuilder builder = createBaseSetBuilder();

		return builder.set;
	}

	public static Optional<Set> createMockSetOpt() {
		SetBuilder builder = createBaseSetBuilder();

		return Optional.of(builder.set);
	}

	public static SetRequestDto createMockSetRequestDto() {
		SetBuilder builder = new SetBuilder();
		builder.setRequestDto = SetRequestDto.builder().setId("2-5").name("Digger Bucket Assembly").year(1981).build();

		return builder.setRequestDto;
	}

	public static List<Set> createMockSetsList() {
		SetBuilder builder = new SetBuilder();
		builder.sets = new ArrayList<Set>();

		for (long i = 1; i <= 10; i++) {
			Set newSet = Set.builder().setId("1-8" + i).name("Basic Set with id: " + i).year(1973).build();
			newSet.setId(i);

			builder.sets.add(newSet);
		}

		return builder.sets;
	}

	private static SetBuilder createBaseSetBuilder() {
		Theme theme = ThemeBuilder.createMockTheme();
		Piece piece = PieceBuilder.createMockPiece();
		SetBuilder builder = new SetBuilder();
		builder.set = Set.builder().setId("1-87").name("Basic Set").year(1973).build();
		builder.set.setId(1L);
		builder.set.getThemes().add(theme);
		builder.set.getPcs().add(piece);

		return builder;
	}

}
