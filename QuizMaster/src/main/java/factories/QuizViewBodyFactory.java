package factories;

import Models.Element;
import Models.Radiogroup;
import Models.Boolean;
import javafx.scene.layout.VBox;

public class QuizViewBodyFactory {

    public QuizViewBody createElementViewBody(Element element) {
        if (element instanceof Radiogroup) {
            return new RadiogroupViewBody();
        } else if (element instanceof Boolean) {
            return new BooleanViewBody();
        } else {
            throw new IllegalArgumentException("Unknown element type: " + element.getClass());
        }
    }
}
