package Util;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Sergey on 18.03.2016.
 */

public class Game {

    private final Person person1;       //Игрок 1
    private final Person person2;       //Игрок 2

    private final Color colorPerson1;   //Цвет фигур
    private final Color colorPerson2;

    // Результат игры
    private boolean isGameOver;
    private boolean draw;               //Ничья

    private Person winner;              //Победитель

    private ChessPiece[] board;         //Доска
    private Deque<Move> history;

    Game (Person person1, Person person2) {
        this.person1 = person1;
        this.person2 = person2;

        colorPerson1 = Color.WHITE;
        colorPerson2 = Color.BLACK;

        history = new ArrayDeque<>();

        initializeBoard();
    }

    //TODO: не тестил
    Game (Person person1, Person person2, Deque<Move> history) {
        this.person1 = person1;
        this.person2 = person2;

        colorPerson1 = Color.WHITE;
        colorPerson2 = Color.BLACK;

        this.history = history;
        initializeBoard();
        for(Move move : history){
            this.doIt(move);
        }
    }


    //TODO: говно какое-то
    public boolean doIt (Move move){
        if (isGameOver) {
            throw new RuntimeException("The game is over");
        } else if (move.getDecision() != Decision.PROMOTION && checkPawnOnTheEdge(this.board)) {
            throw new RuntimeException("The game is over");
        }

        switch (move.getDecision()) {
            case STEP:
                ChessPiece figure = board[move.getFrom().ordinal()];
                Color colorFigure = figure.getColor();

                if (figure.getColor() != getColorOfPerson(move.getPerson())) {
                    throw new RuntimeException("It's not your chess");
                }
                if (!getAvailableMove(move.getFrom()).contains(move.getTo())){
                    throw new RuntimeException("Incorrect move");
                }
                if (checkCheck(step(board.clone(), move)).equals(colorFigure)){
                    throw new RuntimeException("Your king is under attack!");
                }

                Color myColor =  board[move.getFrom().ordinal()].getColor();
                Color enColor = myColor.equals(Color.BLACK) ? Color.WHITE : Color.BLACK;

                board = step(board.clone(), move);

                if (checkStalemate(board).equals(enColor)){
                    draw = true;
                    isGameOver = true;
                }
                if (checkCheckmate(board).equals(enColor)){
                    isGameOver = true;
                }

                history.add(move);
                break;

            case GIVE_UP:
                winner = person1.equals(move.getPerson()) ? person2 : person1;
                isGameOver = true;
                history.add(move);
                break;

            case CASTLING_LONG: case CASTLING_SHORT:
                checkCastling(move);
                doCastling(move);
                break;

            case PROMOTION:
                if (checkPawnOnTheEdge(this.board)) pawnPromotion(move);
                else throw new RuntimeException("nothing to promotion");

            default:
                break;
        }
        return true;
    }




    //Получим ход противника
    public Move getLastMove () {
        return history.getLast();
    }





    //Если игра закончена -- вернет победителя
    public Person getWinner (){
        return winner;
    }

    public boolean isDraw() {
        return draw;
    }

//===== "Активные" методы =====================================================================

    //Передвигаем фигуру
    private ChessPiece[] step(ChessPiece[] board, Move move) {
        board[move.getTo().ordinal()] = board[move.getFrom().ordinal()];
        board[move.getFrom().ordinal()] = ChessPiece.n;
        return board;
    }

    //Превращение пешки
    private void pawnPromotion (Move move) {
        ChessPiece cp =  move.getChessPiece();

        for(Cell cell : Cell.values()){
            if(cell.getRow() == Row.r8 && board[cell.ordinal()] == ChessPiece.pW) {
                this.board[cell.ordinal()] = cp;
                break;
            }
            else if (cell.getRow() == Row.r1 && board[cell.ordinal()] == ChessPiece.pB) {
                this.board[cell.ordinal()] = cp;
                break;
            }
        }
    }

    //Рокировка
    private void doCastling (Move move) {
        Person person = move.getPerson();
        Color personColor = getColorOfPerson(person);

        if (personColor == Color.WHITE){
            switch (move.getDecision()){
                case CASTLING_SHORT:
                    this.board = step(this.board, Move.goFromTo(person,Cell.E1,Cell.G1));
                    this.board = step(this.board, Move.goFromTo(person,Cell.H1,Cell.F1));
                    break;
                case CASTLING_LONG:
                    this.board = step(this.board, Move.goFromTo(person,Cell.E1,Cell.C1));
                    this.board = step(this.board, Move.goFromTo(person,Cell.H1,Cell.D1));
                    break;
                default: throw new RuntimeException("Unknown castling");
            }
        } else {
            switch (move.getDecision()){
                case CASTLING_SHORT:
                    this.board = step(this.board, Move.goFromTo(person,Cell.E8,Cell.G8));
                    this.board = step(this.board, Move.goFromTo(person,Cell.H8,Cell.F8));
                    break;

                case CASTLING_LONG:
                    this.board = step(this.board, Move.goFromTo(person,Cell.E1,Cell.C1));
                    this.board = step(this.board, Move.goFromTo(person,Cell.H1,Cell.D1));
                    break;

                default: throw new RuntimeException("Unknown castling");
            }
        }

    }

