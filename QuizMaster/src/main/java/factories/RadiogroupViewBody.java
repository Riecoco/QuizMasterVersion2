package factories;

import Models.Element;
import Models.Radiogroup;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class RadiogroupViewBody implements QuizViewBody {
    @Override
    public VBox buildViewBody(Element element, VBox choiceBox) {
        Radiogroup rg = (Radiogroup) element;
        ToggleGroup group = new ToggleGroup();
        for (int j = 1; j < rg.choices.size(); j++) {
            RadioButton rb = new RadioButton(rg.choices.get(j));
            rb.setToggleGroup(group);
            choiceBox.getChildren().add(rb);
        }
        return choiceBox;
    }
}
