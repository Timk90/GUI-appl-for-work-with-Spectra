package spectralApp.controller;

import java.sql.*;

/**
 * Created by Tim on 29.06.2017.
 */
public class WorkWithDB implements WorkWithDBint{

    private WorkWithDB(){};

    public static class Singleton{
        public static final WorkWithDB holder = new WorkWithDB();
    }

    public static WorkWithDB getInstance(){return Singleton.holder;}

    static String url = "org.hsqldb.jdbc.JDBCDriver";

    static String createDB = "CREATE TABLE IF NOT EXISTS users " +
            "(ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY, " +
            "username VARCHAR(45), " +
            "password VARCHAR(16));";

    Connection con;

    int init(String url, String createTable){
        try {
            Class.forName(url);
            con = DriverManager.getConnection("jdbc:hsqldb:mydatabase","SA","");
            con.createStatement().executeUpdate(createTable);
            return 1;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -2;
        }finally {
            if(con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showDB() {

        try {
            Class.forName(url);
            con = DriverManager.getConnection("jdbc:hsqldb:mydatabase","SA","");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users;");
            ResultSet rs = ps.executeQuery();


            System.out.print(rs.getMetaData().getColumnName(1)+"     ");
            System.out.print(rs.getMetaData().getColumnName(2)+"     ");
            System.out.print(rs.getMetaData().getColumnName(3));
            System.out.println();
            System.out.println("----------------------------");


            int userID;
            String userName;
            String userPass;

            while(rs.next()) {
                userID = rs.getInt(1);
                userName = rs.getString(2);
                userPass = rs.getString(3);
                System.out.print(userID);
                System.out.print("      " + userName);
                System.out.print("         " + userPass);
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void deleteTable(String table){
        try {
            Class.forName(url);
            con = DriverManager.getConnection("jdbc:hsqldb:mydatabase","SA","");
            Statement stm = con.createStatement();
            stm.executeUpdate("DROP TABLE " + table + ";");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public int checkUserName(String name, String pass) {
        try {
            Class.forName(url);
            con = DriverManager.getConnection("jdbc:hsqldb:mydatabase", "SA", "");
            String sql = "SELECT username FROM USERS WHERE username = '"+name+"' AND password = '"+pass+"'"+";";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()) {
                return 1;
            }else{
                return 0;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }finally {
            if(con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
        }
    }

    @Override
    public int checkUserName(String name) {
        try {
            Class.forName(url);
            con = DriverManager.getConnection("jdbc:hsqldb:mydatabase", "SA", "");
            String sql = "SELECT username FROM USERS WHERE username = '"+name+"'";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()) {
                return 1;
            }else{
                return 0;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }finally {
                if(con != null){
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public int addUser(String userName, String password) {

        try {

                Class.forName(url);
                con = DriverManager.getConnection("jdbc:hsqldb:mydatabase", "SA", "");

                 PreparedStatement psAdd = con.prepareStatement("INSERT INTO users (username, password) VALUES (?,?);");
                 psAdd.setString(1, userName);
                 psAdd.setString(2, password);
                 psAdd.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    @Override
    public int deleteUser(String name) {
        try {
                Class.forName(url);
                con = DriverManager.getConnection("jdbc:hsqldb:mydatabase", "SA", "");
                PreparedStatement psDel = con.prepareStatement("DELETE FROM users WHERE username = '"+name+"'");
                psDel.executeUpdate();
                return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        }finally{
            if(con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void addDefaultRootUser(){
        try {
                con = DriverManager.getConnection("jdbc:hsqldb:mydatabase", "SA", "");
                con.createStatement().executeUpdate("INSERT INTO users (username, password) VALUES ('root','1234');");
        } catch (SQLException e) {
                e.printStackTrace();

        }finally{
            if(con != null){
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

// бесполезный метод для проверки работоспособности функций класса
    public static void main(String[] args) {

        WorkWithDB wwdb = WorkWithDB.getInstance();
        //wwdb.deleteTable("USERS");
        wwdb.init(url, createDB);
        if(wwdb.checkUserName("root") == 0) {
            wwdb.addDefaultRootUser();
        }
//        String name = "1231223321";
//        if(wwdb.checkUserName(name) == 0) {
//           wwdb.addUser(name, "ewqw456e");
//        }
       // wwdb.deleteUser(name);
        System.out.println(wwdb.checkUserName("root","1234"));
        wwdb.showDB();

    }
}
