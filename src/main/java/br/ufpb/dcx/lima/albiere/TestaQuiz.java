package br.ufpb.dcx.lima.albiere;

import br.ufpb.dcx.lima.albiere.exceptions.UserNotExistsException;
import br.ufpb.dcx.lima.albiere.question.Question;
import br.ufpb.dcx.lima.albiere.user.User;

import java.util.ArrayList;
import java.util.List;

public class TestaQuiz {
    void main() {
        QuestionSystemGerence sistema = new QuestionSystemGerence();
        boolean stop = false;
        User u = null;
        do {
            IO.println("O que deseja fazer:");
            IO.println("1. Iniciar");
            IO.println("2. Selecionar Usuário");
            IO.println("3. Registrar Questão");
            IO.println("0. Sair");

            int response = Integer.parseInt(IO.readln("Insira o número da opção: "));

            switch (response) {

                case 0:
                    stop = true;
                    IO.println("Saindo...");
                    sistema.salvarArquivos();
                    break;

                case 1:
                    sistema.init();
                    if (u == null) {
                        IO.println("Selecione um usuário primeiro...");
                        break;
                    }
                    Question q = sistema.getNextQuestion();
                    IO.println("Pergunta: " + q.getQuestion() + "\nAlternativas: " + q.getAnswers());
                    String resposta = IO.readln("Resposta: ");
                   if (q.getAnswer().equals(resposta)) {
                        IO.println("Resposta correta!");
                        sistema.addPoints(u, 10);
                    } else {
                        IO.println("Resposta incorreta!");
                    }
                    break;

                case 2:
                    try {
                        int id = Integer.parseInt(IO.readln("Insira o id do usuário: "));
                    try {
                        u = sistema.getUser(id);
                    } catch (UserNotExistsException e) {
                        if (u == null) {
                            IO.println("ID não encontrado! Registrando novo usuário...");
                            String username = IO.readln("Insira o username: ");
                            id = Integer.parseInt(IO.readln("Insira o id do usuário"));
                            sistema.UserRegister(username, id);
                            u = sistema.getUser(id);
                        }
                    }
                    }
                    catch (Exception e) {
                        IO.println("Insira o ID do usuário!");
                    }

                    break;

                case 3:
                    String question =  IO.readln("Insira a questão: ");
                    int numA = Integer.parseInt(IO.readln("Insira o número correspondente a quantidade de alternativas a mostrar: "));
                    List<String> answers = new ArrayList<>();
                    for (int i = 0; i < numA; i++) {
                        String answer = IO.readln("Insira a alternativa ( "+(i+1)+" ): ");
                        answers.add(answer);
                    }
                    String answer =  IO.readln("Insira a alternativa correta(escrito igual a anterior): ");

                    sistema.QuestionRegister(question, answers, answer, 100);
                    IO.println("Pergunta registrada com sucesso!");
                    break;
            }
        } while(!stop);
    }
}
