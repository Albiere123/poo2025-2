package br.ufpb.dcx.lima.albiere;

import br.ufpb.dcx.lima.albiere.config.MySql;
import br.ufpb.dcx.lima.albiere.exceptions.IdExistsException;
import br.ufpb.dcx.lima.albiere.exceptions.QuestionExistsException;
import br.ufpb.dcx.lima.albiere.exceptions.QuestionNotExistsException;
import br.ufpb.dcx.lima.albiere.exceptions.QuestionNumberIsNotValidException;
import br.ufpb.dcx.lima.albiere.question.Question;
import br.ufpb.dcx.lima.albiere.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionSystemGerence {

    private List<Question> questions;
    private List<Question> questionsTemp;
    private List<User> users;
    private final MySql mysql = new MySql();

    QuestionSystemGerence() {
        mysql.createTablesIfNotExists();
        questions = mysql.loadAllQuestions();
        users = mysql.loadAllUsers();
        questionsTemp = new ArrayList<>(this.questions);
    }


    void UserRegister(String username, int id){
        User u = new  User(username, id);
        for(User user : users) {
            if(user.getId() == id) throw new IdExistsException("Esse id já existe!");
        }
        users.add(u);
    }

    User getUser(int id) {
        for(User user : users) {
            if(user.getId() == id) return user;
        }
        return null;
    }

    void QuestionRegister(String question,  List<String> answers,  String answer, int time) throws QuestionExistsException {
        Question q = new Question(question, answers, answer, time);
        if(questions.contains(q)) throw new QuestionExistsException("A pergunta já existe!");
        else questions.add(q);
    }

    List<Question> getQuestions() {
        return questions;
    }

    Question getAQuestion(String question) throws QuestionNotExistsException {
        for(Question q : questions) {
            if(q.getQuestion().equals(question)) return q;
        }
        throw new QuestionNotExistsException("Pergunta Não Encontrada!");
    }

    boolean correctQuestion(String question, String answer) throws QuestionNotExistsException {
        for(Question q : questions) {
            if(q.getQuestion().equals(question)) {
                return q.getAnswer().equals(answer);
            }
            else throw new QuestionNotExistsException("Pergunta Não Encontrada!");
        }
        return false;
    }

    void addPoints(User user, int points) {
        user.addPoints(points);
    }

    void salvarArquivos() {
        for(Question q : questions) {
            mysql.saveNewQuestion(q);
        }
        mysql.saveAllUsers(users);

    }
    void init() {
        this.questionsTemp = new ArrayList<>(this.questions);
    }

    boolean nextQuestion() {
        return !questionsTemp.isEmpty();
    }

    Question getNextQuestion() {
        if(!nextQuestion()) {
            throw new QuestionNumberIsNotValidException("Já está na ultima pergunta do quiz!");
        }

        Random ran = new Random();
        int indexA = ran.nextInt(questionsTemp.size());

        return questionsTemp.remove(indexA);
    }
}
