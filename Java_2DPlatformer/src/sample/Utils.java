package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.HashMap;

public class Utils {
    public static class Resource {
        // подгрузка ресурсов

        public static HashMap<String, Scene> scenes = new HashMap<>();

        public static void load_scene(Parent root_name, String scene_name, String root_path) throws IOException
        {
            root_name = FXMLLoader.load(Resource.class.getResource(root_path));
            Scene scene = new Scene(root_name);
            scenes.put(scene_name, scene);
        }

    }
}
