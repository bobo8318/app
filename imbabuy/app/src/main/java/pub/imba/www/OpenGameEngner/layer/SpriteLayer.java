package pub.imba.www.OpenGameEngner.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by My on 2017/10/14.
 */
public class SpriteLayer extends Layer{

    // ----- definitions for the various transformations possible -----

    /**
     * No transform is applied to the Sprite. This constant has a value of
     * <code>0</code>.
     */
    public static final int TRANS_NONE = 0;

    /**
     * Causes the Sprite to appear rotated clockwise by 90 degrees. This
     * constant has a value of <code>5</code>.
     */
    public static final int TRANS_ROT90 = 5;

    /**
     * Causes the Sprite to appear rotated clockwise by 180 degrees. This
     * constant has a value of <code>3</code>.
     */
    public static final int TRANS_ROT180 = 3;

    /**
     * Causes the Sprite to appear rotated clockwise by 270 degrees. This
     * constant has a value of <code>6</code>.
     */
    public static final int TRANS_ROT270 = 6;

    /**
     * Causes the Sprite to appear reflected about its vertical center. This
     * constant has a value of <code>2</code>.
     */
    public static final int TRANS_MIRROR = 2;

    /**
     * Causes the Sprite to appear reflected about its vertical center and then
     * rotated clockwise by 90 degrees. This constant has a value of
     * <code>7</code>.
     */
    public static final int TRANS_MIRROR_ROT90 = 7;

    /**
     * Causes the Sprite to appear reflected about its vertical center and then
     * rotated clockwise by 180 degrees. This constant has a value of
     * <code>1</code>.
     */
    public static final int TRANS_MIRROR_ROT180 = 1;

    /**
     * Causes the Sprite to appear reflected about its vertical center and then
     * rotated clockwise by 270 degrees. This constant has a value of
     * <code>4</code>.
     */
    public static final int TRANS_MIRROR_ROT270 = 4;

    // --- member variables

    /**
     * If this bit is set, it denotes that the transform causes the axes to be
     * interchanged
     */
    private static final int INVERTED_AXES = 0x4;

    /**
     * If this bit is set, it denotes that the transform causes the x axis to be
     * flipped.
     */
    private static final int X_FLIP = 0x2;

    /**
     * If this bit is set, it denotes that the transform causes the y axis to be
     * flipped.
     */
    private static final int Y_FLIP = 0x1;

    /**
     * Bit mask for channel value in ARGB pixel.
     */
    private static final int ALPHA_BITMASK = 0xff000000;

    /**
     * Source image
     */
    Bitmap sourceImage;

    /**
     * The number of frames
     */
    int numberFrames; // = 0;

    /**
     * list of X coordinates of individual frames
     */
    int[] frameCoordsX;
    /**
     * list of Y coordinates of individual frames
     */
    int[] frameCoordsY;

    /**
     * Width of each frame in the source image
     */
    int srcFrameWidth;

    /**
     * Height of each frame in the source image
     */
    int srcFrameHeight;

    /**
     * The sequence in which to display the Sprite frames
     */
    int[] frameSequence;

    /**
     * The sequence index
     */
    private int sequenceIndex; // = 0

    /**
     * Set to true if custom sequence is used.
     */
    private boolean customSequenceDefined; // = false;

    // -- reference point
    /**
     * Horizontal offset of the reference point from the top left of the sprite.
     */
    int dRefX; // =0

    /**
     * Vertical offset of the reference point from the top left of the sprite.
     */
    int dRefY; // =0

    // --- collision rectangle

    /**
     * Horizontal offset of the top left of the collision rectangle from the top
     * left of the sprite.
     */
    int collisionRectX; // =0

    /**
     * Vertical offset of the top left of the collision rectangle from the top
     * left of the sprite.
     */
    int collisionRectY; // =0

    /**
     * Width of the bounding rectangle for collision detection.
     */
    int collisionRectWidth;

    /**
     * Height of the bounding rectangle for collision detection.
     */
    int collisionRectHeight;

    // --- transformation(s)
    // --- values that may change on setting transformations
    // start with t_

    /**
     * The current transformation in effect.
     */
    int t_currentTransformation;

    /**
     * Horizontal offset of the top left of the collision rectangle from the top
     * left of the sprite.
     */
    int t_collisionRectX;

    /**
     * Vertical offset of the top left of the collision rectangle from the top
     * left of the sprite.
     */
    int t_collisionRectY;

    /**
     * Width of the bounding rectangle for collision detection, with the current
     * transformation in effect.
     */
    int t_collisionRectWidth;

    /**
     * Height of the bounding rectangle for collision detection, with the
     * current transformation in effect.
     */
    int t_collisionRectHeight;

    // ----- Constructors -----

    /**
     * Creates a new non-animated Sprite using the provided Image. This
     * constructor is functionally equivalent to calling
     * <code>new Sprite(image, image.getWidth(), image.getHeight())</code>
     * <p>
     * By default, the Sprite is visible and its upper-left corner is positioned
     * at (0,0) in the painter's coordinate system. <br>
     *
     * @param image
     *            the <code>Image</code> to use as the single frame for the
     *            </code>Sprite
     * @throws NullPointerException
     *             if <code>img</code> is <code>null</code>
     */
    public SpriteLayer(Bitmap image) {
        super(image.getWidth(), image.getHeight());

        initializeFrames(image, image.getWidth(), image.getHeight(), false);

        // initialize collision rectangle
        initCollisionRectBounds();

        // current transformation is TRANS_NONE
        this.setTransformImpl(TRANS_NONE);

    }

