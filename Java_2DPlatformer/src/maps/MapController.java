package maps;

import CharacterClasses.Player;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MapController {
    public static MapClass Lab1Level1 = new MapClass(0, 0 , 500, 500);


    public static void loadMapLab1Level1(MapClass mapClass){
        mapClass.setMapTexture("Textures/Map_Textures/Level1_lab1.png", "Textures/Props/WalLab1Level1.png");
        mapClass.loadProp("Textures/Props/CrioCamera60x95.png", "Camera_3", 408, 421, 57, 95);
        mapClass.loadProp("Textures/Props/CrioCamera60x95.png", "Camera_2", 408, 455, 57, 95);
        mapClass.loadProp("Textures/Props/CrioCamera60x95.png", "Camera_1", 408, 489, 57, 95);
        mapClass.loadProp("Textures/Props/TableLab95x50.png", "TableLab", 522, 559, 95, 60);
        mapClass.loadProp("Textures/Props/Border.png", "Border_1", 407, 447, 1000, 26, false);
        mapClass.loadProp("Textures/Props/Border.png", "Border_2", 407, 607, 1000, 26, false);
        mapClass.loadProp("Textures/Props/Border.png", "Border_3", 307, 447, 100, 600, false);
        mapClass.loadProp("Textures/Props/Border.png", "Border_4", 617, 447, 100, 600, false);


    }

    public static void showMap(MapClass mapClass, Pane pane, Player player){
        pane.getChildren().add(mapClass.getMapImage());
        player.setScaleCoordinate(Lab1Level1.getPlayerScaleX(), Lab1Level1.getPlayerScaleY());
        player.getRootChildren(pane);
        pane.getChildren().add(Lab1Level1.getWallImage());

        for(ImageView imageView : mapClass.propMapContainer.values()){
            pane.getChildren().add(imageView);
        }


    }

}
