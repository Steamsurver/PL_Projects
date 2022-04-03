package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.HashMap;

public class Utils {//ласс для работы с ресурсами
    public static class Resource {
        // подгрузка ресурсов

        public static HashMap<String, Scene> scenes = new HashMap<>();//мап со сценами

        public static void loadScene(Parent root_name, String scene_name, String root_path) throws IOException
        {//подгрузка сцен
            root_name = FXMLLoader.load(Resource.class.getResource(root_path));
            Scene scene = new Scene(root_name);
            scenes.put(scene_name, scene);
        }

        public static void loadScene(Scene scene, String scene_name) throws IOException
        {//подгрузка сцен
            scenes.put(scene_name, scene);
        }
    }
}
