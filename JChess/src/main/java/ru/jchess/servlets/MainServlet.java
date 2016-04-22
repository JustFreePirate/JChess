package ru.jchess.servlets;

import Util.Color;
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
        if (ajax) {
            String action = request.getParameter("action");
            if (ACTION_SEARCH.equals(action)) {
                Writer writer = response.getWriter();
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");

                //сделаем монитор. С его помощью будет в последствии связываться с сервлетом соперника (notify)
                Object myMonitor = new Object();
                ConcurrentLinkedDeque<GameContainer> waitingQueue = getWaitingQueue(); //получаем очередь ждущих
                synchronized (waitingQueue) {
                    if (!waitingQueue.isEmpty()) {
                        GameContainer opponentGameContainer = waitingQueue.pop();
                        Object opponentMonitor = opponentGameContainer.myMonitor;
                        synchronized (opponentMonitor) {
                            //заполняем его контейнер
                            opponentGameContainer.opponentMonitor = myMonitor;
                            //заполняем свой контейнер
                            GameContainer myGameContainer = new GameContainer(myMonitor);
                            myGameContainer.myMonitor = myMonitor;
                            myGameContainer.opponentMonitor = opponentMonitor;

                            opponentMonitor.notify(); //оповещаем соперника о том, что мы подключились
                            //Записываем в сессию информацию о текущей партии
                            addGameToSession(request.getSession(), myGameContainer);
                            //TODO сделать информацию о самой игре, о завершении, рандомизация цвета.
                            //говорим о цвете партии игроку (он сам редирект делает)
                            writer.write("white");
                            writer.close();
                            return;
                        }
                    }
                }

                //Сюда попадаем только если в очереди никого
                //waiting
                GameContainer gameContainer = new GameContainer(myMonitor);
                waitingQueue.push(gameContainer);
                synchronized (myMonitor) {
                    while (gameContainer.size() < 2) {
                        try {
                            myMonitor.wait();
                        } catch (InterruptedException e) {
                        }
                    }

                    addGameToSession(request.getSession(), gameContainer);
                    writer.write("black");
                    writer.close();
                    return;
                }
            }
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
        if (isSignedIn(request)) {
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

    private GameContainer getCurrentGameContainer(HttpSession session) {
        return (GameContainer) session.getAttribute(CURRENT_GAME_CONTAINER);
    }

    private boolean isSignedIn(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER_PROFILE);
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }
}
