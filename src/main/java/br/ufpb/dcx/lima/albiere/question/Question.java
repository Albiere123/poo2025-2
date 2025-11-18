package br.ufpb.dcx.lima.albiere.question;

import java.util.List;

public class Question implements QuestionInterface {

    private String question;
    private List<String> answers;
    private String answer;
    private int time;

    public Question(String question, List<String> answers, String answer, int time) {
        this.question = question;
        this.answers = answers;
        this.answer = answer;
        this.time = time;
    }

    public Question() {
        this("", List.of(), "", 0);
    }

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Question["+question+", answers=" + answers + ", time=" + time + "]";
    }
}
