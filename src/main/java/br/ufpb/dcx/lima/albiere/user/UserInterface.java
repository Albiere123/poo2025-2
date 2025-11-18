package br.ufpb.dcx.lima.albiere.user;

public interface UserInterface {

    int getId();
    void setId(int id);
    void setUsername(String username);
    String getUsername();
    int getPoints();
    void setPoints(int points);
    void addPoints(int points);
}
