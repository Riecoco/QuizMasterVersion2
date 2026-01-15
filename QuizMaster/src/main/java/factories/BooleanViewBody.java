package factories;

import Models.Element;
import Models.Boolean;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class BooleanViewBody implements QuizViewBody {
    @Override
    public VBox buildViewBody(Element element, VBox choiceBox) {
        ToggleGroup group = new ToggleGroup();
        Boolean bool = (Boolean)element;
        String[] choices = new String[]{bool.labelTrue, bool.labelFalse};
        for (String choice : choices) {
            RadioButton rb = new RadioButton(choice);
            rb.setToggleGroup(group);
            choiceBox.getChildren().add(rb);
        }
        return choiceBox;
    }
}
