package br.com.santiago.ccl.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santiago.ccl.builders.ThemeBuilder;
import br.com.santiago.ccl.domain.Theme;
import br.com.santiago.ccl.dtos.ThemeRequestDto;
import br.com.santiago.ccl.repositories.ThemeRepository;
import br.com.santiago.ccl.services.exceptions.ObjectNotFoundException;
import br.com.santiago.ccl.services.exceptions.ObjectUniqueException;
import br.com.santiago.ccl.services.interfaces.ICrudService;

public class ThemeServiceTest extends AbstractBaseServiceTest<Theme, ThemeRequestDto> {

	@InjectMocks
	private ThemeService themeService;

	@Mock
	private ThemeRepository themerepository;

	@Test
	public void mustReturnException_WhenInsertEntityDuplicateName() {
		when((((ThemeRepository) this.baseRepository).alreadyExistsName(Mockito.anyString()))).thenReturn(true);

		assertThrows(ObjectUniqueException.class, () -> this.baseService.insert(this.baseEntity));
	}

	@Test
	public void mustReturnSuccess_WhenFindByName() {
		String name = this.baseEntity.getName();

		when(this.themerepository.findByName(name)).thenReturn(this.baseEntityOpt);

		Theme result = this.themeService.findByName(name);

		assertEquals(this.baseEntity, result);
	}

	@Test
	public void mustReturnException_WhenFindByName() {
		String name = this.baseEntity.getName();

		when(this.themerepository.findByName(name)).thenReturn(Optional.empty());

		assertThrows(ObjectNotFoundException.class, () -> this.themeService.findByName(name));
	}

	@Override
	public Theme mockEntityBuilder() {
		return ThemeBuilder.createMockTheme();
	}

	@Override
	public Optional<Theme> mockEntityOptBuilder() {
		return ThemeBuilder.createMockThemeOpt();
	}

	@Override
	public List<Theme> mockCollectionEntityListBuilder() {
		return ThemeBuilder.createMockThemesList();
	}

	@Override
	public ICrudService<Theme, ThemeRequestDto> getService() {
		return this.themeService;
	}

	@Override
	public JpaRepository<Theme, Long> getRepository() {
		return this.themerepository;
	}

	@Override
	public Theme getEntityUpdate() {
		Theme themeUpdate = this.baseEntity;
		themeUpdate.setName("Theme update");

		return themeUpdate;
	}

	@Override
	public ThemeRequestDto mockDtoBuilder() {
		return ThemeBuilder.createMockthemeRequestDto();
	}

}
