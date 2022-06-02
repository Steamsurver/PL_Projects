package org.engine;


import imgui.ImFontConfig;
import imgui.ImFontGlyphRangesBuilder;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;
import imgui.type.ImString;
import org.engine.GameElements.GUIInit;
import org.engine.GameElements.Window;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;


public class Main {
    //TODO: потоки - реальзованно
    //TODO: сеть - реализованно
    //TODO: Графическая оболочка - реализованно
    //TODO: База данных - реализованно
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/User_dates";
    static final String USER = "postgres";
    static final String PASS = "defpass";


    public static void main(String[] args) throws InterruptedException, IOException {
        boolean aFlag = false;
        String user_id = "";
        String currentUserLogin = "";
        String currentUserPassword = "";
        int switchNum = 0;

        InputStream inputStream = System.in;
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        System.out.print("Choose operations(1 - for registration, 2 - for Authorisation): ");
        String switchNumStr = bufferedReader.readLine(); //читаем строку с клавиатуры
        switchNum = Integer.parseInt(switchNumStr);


        while (!aFlag) {
            switch (switchNum) {

                case 2:

                    boolean idF = false;
                    boolean logF = false;
                    boolean passF = false;

                    inputStream = System.in;
                    inputStreamReader = new InputStreamReader(inputStream);
                    bufferedReader = new BufferedReader(inputStreamReader);

                    System.out.print("Enter id: ");
                    user_id = bufferedReader.readLine(); //читаем строку с клавиатуры

                    System.out.print("Enter login: ");
                    currentUserLogin = bufferedReader.readLine(); //читаем строку с клавиатуры

                    System.out.print("Enter password: ");
                    currentUserPassword = bufferedReader.readLine(); //читаем строку с клавиатуры

                    if (Main.select(user_id).get(0).equals(user_id))
                        idF = true;

                    if (Main.select(user_id).get(1).equals(currentUserLogin))
                        logF = true;

                    if (Main.select(user_id).get(2).equals(currentUserPassword))
                        passF = true;

                    if (idF && logF && passF) {
                        System.out.println("Authorisation is successful.");
                        aFlag = true;
                    }

                    break;


                case 1:
                    inputStream = System.in;
                    inputStreamReader = new InputStreamReader(inputStream);
                    bufferedReader = new BufferedReader(inputStreamReader);

                    System.out.print("Enter id: ");
                    user_id = bufferedReader.readLine(); //читаем строку с клавиатуры

                    System.out.print("Enter login: ");
                    currentUserLogin = bufferedReader.readLine(); //читаем строку с клавиатуры

                    System.out.print("Enter password: ");
                    currentUserPassword = bufferedReader.readLine(); //читаем строку с клавиатуры

                    Main.inputToDB(user_id, currentUserLogin, currentUserPassword);


                    System.out.print("Choose operations(1 - for registration, 2 - for Authorisation): ");
                    switchNumStr = bufferedReader.readLine(); //читаем строку с клавиатуры
                    switchNum = Integer.parseInt(switchNumStr);
                    break;

            }
        }


        //================================================
        Window.getWindow().run();//вызываем главное окно
        //================================================
    }




    private static ArrayList<String> select(String id){
        String userid = "id";
        String username = "login";
        String password = "password";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
        }


        try {

            String selectTableSQL = "SELECT id, login, password from users";
            Statement statement = Objects.requireNonNull(connection).createStatement();
            ResultSet rs = statement.executeQuery(selectTableSQL);

            while (rs.next()) {
                userid = rs.getString("id");
                username = rs.getString("login");
                password = rs.getString("password");

                if(userid.equals(id)){
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        //============================================DB CODE END===================================================

        ArrayList<String> ret = new ArrayList<>();
        ret.add(userid);
        ret.add(username);
        ret.add(password);

        return ret;
    }
    private static void inputToDB(String id, String login, String password){
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = Objects.requireNonNull(connection).createStatement();
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
        }


        String insertTableSQL = "INSERT INTO users"
                + "(id, login, password) " + "VALUES"
                + "("+id+","+"'"+login+"'"+","+"'"+password+"'"+")";

        try {
            assert statement != null;
            statement.executeUpdate(insertTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
