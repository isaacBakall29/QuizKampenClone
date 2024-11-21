package Server;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {


    @org.junit.jupiter.api.Test
    void getQuestions() {
    GameEngine gameEngine = new GameEngine();
    assertNotNull(gameEngine.getQuestions());

    }

    @org.junit.jupiter.api.Test
    void checkAnswer() {
        GameEngine gameEngine = new GameEngine();
        gameEngine.addPlayer("Player1");
        gameEngine.addPlayer("Player2");
        gameEngine.checkAnswer("Player1", "A");
        gameEngine.checkAnswer("Player2", "B");
    }

    @org.junit.jupiter.api.Test
    void displayScore() {
        GameEngine gameEngine = new GameEngine();
        gameEngine.addPlayer("Player1");
        gameEngine.addPlayer("Player2");
        gameEngine.checkAnswer("Player1", "Wong ANSWER");
        gameEngine.checkAnswer("Player2", "Bla bla");
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
        gameEngine.checkAnswer("Player1", "A");
        gameEngine.checkAnswer("Player2", "B");
        assertFalse(gameEngine.isAnswerCorrect("Player1", "A"));
        assertFalse(gameEngine.isAnswerCorrect("Player2", "B"));
    }
}