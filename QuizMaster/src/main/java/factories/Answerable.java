package factories;

import javafx.scene.control.RadioButton;

import java.util.List;

public interface Answerable {
    String getSelectedAnswer();
    List<RadioButton> getRadioButtons(); // Required for attaching the Next button event
}
