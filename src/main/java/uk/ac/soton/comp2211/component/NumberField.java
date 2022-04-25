package uk.ac.soton.comp2211.component;

import javafx.scene.control.TextField;

public class NumberField extends TextField {
    private int minValue;
    private int maxValue;

    public NumberField(int _minValue, int _maxValue) {
        minValue = _minValue;
        maxValue = _maxValue;

        this.textProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (isInteger()) limitValue();
                else this.setText(newValue.replaceAll("[^-?\\d]", ""));
            }
        );
    }

    public int getValue() { 
        try {
            return Integer.valueOf(this.getText()); 
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean isInteger() {
        try {
            Integer.parseInt(this.getText());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private void limitValue() {
        int value = Integer.valueOf(this.getText());

        if (value < minValue) this.setText(String.valueOf(minValue));
        else if (value > maxValue) this.setText(String.valueOf(maxValue));
    }
}