    /**
     * Creates a new animated Sprite using frames contained in the provided
     * Image. The frames must be equally sized, with the dimensions specified by
     * <code>frameWidth</code> and <code>frameHeight</code>. They may be laid
     * out in the image horizontally, vertically, or as a grid. The width of the
     * source image must be an integer multiple of the frame width, and the
     * height of the source image must be an integer multiple of the frame
     * height. The values returned by {@link Layer#getWidth} and
     * {@link Layer#getHeight} will reflect the frame width and frame height
     * subject to the Sprite's current transform.
     * <p>
     * Sprites have a default frame sequence corresponding to the raw frame
     * numbers, starting with frame 0. The frame sequence may be modified with
     * {@link #setFrameSequence(int[])}.
     * <p>
     * By default, the Sprite is visible and its upper-left corner is positioned
     * at (0,0) in the painter's coordinate system.
     * <p>
     *
     * @param image
     *            the <code>Image</code> to use for <code>Sprite</code>
     * @param frameWidth
     *            the <code>width</code>, in pixels, of the individual raw
     *            frames
     * @param frameHeight
     *            the <code>height</code>, in pixels, of the individual raw
     *            frames
     * @throws NullPointerException
     *             if <code>img</code> is <code>null</code>
     * @throws IllegalArgumentException
     *             if <code>frameHeight</code> or <code>frameWidth</code> is
     *             less than <code>1</code>
     * @throws IllegalArgumentException
     *             if the <code>image</code> width is not an integer multiple of
     *             the <code>frameWidth</code>
     * @throws IllegalArgumentException
     *             if the <code>image</code> height is not an integer multiple
     *             of the <code>frameHeight</code>
     */
    public SpriteLayer(Bitmap image, int frameWidth, int frameHeight) {
        super(frameWidth, frameHeight);
        // if img is null img.getWidth() will throw NullPointerException
        if ((frameWidth < 1 || frameHeight < 1)
                || ((image.getWidth() % frameWidth) != 0)
                || ((image.getHeight() % frameHeight) != 0)) {
            throw new IllegalArgumentException();
        }

        // construct the array of images that
        // we use as "frames" for the sprite.
        // use default frame , sequence index = 0
        initializeFrames(image, frameWidth, frameHeight, false);

        // initialize collision rectangle
        initCollisionRectBounds();

        // current transformation is TRANS_NONE
        this.setTransformImpl(TRANS_NONE);

    }

    /**
     * Creates a new Sprite from another Sprite.
     * <p>
     *
     * All instance attributes (raw frames, position, frame sequence, current
     * frame, reference point, collision rectangle, transform, and visibility)
     * of the source Sprite are duplicated in the new Sprite.
     *
     * @param s
     *            the <code>Sprite</code> to create a copy of
     * @throws NullPointerException
     *             if <code>s</code> is <code>null</code>
     *
     */
    public SpriteLayer(SpriteLayer s) {

        super(s != null ? s.getWidth() : 0, s != null ? s.getHeight() : 0);

        if (s == null) {
            throw new NullPointerException();
        }

        this.sourceImage = s.sourceImage;// Image.createImage(s.sourceImage);

        this.numberFrames = s.numberFrames;

        this.frameCoordsX = new int[this.numberFrames];
        this.frameCoordsY = new int[this.numberFrames];

        System.arraycopy(s.frameCoordsX, 0, this.frameCoordsX, 0, s
                .getRawFrameCount());

        System.arraycopy(s.frameCoordsY, 0, this.frameCoordsY, 0, s
                .getRawFrameCount());

        this.x = s.getX();
        this.y = s.getY();

        // these fields are set by defining a reference point
        this.dRefX = s.dRefX;
        this.dRefY = s.dRefY;

        // these fields are set when defining a collision rectangle
        this.collisionRectX = s.collisionRectX;
        this.collisionRectY = s.collisionRectY;
        this.collisionRectWidth = s.collisionRectWidth;
        this.collisionRectHeight = s.collisionRectHeight;

        // these fields are set when creating a Sprite from an Image
        this.srcFrameWidth = s.srcFrameWidth;
        this.srcFrameHeight = s.srcFrameHeight;

        // the above fields are used in setTransform()
        // which is why we set them first, then call setTransformImpl()
        // to set up internally used data structures.
        setTransformImpl(s.t_currentTransformation);

        this.setVisible(s.isVisible());

        this.frameSequence = new int[s.getFrameSequenceLength()];
        this.setFrameSequence(s.frameSequence);
        this.setFrame(s.getFrame());

        this.setRefPixelPosition(s.getRefPixelX(), s.getRefPixelY());

    }

    // ----- public methods -----


    public void defineReferencePixel(int x, int y) {
        dRefX = x;
        dRefY = y;
    }

    /**
     * Sets this Sprite's position such that its reference pixel is located at
     * (x,y) in the painter's coordinate system.
     *
     * @param x
     *            the horizontal location at which to place the reference pixel
     * @param y
     *            the vertical location at which to place the reference pixel
     * @see #defineReferencePixel
     * @see #getRefPixelX
     * @see #getRefPixelY
     */
    public void setRefPixelPosition(int x, int y) {

        // update this.x and this.y
        this.x = x
                - getTransformedPtX(dRefX, dRefY, this.t_currentTransformation);
        this.y = y
                - getTransformedPtY(dRefX, dRefY, this.t_currentTransformation);

    }

    /**
     * Gets the horizontal position of this Sprite's reference pixel in the
     * painter's coordinate system.
     *
     * @return the horizontal location of the reference pixel
     * @see #defineReferencePixel
     * @see #setRefPixelPosition
     * @see #getRefPixelY
     */
    public int getRefPixelX() {
        return (this.x + getTransformedPtX(dRefX, dRefY,
                this.t_currentTransformation));
    }

    /**
     * Gets the vertical position of this Sprite's reference pixel in the
     * painter's coordinate system.
     *
     * @return the vertical location of the reference pixel
     * @see #defineReferencePixel
     * @see #setRefPixelPosition
     * @see #getRefPixelX
     */
    public int getRefPixelY() {
        return (this.y + getTransformedPtY(dRefX, dRefY,
                this.t_currentTransformation));
    }


    public void setFrame(int sequenceIndex) {
        if (sequenceIndex < 0 || sequenceIndex >= frameSequence.length) {
            throw new IndexOutOfBoundsException();
        }
        this.sequenceIndex = sequenceIndex;
    }

    /**
     * Gets the current index in the frame sequence.
     * <p>
     * The index returned refers to the current entry in the frame sequence, not
     * the index of the actual frame that is displayed.
     *
     * @return the current index in the frame sequence
     * @see #setFrameSequence(int[])
     * @see #setFrame
     */
    public final int getFrame() {
        return sequenceIndex;
    }

    /**
     * Gets the number of raw frames for this Sprite. The value returned
     * reflects the number of frames; it does not reflect the length of the
     * Sprite's frame sequence. However, these two values will be the same if
     * the default frame sequence is used.
     *
     * @return the number of raw frames for this Sprite
     * @see #getFrameSequenceLength
     */
    public int getRawFrameCount() {
        return numberFrames;
    }

