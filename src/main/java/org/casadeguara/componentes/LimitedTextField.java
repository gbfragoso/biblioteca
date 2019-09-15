package org.casadeguara.componentes;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;

/**
 * Um textfield que limita a inserção de caracteres.
 * 
 * @author gbfragoso
 */
public class LimitedTextField extends TextField {

    private final IntegerProperty maxLength;

    public LimitedTextField() {
        this(0);
    }

    public LimitedTextField(int maxLength) {
        this.maxLength = new SimpleIntegerProperty(this, "maxLength", maxLength);

        lengthProperty().addListener((observable, oldValue, newValue) -> {
            String text = getText();

            // If user try to pass the pattern size, consumes all chars
            if (text != null && newValue.intValue() >= getMaxLength()) {
                setText(text.substring(0, getMaxLength()));
            }
        });
    }

    public int getMaxLength() {
        return maxLength.get();
    }

    public void setMaxLength(int length) {
        maxLength.set(length);
    }

    public IntegerProperty maxLengthProperty() {
        return maxLength;
    }
}
