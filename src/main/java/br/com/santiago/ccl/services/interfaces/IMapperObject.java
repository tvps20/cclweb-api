package br.com.santiago.ccl.services.interfaces;

import br.com.santiago.ccl.domain.AbstractBaseEntity;
import br.com.santiago.ccl.dtos.AbstractBaseDto;
import br.com.santiago.ccl.dtos.AbstractResponseBaseDto;

public interface IMapperObject<T extends AbstractBaseEntity, K extends AbstractBaseDto> {

	T parseToEntity(K request);

	AbstractResponseBaseDto parseToDto(T entity);

}
