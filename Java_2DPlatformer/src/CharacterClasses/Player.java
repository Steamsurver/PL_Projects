package CharacterClasses;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import maps.MapClass;

import java.util.HashMap;

public class Player{
    private static Image image = new Image("Textures/Animations/PlayerAnimation.png");
    private static ImageView imageViewPlayer = new ImageView(image);
    private Animation spriteAnimation;
    public static HashMap<String, Animation> animations = new HashMap<>();

    private static double speed =1.5;
    private static int width = 32;
    private static int height = 64;


    public Player(){
        imageViewPlayer = new ImageView(image);
        imageViewPlayer.setViewport(new Rectangle2D(width, 0, width,height));
    }

    public ImageView getImageViewPlayer(){
        return imageViewPlayer;
    }

    public void getRootChildren(Pane pane){
        pane.getChildren().add(imageViewPlayer);
    }

    public double getScaleX(){
        return imageViewPlayer.getX();
    }

    public double getScaleY(){
        return imageViewPlayer.getY();
    }

    public void setScaleCoordinate(int x, int y){
        imageViewPlayer.setX(x);
        imageViewPlayer.setY(y);
    }


    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public void moveUp(MapClass mapClass, Pane pane){
        this.animationPlay("UP");

        if(this.checkCollision(mapClass, pane)){
            imageViewPlayer.setY(imageViewPlayer.getY() - speed);
        }
        if(!this.checkCollision(mapClass, pane)) {
            imageViewPlayer.setY(imageViewPlayer.getY() + speed);
        }

    }

    public void moveDown(MapClass mapClass, Pane pane){
        this.animationPlay("DOWN");

        if(this.checkCollision(mapClass, pane)){
            imageViewPlayer.setY(imageViewPlayer.getY() + speed);
        }
        if(!this.checkCollision(mapClass, pane)) {
            imageViewPlayer.setY(imageViewPlayer.getY() - speed);
        }
    }

    public void moveRight(MapClass mapClass, Pane pane){
        this.animationPlay("RIGHT");

        if(this.checkCollision(mapClass, pane)){
            imageViewPlayer.setX(imageViewPlayer.getX() + speed);
        }
        if(!this.checkCollision(mapClass, pane)) {
            imageViewPlayer.setX(imageViewPlayer.getX() - speed);
        }
    }

    public void moveLeft(MapClass mapClass, Pane pane){
        this.animationPlay("LEFT");

        if(this.checkCollision(mapClass, pane)){
            imageViewPlayer.setX(imageViewPlayer.getX() - speed);
        }
        if(!this.checkCollision(mapClass, pane)) {
            imageViewPlayer.setX(imageViewPlayer.getX() + speed);
        }
    }




    public void animationInstalled(String name, Duration millis, int count, int columns, int offsetX, int offsetY, int width, int height){
        spriteAnimation = new spriteAnimations(imageViewPlayer, millis, count, columns, offsetX, offsetY, width, height);
        animations.put(name, spriteAnimation);

    }

    public void animationPlay(String name){
        animations.get(name).setCycleCount(Animation.INDEFINITE);
        animations.get(name).play();
    }

    public void animationStop(String name){
        animations.get(name).stop();
        imageViewPlayer.setViewport(new Rectangle2D(width, 0, width,height));
    }

    public boolean checkCollision(MapClass mapClass, Pane pane){
        for(ImageView imageView : mapClass.propMapContainer.values()){
            if(((this.getScaleX()-15+this.getWidth()) >= imageView.getX()) && (this.getScaleX()+10 <= imageView.getX() + imageView.getViewport().getWidth())
            &&(this.getScaleY()+ (int)(this.getHeight()/2) >= imageView.getY()) &&(this.getScaleY()+20+(int)(this.getHeight()/2) <= imageView.getY()+imageView.getViewport().getHeight())){

                return false;

            }

        }
        return true;
    }

}

