package CharacterClasses;

import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import sample.Main;

public class spriteAnimations extends Transition {
    private ImageView imageView;
    private int count;
    private int columns;
    private int offsetX;
    private int offsetY;
    private int width;
    private int height;

    public spriteAnimations(ImageView imageView, Duration duration, int count,
                            int columns, int offsetX, int offsetY,
                            int width, int height){
        this.imageView = imageView;
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
    }

    @Override
    protected void interpolate(double v) {
        final int index = Math.min((int) Math.floor(v * count), count - 1);
        int x = (index % columns) * width + offsetX;
        int y = (index / columns) * height + offsetY;
        imageView.setViewport(new Rectangle2D(x, y, width, height));
    }
}
