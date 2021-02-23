package br.com.santiago.ccl.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.santiago.ccl.domain.Piece;

@Repository
public interface PieceRepository extends JpaRepository<Piece, Long> {

	public Optional<Piece> findByPartNum(String partNum);

}
