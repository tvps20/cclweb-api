package br.com.santiago.ccl.services;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.domain.Set;
import br.com.santiago.ccl.dtos.PieceRequestDto;
import br.com.santiago.ccl.dtos.PieceResponseDto;
import br.com.santiago.ccl.repositories.PieceRepository;
import br.com.santiago.ccl.services.exceptions.DataIntegrityException;
import br.com.santiago.ccl.services.exceptions.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PieceService extends AbstractBaseService<Piece, PieceRequestDto> {

	@Autowired
	private SetService setService;

	public PieceService(PieceRepository repository) {
		super(repository);
		this.simpleClassName = "Piece";
	}

	public Piece findByPartNum(String partNum) {
		log.debug("FindByPartNum {} in database", this.simpleClassName);
		log.trace("name parameter [{}]", partNum);
		Optional<Piece> pieceOpt = ((PieceRepository) this.baseRepository).findByPartNum(partNum);

		if (!pieceOpt.isPresent()) {
			this.errorMsg = MessageFormat.format("{0} is not found for partNum [{1}]", this.simpleClassName, partNum);
			log.error(this.errorMsg);
			throw new ObjectNotFoundException(this.errorMsg);
		}

		return pieceOpt.get();
	}

	public List<Piece> savaAll(List<Piece> pieces) {
		log.debug("Save All {}s in database", this.simpleClassName);
//		log.trace("list parameter [{}]", pieces.toString());

		try {
			return this.baseRepository.saveAll(pieces);
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format("Error saving all {0}s in the database", this.simpleClassName);
			log.error(this.errorMsg);
			log.trace("entity parameter", pieces.toString());
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	@Override
	public Piece parseToEntity(PieceRequestDto request) {
		log.debug("Parse pieceRequest to piece");
		log.trace("request parameter [{}]", request.toString());

		Set set = request.getSetId() != null ? this.setService.findById(request.getSetId()) : null;

		return Piece.builder().qtd(request.getQtd()).partNum(request.getPartNum()).color(request.getColor())
				.description(request.getDescription()).pictureUrl(request.getPictureUrl()).note(request.getNote())
				.set(set).build();
	}

	@Override
	public void updateData(Piece entitySaved, Piece newEntity) {
		entitySaved.setQtd(newEntity.getQtd());
		entitySaved.setPartNum(newEntity.getPartNum());
		entitySaved.setColor(newEntity.getColor());
		entitySaved.setDescription(newEntity.getDescription());
	}

	@Override
	public PieceResponseDto parteToDto(Piece entity) {
		log.debug("Parse piece to pieceResponseDto");
		PieceResponseDto response = PieceResponseDto.builder().id(entity.getId()).qtd(entity.getQtd())
				.partNum(entity.getPartNum()).color(entity.getColor()).description(entity.getDescription())
				.pictureUrl(entity.getPictureUrl()).note(entity.getNote()).setId(entity.getSet().getId()).build();

//		log.trace("entity parameter [{}]", entity.toString());

		return response;
	}

}
