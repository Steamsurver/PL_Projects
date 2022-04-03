package maps;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;

public class MapClass {
    private static ImageView imageView;
    private static ImageView imageViewWall;
    private static int scaleX;
    private static int scaleY;
    private static int playerScaleX;
    private static int playerScaleY;
    private static int scaleWidth;
    private static int scaleHeight;
    public static HashMap<String, ImageView> propMapContainer = new HashMap<>();

    public MapClass(int ScaleX, int ScaleY, int PlayerScaleX, int PlayerScaleY){
        MapClass.scaleX = ScaleX;
        MapClass.scaleY = ScaleY;
        MapClass.playerScaleX = PlayerScaleX;
        MapClass.playerScaleY = PlayerScaleY;

    }

    public void setMapTexture(String image_path, String wall_path){
        Image image = new Image(image_path);
        imageView = new ImageView(image);
        Image imageWall = new Image(wall_path);
        imageViewWall = new ImageView(imageWall);
        MapClass.scaleHeight = (int)imageView.getFitHeight();
        MapClass.scaleWidth = (int)imageView.getFitWidth();
    }

    public int getPlayerScaleX(){
        return playerScaleX;
    }

    public int getPlayerScaleY(){
        return playerScaleY;
    }

    public ImageView getMapImage(){
        return imageView;
    }

    public ImageView getWallImage(){
        return imageViewWall;
    }

    public void loadProp(String path_prop, String name_prop, int scaleX, int scaleY, int width, int height){
        Image image = new Image(path_prop);
        ImageView imageView = new ImageView(image);
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
        imageView.setX(scaleX);
        imageView.setY(scaleY);
        propMapContainer.put(name_prop, imageView);
    }
    public void loadProp(String path_prop, String name_prop, int scaleX, int scaleY, int width, int height, boolean key){
        Image image = new Image(path_prop);
        ImageView imageView = new ImageView(image);
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
        imageView.setX(scaleX);
        imageView.setY(scaleY);
        imageView.setVisible(key);
        propMapContainer.put(name_prop, imageView);
    }
}
