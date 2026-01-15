package factories;

import Models.Element;
import Models.Radiogroup;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class QuizViewBodyFactory {
    public VBox createElementViewBody(Element element) {
        //vbox container settings
        VBox choiceBox = new VBox();
        choiceBox.setSpacing(10);
        choiceBox.setAlignment(Pos.CENTER_LEFT);
        GridPane.setHalignment(choiceBox, HPos.LEFT);

        //return a vbox of bool or radio group
        if(element instanceof Radiogroup){
            return new RadiogroupViewBody().buildViewBody(element, choiceBox);
        }
        else return new BooleanViewBody().buildViewBody(element, choiceBox);
    }
}
