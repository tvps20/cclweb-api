package br.com.santiago.ccl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.santiago.ccl.domain.Set;

@Repository
public interface SetRepository extends JpaRepository<Set, Long> {

}
