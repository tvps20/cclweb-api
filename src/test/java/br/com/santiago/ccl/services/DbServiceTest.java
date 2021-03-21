package br.com.santiago.ccl.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.santiago.ccl.domain.Piece;
import br.com.santiago.ccl.domain.Set;
import br.com.santiago.ccl.domain.Theme;
import br.com.santiago.ccl.repositories.PieceRepository;
import br.com.santiago.ccl.repositories.SetRepository;
import br.com.santiago.ccl.repositories.ThemeRepository;

@ExtendWith(MockitoExtension.class)
public class DbServiceTest {

	@InjectMocks
	private DbService dbService;

	@Mock
	private ThemeRepository themeRepository;

	@Mock
	private SetRepository setRepository;

	@Mock
	private PieceRepository peiceRepository;

	@Test
	public void mustReturnSuccess_WhenInitDataBase() {
		when(this.themeRepository.saveAll(Mockito.anyList())).thenReturn(new ArrayList<Theme>());
		when(this.peiceRepository.saveAll(Mockito.anyList())).thenReturn(new ArrayList<Piece>());
		when(this.setRepository.saveAll(Mockito.anyList())).thenReturn(new ArrayList<Set>());

		this.dbService.initDataBase();

		verify(this.themeRepository, Mockito.times(1)).saveAll(Mockito.anyList());
		verify(this.peiceRepository, Mockito.times(1)).saveAll(Mockito.anyList());
		verify(this.setRepository, Mockito.times(1)).saveAll(Mockito.anyList());
	}

}
