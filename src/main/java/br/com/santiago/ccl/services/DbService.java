package br.com.santiago.ccl.services;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.domain.Set;
import br.com.santiago.ccl.domain.Theme;
import br.com.santiago.ccl.repositories.PieceRepository;
import br.com.santiago.ccl.repositories.SetRepository;
import br.com.santiago.ccl.repositories.ThemeRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DbService {

	@Autowired
	private ThemeRepository themeRepository;

	@Autowired
	private SetRepository setRepository;

	@Autowired
	private PieceRepository peiceRepository;

	public void initDataBase() {
		log.debug("[{}] [initDataBase] [Info] - Started database with moke data.", this.getClass().getSimpleName());
		Theme theme1 = Theme.builder().name("LEGO").build();
		Theme theme2 = Theme.builder().name("BASIC").build();
		Theme theme3 = Theme.builder().name("Accessories").build();
		Theme theme4 = Theme.builder().name("Promotional").build();

		Set set1 = Set.builder().setId("1-1").name("Bulldozer Chainlinks").themes(Arrays.asList(theme1, theme2, theme3))
				.year(1982).price(BigDecimal.valueOf(5, 5)).build();
		Set set2 = Set.builder().setId("1-10").name("{Kraft Promotional} Mini-Wheel Model Maker No. 1")
				.themes(Arrays.asList(theme1, theme2, theme4)).year(1971).price(BigDecimal.valueOf(0)).build();

		Piece piece1 = Piece.builder().qtd(50).partNum("3873").color("Black").description("echnic Chain Tread")
				.set(set1).build();
		Piece piece2 = Piece.builder().qtd(2).partNum("3005").color("Black").description("Brick 1 x 1").set(set2)
				.build();
		Piece piece3 = Piece.builder().qtd(4).partNum("3062a").color("Clear")
				.description("Brick 1 x 1 Round with Solid Stud").set(set2).build();

		this.themeRepository.saveAll(Arrays.asList(theme1, theme2, theme3, theme4));
		this.setRepository.saveAll(Arrays.asList(set1, set2));
		this.peiceRepository.saveAll(Arrays.asList(piece1, piece2, piece3));
		log.debug("[{}] [initDataBase] [Info] - Finished insert in the database with moke data.",
				this.getClass().getSimpleName());
	}

}