    private void initializeBoard (){
        //Инициализировали доску
        board = new ChessPiece[64];
        for(int i = Cell.A8.ordinal(); i < Cell.H1.ordinal(); i++){
            board[i] = ChessPiece.n;
        }

        //Тут я сильно не парился -_-
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

//===== Всякие штуки для работы с цветами клеточек =====================================================================

    private Color getCellColor (ChessPiece[] board, Cell cell) {
        return board[cell.ordinal()].getColor();
    }
    private Color getColorOfPerson (Person person){
        return person == person1 ? colorPerson1 : colorPerson2;
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

//===== Всякие штуки для работы с клеточками и фигурами ================================================================

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
        Set<Cell> avaliableMove =  new LinkedHashSet<>();

        Row fromRow = cell.getRow();
        Color color = board[cell.ordinal()].getColor();

        switch (color){
            case BLACK:
                try {
                    if (isNone(board, cell.go(0,-1))){
                        avaliableMove.add(cell.go(0,-1));
                        if (fromRow.equals(Row.r7) && isNone(board, cell.go(0,-1).go(0,-1))){
                            avaliableMove.add(cell.go(0,-1).go(0,-1));
                        }
                    }


                    if (isWHITE(board, cell.go(1,-1))) avaliableMove.add(cell.go(1,-1));
                    if (isWHITE(board, cell.go(-1,-1))) avaliableMove.add(cell.go(-1,-1));
                } catch (NullPointerException e) {}


                break;
            case WHITE:
                try {
                    if (isNone(board, cell.go(0,1))){
                        avaliableMove.add(cell.go(0,1));
                        if (fromRow.equals(Row.r2) && isNone(board, cell.go(0,1).go(0,1))){
                            avaliableMove.add(cell.go(0,1).go(0,1));
                        }
                    }

                    if (isBLACK(board, cell.go(1,1))) avaliableMove.add(cell.go(1,1));
                    if (isBLACK(board, cell.go(-1,1))) avaliableMove.add(cell.go(-1,1));
                } catch (NullPointerException e) {}

                break;
            case None:
                break;
        }

        return avaliableMove;
    }

    //Получить доступные ходы с [этой] клетки
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
    private Set<Cell> getAvailableMove (Cell cell){
        return getAvailableMove(this.board, cell);
    }

    //Все ходы, которые под доступны для белых/черных
    private Set<Move> availableMoves(ChessPiece[] board, Color color){
        Set<Cell> myFigure = findFigure(board, color);
        Set<Move> moves = new LinkedHashSet<>();

        for(Cell from : myFigure){
            Set<Cell> tos = getAvailableMove(board, from);
            moves.addAll(tos.stream().map(to -> Move.goFromTo(person1, from, to)).collect(Collectors.toList()));
        }
        return moves;
    }

    //Множество клеток, на которых стоят фигуры белых/черных
    private Set<Cell> findFigure(ChessPiece[] board, Color color) {
        Set<Cell> figure = new LinkedHashSet<>();

        for (Cell cell : Cell.values()){
            if(getCellColor(board,cell) == color){
                figure.add(cell);
            }
        }
        return figure;
    }

//===== Чекеры =========================================================================================================

    // Чекаем рокировку
    private boolean checkCastling(Move move){
        Color personColor = getColorOfPerson(move.getPerson());

        //Если король под шахом -- не ок
        if (checkCheck(board) == personColor)
            throw new RuntimeException("Your king under attack");


        if (personColor == Color.WHITE){
            if (history.stream().map(Move::getFrom).anyMatch(from -> from == Cell.E1))
                throw new RuntimeException("King has already done step");
            switch (move.getDecision()){
                case CASTLING_SHORT:
                    if (getCellColor(board, Cell.F1) != Color.None ||
                            getCellColor(board, Cell.G1) != Color.None)
                        throw new RuntimeException("Cell are not avaliable");

                    if (history.stream().map(Move::getFrom).anyMatch(from -> from == Cell.H1))
                        throw new RuntimeException("Rook has already done step");

                    //TODO: поле справа не под шахом
                    break;

                case CASTLING_LONG:
                    if (getCellColor(board, Cell.B1) != Color.None ||
                            getCellColor(board, Cell.C1) != Color.None ||
                            getCellColor(board, Cell.D1) != Color.None)
                        throw new RuntimeException("Cell are not avaliable");

                    if (history.stream().map(Move::getFrom).anyMatch(from -> from == Cell.A1))
                        throw new RuntimeException("Rook has already done step");

                    //TODO: поле справа не под шахом
                    break;
                default: throw new RuntimeException("Unknown castling");
            }
        } else {
            if (! history.stream().map(Move::getFrom).anyMatch(from -> from == Cell.E8))
                throw new RuntimeException("King has already done step");
            switch (move.getDecision()){
                case CASTLING_SHORT:
                    if (getCellColor(board, Cell.F8) != Color.None ||
                            getCellColor(board, Cell.G8) != Color.None)
                        throw new RuntimeException("King has already done step");

                    if (history.stream().map(Move::getFrom).anyMatch(from -> from == Cell.H8))
                        throw new RuntimeException("Rook has already done step");

                    //TODO: поле справа не под шахом
                    break;

                case CASTLING_LONG:
                    if (getCellColor(board, Cell.B8) != Color.None ||
                            getCellColor(board, Cell.C8) != Color.None ||
                            getCellColor(board, Cell.D8) != Color.None)
                        throw new RuntimeException("Cell are not avaliable");

                    //TODO: поле справа не под шахом

                    if (history.stream().map(Move::getFrom).anyMatch(from -> from == Cell.A8))
                        throw new RuntimeException("Rook has already done step");
                    break;
                default: throw new RuntimeException("Unknown castling");
            }
        }

        return true;
    }

    //Возвращает цвет тех, кто бьет [эту] клетку
    private Color checkCell (ChessPiece[] board, Cell cell) {
        Set<Cell> attackWhile =  new LinkedHashSet<>();
        Set<Cell> attackfBlack =  new LinkedHashSet<>();

        Set<Cell> whiteFigure = findFigure(board, Color.WHITE);
        Set<Cell> blackFigure = findFigure(board, Color.BLACK);

        for(Cell c : whiteFigure){
            attackWhile.addAll(getAvailableMove(board, c));
        }
        for(Cell c : blackFigure){
            attackfBlack.addAll(getAvailableMove(board, c));
        }

        // Если этой фигней воспользоваться для пустой клетки, кажется работать будет неправильно
        // Но мы и не пользуемся
        assert getCellColor(board, cell) == Color.None : "А вот и нет, пользуемся";

        if(attackWhile.contains(cell)){
            return Color.WHITE;
        } else if(attackfBlack.contains(cell)){
            return Color.BLACK;
        } else {
            return Color.None;
        }
    }

    //Чекнуть мат
    private Color checkCheckmate (ChessPiece[] board) {
        Color color = checkCheck(board);
        if (color.equals(Color.None)) return Color.None;    //Если не шах, то всё
        Set<Move> aMoves = availableMoves(board, color);
        return aMoves.stream().map(mov -> checkCheck(step(board.clone(), mov))).allMatch(col -> col == color) ? color : Color.None;
    }

    //Чекнуть шах
    private Color checkCheck (ChessPiece[] board) {
        Cell coordKingWhite = (Cell) findFigure(board, Color.WHITE).stream().filter((m) -> board[m.ordinal()].equals(ChessPiece.KW)).toArray()[0];
        Cell coordKingBlack = (Cell) findFigure(board, Color.BLACK).stream().filter((m) -> board[m.ordinal()].equals(ChessPiece.KB)).toArray()[0];

        if(checkCell(board, coordKingWhite) == Color.BLACK){
            return Color.WHITE;
        } else if(checkCell(board, coordKingBlack) == Color.WHITE){
            return Color.BLACK;
        } else {
            return Color.None;
        }
    }

    //Чекнуть пат
    private Color checkStalemate (ChessPiece[] board) {
        if (!checkCheck(board).equals(Color.None)) return Color.None;

        Set<Move> aWhiteMoves= availableMoves(board, Color.WHITE);
        Set<Move> aBlackMoves= availableMoves(board, Color.BLACK);

        Color cW = aWhiteMoves.stream().map(mov -> checkCheck(step(board.clone(), mov))).allMatch(col -> col == Color.WHITE) ? Color.WHITE : Color.None;
        Color cB = aBlackMoves.stream().map(mov -> checkCheck(step(board.clone(), mov))).allMatch(col -> col == Color.BLACK) ? Color.BLACK : Color.None;
        return cW.equals(Color.None) ? cW : cB;
    }

    //Чекнуть, не нужно ли превращать пешку
    private boolean checkPawnOnTheEdge (ChessPiece[] board) {
        Set<Cell> whiteEdge = new HashSet<>();
        Set<Cell> blackEdge = new HashSet<>();

        for(Cell cell : Cell.values()){
            if(cell.getRow() == Row.r8) {
                blackEdge.add(cell);
            }
            else if (cell.getRow() == Row.r1) {
                whiteEdge.add(cell);
            }
        }

        long nP =
                whiteEdge.stream().filter( c -> board[c.ordinal()] == ChessPiece.pB).count()
                        + blackEdge.stream().filter( c -> board[c.ordinal()] == ChessPiece.pW).count();


        //Если на краю больше чем одна пешка, значит мы вовремя не превратили прошлую, это fail
        assert nP > 1 : ">1 pawn on edge";


        // Если хотя бы одна пешка стоит на краю -- нужно делать превращение
        return nP > 0;
    }
//======================================================================================================================

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                builder.append("\t").append(board[i*8 + j].toString());
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}