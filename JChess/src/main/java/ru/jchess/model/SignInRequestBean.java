package ru.jchess.model;

/**
 * Created by dima on 10.04.16.
 */
public class SignInRequestBean implements java.io.Serializable {
    public String login;
    public String password;

    public SignInRequestBean() {

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

    @Override
    public String toString() {
        return "SignInRequestBean{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignInRequestBean that = (SignInRequestBean) o;

        if (!login.equals(that.login)) return false;
        return password.equals(that.password);

    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
