package br.com.santiago.ccl.services;

import java.text.MessageFormat;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santiago.ccl.domain.AbstractBaseEntity;
import br.com.santiago.ccl.dtos.AbstractBaseDto;
import br.com.santiago.ccl.services.exceptions.ObjectUniqueException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractBaseWithValidation<T extends AbstractBaseEntity, K extends AbstractBaseDto>
		extends AbstractBaseService<T, K> {

	public AbstractBaseWithValidation(JpaRepository<T, Long> repository) {
		super(repository);
	}

	public Boolean validUniqueValue(String value, String fieldUniqueName) {
		log.debug("ValidUniqueValue {} of the {} in database", fieldUniqueName, this.simpleClassName);
		log.trace("value parameter [{}]", value);

		if (this.alreadyExistsValue(value)) {
			this.errorMsg = MessageFormat.format("There is already a {0} registered for this {1} [{2}]",
					this.simpleClassName, fieldUniqueName, value);
			log.error(this.errorMsg);
			throw new ObjectUniqueException(this.errorMsg);
		}

		return false;
	}

	public abstract Boolean alreadyExistsValue(String value);

}
