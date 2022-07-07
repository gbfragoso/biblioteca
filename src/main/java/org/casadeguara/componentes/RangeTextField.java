package org.casadeguara.componentes;

import java.util.ArrayList;
import java.util.List;
import org.casadeguara.alertas.Alerta;
import javafx.scene.control.TextField;

/**
 * Textfield que implementa a funcionalidade de captura de intervalos numéricos,
 * sejam eles separados por vírgula ou hífen.
 * 
 * @author Gustavo
 * @since 3.0
 */
public class RangeTextField extends TextField {

	public RangeTextField() {
		super.setPromptText("Ex: 1,2,3 ou 1-3 ou 1,2,5-8");
	}

	/**
	 * Transforma a informação digitada pelo usuário em um intervalo númerico.
	 * 
	 * @return Lista de inteiros
	 */
	public List<Integer> getRange() {

		List<Integer> range = new ArrayList<>();

		String text = getText().replaceAll("[^0-9,-]", "");
		boolean match = text.matches("\\d+(?:-\\d+)?(?:,\\d+(?:-\\d+)?)*");

		if (match) {
			String[] numbers = text.split(",");

			for (String s : numbers) {
				if (s.contains("-")) {
					String[] range2 = s.split("-");
					int start = Integer.parseInt(range2[0]);
					int end = Integer.parseInt(range2[1]);

					for (int i = start; i <= end; i++) {
						range.add(i);
					}
				} else {
					range.add(Integer.parseInt(s));
				}
			}
		} else {
			new Alerta().informacao("O intervalo de valores está incorreto.");
		}

		return range;
	}

}
