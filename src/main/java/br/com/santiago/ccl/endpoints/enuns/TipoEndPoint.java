package br.com.santiago.ccl.endpoints.enuns;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class TipoEndPoint {
	// Não foi criada como enum pois a anotação do controle não aceita

	public static final String THEME = "/themes";
	public static final String PIECE = "/pieces";
	public static final String SET = "/sets";

	public static final String PAGE = "/page";
	public static final String ID = "/{id}";

	public static final String SET_ID = "/{setId}";
	public static final String PIECE_ID = "/{pieceId}";
	public static final String THEME_ID = "/{themeId}";

	public static String mountRoute(String... endpoints) {
		StringBuilder route = new StringBuilder();

		for (String endPoint : endpoints) {
			log.trace("binding parameter [endpoint] as [{0}]", endPoint);
			route.append(endPoint);
		}

		log.debug("[TipoEndPoint] [makeRoute] [Success] - New endpoint made available: {}.", route);
		return route.toString();
	}
}
