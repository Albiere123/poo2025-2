package br.ufpb.dcx.lima.albiere.question;


import java.util.List;

public interface QuestionInterface {
    String getQuestion();
    void setQuestion(String question);
    List<String> getAnswers();
    void setAnswers(List<String> answers);
    String getAnswer();
    void setAnswer(String answer);
    int getTime();
    void setTime(int time);
}
