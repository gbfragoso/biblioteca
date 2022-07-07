package org.casadeguara.utilitarios;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatador {

	public String data(LocalDate data) {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(data);
	}

	public String dataHora(LocalDateTime data) {
		return DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(data);
	}
}
