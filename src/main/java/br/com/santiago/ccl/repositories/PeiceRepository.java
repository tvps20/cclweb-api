package br.com.santiago.ccl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.santiago.ccl.domain.Piece;

@Repository
public interface PeiceRepository extends JpaRepository<Piece, Long> {

}
