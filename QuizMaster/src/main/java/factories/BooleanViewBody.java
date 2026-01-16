package factories;

import Models.Element;
import Models.Boolean;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class BooleanViewBody implements QuizViewBody, Answerable {
    private final ToggleGroup group = new ToggleGroup();
    private final List<RadioButton> radioButtons = new ArrayList<>();

    @Override
    public VBox buildViewBody(Element element, VBox choiceBox) {
        Boolean bool = (Boolean) element;
        radioButtons.clear();

        String[] choices = {bool.labelTrue, bool.labelFalse};
        for (String choice : choices) {
            RadioButton rb = new RadioButton(choice);
            rb.setToggleGroup(group);
            choiceBox.getChildren().add(rb);
            radioButtons.add(rb);
        }
        return choiceBox;
    }

    @Override
    public String getSelectedAnswer() {
        RadioButton selected = (RadioButton) group.getSelectedToggle();
        return selected != null ? selected.getText() : null;
    }

    @Override
    public List<RadioButton> getRadioButtons() {
        return radioButtons;
    }
}
