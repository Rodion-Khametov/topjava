package ru.javawebinar.topjava;

public class Profiles {
    public static final String
            JDBC = "jdbc",
            JPA = "jpa",
            DATAJPA = "datajpa";

    public static final String REPOSITORY_IMPLEMENTATION = DATAJPA;

    public static final String
            POSTGRES_DB = "postgres",
            HSQL_DB = "hsqldb";

    //  Get DB profile depending of DB driver in classpath
    public static String getActiveDbProfile() {
        try {
            Class.forName("org.postgresql.Driver");
            return POSTGRES_DB;
        } catch (ClassNotFoundException ex) {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
                return Profiles.HSQL_DB;
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Could not find DB driver");
            }
        }
    }

    public static String getActiveReposProfile() {
        try {
            Class.forName("org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate");
            return JDBC;
        } catch (ClassNotFoundException ex) {
            try {
                Class.forName("org.springframework.orm.jpa.JpaTransactionManager");
                return JPA;
            } catch (ClassNotFoundException exception) {
                try {
                    Class.forName("org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean");
                    return DATAJPA;
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException("Could not find repository");
                }
            }
        }
    }
}
