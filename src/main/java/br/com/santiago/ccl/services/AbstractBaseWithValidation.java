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
		if (this.alreadyExistsValue(value)) {
			this.errorMsg = MessageFormat.format(
					"[{0}] [validUniqueValue] [Error] - Data already registered in the database for the value: {1}.",
					this.simpleClassName, value);
			log.error(this.errorMsg);
			throw new ObjectUniqueException(this.errorMsg);
		}

		log.debug("[{}] [validUniqueValue] [Success] - Data not registered in the database for the value: {}.",
				this.simpleClassName, value);
		log.trace("binding parameter [value] as [{}]", value);
		log.trace("binding parameter [fieldUniqueName] as [{}]", fieldUniqueName);

		return false;
	}

	public abstract Boolean alreadyExistsValue(String value);

}
