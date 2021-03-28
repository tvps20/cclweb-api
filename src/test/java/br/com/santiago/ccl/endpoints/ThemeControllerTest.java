package br.com.santiago.ccl.endpoints;

import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import br.com.santiago.ccl.builders.ThemeBuilder;
import br.com.santiago.ccl.domain.Theme;
import br.com.santiago.ccl.dtos.ThemeRequestDto;
import br.com.santiago.ccl.dtos.ThemeResponseDto;
import br.com.santiago.ccl.services.ThemeService;
import br.com.santiago.ccl.services.interfaces.ICrudService;

class ThemeControllerTest extends AbstractBaseControllerTest<Theme, ThemeRequestDto> {

	@InjectMocks
	private ThemeController themeController;

	@Mock
	private ThemeService themeService;

	@Override
	public List<Theme> mockCollectionEntityListBuilder() {
		return ThemeBuilder.createMockThemesList();
	}

	@Override
	public ICrudService<Theme, ThemeRequestDto> getService() {
		return this.themeService;
	}

	@Override
	public AbstractBaseController<Theme, ThemeRequestDto> getController() {
		return this.themeController;
	}

	@Override
	public Theme mockEntityBuilder() {
		return ThemeBuilder.createMockTheme();
	}

	@Override
	public ThemeResponseDto mockResponseDtoBuilder() {
		return ThemeBuilder.createMockthemeResponsetDto();
	}

	@Override
	public ThemeRequestDto mockRequestDtoBuilder() {
		return ThemeBuilder.createMockthemeRequestDto();
	}

	@Override
	public Theme getEntityUpdate() {
		Theme themeUpdate = this.baseEntity;
		themeUpdate.setName("Theme update");

		return themeUpdate;
	}

}
