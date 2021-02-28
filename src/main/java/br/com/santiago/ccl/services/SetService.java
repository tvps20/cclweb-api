package br.com.santiago.ccl.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.domain.Set;
import br.com.santiago.ccl.domain.Theme;
import br.com.santiago.ccl.dtos.SetPieceResponseDto;
import br.com.santiago.ccl.dtos.SetRequestDto;
import br.com.santiago.ccl.dtos.SetDetailResponseDto;
import br.com.santiago.ccl.dtos.SetListResponseDto;
import br.com.santiago.ccl.dtos.ThemeResponseDto;
import br.com.santiago.ccl.repositories.SetRepository;
import br.com.santiago.ccl.services.exceptions.DataIntegrityException;
import br.com.santiago.ccl.services.exceptions.ObjectNotFoundException;
import br.com.santiago.ccl.services.exceptions.ObjectUniqueException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SetService extends AbstractBaseWithValidation<Set, SetRequestDto> {

	@Autowired
	private ThemeService themeService;

	@Autowired
	private PieceService pieceService;

	public SetService(SetRepository repository) {
		super(repository);
		this.simpleClassName = "Set";
	}

	@Override
	@Transactional
	public Set insert(Set entity) {
		log.debug("Insert new {} in database", this.simpleClassName);
		this.validUniqueValue(entity.getSetId(), "setId");

		try {
			List<Theme> themes = this.prepareThemesToInsert(entity);
			this.themeService.savaAll(themes);
			entity.setThemes(themes);
			Set setSalved = this.baseRepository.save(entity);
			List<Piece> pcs = this.preparePiecesToInsert(entity);
			this.pieceService.savaAll(pcs);

			return setSalved;
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format("Error saving {0} in the database", this.simpleClassName);
			log.error(this.errorMsg);
//			log.trace("entity parameter [{}]", entity.toString());
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	@Override
	public Set update(Set entity) {
		log.debug("Update {} in database", this.simpleClassName);

		try {
			Set entitySaved = this.findById(entity.getId());

			// Verificando se o setId foi alterado
			if (!entity.getSetId().equals(entitySaved.getSetId())) {
				this.validUniqueValue(entity.getSetId(), "setId");
			}

			this.updateData(entitySaved, entity);

			return this.baseRepository.save(entitySaved);
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format("Error updating {0} in the database", this.simpleClassName);
			log.error(this.errorMsg);
//			log.trace("entity parameter [{}]", entity.toString());
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	public Piece insetPiece(Long setId, Piece piece) {
		log.debug("Insert new Piece in Set in database");
		Set setSalved = this.findById(setId);

		piece.setSet(setSalved);
		Piece pieceSalved = this.pieceService.insert(piece);
		setSalved.getPcs().add(pieceSalved);

		return pieceSalved;
	}

	@Transactional
	public Theme insertTheme(Long setId, Theme theme) {
		log.debug("Insert new Theme in Set in database");

		try {
			Set setSalved = this.findById(setId);

			// Verificando se o tema ja foi adicionado ao set
			setSalved.getThemes().forEach(x -> {
				if (x.getName().equals(theme.getName())) {
					throw new ObjectUniqueException("Theme already registered in this set");
				}
			});

			setSalved.getThemes().add(theme);
			List<Theme> themes = this.prepareThemesToInsert(setSalved);
			setSalved.setThemes(themes);
			this.themeService.savaAll(themes);
			this.baseRepository.save(setSalved);

			return theme;
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format("Error saving {0} in the database", "Theme in Set");
			log.error(this.errorMsg);
//			log.trace("entity parameter [{}]", entity.toString());
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	public void deletePiece(Long pieceId) {
		this.pieceService.deleteById(pieceId);
	}
	
	public void deleteTheme(Long setId, Long themeId) {
		log.debug("Delete Theme in Set in database");

		try {
			Set setSaved = this.findById(setId);
			Theme themeSalved = this.themeService.findById(themeId);

			// Verificando se o theme ainda existe no set
			if (!setSaved.getThemes().contains(themeSalved)) {
				throw new ObjectNotFoundException("Theme not registered in this set");
			}

			List<Theme> themes = this.prepareThemesToDelete(setSaved, themeId);
			setSaved.setThemes(themes);
			this.baseRepository.save(setSaved);

		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format("Error delete {0} in the database", "Theme in Set");
			log.error(this.errorMsg);
//			log.trace("entity parameter [{}]", entity.toString());
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	private List<Theme> prepareThemesToInsert(Set set) {
		log.debug("prepare Themes To Insert in database");
		List<Theme> themesForInsert = new ArrayList<>();

		set.getThemes().forEach(x -> {
			try {
				Theme theme = this.themeService.findByName(x.getName());
				if (!themesForInsert.contains(theme)) {
					themesForInsert.add(theme);
				}
			} catch (ObjectNotFoundException ex) {
				themesForInsert.add(x);
			}
		});

		return themesForInsert;
	}

	private List<Theme> prepareThemesToDelete(Set set, Long themeId) {
		log.debug("prepare Themes To Delete in database");
		List<Theme> themesForDelete = new ArrayList<>();

		set.getThemes().forEach(x -> {
			if (x.getId() != themeId) {
				themesForDelete.add(x);
			}
		});

		return themesForDelete;
	}

	private List<Piece> preparePiecesToInsert(Set set) {
		log.debug("prepare Pieces To Insert in database");
		List<Piece> piecesForInsert = new ArrayList<>();

		set.getPcs().forEach(x -> {
			x.setSet(set);
			piecesForInsert.add(x);
		});

		return piecesForInsert;
	}

	@Override
	public Set parseToEntity(SetRequestDto request) {
		log.debug("Parse setRequest to set");
//		log.trace("request parameter [{}]", request.toString());

		List<Theme> themes = request.getThemes().stream().map(entity -> this.themeService.parseToEntity(entity))
				.collect(Collectors.toList());
		List<Piece> pcs = request.getPcs().stream().map(entity -> this.pieceService.parseToEntity(entity))
				.collect(Collectors.toList());

		return Set.builder().setId(request.getSetId()).name(request.getName()).themes(themes).year(request.getYear())
				.price(request.getPrice()).pcs(pcs).figuresUrls(request.getFiguresUrls())
				.instructionsUrls(request.getInstructionsUrls()).build();
	}

	@Override
	public void updateData(Set entitySaved, Set newEntity) {
		entitySaved.setSetId(newEntity.getSetId());
		entitySaved.setName(newEntity.getName());
		entitySaved.setYear(newEntity.getYear());
		entitySaved.setPrice(newEntity.getPrice());
	}

	@Override
	public Boolean alreadyExistsValue(String value) {
		return (((SetRepository) this.baseRepository).alreadyExistsSetId(value));
	}

	@Override
	public SetDetailResponseDto parteToDto(Set entity) {
		log.debug("Parse set to SetDetailResponseDto");
		List<ThemeResponseDto> themes = entity.getThemes().stream().map(dto -> this.themeService.parteToDto(dto))
				.collect(Collectors.toList());
		List<SetPieceResponseDto> pcs = entity.getPcs().stream().map(dto -> this.parseToSetPieceDto(dto))
				.collect(Collectors.toList());

//		log.trace("entity parameter [{}]", entity.toString());

		return SetDetailResponseDto.builder().id(entity.getId()).setId(entity.getSetId()).name(entity.getName())
				.themes(themes).year(entity.getYear()).price(entity.getPrice()).pcs(pcs)
				.figuresUrls(entity.getFiguresUrls()).instructionsUrls(entity.getInstructionsUrls()).build();
	}

	public SetListResponseDto parseToSetListDto(Set entity) {
		log.debug("Parse set to SetListResponseDto");

		String theme = this.makeCreateThemeName(entity.getThemes());

		return SetListResponseDto.builder().id(entity.getId()).setId(entity.getSetId()).name(entity.getName())
				.theme(theme).year(entity.getYear()).price(entity.getPrice()).pcs(entity.getPcs().size())
				.intructions(!entity.getInstructionsUrls().isEmpty()).imgs(!entity.getFiguresUrls().isEmpty()).build();
	}

	private String makeCreateThemeName(List<Theme> themes) {
		StringBuilder theme = new StringBuilder();

		themes.stream().forEach(x -> theme.append(x.getName() + "/"));

		return theme.substring(0, theme.length() - 1);
	}

	private SetPieceResponseDto parseToSetPieceDto(Piece piece) {
		log.debug("Parse piece to SetPieceResponseDto");

		return SetPieceResponseDto.builder().id(piece.getId()).qtd(piece.getQtd()).partNum(piece.getPartNum())
				.color(piece.getColor()).description(piece.getDescription()).pictureUrl(piece.getPictureUrl())
				.note(piece.getNote()).build();
	}

}
