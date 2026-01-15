package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuizGame implements Serializable {
    public String title;
    public String description;
    public List<Page> pages = new ArrayList<Page>();
    public String completedHtml;
    public List<CompletedHTMLOnCondition> completedHtmlOnCondition = new ArrayList<>();//change name

    public QuizGame() {}
    public QuizGame(String title, String description, List<Page> pages, String completedHtml, List<CompletedHTMLOnCondition> completedHtmlOnCondition) {
        this.title = title;
        this.description = description;
        this.pages = pages;
        this.completedHtml = completedHtml;
        this.completedHtmlOnCondition = completedHtmlOnCondition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public String getCompletedHtml() {
        return completedHtml;
    }

    public void setCompletedHtml(String completedHtml) {
        this.completedHtml = completedHtml;
    }

}
