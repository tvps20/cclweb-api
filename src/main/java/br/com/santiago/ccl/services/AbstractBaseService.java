package br.com.santiago.ccl.services;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santiago.ccl.domain.AbstractBaseEntity;
import br.com.santiago.ccl.dtos.AbstractBaseDto;
import br.com.santiago.ccl.services.exceptions.DataIntegrityException;
import br.com.santiago.ccl.services.exceptions.ObjectNotFoundException;
import br.com.santiago.ccl.services.interfaces.ICrudService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractBaseService<T extends AbstractBaseEntity, K extends AbstractBaseDto>
		implements ICrudService<T, K> {

	protected JpaRepository<T, Long> baseRepository;
	protected String simpleClassName = "Base";
	protected String errorMsg;

	public AbstractBaseService(JpaRepository<T, Long> repository) {
		this.baseRepository = repository;
		this.errorMsg = "Message is not define";
	}

	public List<T> findAll() {
		log.debug("FindAll {}s in database", this.simpleClassName);
		return this.baseRepository.findAll();
	}

	public Page<T> findAllPage(Integer page, Integer linesPerPage, String direction, String orderBy) {
		log.debug("FindAllPage {}s in database", this.simpleClassName);
		log.trace("page parameter [{}]", page);
		log.trace("linesPerPage parameter [{}]", linesPerPage);
		log.trace("orderBy parameter [{}]", orderBy);
		log.trace("direction parameter [{}]", direction);
		Direction directionParse = direction.toUpperCase().equals("ASC") ? Direction.ASC : Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, directionParse, orderBy);
		return this.baseRepository.findAll(pageRequest);
	}

	public T findById(Long id) {
		log.debug("FindById {} in database", this.simpleClassName);
		log.trace("id parameter [{}]", id);
		Optional<T> entityOpt = this.baseRepository.findById(id);

		if (!entityOpt.isPresent()) {
			this.errorMsg = MessageFormat.format("{0} is not found for id [{1}]", this.simpleClassName, id);
			log.error(this.errorMsg);
			throw new ObjectNotFoundException(this.errorMsg);
		}

		return entityOpt.get();
	}

	public T insert(T entity) {
		log.debug("Insert new {} in database", this.simpleClassName);
//		log.trace("entity parameter [{}]", entity.toString());

		try {
			return this.baseRepository.save(entity);
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format("Error saving {0} in the database", this.simpleClassName);
			log.error(this.errorMsg);
//			log.trace("entity parameter [{}]", entity.toString());
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	public T update(T entity) {
		log.debug("Update {} in database", this.simpleClassName);
//		log.trace("entity parameter [{}]", entity.toString());

		try {
			T entitySaved = this.findById(entity.getId());
			this.updateData(entitySaved, entity);

			return this.baseRepository.save(entitySaved);
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format("Error updating {0} in the database", this.simpleClassName);
			log.error(this.errorMsg);
//			log.trace("entity parameter [{}]", entity.toString());
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	public void deleteById(Long id) {
		log.debug("Delete {} in database", this.simpleClassName);
		log.trace("id parameter [{}]", id);
		try {
			this.findById(id);
			this.baseRepository.deleteById(id);
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format("Error deleting {0} in the database with id {1}", this.simpleClassName,
					id);
			log.error(this.errorMsg);
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	/**
	 * Atualiza a entidade salva no banco com os novos dados passando pela nova
	 * entidade
	 * 
	 * @param entitySaved entidade salva no banco
	 * @param newEntity   entidade com novos dados a ser salvo no banco
	 */
	public abstract void updateData(T entitySaved, T newEntity);
}
