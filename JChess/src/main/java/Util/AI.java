package Util;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Sergey on 01.04.2016.
 */
public class AI {
    private static final int DEPTH = 6;
    private Person me;
    private ChessPiece[] board;
    private Color myColor;
    private Color enColor;

    AI (Person person, Color color){
        me = person;
        myColor = color;
        enColor = myColor.equals(Color.BLACK) ? Color.WHITE : Color.BLACK;
        initializeBoard();
    }
    public void in (Move move){
        board[move.getTo().ordinal()] = board[move.getFrom().ordinal()];
        board[move.getFrom().ordinal()] = ChessPiece.n;
    }
    public Move out(){
        if (findFigure(board, myColor)
                .stream()
                .filter((m) -> board[m.ordinal()].equals(ChessPiece.KB) || board[m.ordinal()].equals(ChessPiece.KW))
                .count()
                == 0){
            return Move.giveUp(me);
        }


        Random random = new Random(System.currentTimeMillis());

        while (true){
            Set<Move> moves = avaliableMoves(board, myColor);

            Move best = null;
            int b = -1000000;
            for(Move move : moves){

                ChessPiece[] board = this.board.clone();
                board[move.getTo().ordinal()] = board[move.getFrom().ordinal()];
                board[move.getFrom().ordinal()] = ChessPiece.n;


                int cost = costMove(board, myColor, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
                if (b < cost){
                    b = cost;
                    best = move;
                }
            }
            in(best);
            return best;

        }
    }

    private Set<Move> avaliableMoves(ChessPiece[] board, Color color){
        Set<Cell> myFigure = findFigure(board, color);
        Set<Move> moves = new LinkedHashSet<Move>();

        for(Cell from : myFigure){
            Set<Cell> tos = getAvailableMove(board, from);
            for (Cell to : tos){
                moves.add(Move.goFromTo(me,from,to));
            }
        }
        return moves;
    }
    private Set<Cell> findFigure(ChessPiece[] board, Color color) {
        Set<Cell> figure = new LinkedHashSet<Cell>();

        for (Cell cell : Cell.values()){
            if(getCellColor(board,cell) == color){
                figure.add(cell);
            }
        }
        return figure;
    }

    private int costMove (ChessPiece[] board, Color myColor, int depth, int alpha, int beta, boolean maxmin) {
        if (depth <= 0) return costBoard(board, myColor);

        Color enColor = enColor = myColor.equals(Color.BLACK) ? Color.WHITE : Color.BLACK;
        Set<Move> moves = avaliableMoves(board, myColor);

        if (maxmin) {
            Integer v = Integer.MIN_VALUE;
            for(Move mov : moves) {
                ChessPiece[] board2 = board.clone();
                board2[mov.getTo().ordinal()] = board2[mov.getFrom().ordinal()];
                board2[mov.getFrom().ordinal()] = ChessPiece.n;
                v = Math.max(v, costMove(board2, myColor, depth - 1, alpha, beta, false));
                alpha = Math.max(alpha, v);
                if (beta <= alpha){
                    break;
                }
            }
            return v;
        } else {
            Integer v = Integer.MAX_VALUE;
            for(Move mov : moves) {
                ChessPiece[] board2 = board.clone();
                board2[mov.getTo().ordinal()] = board2[mov.getFrom().ordinal()];
                board2[mov.getFrom().ordinal()] = ChessPiece.n;

                v = Math.min(v, costMove(board2, myColor, depth - 1, alpha, beta, true));
                beta = Math.min(alpha, v);
                if (beta <= alpha){
                    break;
                }
            }
            return v;
        }
    }

    private int costBoard (ChessPiece[] board, Color color){
        Color myColor = color;
        Color enColor = color.equals(Color.BLACK) ? Color.WHITE : Color.BLACK;

        int result = 0;
        for(int i = 0; i < 64; i++){
            ChessPiece cp = board[i];

            if(getCellColor(board, Cell.values()[i]) == myColor){
                result += cp.getCost();

            } else if (getCellColor(board, Cell.values()[i]) == enColor){
                result -= cp.getCost();
            }
        }
        return result;
    }



    private Color getCellColor (ChessPiece[] board, Cell cell) {
        return board[cell.ordinal()].getColor();
    }

    private boolean isSameColor (ChessPiece[] board, Cell cell1, Cell cell2) {
        return board[cell1.ordinal()].getColor() == board[cell2.ordinal()].getColor();
    }
    private boolean isBLACK(ChessPiece[] board, Cell cell) {
        return board[cell.ordinal()].getColor() == Color.BLACK;
    }
    private boolean isWHITE(ChessPiece[] board, Cell cell) {
        return board[cell.ordinal()].getColor() == Color.WHITE;
    }
    private boolean isNone(ChessPiece[] board, Cell cell) {
        return board[cell.ordinal()].getColor() == Color.None;
    }

    private Set<Cell> stepWhile(ChessPiece[] board, Cell cell, int x, int y){
        Color color = board[cell.ordinal()].getColor();
        Set<Cell> cells = new LinkedHashSet<>();

        try {
            while(board[cell.go(x,y).ordinal()] == ChessPiece.n){ //Шагаем, пока на пути пусто. И добавляем в cells
                cells.add(cell.go(x,y));
                cell = cell.go(x,y);
            }
            Color color2 = board[cell.go(x,y).ordinal()].getColor();
            if (color != color2){ //None не может быть, поэтому тут будет фигура противоположного цвета
                cells.add(cell.go(x,y));
            }
        } catch (NullPointerException npe){} //Когда выпали с доски

        return cells;
    }
    private Set<Cell> getAvailableKingMove (ChessPiece[] board, Cell cell) {
        Set<Cell> avaliableMove =  new LinkedHashSet<>();
        Set<Cell> cells = new LinkedHashSet<>();

        cells.add(cell.go(1,1));
        cells.add(cell.go(1,0));
        cells.add(cell.go(1,-1));

        cells.add(cell.go(0,1));
        cells.add(cell.go(0,-1));

        cells.add(cell.go(-1,1));
        cells.add(cell.go(-1,0));
        cells.add(cell.go(-1,-1));

        for (Cell c : cells){
            if (c != null && !isSameColor(board, c, cell)){
                avaliableMove.add(c);
            }
        }
        return avaliableMove;
    }
    private Set<Cell> getAvailableQueenMove (ChessPiece[] board, Cell cell) {
        Set<Cell> avaliableMove = new LinkedHashSet<>();
        avaliableMove.addAll(getAvailableBishopMove(board, cell));
        avaliableMove.addAll(getAvailableRookMove(board, cell));
        return avaliableMove;
    }
    private Set<Cell> getAvailableRookMove (ChessPiece[] board, Cell cell) {
        Set<Cell> avaliableMove = new LinkedHashSet<>();

        avaliableMove.addAll(stepWhile(board, cell, 1, 0));
        avaliableMove.addAll(stepWhile(board, cell, 0, 1));
        avaliableMove.addAll(stepWhile(board, cell, 0,-1));
        avaliableMove.addAll(stepWhile(board, cell,-1, 0));

        return avaliableMove;
    }
    private Set<Cell> getAvailableKnightMove (ChessPiece[] board, Cell cell) {
        Set<Cell> avaliableMove =  new LinkedHashSet<Cell>();
        Set<Cell> cells = new LinkedHashSet<Cell>();

        cells.add(cell.go(2,1));
        cells.add(cell.go(2,-1));

        cells.add(cell.go(-2,1));
        cells.add(cell.go(-2,-1));

        cells.add(cell.go(1,2));
        cells.add(cell.go(-1,2));

        cells.add(cell.go(1,-2));
        cells.add(cell.go(-1,-2));

        for (Cell c : cells){
            if (c != null && !isSameColor(board, c, cell)){
                avaliableMove.add(c);
            }
        }
        return avaliableMove;
    }
    private Set<Cell> getAvailableBishopMove (ChessPiece[] board, Cell cell) {
        Set<Cell> avaliableMove = new LinkedHashSet<Cell>();

        avaliableMove.addAll(stepWhile(board, cell, 1, 1));
        avaliableMove.addAll(stepWhile(board, cell, 1,-1));
        avaliableMove.addAll(stepWhile(board, cell,-1, 1));
        avaliableMove.addAll(stepWhile(board, cell,-1,-1));

        return avaliableMove;
    }
    private Set<Cell> getAvailablePawnMove (ChessPiece[] board, Cell cell) {
        Set<Cell> avaliableMove =  new LinkedHashSet<Cell>();

        Row fromRow = cell.getRow();
        Color color = board[cell.ordinal()].getColor();

        switch (color){
            case BLACK:
                try {
                    if (isNone(board, cell.go(0,-1))) {
                        avaliableMove.add(cell.go(0, -1));
                        if (fromRow.equals(Row.r7) && isNone(board, cell.go(0, -1).go(0, -1))) {
                            avaliableMove.add(cell.go(0, -1).go(0, -1));
                        }
                    }
                }catch (NullPointerException e){}

                try{
                    if (isWHITE(board, cell.go(1,-1))) avaliableMove.add(cell.go(1,-1));
                    if (isWHITE(board, cell.go(-1,-1))) avaliableMove.add(cell.go(-1,-1));
                } catch (NullPointerException e) {}


                break;
            case WHITE:
                try {
                    if (isNone(board, cell.go(0, 1))) {
                        avaliableMove.add(cell.go(0, 1));
                        if (fromRow.equals(Row.r2) && isNone(board, cell.go(0, 1).go(0, 1))) {
                            avaliableMove.add(cell.go(0, 1).go(0, 1));
                        }
                    }
                }catch (NullPointerException e){}

                try{
                    if (isBLACK(board, cell.go(1,1))) avaliableMove.add(cell.go(1,1));
                    if (isBLACK(board, cell.go(-1,1))) avaliableMove.add(cell.go(-1,1));
                } catch (NullPointerException e) {}

        }

        return avaliableMove;
    }
    private Set<Cell> getAvailableMove (ChessPiece[] board, Cell cell) {
        ChessPiece figure = board[cell.ordinal()];
        switch (figure) {
            case KB: case KW:   return getAvailableKingMove(board, cell);
            case QB: case QW:   return getAvailableQueenMove(board, cell);
            case RB: case RW:   return getAvailableRookMove(board, cell);
            case NB: case NW:   return getAvailableKnightMove(board, cell);
            case BB: case BW:   return getAvailableBishopMove(board, cell);
            case pB: case pW:   return getAvailablePawnMove(board, cell);
            case n: break;      //Тут не бывает
        }

        throw new RuntimeException("Code 0001");
    }

    private void initializeBoard (){
        board = new ChessPiece[64];
        for(int i = Cell.A8.ordinal(); i < Cell.H1.ordinal(); i++){
            board[i] = ChessPiece.n;
        }
        //Sorry for this -_-
        board[Cell.A1.ordinal()] = ChessPiece.RW;
        board[Cell.B1.ordinal()] = ChessPiece.NW;
        board[Cell.C1.ordinal()] = ChessPiece.BW;
        board[Cell.D1.ordinal()] = ChessPiece.QW;
        board[Cell.E1.ordinal()] = ChessPiece.KW;
        board[Cell.F1.ordinal()] = ChessPiece.BW;
        board[Cell.G1.ordinal()] = ChessPiece.NW;
        board[Cell.H1.ordinal()] = ChessPiece.RW;

        board[Cell.A8.ordinal()] = ChessPiece.RB;
        board[Cell.B8.ordinal()] = ChessPiece.NB;
        board[Cell.C8.ordinal()] = ChessPiece.BB;
        board[Cell.D8.ordinal()] = ChessPiece.QB;
        board[Cell.E8.ordinal()] = ChessPiece.KB;
        board[Cell.F8.ordinal()] = ChessPiece.BB;
        board[Cell.G8.ordinal()] = ChessPiece.NB;
        board[Cell.H8.ordinal()] = ChessPiece.RB;

        board[Cell.A2.ordinal()] = ChessPiece.pW;
        board[Cell.B2.ordinal()] = ChessPiece.pW;
        board[Cell.C2.ordinal()] = ChessPiece.pW;
        board[Cell.D2.ordinal()] = ChessPiece.pW;
        board[Cell.E2.ordinal()] = ChessPiece.pW;
        board[Cell.F2.ordinal()] = ChessPiece.pW;
        board[Cell.G2.ordinal()] = ChessPiece.pW;
        board[Cell.H2.ordinal()] = ChessPiece.pW;

        board[Cell.A7.ordinal()] = ChessPiece.pB;
        board[Cell.B7.ordinal()] = ChessPiece.pB;
        board[Cell.C7.ordinal()] = ChessPiece.pB;
        board[Cell.D7.ordinal()] = ChessPiece.pB;
        board[Cell.E7.ordinal()] = ChessPiece.pB;
        board[Cell.F7.ordinal()] = ChessPiece.pB;
        board[Cell.G7.ordinal()] = ChessPiece.pB;
        board[Cell.H7.ordinal()] = ChessPiece.pB;
    }
}
