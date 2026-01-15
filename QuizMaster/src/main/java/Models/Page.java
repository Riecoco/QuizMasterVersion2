package Models;

import java.util.ArrayList;
import java.util.List;

public class Page {
    public int timeLimit;
    public List<Element> elements = new ArrayList<>();

    public Page() {}
    public Page(int timeLimit, List<Element> elements) {
        this.timeLimit = timeLimit;
        this.elements = elements;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}
