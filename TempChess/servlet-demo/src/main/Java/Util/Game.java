package Util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.Random;

/**
 * Created by Sergey on 18.03.2016.
 */

//TODO: аннотации
public class Game {
    private enum Color {
        BLACK, WHITE
    }
    private enum ChessPiece {
        KB,  // king, Король
        QB,  // queen, Ферзь
        RB,  // rook, Ладья
        NB,  // kNight, Конь
        BB,  // bishop,Слон
        pB,  // pawn,Пешка

        KW,  // king, Король
        QW,  // queen, Ферзь
        RW,  // rook, Ладья
        NW,  // kNight, Конь
        BW,  // bishop,Слон
        pW,  // pawn,Пешка

        n;   // Пусто

        public Color getColor(){
            if (this.name().charAt(1) == 'W'){
                return Color.WHITE;
            } else if (this.name().charAt(1) == 'B'){
                return Color.BLACK;
            } else {
                throw new RuntimeException("in method getColor char(1) != W != B");
            }
        }
    }

    private final Person person1;       //Игрок 1
    private final Person person2;       //Игрок 2

    private final Color colorPerson1;   //Цвет фигур
    private final Color colorPerson2;

    private boolean castlingPerson1;    //Пользовался ли рокировкой?
    private boolean castlingPerson2;

    // "Состояния" игры
    private Optional<Color> checkmate;  // Мат
    private Optional<Color> check;      // Шах
    private Optional<Color> stalemate;  // Пат

    // Результат игры
    private boolean isGameOver;
    private boolean win;                //Победа одной из сторон
    private boolean draw;               //Ничья

    private Person winner;              //Победитель
    private Person loser;               //На всякий случай

    private ChessPiece[] board;         //Доска
    private Deque<Move> history;

    Game (Person person1, Person person2) {
        this.person1 = person1;
        this.person2 = person2;

        colorPerson1 = Color.WHITE;
        colorPerson2 = Color.BLACK;

//        if (new Random(10).nextBoolean()){
//            colorPerson1 = Color.BLACK;
//            colorPerson2 = Color.WHITE;
//        } else {
//            colorPerson1 = Color.WHITE;
//            colorPerson2 = Color.BLACK;
//        }

        history = new ArrayDeque<Move>();
        initializeBoard();
    }
    //Game (Deque<Move> history) {} // TODO: restore game

    public /* тут будет Response, а может быть только exception */ boolean doIt (Move move){
        switch (move.getDecision()) {
            case STEP:
                checkMove(move);
                board = swapCell(board.clone(), move);
                history.add(move);
                break;

            case GIVE_UP:
                winner = person1.equals(move.getPerson()) ? person2 : person1;
                isGameOver = true;
                history.add(move);
                break;

            default:
                break;
        }
        return true;
    }

    //Эта штука кидает исключения
    private boolean checkMove (Move move){

        //TODO: Проверка: можно ходить только своими фигурами

        ChessPiece figure = board[move.getFrom().ordinal()];
        switch (figure) { //Если мы тут, значит  move.getDecision() == STEP
            case KB:case KW: if (!checkMoveKing(move))      throw new RuntimeException("Incorrect move1"); break;
            case QB:case QW: if (!checkMoveQueen(move))     throw new RuntimeException("Incorrect move2"); break;
            case RB:case RW: if (!checkMoveRook(move))      throw new RuntimeException("Incorrect move2"); break;
            case NB:case NW: if (!checkMoveKnight(move))    throw new RuntimeException("Incorrect move3"); break;
            case BB:case BW: if (!checkMoveBishop(move))    throw new RuntimeException("Incorrect move4"); break;
            case pW:case pB: if (!checkMovePawn(move))      throw new RuntimeException("Incorrect move5"); break;
            case n:                                         throw new RuntimeException("Incorrect move6");
        }

        if (isGameOver) {
            throw new RuntimeException("The game is over");
        } else if (checkCheck(swapCell(board.clone(), move)) && getColorOfPerson(move.getPerson()).equals(check.get()) ){
            throw new RuntimeException("Your king is under attack!");
        } else if (checkCheckmate(swapCell(board.clone(), move))){
            //TODO: тут игра должна закончиться
            return true;
        } else if (checkStalemate(swapCell(board.clone(), move))){
            //TODO: тут игра должна закончиться
            return true;
        } else {

        }
        return true;
    }


    private boolean isSameColor (Cell cell, Color color){
        if (board[cell.ordinal()] == ChessPiece.n) return true;
        return color != board[cell.ordinal()].getColor();
    }

    private boolean checkMoveKing(Move move) {
        Cell from = move.getFrom();
        Cell to = move.getTo();

        Column fromColumn =  from.getColumn();
        Row fromRow = from.getRow();
        Column toColumn =  to.getColumn();
        Row toRow = to.getRow();

        if (Math.abs(fromColumn.ordinal() - toColumn.ordinal()) <=1 && Math.abs(fromRow.ordinal() - toRow.ordinal()) <=1 ){
            return isSameColor(to, getColorOfPerson(move.getPerson()));
        } else {
            return false;
        }
    }
    private boolean checkMoveQueen(Move move) {
        return true;
    }
    private boolean checkMoveRook(Move move) {
        return true;
    }
    private boolean checkMoveKnight(Move move) {
        return true;
    }
    private boolean checkMoveBishop(Move move) {
        return true;
    }
    private boolean checkMovePawn(Move move) {
        return true;
    }


    private Person getPersonOfColor (Color color){
        return colorPerson1 == color ? person1 : person2;
    }
    private Color getColorOfPerson (Person person){
        return person == person1 ? colorPerson1 : colorPerson2;
    }

    private boolean checkCheckmate (ChessPiece[] board) {
        //this.checkmate.of(Color.WHITE);
        //this.checkmate.of(Color.BLACK);

        this.checkmate = Optional.empty();
        //return this.checkmate.isPresent();

        return false;
    }
    private boolean checkCheck (ChessPiece[] board) {
        //this.сheck.of(Color.WHITE);
        //this.check.of(Color.BLACK);

        this.check = Optional.empty();
        //return this.check.isPresent();

        return false;
    }
    private boolean checkStalemate (ChessPiece[] board) {
        //this.stalemate.of(Color.WHITE);
        //this.stalemate.of(Color.BLACK);

        this.stalemate = Optional.empty();
        //return this.stalemate.isPresent();

        return false;
    }

    private ChessPiece[] swapCell (ChessPiece[] tempBoard, Move move) {
        ChessPiece temp = tempBoard[move.getTo().ordinal()];
        tempBoard[move.getTo().ordinal()] = tempBoard[move.getFrom().ordinal()];
        tempBoard[move.getFrom().ordinal()] = temp;
        return tempBoard;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                builder.append("\t" + board[i*8 + j].toString());
            }
            builder.append("\n");
        }
        return builder.toString();
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

    public Person getWinner (){
        return winner;
    }
}
