package ru.jchess.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.jchess.model.DatabaseManager;
import ru.jchess.model.SignInRequestBean;
import ru.jchess.model.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dima on 09.04.16.
 */
public class LoginServlet extends HttpServlet {
    final static String SIGN_IN_CORRECT = "correct";
    final static String SIGN_IN_NOT_CORRECT = "not_correct";
    final static String SIGN_UP_CORRECT = "correct";
    final static String SIGN_UP_NOT_CORRECT = "not_correct";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        if (ajax) {
            String action = request.getParameter("action");
            String login = request.getParameter("login");
            String pass = request.getParameter("password");
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            if ("SignIn".equals(action)) {
                if (checkLoginPassAlreadyExist(login, pass)) {
                    response.getWriter().write(SIGN_IN_CORRECT);
                } else {
                    response.getWriter().write(SIGN_IN_NOT_CORRECT);
                }
            } else if ("SignUp".equals(action)) {
                //проверяем логин и пасс на валидность (по идее почту тоже)
                if (User.checkLoginForCorrect(login) && User.checkPasswordForCorrect(pass)) {
                    if (checkLoginPassAlreadyExist(login,pass)) { //пользователь с таким именем уже есть
                        response.getWriter().write(SIGN_UP_NOT_CORRECT);
                    } else {
                        //регистрируем пользователя
                        userSignUp(login,pass);
                        response.getWriter().write(SIGN_UP_CORRECT);
                    }
                } else {
                    response.getWriter().write(SIGN_UP_NOT_CORRECT);
                }
            }
            response.getWriter().flush();
        } else {
            //nothing to do
        }
    }

    private boolean checkLoginPassAlreadyExist(String login, String pass) {
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

    private void userSignUp(String login, String pass) {
        DatabaseManager dbm = getDatabaseManager();
        User user = new User();
        user.setLogin(login);
        user.setPassword(pass);
        dbm.addUser(user);
    }


    private DatabaseManager getDatabaseManager() {
        ServletContext sc = this.getServletContext();
        DatabaseManager dbm = (DatabaseManager) sc.getAttribute("db_manager");
        if (dbm == null) {
            dbm = new DatabaseManager();
            sc.setAttribute("db_manager", dbm);
        }
        return dbm;
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/auth.jsp").forward(request,response);
    }
}
