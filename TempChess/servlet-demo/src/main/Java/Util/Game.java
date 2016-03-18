package Util;

import javax.xml.ws.Response;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/**
 * Created by Sergey on 18.03.2016.
 */
public class Game {
    private enum Color {
        BLACK, WHITE
    }

    private final Person person1;       //Игрок 1
    private final Person person2;       //Игрок 2

    private final Color colorPerson1;   //Цвет фигур
    private final Color colorPerson2;

    private boolean castlingPerson1;    //Пользовался ли рокировкой?
    private boolean castlingPerson2;

    // "Состояния" игры
    private boolean checkmate;          // Мат
    private boolean сheck;              // Шах
    private boolean stalemate;          // Пат

    // Результат игры
    private boolean isGameOver;
    private boolean win;                //Победа одной из сторон
    private boolean draw;               //Ничья

    private Person winner;              //Победитель
    private Person loser;               //На всякий случай

    Deque<Move> history = new ArrayDeque<Move>();

    Game (Person person1, Person person2) {
        this.person1 = person1;
        this.person2 = person2;

        if (new Random(10).nextBoolean()){
            colorPerson1 = Color.BLACK;
            colorPerson2 = Color.WHITE;
        } else {
            colorPerson1 = Color.WHITE;
            colorPerson2 = Color.BLACK;
        }
    }

    public /* тут будет Response */ boolean doIt (Move move){
        switch (move.getDecision()) {
            case STEP:
                check(move);
                history.add(move);
                break;
            case GIVE_UP:
                if (person1.equals(move.getPerson())){
                    winner = person2;
                } else {
                    winner = person1;
                }
                history.add(move);
                break;
            case CASTLING:
                break;
            case ENPASSANT:
                break;
        }
        return true;
    }

    private boolean check (Move move){
        if (isGameOver && move.getDecision().equals(Decision.STEP)){
            throw new RuntimeException("The game is over");
        }
        return true;
    }


    public Person getWinner (){
        return winner;
    }

}
