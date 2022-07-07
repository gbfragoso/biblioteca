package org.casadeguara.componentes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * Botão que exibe uma notificação da forma de número.
 * 
 * @author Gustavo
 * @since 3.0
 */
public class NotificationButton extends Button {

	private Label notification;

	public NotificationButton() {
		this(null);
	}

	public NotificationButton(String buttonText) {
		super(buttonText);
		super.getStyleClass().setAll("notification-button");
	}

	/**
	 * Cria a notificação com uma cor específica.
	 * 
	 * @param backgroundColor Cor da notificação
	 */
	public void createNotification(Color backgroundColor) {
		notification = new Label("0");
		notification.setPadding(new Insets(2, 4, 2, 4));
		notification
				.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
		super.setGraphic(notification);
		super.setAlignment(Pos.CENTER_LEFT);
	}

	/**
	 * Muda o texto da notificação.
	 * 
	 * @param number Texto da notificação (apenas números)
	 */
	public void setNotificationText(int number) {
		if (number > 0) {
			setNotificationColor(Color.GREEN);
		} else {
			setNotificationColor(Color.RED);
		}
		notification.setText(Integer.toString(number));

	}

	/**
	 * Muda a cor da notificação.
	 * 
	 * @param backgroundColor Nova cor de fundo.
	 */
	private void setNotificationColor(Color backgroundColor) {
		notification
				.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
	}
}
