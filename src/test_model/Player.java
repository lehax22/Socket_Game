package test_model;

/**
 * Created by Alex on 11.12.2016.
 */
public class Player {

    private String name;
    private int score;
    public Answer answer;

    public Player(String name) {
        this.name = name;
        score = 0;
    }

    public Answer getAnswer() {
        return answer;
    }
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int i) {
        this.score += i;
    }
}
