package Util;

/**
 * Created by Sergey on 18.03.2016.
 */
public class ForTest {
    public static void main(String[] args) {
        Person Anna = new Person("Anna");
        Person Nick = new Person("Nick");

        Game game = new Game(Anna, Nick);

        game.doIt(Move.goFromTo(Anna,Cell.A4,Cell.A5));
        game.doIt(Move.goFromTo(Nick,Cell.A7,Cell.A2));
        game.doIt(Move.giveUp(Anna));
        System.out.println(game.getWinner().name);
        game.doIt(Move.goFromTo(Nick,Cell.C3,Cell.E7));
    }
}
