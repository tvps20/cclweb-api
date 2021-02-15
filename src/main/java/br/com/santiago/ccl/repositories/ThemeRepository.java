package br.com.santiago.ccl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.santiago.ccl.domain.Theme;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

}
