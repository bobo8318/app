package pub.imba.www.OpenGameEngner.layer;

import android.graphics.Canvas;

/**
 * Created by My on 2017/10/13.
 */
public abstract class Layer {

    /**
     * position of layer in x offset
     */
    int x; // = 0;

    /**
     * position of layer in y offset
     */
    int y; // = 0;

    /**
     * width of layer
     */
    int width; // = 0;

    /**
     * height of layer
     */
    int height; // = 0;

    /**
     * If the Layer is visible it will be drawn when <code>paint</code>
     * is called.
     */
    boolean visible = true;

    Layer(int width, int height) {
        setWidthImpl(width);
        setHeightImpl(height);
    }


    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }


    public final int getX() {
        return x;
    }


    public final int getY() {
        return y;
    }


    public final int getWidth() {
        return width;
    }


    public final int getHeight() {
        return height;
    }


    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    public final boolean isVisible() {
        return visible;
    }


    public abstract void paint(Canvas canvas);


    void setWidthImpl(int width) {
        if (width < 0) {
            throw new IllegalArgumentException();
        }
        this.width = width;
    }



    void setHeightImpl(int height) {
        if (height < 0) {
            throw new IllegalArgumentException();
        }
        this.height = height;
    }

}
