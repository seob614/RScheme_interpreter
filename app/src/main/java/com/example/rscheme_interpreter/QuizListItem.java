package com.example.rscheme_interpreter;

public class QuizListItem {
    private String title;
    private String quiz;
    private String hint;

    private String answer;

    public void setTitle(String title){
        this.title = title;
    }
    public void setQuiz(String quiz){
        this.quiz = quiz;
    }
    public void setHint(String hint){
        this.hint = hint;
    }
    public void setAnswer(String answer){
        this.answer = answer;
    }
    public String getTitle() {
        return this.title;
    }
    public String getQuiz() {
        return this.quiz;
    }
    public String getHint() {
        return this.hint;
    }

    public String getAnswer() {
        return this.answer;
    }
}
