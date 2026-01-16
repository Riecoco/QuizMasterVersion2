package Models;

public class CompletedHTMLOnCondition {
    public String expression;
    public String html;

    public CompletedHTMLOnCondition() {
    }

    public CompletedHTMLOnCondition(String expression, String html) {
        this.expression = expression;
        this.html = html;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
