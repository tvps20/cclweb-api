package br.com.santiago.ccl.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.santiago.ccl.domain.Theme;
import br.com.santiago.ccl.dtos.ThemeRequestDto;
import br.com.santiago.ccl.dtos.ThemeResponseDto;

public class ThemeBuilder {

	private Theme theme;

	private ThemeRequestDto themeRequestDto;

	private ThemeResponseDto themeResponseDto;

	private List<Theme> themes;

	private ThemeBuilder() {

	}

	public static Theme createMockTheme() {
		ThemeBuilder builder = createBaseThemeBuilder();

		return builder.theme;
	}

	public static Optional<Theme> createMockThemeOpt() {
		ThemeBuilder builder = createBaseThemeBuilder();

		return Optional.of(builder.theme);
	}

	public static ThemeRequestDto createMockthemeRequestDto() {
		ThemeBuilder builder = new ThemeBuilder();
		builder.themeRequestDto = ThemeRequestDto.builder().name("Parts Pack").build();

		return builder.themeRequestDto;
	}

	public static ThemeResponseDto createMockthemeResponsetDto() {
		ThemeBuilder builder = new ThemeBuilder();
		builder.themeResponseDto = ThemeResponseDto.builder().name("Parts Pack").build();
		builder.themeResponseDto.setId(1L);

		return builder.themeResponseDto;
	}

	public static List<Theme> createMockThemesList() {
		ThemeBuilder builder = new ThemeBuilder();
		builder.themes = new ArrayList<Theme>();

		for (long i = 1; i <= 10; i++) {
			Theme newTheme = Theme.builder().name("Theme " + i).build();
			newTheme.setId(i);

			builder.themes.add(newTheme);
		}

		return builder.themes;
	}

	private static ThemeBuilder createBaseThemeBuilder() {
		ThemeBuilder builder = new ThemeBuilder();
		builder.theme = Theme.builder().name("Universal Building Set").build();
		builder.theme.setId(1L);

		return builder;
	}

}
