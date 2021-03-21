package br.com.santiago.ccl.services.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.santiago.ccl.domain.AbstractBaseEntity;
import br.com.santiago.ccl.dtos.AbstractBaseDto;

public interface ICrudService<T extends AbstractBaseEntity, K extends AbstractBaseDto> extends IMapperObject<T, K> {

	public List<T> findAll();

	public Page<T> findAllPage(Integer page, Integer linesPerPage, String direction, String orderBy);

	public T findById(Long id);

	public T insert(T entity);
	
	public List<T> saveAll(List<T> list); 

	public T update(T entity);

	public void deleteById(Long id);
}