    /**
     * Gets the number of elements in the frame sequence. The value returned
     * reflects the length of the Sprite's frame sequence; it does not reflect
     * the number of raw frames. However, these two values will be the same if
     * the default frame sequence is used.
     *
     * @return the number of elements in this Sprite's frame sequence
     * @see #getRawFrameCount
     */
    public int getFrameSequenceLength() {
        return frameSequence.length;
    }

    /**
     * Selects the next frame in the frame sequence.
     * <p>
     *
     * The frame sequence is considered to be circular, i.e. if
     * {@link #nextFrame} is called when at the end of the sequence, this method
     * will advance to the first entry in the sequence.
     *
     * @see #setFrameSequence(int[])
     * @see #prevFrame
     */
    public void nextFrame() {
        sequenceIndex = (sequenceIndex + 1) % frameSequence.length;
    }

    /**
     * Selects the previous frame in the frame sequence.
     * <p>
     *
     * The frame sequence is considered to be circular, i.e. if
     * {@link #prevFrame} is called when at the start of the sequence, this
     * method will advance to the last entry in the sequence.
     *
     * @see #setFrameSequence(int[])
     * @see #nextFrame
     */
    public void prevFrame() {
        if (sequenceIndex == 0) {
            sequenceIndex = frameSequence.length - 1;
        } else {
            sequenceIndex--;
        }
    }


    public final void paint(Canvas canvas) {
        // managing the painting order is the responsibility of
        // the layermanager, so depth is ignored
        if (canvas == null) {
            throw new NullPointerException();
        }

        if (visible) {

            // width and height of the source
            // image is the width and height
            // of the original frame
            drawImage(canvas,this.x,this.y,sourceImage,frameCoordsX[frameSequence[sequenceIndex]],
                    frameCoordsY[frameSequence[sequenceIndex]],
                    srcFrameWidth,
                    srcFrameHeight);
        }

    }

    private void drawImage(Canvas canvas, int x, int y,
                           Bitmap bsrc, int sx, int sy, int w, int h) {
        Rect rect_src = new Rect();
        rect_src.left = sx;
        rect_src.right = sx + w;
        rect_src.top = sy;
        rect_src.bottom = sy + h;

        Rect rect_dst = new Rect();
        rect_dst.left = x;
        rect_dst.right = x + w;
        rect_dst.top = y;
        rect_dst.bottom = y + h;
        canvas.drawBitmap(bsrc, rect_src, rect_dst, null);

        rect_src = null;
        rect_dst = null;
    }


