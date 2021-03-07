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
	protected String simpleClassName = this.getClass().getSimpleName();
	protected String errorMsg;

	public AbstractBaseService(JpaRepository<T, Long> repository) {
		this.baseRepository = repository;
		this.errorMsg = "Message is not define";
	}

	public List<T> findAll() {
		log.debug("[{}] [findAll] [Success] - Finding the data in the database.", this.simpleClassName);
		return this.baseRepository.findAll();
	}

	public Page<T> findAllPage(Integer page, Integer linesPerPage, String direction, String orderBy) {
		log.debug("[{}] [findAllPage] [Success] - Finding the data in the database.", this.simpleClassName);
		log.trace("binding parameter [page] as [{}]", page);
		log.trace("binding parameter [linesPerPage] as [{}]", linesPerPage);
		log.trace("binding parameter [orderBy] as [{}]", orderBy);
		log.trace("binding parameter [direction] as [{}]", direction);
		Direction directionParse = direction.toUpperCase().equals("ASC") ? Direction.ASC : Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, directionParse, orderBy);

		return this.baseRepository.findAll(pageRequest);
	}

	public T findById(Long id) {
		Optional<T> entityOpt = this.baseRepository.findById(id);

		if (!entityOpt.isPresent()) {
			this.errorMsg = MessageFormat.format(
					"[{0}] [findById] [Error] - Failed to fetch data from database with id: {1}.", this.simpleClassName,
					id);
			log.error(this.errorMsg);
			throw new ObjectNotFoundException(this.errorMsg);
		}

		log.debug("[{}] [findById] [Success] - Finding the data in the database with id: {}.", this.simpleClassName, id);
		return entityOpt.get();
	}

	public T insert(T entity) {
		try {
			T entitySalved = this.baseRepository.save(entity);
			log.debug("[{}] [insert] [Success] - Data saved in the database with id: {}.", this.simpleClassName,
					entitySalved.getId());

			return entitySalved;
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format("[{0}] [insert] [Error] - Failed to save data to the database.",
					this.simpleClassName);
			log.error(this.errorMsg);
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	public List<T> saveAll(List<T> list) {
		try {
			List<T> listSalved = this.baseRepository.saveAll(list);
			log.debug("[{}] [savaAll] [Success] - List saved in the database.", this.simpleClassName);

			return listSalved;
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format("[{0}] [savaAll] [Error] - Failed to save list to the database.",
					this.simpleClassName);
			log.error(this.errorMsg);
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	public T update(T entity) {

		try {
			T entitySaved = this.findById(entity.getId());
			this.updateData(entitySaved, entity);
			T entityUpdate = this.baseRepository.save(entitySaved);
			log.debug("[{}] [update] [Success] - Data updated in the database with id: {}.", this.simpleClassName,
					entityUpdate.getId());

			return entityUpdate;
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format(
					"[{0}] [update] [Error] - Failed to update data to the database with id: {1}.", this.simpleClassName,
					entity.getId());
			log.error(this.errorMsg);
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	public void deleteById(Long id) {
		try {
			this.findById(id);
			this.baseRepository.deleteById(id);
			log.debug("[{}] [deleteById] [Success] - Data deleted in the database with id: {}.", this.simpleClassName,
					id);
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format(
					"[{0}] [deleteById] [Error] - Failed to update data to the database with id: {1}.",
					this.simpleClassName, id);
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
