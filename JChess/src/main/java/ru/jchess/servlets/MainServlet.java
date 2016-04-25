package ru.jchess.servlets;

import Util.Color;
import Util.Game;
import ru.jchess.model.GameContainer;
import ru.jchess.model.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.ConcurrentLinkedDeque;

import static ru.jchess.model.Constants.*;

/**
 * Created by dima on 09.04.16.
 */
public class MainServlet extends HttpServlet {
    public static String ACTION_SEARCH = "search";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        if (ajax && LoginServlet.isSignedIn(request)) {
            String action = request.getParameter("action");
            if (ACTION_SEARCH.equals(action)) {
                //сделаем монитор. С его помощью будет в последствии связываться с сервлетом соперника (notify)
                Object myMonitor = new Object();
                ConcurrentLinkedDeque<GameContainer> waitingQueue = getWaitingQueue(); //получаем очередь ждущих
                synchronized (waitingQueue) {
                    if (!waitingQueue.isEmpty()) {
                        GameContainer opponentGameContainer = waitingQueue.pop();
                        Object opponentMonitor = opponentGameContainer.getMyMonitor();
                        synchronized (opponentMonitor) {
                            //заполняем его контейнер
                            opponentGameContainer.setOpponentMonitor(myMonitor);
                            //заполняем свой контейнер
                            GameContainer myGameContainer = new GameContainer(myMonitor);
                            myGameContainer.setMyMonitor(myMonitor);
                            myGameContainer.setOpponentMonitor(opponentMonitor);
                            myGameContainer.setOppositeColor(opponentGameContainer.getMyColor());
                            myGameContainer.setGame(opponentGameContainer.getGame());

                            opponentMonitor.notify(); //оповещаем соперника о том, что мы подключились
                            //Записываем в сессию информацию о текущей партии
                            addGameToSession(request.getSession(), myGameContainer);
                            //говорим о цвете партии игроку (он сам редирект делает)
                            //sendRedirect(response, myGameContainer.getMyColor());
                            response.getWriter().write(myGameContainer.getMyColor().name());
                            return;
                        }
                    }
                }

                //Сюда попадаем только если в очереди никого
                //waiting
                GameContainer gameContainer = new GameContainer(myMonitor);
                gameContainer.chooseRandomColor();
                Game game = new Game(WHITE_PERSON, BLACK_PERSON); //создаем игру (пусть конструктор не смущает)
                gameContainer.setGame(game); //кидаем игру в контейнер
                waitingQueue.push(gameContainer); //кидаем контейнер в очередь ожидания
                synchronized (myMonitor) {
                    while (gameContainer.size() < 2) {
                        try {
                            myMonitor.wait(); //ждем коннекта
                        } catch (InterruptedException e) {
                        }
                    }
                    addGameToSession(request.getSession(), gameContainer);
                    //sendRedirect(response, gameContainer.getMyColor());
                    response.getWriter().write(gameContainer.getMyColor().name());
                    return;
                }
            }
        }
    }

    private void sendRedirect(HttpServletResponse response, Color color) throws IOException {
        switch (color) {
            case BLACK: response.sendRedirect("blackBoard.jsp"); return;
            case WHITE: response.sendRedirect("whiteBoard.jsp"); return;
        }
    }


    private void addGameToSession(HttpSession session, GameContainer gameContainer) {
        session.setAttribute(CURRENT_GAME_CONTAINER, gameContainer);
    }

    private ConcurrentLinkedDeque<GameContainer> getWaitingQueue() {
        ServletContext sc = this.getServletContext();
        ConcurrentLinkedDeque<GameContainer> deque = (ConcurrentLinkedDeque<GameContainer>) sc.getAttribute(WAITING_DEQUE);
        if (deque == null) {
            deque = new ConcurrentLinkedDeque<>();
            sc.setAttribute(WAITING_DEQUE, deque);
        }
        return deque;
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //проверим залогинен ли.
        getServletContext().log("Im in main servlet and logged perfectly");
        if (LoginServlet.isSignedIn(request)) {
                GameContainer currentGameContainer = getCurrentGameContainer(request.getSession());
                if (currentGameContainer == null) {
                    request.getRequestDispatcher("/main.jsp").forward(request, response);
                } else {
                    Color color = currentGameContainer.getMyColor();
                    switch (color) {
                        case BLACK:
                            response.sendRedirect("blackBoard.jsp"); break;
                        case WHITE:
                            response.sendRedirect("whiteBoard.jsp"); break;
                        default:
                            throw new RuntimeException("Game container have None color!");
                    }
                }
            } else { //Если не залогинен
                response.sendRedirect("login");
            }

        }

    public static GameContainer getCurrentGameContainer(HttpSession session) {
        return (GameContainer) session.getAttribute(CURRENT_GAME_CONTAINER);
    }


}