    public void setFrameSequence(int sequence[]) {

        if (sequence == null) {
            // revert to the default sequence
            sequenceIndex = 0;
            customSequenceDefined = false;
            frameSequence = new int[numberFrames];
            // copy frames indices into frameSequence
            for (int i = 0; i < numberFrames; i++) {
                frameSequence[i] = i;
            }
            return;
        }

        if (sequence.length < 1) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i] < 0 || sequence[i] >= numberFrames) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
        customSequenceDefined = true;
        frameSequence = new int[sequence.length];
        System.arraycopy(sequence, 0, frameSequence, 0, sequence.length);
        sequenceIndex = 0;
    }


    public void setImage(Bitmap img, int frameWidth, int frameHeight) {

        // if image is null image.getWidth() will throw NullPointerException
        if ((frameWidth < 1 || frameHeight < 1)
                || ((img.getWidth() % frameWidth) != 0)
                || ((img.getHeight() % frameHeight) != 0)) {
            throw new IllegalArgumentException();
        }

        int noOfFrames = (img.getWidth() / frameWidth)
                * (img.getHeight() / frameHeight);

        boolean maintainCurFrame = true;
        if (noOfFrames < numberFrames) {
            // use default frame , sequence index = 0
            maintainCurFrame = false;
            customSequenceDefined = false;
        }

        if (!((srcFrameWidth == frameWidth) && (srcFrameHeight == frameHeight))) {

            // computing is the location
            // of the reference pixel in the painter's coordinate system.
            // and then use this to find x and y position of the Sprite
            int oldX = this.x
                    + getTransformedPtX(dRefX, dRefY,
                    this.t_currentTransformation);

            int oldY = this.y
                    + getTransformedPtY(dRefX, dRefY,
                    this.t_currentTransformation);

            setWidthImpl(frameWidth);
            setHeightImpl(frameHeight);

            initializeFrames(img, frameWidth, frameHeight, maintainCurFrame);

            // initialize collision rectangle
            initCollisionRectBounds();

            // set the new x and y position of the Sprite
            this.x = oldX
                    - getTransformedPtX(dRefX, dRefY,
                    this.t_currentTransformation);

            this.y = oldY
                    - getTransformedPtY(dRefX, dRefY,
                    this.t_currentTransformation);

            // Calculate transformed sprites collision rectangle
            // and transformed width and height

            computeTransformedBounds(this.t_currentTransformation);

        } else {
            // just reinitialize the animation frames.
            initializeFrames(img, frameWidth, frameHeight, maintainCurFrame);
        }

    }


    public void defineCollisionRectangle(int x, int y, int width, int height) {

        if (width < 0 || height < 0) {
            throw new IllegalArgumentException();
        }

        collisionRectX = x;
        collisionRectY = y;
        collisionRectWidth = width;
        collisionRectHeight = height;

        // call set transform with current transformation to
        // update transformed sprites collision rectangle
        setTransformImpl(t_currentTransformation);
    }


    public void setTransform(int transform) {
        setTransformImpl(transform);
    }


    public final boolean collidesWith(SpriteLayer s, boolean pixelLevel) {

        // check if either of the Sprite's are not visible
        if (!(s.visible && this.visible)) {
            return false;
        }

        // these are package private
        // and can be accessed directly
        int otherLeft = s.x + s.t_collisionRectX;
        int otherTop = s.y + s.t_collisionRectY;
        int otherRight = otherLeft + s.t_collisionRectWidth;
        int otherBottom = otherTop + s.t_collisionRectHeight;

        int left = this.x + this.t_collisionRectX;
        int top = this.y + this.t_collisionRectY;
        int right = left + this.t_collisionRectWidth;
        int bottom = top + this.t_collisionRectHeight;

        // check if the collision rectangles of the two sprites intersect
        if (intersectRect(otherLeft, otherTop, otherRight, otherBottom, left,
                top, right, bottom)) {

            // collision rectangles intersect
            if (pixelLevel) {

                // we need to check pixel level collision detection.
                // use only the coordinates within the Sprite frame if
                // the collision rectangle is larger than the Sprite
                // frame
                if (this.t_collisionRectX < 0) {
                    left = this.x;
                }
                if (this.t_collisionRectY < 0) {
                    top = this.y;
                }
                if ((this.t_collisionRectX + this.t_collisionRectWidth) > this.width) {
                    right = this.x + this.width;
                }
                if ((this.t_collisionRectY + this.t_collisionRectHeight) > this.height) {
                    bottom = this.y + this.height;
                }

                // similarly for the other Sprite
                if (s.t_collisionRectX < 0) {
                    otherLeft = s.x;
                }
                if (s.t_collisionRectY < 0) {
                    otherTop = s.y;
                }
                if ((s.t_collisionRectX + s.t_collisionRectWidth) > s.width) {
                    otherRight = s.x + s.width;
                }
                if ((s.t_collisionRectY + s.t_collisionRectHeight) > s.height) {
                    otherBottom = s.y + s.height;
                }

                // recheck if the updated collision area rectangles intersect
                if (!intersectRect(otherLeft, otherTop, otherRight,
                        otherBottom, left, top, right, bottom)) {

                    // if they don't intersect, return false;
                    return false;
                }

                // the updated collision rectangles intersect,
                // go ahead with collision detection

                // find intersecting region,
                // within the collision rectangles
                int intersectLeft = (left < otherLeft) ? otherLeft : left;
                int intersectTop = (top < otherTop) ? otherTop : top;

                // used once, optimize.
                int intersectRight = (right < otherRight) ? right : otherRight;
                int intersectBottom = (bottom < otherBottom) ? bottom
                        : otherBottom;

                int intersectWidth = Math.abs(intersectRight - intersectLeft);
                int intersectHeight = Math.abs(intersectBottom - intersectTop);

                // have the coordinates in painter space,
                // need coordinates of top left and width, height
                // in source image of Sprite.

                int thisImageXOffset = getImageTopLeftX(intersectLeft,
                        intersectTop, intersectRight, intersectBottom);

                int thisImageYOffset = getImageTopLeftY(intersectLeft,
                        intersectTop, intersectRight, intersectBottom);

                int otherImageXOffset = s.getImageTopLeftX(intersectLeft,
                        intersectTop, intersectRight, intersectBottom);

                int otherImageYOffset = s.getImageTopLeftY(intersectLeft,
                        intersectTop, intersectRight, intersectBottom);

                // check if opaque pixels intersect.

                return doPixelCollision(thisImageXOffset, thisImageYOffset,
                        otherImageXOffset, otherImageYOffset, this.sourceImage,
                        this.t_currentTransformation, s.sourceImage,
                        s.t_currentTransformation, intersectWidth,
                        intersectHeight);

            } else {
                // collides!
                return true;
            }
        }
        return false;

    }


    public final boolean collidesWith(TileLayer t, boolean pixelLevel) {

        // check if either this Sprite or the TiledLayer is not visible
        if (!(t.visible && this.visible)) {
            return false;
        }

        // dimensions of tiledLayer, cell, and
        // this Sprite's collision rectangle

        // these are package private
        // and can be accessed directly
        int tLx1 = t.x;
        int tLy1 = t.y;
        int tLx2 = tLx1 + t.width;
        int tLy2 = tLy1 + t.height;

        int tW = t.getCellWidth();
        int tH = t.getCellHeight();

        int sx1 = this.x + this.t_collisionRectX;
        int sy1 = this.y + this.t_collisionRectY;
        int sx2 = sx1 + this.t_collisionRectWidth;
        int sy2 = sy1 + this.t_collisionRectHeight;

        // number of cells
        int tNumCols = t.getColumns();
        int tNumRows = t.getRows();

        // temporary loop variables.
        int startCol; // = 0;
        int endCol; // = 0;
        int startRow; // = 0;
        int endRow; // = 0;

        if (!intersectRect(tLx1, tLy1, tLx2, tLy2, sx1, sy1, sx2, sy2)) {
            // if the collision rectangle of the sprite
            // does not intersect with the dimensions of the entire
            // tiled layer
            return false;
        }

        // so there is an intersection

        // note sx1 < sx2, tLx1 < tLx2, sx2 > tLx1 from intersectRect()
        // use <= for comparison as this saves us some
        // computation - the result will be 0
        startCol = (sx1 <= tLx1) ? 0 : (sx1 - tLx1) / tW;
        startRow = (sy1 <= tLy1) ? 0 : (sy1 - tLy1) / tH;
        // since tLx1 < sx2 < tLx2, the computation will yield
        // a result between 0 and tNumCols - 1
        // subtract by 1 because sx2,sy2 represent
        // the enclosing bounds of the sprite, not the
        // locations in the coordinate system.
        endCol = (sx2 < tLx2) ? ((sx2 - 1 - tLx1) / tW) : tNumCols - 1;
        endRow = (sy2 < tLy2) ? ((sy2 - 1 - tLy1) / tH) : tNumRows - 1;

        if (!pixelLevel) {
            // check for intersection with a non-empty cell,
            for (int row = startRow; row <= endRow; row++) {
                for (int col = startCol; col <= endCol; col++) {
                    if (t.getCell(col, row) != 0) {
                        return true;
                    }
                }
            }
            // worst case! we scanned through entire
            // overlapping region and
            // all the cells are empty!
            return false;
        } else {
            // do pixel level

            // we need to check pixel level collision detection.
            // use only the coordinates within the Sprite frame if
            // the collision rectangle is larger than the Sprite
            // frame
            if (this.t_collisionRectX < 0) {
                sx1 = this.x;
            }
            if (this.t_collisionRectY < 0) {
                sy1 = this.y;
            }
            if ((this.t_collisionRectX + this.t_collisionRectWidth) > this.width) {
                sx2 = this.x + this.width;
            }
            if ((this.t_collisionRectY + this.t_collisionRectHeight) > this.height) {
                sy2 = this.y + this.height;
            }

            if (!intersectRect(tLx1, tLy1, tLx2, tLy2, sx1, sy1, sx2, sy2)) {
                return (false);
            }

            // we have an intersection between the Sprite and
            // one or more cells of the tiledlayer

            // note sx1 < sx2, tLx1 < tLx2, sx2 > tLx1 from intersectRect()
            // use <= for comparison as this saves us some
            // computation - the result will be 0
            startCol = (sx1 <= tLx1) ? 0 : (sx1 - tLx1) / tW;
            startRow = (sy1 <= tLy1) ? 0 : (sy1 - tLy1) / tH;
            // since tLx1 < sx2 < tLx2, the computation will yield
            // a result between 0 and tNumCols - 1
            // subtract by 1 because sx2,sy2 represent
            // the enclosing bounds of the sprite, not the
            // locations in the coordinate system.
            endCol = (sx2 < tLx2) ? ((sx2 - 1 - tLx1) / tW) : tNumCols - 1;
            endRow = (sy2 < tLy2) ? ((sy2 - 1 - tLy1) / tH) : tNumRows - 1;

            // current cell coordinates
            int cellTop = startRow * tH + tLy1;
            int cellBottom = cellTop + tH;

            // the index of the current tile.
            int tileIndex; // = 0;

            for (int row = startRow; row <= endRow; row++, cellTop += tH, cellBottom += tH) {

                // current cell coordinates
                int cellLeft = startCol * tW + tLx1;
                int cellRight = cellLeft + tW;

                for (int col = startCol; col <= endCol; col++, cellLeft += tW, cellRight += tW) {

                    tileIndex = t.getCell(col, row);

                    if (tileIndex != 0) {

                        // current cell/sprite intersection coordinates
                        // in painter coordinate system.
                        // find intersecting region,
                        int intersectLeft = (sx1 < cellLeft) ? cellLeft : sx1;
                        int intersectTop = (sy1 < cellTop) ? cellTop : sy1;

                        // used once, optimize.
                        int intersectRight = (sx2 < cellRight) ? sx2
                                : cellRight;
                        int intersectBottom = (sy2 < cellBottom) ? sy2
                                : cellBottom;

                        if (intersectLeft > intersectRight) {
                            int temp = intersectRight;
                            intersectRight = intersectLeft;
                            intersectLeft = temp;
                        }

                        if (intersectTop > intersectBottom) {
                            int temp = intersectBottom;
                            intersectBottom = intersectTop;
                            intersectTop = temp;
                        }

                        int intersectWidth = intersectRight - intersectLeft;
                        int intersectHeight = intersectBottom - intersectTop;

                        int image1XOffset = getImageTopLeftX(intersectLeft,
                                intersectTop, intersectRight, intersectBottom);

                        int image1YOffset = getImageTopLeftY(intersectLeft,
                                intersectTop, intersectRight, intersectBottom);

                        int image2XOffset = t.tileSetX[tileIndex]
                                + (intersectLeft - cellLeft);
                        int image2YOffset = t.tileSetY[tileIndex]
                                + (intersectTop - cellTop);

                        if (doPixelCollision(image1XOffset, image1YOffset,
                                image2XOffset, image2YOffset, this.sourceImage,
                                this.t_currentTransformation, t.sourceImage,
                                TRANS_NONE, intersectWidth, intersectHeight)) {
                            // intersection found with this tile
                            return true;
                        }
                    }
                } // end of for col
            }// end of for row

            // worst case! we scanned through entire
            // overlapping region and
            // no pixels collide!
            return false;
        }

    }


    public final boolean collidesWith(Bitmap image, int x, int y,
                                      boolean pixelLevel) {

        // check if this Sprite is not visible
        if (!(this.visible)) {
            return false;
        }

        // if image is null
        // image.getWidth() will throw NullPointerException
        int otherLeft = x;
        int otherTop = y;
        int otherRight = x + image.getWidth();
        int otherBottom = y + image.getHeight();

        int left = this.x + this.t_collisionRectX;
        int top = this.y + this.t_collisionRectY;
        int right = left + this.t_collisionRectWidth;
        int bottom = top + this.t_collisionRectHeight;

        // first check if the collision rectangles of the two sprites intersect
        if (intersectRect(otherLeft, otherTop, otherRight, otherBottom, left,
                top, right, bottom)) {

            // collision rectangles intersect
            if (pixelLevel) {

                // find intersecting region,

                // we need to check pixel level collision detection.
                // use only the coordinates within the Sprite frame if
                // the collision rectangle is larger than the Sprite
                // frame
                if (this.t_collisionRectX < 0) {
                    left = this.x;
                }
                if (this.t_collisionRectY < 0) {
                    top = this.y;
                }
                if ((this.t_collisionRectX + this.t_collisionRectWidth) > this.width) {
                    right = this.x + this.width;
                }
                if ((this.t_collisionRectY + this.t_collisionRectHeight) > this.height) {
                    bottom = this.y + this.height;
                }

                // recheck if the updated collision area rectangles intersect
                if (!intersectRect(otherLeft, otherTop, otherRight,
                        otherBottom, left, top, right, bottom)) {

                    // if they don't intersect, return false;
                    return false;
                }

                // within the collision rectangles
                int intersectLeft = (left < otherLeft) ? otherLeft : left;
                int intersectTop = (top < otherTop) ? otherTop : top;

                // used once, optimize.
                int intersectRight = (right < otherRight) ? right : otherRight;
                int intersectBottom = (bottom < otherBottom) ? bottom
                        : otherBottom;

                int intersectWidth = Math.abs(intersectRight - intersectLeft);
                int intersectHeight = Math.abs(intersectBottom - intersectTop);

                // have the coordinates in painter space,
                // need coordinates of top left and width, height
                // in source image of Sprite.

                int thisImageXOffset = getImageTopLeftX(intersectLeft,
                        intersectTop, intersectRight, intersectBottom);

                int thisImageYOffset = getImageTopLeftY(intersectLeft,
                        intersectTop, intersectRight, intersectBottom);

                int otherImageXOffset = intersectLeft - x;
                int otherImageYOffset = intersectTop - y;

                // check if opaque pixels intersect.
                return doPixelCollision(thisImageXOffset, thisImageYOffset,
                        otherImageXOffset, otherImageYOffset, this.sourceImage,
                        this.t_currentTransformation, image, SpriteLayer.TRANS_NONE,
                        intersectWidth, intersectHeight);

            } else {
                // collides!
                return true;
            }
        }
        return false;

    }


    private void initializeFrames(Bitmap image, int fWidth, int fHeight,
                                  boolean maintainCurFrame) {

        int imageW = image.getWidth();
        int imageH = image.getHeight();

        int numHorizontalFrames = imageW / fWidth;
        int numVerticalFrames = imageH / fHeight;

        sourceImage = image;

        srcFrameWidth = fWidth;
        srcFrameHeight = fHeight;

        numberFrames = numHorizontalFrames * numVerticalFrames;

        frameCoordsX = new int[numberFrames];
        frameCoordsY = new int[numberFrames];

        if (!maintainCurFrame) {
            sequenceIndex = 0;
        }

        if (!customSequenceDefined) {
            frameSequence = new int[numberFrames];
        }

        int currentFrame = 0;

        for (int yy = 0; yy < imageH; yy += fHeight) {
            for (int xx = 0; xx < imageW; xx += fWidth) {

                frameCoordsX[currentFrame] = xx;
                frameCoordsY[currentFrame] = yy;

                if (!customSequenceDefined) {
                    frameSequence[currentFrame] = currentFrame;
                }
                currentFrame++;

            }
        }
    }

    /**
     * initialize the collision rectangle
     */
    private void initCollisionRectBounds() {

        // reset x and y of collision rectangle
        collisionRectX = 0;
        collisionRectY = 0;

        // intialize the collision rectangle bounds to that of the sprite
        collisionRectWidth = this.width;
        collisionRectHeight = this.height;

    }

    /**
     * Detect rectangle intersection
     *
     * @param r1x1
     *            left co-ordinate of first rectangle
     * @param r1y1
     *            top co-ordinate of first rectangle
     * @param r1x2
     *            right co-ordinate of first rectangle
     * @param r1y2
     *            bottom co-ordinate of first rectangle
     * @param r2x1
     *            left co-ordinate of second rectangle
     * @param r2y1
     *            top co-ordinate of second rectangle
     * @param r2x2
     *            right co-ordinate of second rectangle
     * @param r2y2
     *            bottom co-ordinate of second rectangle
     * @return True if there is rectangle intersection
     */
    private boolean intersectRect(int r1x1, int r1y1, int r1x2, int r1y2,
                                  int r2x1, int r2y1, int r2x2, int r2y2) {
        if (r2x1 >= r1x2 || r2y1 >= r1y2 || r2x2 <= r1x1 || r2y2 <= r1y1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Detect opaque pixel intersection between regions of two images
     *
     * @param image1XOffset
     *            left coordinate in the first image
     * @param image1YOffset
     *            top coordinate in the first image
     * @param image2XOffset
     *            left coordinate in the second image
     * @param image2YOffset
     *            top coordinate in the second image
     * @param image1
     *            first source image
     * @param transform1
     *            The transform for the first image
     * @param image2
     *            second source image
     * @param transform2
     *            transform set on the second image
     * @param width
     *            width of overlapping region, when transformed
     * @param height
     *            height of overlapping region, when transformed
     *
     *            Clarification required on parameters: XOffset and YOffset are
     *            the offsets from the top left hand corner of the image. width,
     *            height is the dimensions of the intersecting regions in the
     *            two transformed images. there fore appropriate conversions
     *            have to be made on these dimensions when using the values,
     *            according to the transformation that has been set.
     *
     * @return True if there is a pixel level collision
     */
    private static boolean doPixelCollision(int image1XOffset,
                                            int image1YOffset, int image2XOffset, int image2YOffset,
                                            Bitmap image1, int transform1, Bitmap image2, int transform2,
                                            int width, int height) {

        // starting point of comparison
        int startY1;
        // x and y increments
        int xIncr1, yIncr1;

        // .. for image 2
        int startY2;
        int xIncr2, yIncr2;

        int numPixels = height * width;

        int[] argbData1 = new int[numPixels];
        int[] argbData2 = new int[numPixels];

        if (0x0 != (transform1 & INVERTED_AXES)) {
            // inverted axes

            // scanlength = height

            if (0x0 != (transform1 & Y_FLIP)) {
                xIncr1 = -(height); // - scanlength

                startY1 = numPixels - height; // numPixels - scanlength
            } else {
                xIncr1 = height; // + scanlength

                startY1 = 0;
            }

            if (0x0 != (transform1 & X_FLIP)) {
                yIncr1 = -1;

                startY1 += (height - 1);
            } else {
                yIncr1 = +1;
            }

            image1.getPixels(argbData1, 0, height, // scanlength = height
                    image1XOffset, image1YOffset, height, width);

        } else {

            // scanlength = width

            if (0x0 != (transform1 & Y_FLIP)) {

                startY1 = numPixels - width; // numPixels - scanlength

                yIncr1 = -(width); // - scanlength
            } else {
                startY1 = 0;

                yIncr1 = width; // + scanlength
            }

            if (0x0 != (transform1 & X_FLIP)) {
                xIncr1 = -1;

                startY1 += (width - 1);
            } else {
                xIncr1 = +1;
            }

            image1.getPixels(argbData1, 0, width, // scanlength = width
                    image1XOffset, image1YOffset, width, height);

        }

        if (0x0 != (transform2 & INVERTED_AXES)) {
            // inverted axes

            if (0x0 != (transform2 & Y_FLIP)) {
                xIncr2 = -(height);

                startY2 = numPixels - height;
            } else {
                xIncr2 = height;

                startY2 = 0;
            }

            if (0x0 != (transform2 & X_FLIP)) {
                yIncr2 = -1;

                startY2 += height - 1;
            } else {
                yIncr2 = +1;
            }

            image2.getPixels(argbData2, 0, height, image2XOffset,
                    image2YOffset, height, width);

        } else {

            if (0x0 != (transform2 & Y_FLIP)) {
                startY2 = numPixels - width;

                yIncr2 = -(width);
            } else {
                startY2 = 0;

                yIncr2 = +width;
            }

            if (0x0 != (transform2 & X_FLIP)) {
                xIncr2 = -1;

                startY2 += (width - 1);
            } else {
                xIncr2 = +1;
            }

            image2.getPixels(argbData2, 0, width, image2XOffset, image2YOffset,
                    width, height);

        }

        int x1, x2;
        int xLocalBegin1, xLocalBegin2;

        // the loop counters
        int numIterRows;
        int numIterColumns;

        for (numIterRows = 0, xLocalBegin1 = startY1, xLocalBegin2 = startY2; numIterRows < height; xLocalBegin1 += yIncr1, xLocalBegin2 += yIncr2, numIterRows++) {

            for (numIterColumns = 0, x1 = xLocalBegin1, x2 = xLocalBegin2; numIterColumns < width; x1 += xIncr1, x2 += xIncr2, numIterColumns++) {

                if (((argbData1[x1] & ALPHA_BITMASK) != 0)
                        && ((argbData2[x2] & ALPHA_BITMASK) != 0)) {

                    return true;
                }

            } // end for x

        } // end for y

        // worst case! couldn't find a single colliding pixel!
        return false;
    }

    /**
     * Given a rectangle that lies within the sprite in the painter's
     * coordinates, find the X coordinate of the top left corner in the source
     * image of the sprite
     *
     * @param x1
     *            the x coordinate of the top left of the rectangle
     * @param y1
     *            the y coordinate of the top left of the rectangle
     * @param x2
     *            the x coordinate of the bottom right of the rectangle
     * @param y2
     *            the y coordinate of the bottom right of the rectangle
     *
     * @return the X coordinate in the source image
     *
     */
    private int getImageTopLeftX(int x1, int y1, int x2, int y2) {
        int retX = 0;

        // left = this.x
        // right = this.x + this.width
        // top = this.y
        // bottom = this.y + this.height

        switch (this.t_currentTransformation) {

            case TRANS_NONE:
            case TRANS_MIRROR_ROT180:
                retX = x1 - this.x;
                break;

            case TRANS_MIRROR:
            case TRANS_ROT180:
                retX = (this.x + this.width) - x2;
                break;

            case TRANS_ROT90:
            case TRANS_MIRROR_ROT270:
                retX = y1 - this.y;
                break;

            case TRANS_ROT270:
            case TRANS_MIRROR_ROT90:
                retX = (this.y + this.height) - y2;
                break;
        }

        retX += frameCoordsX[frameSequence[sequenceIndex]];

        return retX;
    }

    /**
     * Given a rectangle that lies within the sprite in the painter's
     * coordinates, find the Y coordinate of the top left corner in the source
     * image of the sprite
     *
     * @param x1
     *            the x coordinate of the top left of the rectangle
     * @param y1
     *            the y coordinate of the top left of the rectangle
     * @param x2
     *            the x coordinate of the bottom right of the rectangle
     * @param y2
     *            the y coordinate of the bottom right of the rectangle
     *
     * @return the Y coordinate in the source image
     *
     */
    private int getImageTopLeftY(int x1, int y1, int x2, int y2) {
        int retY = 0;

        // left = this.x
        // right = this.x + this.width
        // top = this.y
        // bottom = this.y + this.height

        switch (this.t_currentTransformation) {

            case TRANS_NONE:
            case TRANS_MIRROR:
                retY = y1 - this.y;
                break;

            case TRANS_ROT180:
            case TRANS_MIRROR_ROT180:
                retY = (this.y + this.height) - y2;
                break;

            case TRANS_ROT270:
            case TRANS_MIRROR_ROT270:
                retY = x1 - this.x;
                break;

            case TRANS_ROT90:
            case TRANS_MIRROR_ROT90:
                retY = (this.x + this.width) - x2;
                break;
        }

        retY += frameCoordsY[frameSequence[sequenceIndex]];

        return retY;
    }

    /**
     * Sets the transform for this Sprite
     *
     * @param transform
     *            the desired transform for this Sprite
     */
    private void setTransformImpl(int transform) {

        // ---

        // setTransform sets up all transformation related data structures
        // except transforming the current frame's bitmap.

        // x, y, width, height, dRefX, dRefY,
        // collisionRectX, collisionRectY, collisionRectWidth,
        // collisionRectHeight, t_currentTransformation,
        // t_bufferImage

        // The actual tranformed frame is drawn at paint time.

        // ---

        // update top-left corner position
        this.x = this.x
                + getTransformedPtX(dRefX, dRefY, this.t_currentTransformation)
                - getTransformedPtX(dRefX, dRefY, transform);

        this.y = this.y
                + getTransformedPtY(dRefX, dRefY, this.t_currentTransformation)
                - getTransformedPtY(dRefX, dRefY, transform);

        // Calculate transformed sprites collision rectangle
        // and transformed width and height
        computeTransformedBounds(transform);

        // set the current transform to be the one requested
        t_currentTransformation = transform;

    }

    /**
     * Calculate transformed sprites collision rectangle and transformed width
     * and height
     *
     * @param transform
     *            the desired transform for this <code>Sprite</code>
     */
    private void computeTransformedBounds(int transform) {
        switch (transform) {

            case TRANS_NONE:

                t_collisionRectX = collisionRectX;
                t_collisionRectY = collisionRectY;
                t_collisionRectWidth = collisionRectWidth;
                t_collisionRectHeight = collisionRectHeight;
                this.width = srcFrameWidth;
                this.height = srcFrameHeight;

                break;

            case TRANS_MIRROR:

                // flip across vertical

                // NOTE: top left x and y coordinate must reflect the transformation
                // performed around the reference point

                // the X-offset of the reference point from the top left corner
                // changes.
                t_collisionRectX = srcFrameWidth
                        - (collisionRectX + collisionRectWidth);

                t_collisionRectY = collisionRectY;
                t_collisionRectWidth = collisionRectWidth;
                t_collisionRectHeight = collisionRectHeight;

                // the Y-offset of the reference point from the top left corner
                // remains the same,
                // top left X-co-ordinate changes

                this.width = srcFrameWidth;
                this.height = srcFrameHeight;

                break;

            case TRANS_MIRROR_ROT180:

                // flip across horizontal

                // NOTE: top left x and y coordinate must reflect the transformation
                // performed around the reference point

                // the Y-offset of the reference point from the top left corner
                // changes
                t_collisionRectY = srcFrameHeight
                        - (collisionRectY + collisionRectHeight);

                t_collisionRectX = collisionRectX;
                t_collisionRectWidth = collisionRectWidth;
                t_collisionRectHeight = collisionRectHeight;

                // width and height are as before
                this.width = srcFrameWidth;
                this.height = srcFrameHeight;

                // the X-offset of the reference point from the top left corner
                // remains the same.
                // top left Y-co-ordinate changes

                break;

            case TRANS_ROT90:

                // NOTE: top left x and y coordinate must reflect the transformation
                // performed around the reference point

                // the bottom-left corner of the rectangle becomes the
                // top-left when rotated 90.

                // both X- and Y-offset to the top left corner may change

                // update the position information for the collision rectangle

                t_collisionRectX = srcFrameHeight
                        - (collisionRectHeight + collisionRectY);
                t_collisionRectY = collisionRectX;

                t_collisionRectHeight = collisionRectWidth;
                t_collisionRectWidth = collisionRectHeight;

                // set width and height
                this.width = srcFrameHeight;
                this.height = srcFrameWidth;

                break;

            case TRANS_ROT180:

                // NOTE: top left x and y coordinate must reflect the transformation
                // performed around the reference point

                // width and height are as before

                // both X- and Y- offsets from the top left corner may change

                t_collisionRectX = srcFrameWidth
                        - (collisionRectWidth + collisionRectX);
                t_collisionRectY = srcFrameHeight
                        - (collisionRectHeight + collisionRectY);

                t_collisionRectWidth = collisionRectWidth;
                t_collisionRectHeight = collisionRectHeight;

                // set width and height
                this.width = srcFrameWidth;
                this.height = srcFrameHeight;

                break;

            case TRANS_ROT270:

                // the top-right corner of the rectangle becomes the
                // top-left when rotated 270.

                // both X- and Y-offset to the top left corner may change

                // update the position information for the collision rectangle

                t_collisionRectX = collisionRectY;
                t_collisionRectY = srcFrameWidth
                        - (collisionRectWidth + collisionRectX);

                t_collisionRectHeight = collisionRectWidth;
                t_collisionRectWidth = collisionRectHeight;

                // set width and height
                this.width = srcFrameHeight;
                this.height = srcFrameWidth;

                break;

            case TRANS_MIRROR_ROT90:

                // both X- and Y- offset from the top left corner may change

                // update the position information for the collision rectangle

                t_collisionRectX = srcFrameHeight
                        - (collisionRectHeight + collisionRectY);
                t_collisionRectY = srcFrameWidth
                        - (collisionRectWidth + collisionRectX);

                t_collisionRectHeight = collisionRectWidth;
                t_collisionRectWidth = collisionRectHeight;

                // set width and height
                this.width = srcFrameHeight;
                this.height = srcFrameWidth;

                break;

            case TRANS_MIRROR_ROT270:

                // both X- and Y- offset from the top left corner may change

                // update the position information for the collision rectangle

                t_collisionRectY = collisionRectX;
                t_collisionRectX = collisionRectY;

                t_collisionRectHeight = collisionRectWidth;
                t_collisionRectWidth = collisionRectHeight;

                // set width and height
                this.width = srcFrameHeight;
                this.height = srcFrameWidth;

                break;

            default:
                // INVALID TRANSFORMATION!
                throw new IllegalArgumentException();

        }
    }

    /**
     * Given the x and y offsets off a pixel from the top left corner, in an
     * untransformed sprite, calculates the x coordinate of the pixel when the
     * same sprite is transformed, with the coordinates of the top-left pixel of
     * the transformed sprite as (0,0).
     *
     * @param x
     *            Horizontal offset within the untransformed sprite
     * @param y
     *            Vertical offset within the untransformed sprite
     * @param transform
     *            transform for the sprite
     * @return The x-offset, of the coordinates of the pixel, with the top-left
     *         corner as 0 when transformed.
     */
    int getTransformedPtX(int x, int y, int transform) {

        int t_x = 0;

        switch (transform) {

            case TRANS_NONE:
                t_x = x;
                break;
            case TRANS_MIRROR:
                t_x = srcFrameWidth - x - 1;
                break;
            case TRANS_MIRROR_ROT180:
                t_x = x;
                break;
            case TRANS_ROT90:
                t_x = srcFrameHeight - y - 1;
                break;
            case TRANS_ROT180:
                t_x = srcFrameWidth - x - 1;
                break;
            case TRANS_ROT270:
                t_x = y;
                break;
            case TRANS_MIRROR_ROT90:
                t_x = srcFrameHeight - y - 1;
                break;
            case TRANS_MIRROR_ROT270:
                t_x = y;
                break;
            default:
                // INVALID TRANSFORMATION!
                throw new IllegalArgumentException();

        }

        return t_x;

    }

    /**
     * Given the x and y offsets off a pixel from the top left corner, in an
     * untransformed sprite, calculates the y coordinate of the pixel when the
     * same sprite is transformed, with the coordinates of the top-left pixel of
     * the transformed sprite as (0,0).
     *
     * @param x
     *            Horizontal offset within the untransformed sprite
     * @param y
     *            Vertical offset within the untransformed sprite
     * @param transform
     *            transform for the sprite
     * @return The y-offset, of the coordinates of the pixel, with the top-left
     *         corner as 0 when transformed.
     */
    int getTransformedPtY(int x, int y, int transform) {

        int t_y = 0;

        switch (transform) {

            case TRANS_NONE:
                t_y = y;
                break;
            case TRANS_MIRROR:
                t_y = y;
                break;
            case TRANS_MIRROR_ROT180:
                t_y = srcFrameHeight - y - 1;
                break;
            case TRANS_ROT90:
                t_y = x;
                break;
            case TRANS_ROT180:
                t_y = srcFrameHeight - y - 1;
                break;
            case TRANS_ROT270:
                t_y = srcFrameWidth - x - 1;
                break;
            case TRANS_MIRROR_ROT90:
                t_y = srcFrameWidth - x - 1;
                break;
            case TRANS_MIRROR_ROT270:
                t_y = x;
                break;
            default:
                // INVALID TRANSFORMATION!
                throw new IllegalArgumentException();
        }

        return t_y;

    }

}
