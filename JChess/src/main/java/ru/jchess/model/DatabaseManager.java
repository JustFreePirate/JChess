package ru.jchess.model;
import org.sql2o.Sql2o;

import org.sql2o.Connection;

/**
 * Created by dima on 20.04.16.
 */
public class DatabaseManager {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/jchess_db";

    static final String DB_USER = "root";
    static final String DB_PASS = "admin";

    static final String USER_TABLE = "users";

    private static Sql2o sql2o;

    static {
        sql2o = new Sql2o(DB_URL, DB_USER, DB_PASS);
    }

    public DatabaseManager() {
        try {
            Class.forName(JDBC_DRIVER).newInstance();
        } catch (Throwable t) {
            throw new RuntimeException("Error in Class.forName(JDBC_DRIVER).newInstance();");
        }

        if (!isTableExists(USER_TABLE)) {
            createUsersTable();
        }
    }

    private boolean isTableExists(String tableName) {
        final String request = "SELECT 1 FROM `" + tableName + "` LIMIT 1;";
        try (Connection con = sql2o.beginTransaction()) {
            Object t = con.createQuery(request).executeScalar();
        } catch (Exception t) {
            return false;
        }
        return true;
    }

    private void createUsersTable() {
        final String createTable =
                "CREATE TABLE\n" +
                        "    `" + USER_TABLE + "` (\n" +
                        "        `user_id`      INT AUTO_INCREMENT,\n" +
                        "        `login`        VARCHAR(100) NOT NULL,\n" +
                        "        `hash_pass`    VARCHAR(100) NOT NULL,\n" +
                        "        `count_games`    int,\n" +
                        "        `count_wins`    int,\n" +
                        "        `count_draws`    int,\n" +
                        "         PRIMARY KEY(`user_id`)\n" +
                        "    );";

        try (Connection con = sql2o.beginTransaction()) {
            con.createQuery(createTable).executeUpdate();
            con.commit();
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isUserExist(User user) {
        final String findUser =
                "SELECT count(*) " +
                        " FROM " + USER_TABLE  +
                        " WHERE login = :loginParam AND hash_pass = :hashPassParam;";
        Long result = (Long) sql2o
                .createQuery(findUser)
                .addParameter("loginParam", user.getLogin())
                .addParameter("hashPassParam", user.getHashPass())
                .executeScalar();
        return result > 0;
    }

    public void addUser(User user) {
        final String insertPerson =
                "INSERT INTO\n" +
                        USER_TABLE + " (`login`, `hash_pass`, `count_games`, `count_wins`, `count_draws`)\n" +
                        "VALUES\n" +
                        "    (:loginParam, :hashPassParam, :countGamesParam, :countWinsParam, :countDrawsParam);";

        try (Connection con = sql2o.beginTransaction()) {
            con.createQuery(insertPerson)
                    .addParameter("loginParam", user.getLogin())
                    .addParameter("hashPassParam", user.getHashPass())
                    .addParameter("countGamesParam", user.getCount_games())
                    .addParameter("countWinsParam", user.getCount_wins())
                    .addParameter("countDrawsParam", user.getCount_draws())
                    .executeUpdate();
            con.commit();
        }
    }
}
