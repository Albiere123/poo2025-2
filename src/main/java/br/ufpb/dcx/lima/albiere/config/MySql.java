package br.ufpb.dcx.lima.albiere.config;

import br.ufpb.dcx.lima.albiere.question.Question;
import br.ufpb.dcx.lima.albiere.user.User;
import java.sql.*;
import java.util.*;
import br.ufpb.dcx.lima.albiere.config.ConnectionSQL;

public class MySql {

    private final String scoresTableName;
    private final String questionsTableName;
    private static final String OPTIONS_DELIMITER = ";";

    public MySql() {
        this.scoresTableName = "quiz_scores";
        this.questionsTableName = "quiz_questions";
    }

    /**
     * Cria as tabelas necessárias.
     * ATENÇÃO: A tabela 'quiz_scores' foi modificada para sua classe User.
     */
    public void createTablesIfNotExists() {

        String sqlScores = "CREATE TABLE IF NOT EXISTS `" + scoresTableName + "` (" +
                "`user_id` INT NOT NULL PRIMARY KEY," +
                "`username` VARCHAR(100) NOT NULL," +
                "`high_score` INT NOT NULL DEFAULT 0" +
                ");";


        String sqlQuestions = "CREATE TABLE IF NOT EXISTS `" + questionsTableName + "` (" +
                "`question_id` INT AUTO_INCREMENT PRIMARY KEY," +
                "`question_text` TEXT NOT NULL," +
                "`options` TEXT NOT NULL," +
                "`correct_answer` VARCHAR(255) NOT NULL," +
                "`time_limit` INT NOT NULL DEFAULT 0" +
                ");";

        try (Connection conn = ConnectionSQL.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sqlScores);
            System.out.println(" Tabela de PONTUAÇÕES do Quiz verificada/criada com sucesso!");

            stmt.executeUpdate(sqlQuestions);
            System.out.println(" Tabela de PERGUNTAS do Quiz verificada/criada com sucesso!");

        } catch (SQLException e) {
            System.err.println(" ERRO FATAL ao tentar criar as tabelas do quiz!");
            e.printStackTrace();
        }
    }


    public List<Question> loadAllQuestions() {
        List<Question> allQuestions = new ArrayList<>();
        String sql = "SELECT question_text, options, correct_answer, time_limit FROM `" + questionsTableName + "`;";
        try (Connection conn = ConnectionSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt. executeQuery(sql)) {
            while (rs.next()) {
                String questionText = rs.getString("question_text");
                String optionsString = rs.getString("options");
                String correctAnswer = rs.getString("correct_answer");
                int time = rs.getInt("time_limit");
                List<String> optionsList = Arrays.asList(optionsString.split(OPTIONS_DELIMITER));
                Question q = new Question(questionText, optionsList, correctAnswer, time);
                allQuestions.add(q);
            }
            System.out.println(" Todas as perguntas do quiz foram carregadas do MySQL!");
        } catch (SQLException e) {
            System.err.println("Falha ao carregar todas as perguntas do servidor.");
            e.printStackTrace();
        }
        return allQuestions;
    }

    public void saveNewQuestion(Question q) {
        String sql = "INSERT INTO `" + questionsTableName + "` " +
                "(question_text, options, correct_answer, time_limit) " +
                "VALUES (?, ?, ?, ?);";
        try (Connection conn = ConnectionSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String optionsString = String.join(OPTIONS_DELIMITER, q.getAnswers());
            pstmt.setString(1, q.getQuestion());
            pstmt.setString(2, optionsString);
            pstmt.setString(3, q.getAnswer());
            pstmt.setInt(4, q.getTime());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Falha ao salvar a nova pergunta: " + q.getQuestion());
            e.printStackTrace();
        }
    }


    /**
     * [ATUALIZADO]
     * Salva uma lista de objetos User em lote.
     * Atualiza o nome de usuário caso ele mude e o high_score se for maior.
     *
     * @param users Uma Lista de objetos User.
     */
    public void saveAllUsers(List<User> users) {
        String sql = "INSERT INTO `" + scoresTableName + "` (user_id, username, high_score) " +
                "VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "username = VALUES(username), " +
                "high_score = GREATEST(VALUES(high_score), high_score);";

        try (Connection conn = ConnectionSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            for (User user : users) {
                pstmt.setInt(1, user.getId());
                pstmt.setString(2, user.getUsername());
                pstmt.setInt(3, user.getPoints());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            conn.commit();
            System.out.println(" " + users.size() + " registros de pontuação salvos/atualizados.");

        } catch (SQLException e) {
            System.err.println("Falha ao salvar pontuações em lote.");
            e.printStackTrace();
        }
    }

    /**
     * [ATUALIZADO]
     * Carrega todas as pontuações e retorna uma Lista de objetos User.
     *
     * @return Uma List<User> com todos os usuários do banco.
     */
    public List<User> loadAllUsers() {
        List<User> allUsers = new ArrayList<>();
        String sql = "SELECT user_id, username, high_score FROM `" + scoresTableName + "`;";

        try (Connection conn = ConnectionSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String username = rs.getString("username");
                int highScore = rs.getInt("high_score");

                User user = new User(username, id);
                user.setPoints(highScore);

                allUsers.add(user);
            }
            System.out.println(" " + allUsers.size() + " pontuações de usuários carregadas.");

        } catch (SQLException e) {
            System.err.println("Falha ao carregar todas as pontuações de usuários.");
            e.printStackTrace();
        }
        return allUsers;
    }

    /**
     * [ATUALIZADO]
     * Busca os 10 melhores usuários e retorna uma Lista de objetos User.
     *
     * @return Uma List<User> ordenada, contendo o Top 10.
     */
    public List<User> getTop10Scores() {
        List<User> ranking = new ArrayList<>();
        String sql = "SELECT user_id, username, high_score FROM `" + scoresTableName + "` " +
                "ORDER BY high_score DESC LIMIT 10;";

        try (Connection conn = ConnectionSQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String username = rs.getString("username");
                int highScore = rs.getInt("high_score");

                User user = new User(username, id);

                user.setPoints(highScore);

                ranking.add(user);
            }

        } catch (SQLException e) {
            System.err.println("Falha ao carregar o ranking Top 10.");
            e.printStackTrace();
        }
        return ranking;
    }
}