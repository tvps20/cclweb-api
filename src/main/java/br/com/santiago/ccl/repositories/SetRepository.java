package br.com.santiago.ccl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.santiago.ccl.domain.Set;

@Repository
public interface SetRepository extends JpaRepository<Set, Long> {

	@Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM Set c WHERE c.setId = :value")
	boolean alreadyExistsSetId(@Param("value") String value);

}
