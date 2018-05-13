package org.smart4j.framework.helper;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author Warren
 * @CreateTime 09.May.2018
 * @Version V1.0
 */
public final class DatabaseHelper {

    private static final Logger LOGGER = Logger.getLogger(DatabaseHelper.class);

    public static void beginTransaction () {
        Connection conn = getConnection();
        if (conn == null) return;
        try {
            conn.setAutoCommit(false);
        } catch (SQLException pE) {
            LOGGER.error("beginTransaction failed",pE);
        } finally {

        }
    }

    public static void commitTransaction() {
        Connection conn = getConnection();
        if (conn == null) return;
        try {
            conn.commit();
            conn.close();
        } catch (SQLException pE) {
            LOGGER.error("commitTransaction failed",pE);
        } finally {

        }
    }

    public static void rollbackTransaction() {
        Connection conn = getConnection();
        if (conn == null) return;
        try {
            conn.rollback();
            conn.close();
        } catch (SQLException pE) {
            LOGGER.error("rollbackTransaction failed",pE);
        } finally {

        }
    }

    // TODO
    private static Connection getConnection() {
        return null;
    }

}
