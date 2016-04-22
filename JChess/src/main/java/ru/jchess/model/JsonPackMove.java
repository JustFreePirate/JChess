package ru.jchess.model;

import Util.Move;

/**
 * Created by dima on 23.04.16.
 */
public class JsonPackMove {
    private String action;
    private String from;
    private String to;
    public JsonPackMove() {

    }
    public JsonPackMove(Move move, String action) {
        from = move.getFrom().name();
        to = move.getTo().name();
        this.action = action;
    }
}
