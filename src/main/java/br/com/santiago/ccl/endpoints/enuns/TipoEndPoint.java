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

	public static String makeRoute(String... endpoints) {
		log.debug("Create new endpoint");
		StringBuilder router = new StringBuilder();

		for (String endPoint : endpoints) {
			log.trace("endpoint parameter [{0}]", endPoint);
			router.append(endPoint);
		}

		log.trace("endpoint router [{0}]", router);
		return router.toString();
	}
}
