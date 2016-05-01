package ru.jchess.model;

import Util.Color;
import Util.Game;

import java.util.Date;
import java.util.Random;

/**
 * Created by dima on 22.04.16.
 */
public class GameContainer {
    private volatile Object myMonitor; //монитор того, кто создал игру
    private volatile Object opponentMonitor; //монитор того, кто подключился
    private volatile Game game;
    private volatile Color myColor;
    public GameContainer(Object monitor) {
        myMonitor = monitor;
        myColor = Color.None;
    }
    public int size() {
        return (myMonitor != null? 1 : 0) + (opponentMonitor != null? 1 : 0);
    }

    public void chooseRandomColor() {
        Random random = new Random(new Date().getTime());
        int col = Math.round(random.nextFloat());
        switch (col) {
            case 0:
                myColor = Color.BLACK; break;
            case 1:
                myColor = Color.WHITE; break;
            default:
                throw new RuntimeException("choose random exception");
        }
    }

    public void setOppositeColor(Color color) {
        if (color == Color.BLACK)
            myColor = Color.WHITE;
        else
            myColor = Color.BLACK;
    }

    public Object getMyMonitor() {
        return myMonitor;
    }

    public void setMyMonitor(Object myMonitor) {
        this.myMonitor = myMonitor;
    }

    public Object getOpponentMonitor() {
        return opponentMonitor;
    }

    public void setOpponentMonitor(Object opponentMonitor) {
        this.opponentMonitor = opponentMonitor;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Color getMyColor() {
        return myColor;
    }

    public void setMyColor(Color myColor) {
        this.myColor = myColor;
    }

    //    public boolean isMyTurn() {
//        //TODO
//    }
}
