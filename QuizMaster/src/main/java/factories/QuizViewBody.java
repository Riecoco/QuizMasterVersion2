package factories;

import Models.Element;
import javafx.scene.layout.VBox;

public interface QuizViewBody {
    public VBox buildViewBody(Element element, VBox choiceBox);
}
