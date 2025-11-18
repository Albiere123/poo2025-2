package br.ufpb.dcx.lima.albiere.user;

public class User implements UserInterface{
    private String username;
    private int id;
    private int points;


    public User(String username, int id) {
        this.username = username;
        this.id = id;
        this.points = 0;
    }

    public User(String username) {
        this.username = username;
        this.id = Integer.parseInt(String.valueOf(Math.round(Math.random() * 367)));
        this.points = 0;
    }

    public User() {
        this.username = "Não Definido";
        this.id = Integer.parseInt(String.valueOf(Math.round(Math.random() * 367)));
        this.points = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points){
        this.points += points;
    }

    @Override
    public String toString() {
        return "Usuario[Username=" + username + ", Id=" + id + ", Points=" + points + "]";
    }
}
