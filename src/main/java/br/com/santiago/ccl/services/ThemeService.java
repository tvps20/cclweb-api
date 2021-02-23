package br.com.santiago.ccl.services;

import java.text.MessageFormat;
import java.util.List;
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
		this.simpleClassName = "Theme";
	}

	@Override
	public Theme insert(Theme entity) {
		this.validUniqueValue(entity.getName(), "name");
		return super.insert(entity);
	}

	@Override
	public Theme update(Theme entity) {
		log.debug("Update {} in database", this.simpleClassName);
		
		try {
			Theme entitySaved = this.findById(entity.getId());
			
			if(!entity.getName().equals(entitySaved.getName())) {
				this.validUniqueValue(entity.getName(), "name");				
			}
			
			this.updateData(entitySaved, entity);
			
			return this.baseRepository.save(entitySaved);
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format("Error updating {0} in the database", this.simpleClassName);
			log.error(this.errorMsg);
//			log.trace("entity parameter [{}]", entity.toString());
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	public Theme findByName(String name) {
		log.debug("FindByName {} in database", this.simpleClassName);
		log.trace("name parameter [{}]", name);
		Optional<Theme> themeOpt = ((ThemeRepository) this.baseRepository).findByName(name);

		if (!themeOpt.isPresent()) {
			this.errorMsg = MessageFormat.format("{0} is not found for name [{1}]", this.simpleClassName, name);
			log.error(this.errorMsg);
			throw new ObjectNotFoundException(this.errorMsg);
		}

		return themeOpt.get();
	}

	public List<Theme> savaAll(List<Theme> themes) {
		log.debug("Save All {}s in database", this.simpleClassName);
//		log.trace("list parameter [{}]", themes.toString());

		try {
			return this.baseRepository.saveAll(themes);
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format("Error saving all {0}s in the database", this.simpleClassName);
			log.error(this.errorMsg);
			log.trace("entity parameter", themes.toString());
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	@Override
	public Theme parseToEntity(ThemeRequestDto request) {
		log.debug("Parse themeRequest to theme");
//		log.trace("request parameter [{}]", request.toString());
		return Theme.builder().name(request.getName()).build();
	}

	@Override
	public void updateData(Theme entitySaved, Theme newEntity) {
		entitySaved.setName(newEntity.getName());
	}

	@Override
	public Boolean alreadyExistsValue(String value) {
		return (((ThemeRepository) this.baseRepository).alreadyExistsName(value));
	}

	@Override
	public ThemeResponseDto parteToDto(Theme entity) {
		log.debug("Parse theme to themeResponseDto");
//		log.trace("entity parameter [{}]", entity.toString());
		return ThemeResponseDto.builder().id(entity.getId()).name(entity.getName()).build();
	}

}
