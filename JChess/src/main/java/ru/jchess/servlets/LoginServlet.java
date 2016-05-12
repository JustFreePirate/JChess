package ru.jchess.servlets;

import ru.jchess.model.DatabaseManager;
import ru.jchess.model.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import static ru.jchess.model.Constants.DB_MANAGER;
import static ru.jchess.model.Constants.USER_PROFILE;

/**
 * Created by dima on 09.04.16.
 */
public class LoginServlet extends HttpServlet {
    final static String SIGN_IN_SUCCESS = "sign_in_success";
    final static String SIGN_IN_FAILURE = "login_password_invalid";
    final static String SIGN_UP_SUCCESS = "sign_up_success";
    final static String SIGN_UP_FILTER_FAILURE = "sign_up_filter_failure";
    final static String SIGN_UP_USER_ALREADY_EXIST = "sign_up_user_already_exist";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        if (ajax) {
            String action = request.getParameter("action");
            String login = request.getParameter("login");
            String pass = request.getParameter("password");
            Writer writer = response.getWriter();
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            if ("SignIn".equals(action)) {
                if (checkLoginPass(login, pass)) { //auth
                    userSignIn(request, login);
                    writer.write(SIGN_IN_SUCCESS);
                } else {
                    writer.write(SIGN_IN_FAILURE);
                }
            } else if ("SignUp".equals(action)) {
                //проверяем логин и пасс на валидность (по идее почту тоже)
                if (User.checkLoginForCorrect(login) && User.checkPasswordForCorrect(pass)) {
                    if (checkLoginAlreadyExist(login)) { //пользователь с таким именем уже есть
                        writer.write(SIGN_UP_USER_ALREADY_EXIST);
                    } else {
                        //регистрируем пользователя
                        userSignUp(login,pass);
                        //логинем его
                        userSignIn(request, login);
                        writer.write(SIGN_UP_SUCCESS);
                    }
                } else {
                    writer.write(SIGN_UP_FILTER_FAILURE);
                }
            }
            writer.close();
        }
    }

    //check login and pass for sign in
    private boolean checkLoginPass(String login, String pass) {
        DatabaseManager dbm = getDatabaseManager();
        User user = new User();
        user.setLogin(login);
        user.setPassword(pass);
        if (dbm.isUserExist(user)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkLoginAlreadyExist(String login) {
        DatabaseManager dbm = getDatabaseManager();
        if (dbm.isLoginExist(login)) {
            return true;
        } else {
            return false;
        }
    }

    //регистрирует нового пользователя. Без проверок.
    private void userSignUp(String login, String pass) {
        DatabaseManager dbm = getDatabaseManager();
        User user = new User();
        user.setLogin(login);
        user.setPassword(pass);
        dbm.addUser(user);
    }

    //логинит пользователя, который уже есть в БД без проверки пароля
    private void userSignIn(HttpServletRequest request,String login) {
        HttpSession session = request.getSession();
        DatabaseManager dbm = getDatabaseManager();
        User user = dbm.getUserProfile(login);
        session.setAttribute(USER_PROFILE, user);
    }

    public static boolean isSignedIn(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER_PROFILE);
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }

    public static String getLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER_PROFILE);
        if (user != null) {
            return user.getLogin();
        } else {
            return null;
        }
    }

    private DatabaseManager getDatabaseManager() {
        ServletContext sc = this.getServletContext();
        DatabaseManager dbm = (DatabaseManager) sc.getAttribute(DB_MANAGER);
        if (dbm == null) {
            dbm = new DatabaseManager();
            sc.setAttribute(DB_MANAGER, dbm);
        }
        return dbm;
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/auth.jsp").forward(request,response);
    }
}
