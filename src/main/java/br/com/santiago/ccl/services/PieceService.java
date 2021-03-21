package br.com.santiago.ccl.services;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.dtos.PieceRequestDto;
import br.com.santiago.ccl.dtos.PieceResponseDto;
import br.com.santiago.ccl.repositories.PieceRepository;
import br.com.santiago.ccl.services.exceptions.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PieceService extends AbstractBaseService<Piece, PieceRequestDto> {

	public PieceService(PieceRepository repository) {
		super(repository);
		this.simpleClassName = this.getClass().getSimpleName();
	}

	public Piece findByPartNum(String partNum) {
		Optional<Piece> pieceOpt = ((PieceRepository) this.baseRepository).findByPartNum(partNum);

		if (!pieceOpt.isPresent()) {
			this.errorMsg = MessageFormat.format(
					"[{0}] [findByPartNum] [Error] - Failed to fetch data from database with partNum: {1}.",
					this.simpleClassName, partNum);
			log.error(this.errorMsg);
			throw new ObjectNotFoundException(this.errorMsg);
		}

		log.debug("[{}] [findByPartNum] [Success] - Finding the data in the database with partNum: {}.",
				this.simpleClassName, partNum);
		return pieceOpt.get();
	}

	@Override
	public Piece parseToEntity(PieceRequestDto request) {
		log.trace("[{}] [parseToEntity] [Info] - Parse from PieceRequestDto to Piece.", this.simpleClassName);

		return Piece.builder().qtd(request.getQtd()).partNum(request.getPartNum()).color(request.getColor())
				.description(request.getDescription()).pictureUrl(request.getPictureUrl()).note(request.getNote())
				.build();
	}

	@Override
	public void updateData(Piece entitySaved, Piece newEntity) {
		log.trace("[{}] [updateData] [Info] - Updating data that can be modified.", this.simpleClassName);
		entitySaved.setQtd(newEntity.getQtd());
		entitySaved.setPartNum(newEntity.getPartNum());
		entitySaved.setColor(newEntity.getColor());
		entitySaved.setDescription(newEntity.getDescription());
	}

	@Override
	public PieceResponseDto parseToDto(Piece entity) {
		log.trace("[{}] [parseToEntity] [Info] - Parse from Piece to PieceResponseDto.", this.simpleClassName);

		return PieceResponseDto.builder().id(entity.getId()).qtd(entity.getQtd()).partNum(entity.getPartNum())
				.color(entity.getColor()).description(entity.getDescription()).pictureUrl(entity.getPictureUrl())
				.note(entity.getNote()).setId(entity.getSet().getId()).build();
	}

}
