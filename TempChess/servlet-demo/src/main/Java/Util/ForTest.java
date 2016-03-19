package Util;

/**
 * Created by Sergey on 18.03.2016.
 */
public class ForTest {
    public static void main(String[] args) {

        Person Anna = new Person("Anna");
        Person Nick = new Person("Nick");

        Game game = new Game(Anna, Nick);
        System.out.println(game);

        //game.doIt(Move.goFromTo(Anna,Cell.E1,Cell.C2));
        game.doIt(Move.goFromTo(Nick,Cell.C7,Cell.C5));
        game.doIt(Move.goFromTo(Anna,Cell.E2,Cell.E4));
        game.doIt(Move.goFromTo(Nick,Cell.A7,Cell.A5));
        game.doIt(Move.goFromTo(Anna,Cell.E1,Cell.E2));
        game.doIt(Move.goFromTo(Anna,Cell.E2,Cell.D3));

        System.out.println(game);
    }
}
