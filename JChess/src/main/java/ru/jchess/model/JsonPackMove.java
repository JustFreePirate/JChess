package ru.jchess.model;

import Util.ChessPiece;
import Util.Move;

/**
 * Created by dima on 23.04.16.
 */
public class JsonPackMove {
    private String action;
    private String from;
    private String to;
    private ChessPiece chessPiece;
    public JsonPackMove() {

    }
    public JsonPackMove(Move move, String action) {
        from = move.getFrom().name();
        to = move.getTo().name();
        chessPiece = move.getChessPiece();
        this.action = action;
    }
}
