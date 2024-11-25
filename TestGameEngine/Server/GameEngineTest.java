package Server;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {


    @org.junit.jupiter.api.Test
    void getQuestions() {
    GameEngine gameEngine = new GameEngine();


    }

    @org.junit.jupiter.api.Test
    void checkAnswer() {
        GameEngine gameEngine = new GameEngine();
        gameEngine.addPlayer("Player1");
        gameEngine.addPlayer("Player2");

    }

    @org.junit.jupiter.api.Test
    void displayScore() {
        GameEngine gameEngine = new GameEngine();
        gameEngine.addPlayer("Player1");
        gameEngine.addPlayer("Player2");

        gameEngine.displayScore();
    }

    @org.junit.jupiter.api.Test
    void addPlayer() {
        GameEngine gameEngine = new GameEngine();
        gameEngine.addPlayer("Player1");
        gameEngine.addPlayer("Player2");

    }

    @org.junit.jupiter.api.Test
    void isAnswerCorrect() {
        GameEngine gameEngine = new GameEngine();
        gameEngine.addPlayer("Player1");
        gameEngine.addPlayer("Player2");

    }
}