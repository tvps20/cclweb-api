package br.com.santiago.ccl.services;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.santiago.ccl.domain.Theme;
import br.com.santiago.ccl.dtos.ThemeRequestDto;
import br.com.santiago.ccl.dtos.ThemeResponseDto;
import br.com.santiago.ccl.repositories.ThemeRepository;
import br.com.santiago.ccl.services.exceptions.DataIntegrityException;
import br.com.santiago.ccl.services.exceptions.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ThemeService extends AbstractBaseWithValidation<Theme, ThemeRequestDto> {

	public ThemeService(ThemeRepository repository) {
		super(repository);
		this.simpleClassName = this.getClass().getSimpleName();
	}

	@Override
	public Theme insert(Theme entity) {
		this.validUniqueValue(entity.getName(), "name");
		return super.insert(entity);
	}

	@Override
	public Theme update(Theme entity) {
		try {
			Theme entitySaved = this.findById(entity.getId());

			// Verificando se o name foi alterado
			if (!entity.getName().equals(entitySaved.getName())) {
				this.validUniqueValue(entity.getName(), "name");
			}

			this.updateData(entitySaved, entity);
			Theme themeUpdate = this.baseRepository.save(entitySaved);
			log.debug("[{}] [update] [Success] - Data updated in the database with id: {}.", this.simpleClassName,
					themeUpdate.getId());

			return themeUpdate;
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format(
					"[{0}] [update] [Error] - Failed to update data to the database with id: {1}.", this.simpleClassName,
					entity.getId());
			log.error(this.errorMsg);
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	public Theme findByName(String name) {
		Optional<Theme> themeOpt = ((ThemeRepository) this.baseRepository).findByName(name);

		if (!themeOpt.isPresent()) {
			this.errorMsg = MessageFormat.format(
					"[{0}] [findByName] [Error] - Failed to fetch data from database with name: {1}.",
					this.simpleClassName, name);
			log.error(this.errorMsg);
			throw new ObjectNotFoundException(this.errorMsg);
		}

		log.debug("[{}] [findByName] [Success] - Finding the data in the database with name: {}.", this.simpleClassName,
				name);
		return themeOpt.get();
	}

	@Override
	public Theme parseToEntity(ThemeRequestDto request) {
		log.trace("[{}] [parseToEntity] [Info] - Parse from ThemeRequestDto to Theme.", this.simpleClassName);
		return Theme.builder().name(request.getName()).build();
	}

	@Override
	public void updateData(Theme entitySaved, Theme newEntity) {
		log.trace("[{}] [updateData] [Info] - Updating data that can be modified.", this.simpleClassName);
		entitySaved.setName(newEntity.getName());
	}

	@Override
	public Boolean alreadyExistsValue(String value) {
		return (((ThemeRepository) this.baseRepository).alreadyExistsName(value));
	}

	@Override
	public ThemeResponseDto parseToDto(Theme entity) {
		log.trace("[{}] [parseToEntity] [Info] - Parse from Theme to ThemeResponseDto.", this.simpleClassName);
		return ThemeResponseDto.builder().id(entity.getId()).name(entity.getName()).build();
	}

}
