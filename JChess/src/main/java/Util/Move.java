package Util;

/**
 * Created by Sergey on 18.03.2016.
 */
public class Move {
    private Person person;
    private Decision decision;
    private Cell from;
    private Cell to;
    private ChessPiece cp;

    private Move (Person person, Decision decision, Cell from, Cell to, ChessPiece cp) {
        this.person = person;
        this.decision = decision;
        this.from = from;
        this.to = to;
        this.cp = cp;
    }

    //Все решения, которые игрок может принять за всё время игры
    public static Move goFromTo (Person person, Cell from, Cell to){
        return new Move(person, Decision.STEP, from, to, null);
    }
    static Move giveUp (Person person){
        return new Move(person, Decision.GIVE_UP, null, null, null);
    }
    static Move castlingLong (Person person, Cell from, Cell to) {
        return new Move (person, Decision.CASTLING_LONG, from, to, null);
    }
    static Move castlingShort (Person person, Cell from, Cell to) {
        return new Move (person, Decision.CASTLING_SHORT, from, to, null);
    }
    public static Move promotion (Person person, ChessPiece chessPiece) {
        return new Move (person, Decision.PROMOTION, null, null, chessPiece);
    }
    static Move enpassant (Person person, Cell from, Cell to) {
        return new Move (person, Decision.EN_PASSANT, from, to, null);
    }

    //getters
    public Person getPerson(){
        return this.person;
    }
    public Decision getDecision (){
        return this.decision;
    }
    public Cell getFrom (){
        return this.from;
    }
    public Cell getTo () {
        return this.to;
    }
    public ChessPiece getChessPiece () {
        return this.cp;
    }
}
