package CharacterClasses;

import Scenes.ControllerGameProcess;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sample.Main;

public class Player{
    private static Image image = new Image("Animations/PlayerAnimation.png");
    private static ImageView imageView_player = new ImageView(image);

    private static double speed = 8;



    public Player(){
        imageView_player = new ImageView(image);
        imageView_player.setViewport(new Rectangle2D(80, 0, 80,172));
    }

    public void GetRootChildren(Pane pane){
        pane.getChildren().add(imageView_player);
    }

    public void moveUp(){
        imageView_player.setY(imageView_player.getY() - speed);
        Animation spriteAnimations_down = new SpriteAnimations(imageView_player, Duration.millis(500), 3, 3, 0, 188, 80, 172);
        //spriteAnimations_down.setCycleCount(Animation.INDEFINITE);
        spriteAnimations_down.play();
    }

    public void moveDown(){
        imageView_player.setY(imageView_player.getY() + speed);
        Animation spriteAnimations_down = new SpriteAnimations(imageView_player, Duration.millis(500), 3, 3, 0, 0, 80, 172);
        //spriteAnimations_down.setCycleCount(Animation.INDEFINITE);
        spriteAnimations_down.play();
    }

    public void moveRight(){
        imageView_player.setX(imageView_player.getX() + speed);
        Animation spriteAnimations_down = new SpriteAnimations(imageView_player, Duration.millis(500), 3, 3, 0, 560, 112, 180);
        //spriteAnimations_down.setCycleCount(Animation.INDEFINITE);
        spriteAnimations_down.play();
    }

    public void moveLeft(){
        imageView_player.setX(imageView_player.getX() - speed);
        Animation spriteAnimations_down = new SpriteAnimations(imageView_player, Duration.millis(500), 3, 3, 0, 372, 120, 180);
        //spriteAnimations_down.setCycleCount(Animation.INDEFINITE);
        spriteAnimations_down.play();
    }
}

