package br.com.santiago.ccl.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.santiago.ccl.domain.Theme;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

	public Optional<Theme> findByName(String name);

	@Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM Theme c WHERE c.name = :value")
	boolean alreadyExistsName(@Param("value") String value);

}
