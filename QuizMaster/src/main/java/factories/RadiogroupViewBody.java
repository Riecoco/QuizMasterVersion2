package factories;

import Models.Element;
import Models.Radiogroup;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class RadiogroupViewBody implements Answerable, QuizViewBody {
    private final ToggleGroup group = new ToggleGroup();
    private final List<RadioButton> radioButtons = new ArrayList<>();

    public VBox buildViewBody(Element element, VBox choiceBox) {
        Radiogroup rg = (Radiogroup) element;

        for (String choice : rg.getChoices()) {
            RadioButton rb = new RadioButton(choice);
            rb.setToggleGroup(group);
            radioButtons.add(rb);
            choiceBox.getChildren().add(rb);
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
