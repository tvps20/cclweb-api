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
import br.com.santiago.ccl.dtos.SetDetailResponseDto;
import br.com.santiago.ccl.dtos.SetListResponseDto;
import br.com.santiago.ccl.dtos.SetPieceResponseDto;
import br.com.santiago.ccl.dtos.SetRequestDto;
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
		this.simpleClassName = this.getClass().getSimpleName();
	}

	@Override
	@Transactional
	public Set insert(Set entity) {
		this.validUniqueValue(entity.getSetId(), "setId");

		try {
			List<Theme> themes = this.prepareThemesToInsert(entity);
			this.themeService.saveAll(themes);
			entity.setThemes(themes);
			Set setSalved = this.baseRepository.save(entity);
			List<Piece> pcs = this.preparePiecesToInsert(entity);
			this.pieceService.saveAll(pcs);
			log.debug("[{}] [insert] [Success] - Data saved in the database with id: {}.", this.simpleClassName,
					setSalved.getId());

			return setSalved;
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format("[{0}] [insert] [Error] - Failed to save data to the database.",
					this.simpleClassName);
			log.error(this.errorMsg);
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	@Override
	public Set update(Set entity) {
		try {
			Set entitySaved = this.findById(entity.getId());

			// Verificando se o setId foi alterado
			if (!entity.getSetId().equals(entitySaved.getSetId())) {
				this.validUniqueValue(entity.getSetId(), "setId");
			}

			this.updateData(entitySaved, entity);
			Set entityUpdate = this.baseRepository.save(entitySaved);
			log.debug("[{}] [update] [Success] - Data updated in the database with id: {}.", this.simpleClassName,
					entityUpdate.getId());

			return entityUpdate;
		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format(
					"[{0}] [update] [Error] - Failed to update data to the database with id: {1}.", this.simpleClassName,
					entity.getId());
			log.error(this.errorMsg);
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	public Piece insetPiece(Long setId, Piece piece) {
		Set setSalved = this.findById(setId);

		piece.setSet(setSalved);
		Piece pieceSalved = this.pieceService.insert(piece);
		setSalved.getPcs().add(pieceSalved);

		return pieceSalved;
	}

	@Transactional
	public Theme insertTheme(Long setId, Theme theme) {
		try {
			Set setSalved = this.findById(setId);

			// Verificando se o tema ja foi adicionado ao set
			setSalved.getThemes().forEach(x -> {
				if (x.getName().equals(theme.getName())) {
					this.errorMsg = MessageFormat.format(
							"[{0}] [insertTheme] [Error] - Theme already registered in this set with id: {1}.",
							this.simpleClassName, setId);
					log.error(this.errorMsg);
					throw new ObjectUniqueException(this.errorMsg);
				}
			});

			setSalved.getThemes().add(theme);
			List<Theme> themes = this.prepareThemesToInsert(setSalved);
			setSalved.setThemes(themes);
			this.themeService.saveAll(themes);
			this.baseRepository.save(setSalved);

			log.debug("[{}] [insertTheme] [Success] - Theme salved with success in the set with id: {}.",
					this.simpleClassName, setId);
			return theme;

		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format(
					"[{0}] [insertTheme] [Error] - Error when trying to save theme on set with id: {1}.",
					this.simpleClassName, setId);
			log.error(this.errorMsg);
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	public void deletePiece(Long pieceId) {
		this.pieceService.deleteById(pieceId);
	}

	public void deleteTheme(Long setId, Long themeId) {
		try {
			Set setSaved = this.findById(setId);
			Theme themeSalved = this.themeService.findById(themeId);

			// Verificando se o theme ainda existe no set
			if (!setSaved.getThemes().contains(themeSalved)) {
				this.errorMsg = MessageFormat.format(
						"[{0}] [deleteTheme] [Error] - Theme not registered in this set with id: {1}.",
						this.simpleClassName, setId);
				log.error(this.errorMsg);
				throw new ObjectNotFoundException(this.errorMsg);
			}

			List<Theme> themes = this.prepareThemesToDelete(setSaved, themeId);
			setSaved.setThemes(themes);
			this.baseRepository.save(setSaved);
			log.debug("[{}] [deleteTheme] [Success] - Theme deleted in the database with id: {}.", this.simpleClassName,
					themeId);

		} catch (DataIntegrityViolationException ex) {
			this.errorMsg = MessageFormat.format(
					"[{0}] [deleteTheme] [Error] - Error when trying to delete the theme from the database with id: {1}.",
					this.simpleClassName, themeId);
			log.error(this.errorMsg);
			throw new DataIntegrityException(this.errorMsg);
		}
	}

	private List<Theme> prepareThemesToInsert(Set set) {
		log.debug("[{}] [prepareThemesToInsert] [Info] - Prepare Themes To Insert in database.", this.simpleClassName);
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
		log.debug("[{}] [prepareThemesToDelete] [Info] - Prepare Themes To Delete in database.", this.simpleClassName);
		List<Theme> themesForDelete = new ArrayList<>();

		set.getThemes().forEach(x -> {
			if (x.getId() != themeId) {
				themesForDelete.add(x);
			}
		});

		return themesForDelete;
	}

	private List<Piece> preparePiecesToInsert(Set set) {
		log.debug("[{}] [preparePiecesToInsert] [Info] - Prepare Pieces To Insert in database.", this.simpleClassName);
		List<Piece> piecesForInsert = new ArrayList<>();

		set.getPcs().forEach(x -> {
			x.setSet(set);
			piecesForInsert.add(x);
		});

		return piecesForInsert;
	}

	@Override
	public Set parseToEntity(SetRequestDto request) {
		log.trace("[{}] [parseToEntity] [Info] - Parse from SetRequestDto to Set.", this.simpleClassName);
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
		log.trace("[{}] [updateData] [Info] - Updating data that can be modified.", this.simpleClassName);
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
	public SetDetailResponseDto parseToDto(Set entity) {
		log.trace("[{}] [parseToEntity] [Info] - Parse from Set to SetDetailResponseDto.", this.simpleClassName);
		List<ThemeResponseDto> themes = entity.getThemes().stream().map(dto -> this.themeService.parseToDto(dto))
				.collect(Collectors.toList());
		List<SetPieceResponseDto> pcs = entity.getPcs().stream().map(dto -> this.parseToSetPieceDto(dto))
				.collect(Collectors.toList());

		return SetDetailResponseDto.builder().id(entity.getId()).setId(entity.getSetId()).name(entity.getName())
				.themes(themes).year(entity.getYear()).price(entity.getPrice()).pcs(pcs)
				.figuresUrls(entity.getFiguresUrls()).instructionsUrls(entity.getInstructionsUrls()).build();
	}

	public SetListResponseDto parseToSetListDto(Set entity) {
		log.trace("[{}] [parseToEntity] [Info] - Parse from Set to SetListResponseDto.", this.simpleClassName);

		String theme = this.mountThemeName(entity.getThemes());

		return SetListResponseDto.builder().id(entity.getId()).setId(entity.getSetId()).name(entity.getName())
				.theme(theme).year(entity.getYear()).price(entity.getPrice()).pcs(entity.getPcs().size())
				.intructions(!entity.getInstructionsUrls().isEmpty()).imgs(!entity.getFiguresUrls().isEmpty()).build();
	}

	private String mountThemeName(List<Theme> themes) {
		StringBuilder theme = new StringBuilder();

		themes.stream().forEach(x -> theme.append(x.getName() + "/"));
		String newTheme = theme.substring(0, theme.length() - 1);
		log.debug("[{}] [makeCreateThemeName] [Info] - Mount new theme name: {}.", this.simpleClassName, newTheme);

		return newTheme;
	}

	private SetPieceResponseDto parseToSetPieceDto(Piece piece) {
		log.trace("[{}] [parseToEntity] [Info] - Parse from Piece to SetPieceResponseDto.", this.simpleClassName);

		return SetPieceResponseDto.builder().id(piece.getId()).qtd(piece.getQtd()).partNum(piece.getPartNum())
				.color(piece.getColor()).description(piece.getDescription()).pictureUrl(piece.getPictureUrl())
				.note(piece.getNote()).build();
	}

}
