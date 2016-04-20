package ru.jchess.model;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * Created by dima on 10.04.16.
 */
public class User {
    private String login;
    private String password;
    private int count_games;
    private int count_wins;
    private int count_draws;

    public User() {

    }

    public String getHashPass() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes(Charset.forName("UTF-8")));
            byte[] digest = md.digest();
            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }

    static public boolean checkLoginForCorrect(String login) {
        if (login != null && login.length() > 5) {
            return true; //TODO
        } else {
            return false;
        }
    }

    static public boolean checkPasswordForCorrect(String password) {
        if (password != null && password.length() > 5) {
            return true; //TODO
        } else {
            return false;
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCount_games() {
        return count_games;
    }

    public void setCount_games(int count_games) {
        this.count_games = count_games;
    }

    public int getCount_wins() {
        return count_wins;
    }

    public void setCount_wins(int count_wins) {
        this.count_wins = count_wins;
    }

    public int getCount_draws() {
        return count_draws;
    }

    public void setCount_draws(int count_draws) {
        this.count_draws = count_draws;
    }
}
