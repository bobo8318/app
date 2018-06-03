package pub.imba.www.OpenGameEngner.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by My on 2017/10/13.
 */
public class TileLayer extends Layer{
    /**
     * the overall height of the TiledLayer grid
     */
    private int cellHeight; // = 0;
    /**
     * the overall cell width of the TiledLayer grid
     */
    private int cellWidth; // = 0;
    /**
     * The num of rows of the TiledLayer grid.
     */
    private int rows; // = 0;
    /**
     * the num of columns in the TiledLayer grid
     */
    private int columns; // = 0;
    /**
     * int array for storing row and column of cell
     *
     * it contains the tile Index for both static and animated tiles
     */
    private int[][] cellMatrix; // = null;
    /**
     * Source image for tiles
     */
    // package access as it is used by Pixel level Collision
    // detection with a Sprite
    Bitmap sourceImage; // = null;
    /**
     * no. of tiles
     */
    private int numberOfTiles; // = 0;
    /**
     * X co-ordinate definitions for individual frames into the source image
     */
    // package access as it is used by Pixel level Collision
    // detection with a Sprite
    int[] tileSetX;
    /**
     * Y co-ordinate definitions for individual frames into the source image
     */
    // package access as it is used by Pixel level Collision
    // detection with a Sprite
    int[] tileSetY;
    /**
     * Table to map from animated Index to static Index 0th location is unused.
     * anim --> static Index -1 --> 21 -2 --> 34 -3 --> 45 for now keep 0 the
     * location of the table empty instead of computing -index make index +ve
     * and access this Table.
     *
     */
    private int[] anim_to_static; // = null;
    /**
     * total number of animated tiles. This variable is also used as index in
     * the above table to add new entries to the anim_to_static table.
     * initialized to 1 when table is created.
     */
    private int numOfAnimTiles; // = 0

    public TileLayer(int columns, int rows, Bitmap image, int tileWidth,
                      int tileHeight) {
        // IllegalArgumentException will be thrown
        // in the Layer super-class constructor
        super(columns < 1 || tileWidth < 1 ? -1 : columns * tileWidth, rows < 1
                || tileHeight < 1 ? -1 : rows * tileHeight);

        // if img is null img.getWidth() will throw NullPointerException
        if (((image.getWidth() % tileWidth) != 0)
                || ((image.getHeight() % tileHeight) != 0)) {
            throw new IllegalArgumentException();
        }
        this.columns = columns;
        this.rows = rows;

        cellMatrix = new int[rows][columns];

        int noOfFrames = (image.getWidth() / tileWidth)
                * (image.getHeight() / tileHeight);
        // the zero th index is left empty for transparent tile
        // so it is passed in createStaticSet as noOfFrames + 1
        // Also maintain static indices is true
        // all elements of cellMatrix[][]
        // are set to zero by new, so maintainIndices = true
        createStaticSet(image, noOfFrames + 1, tileWidth, tileHeight, true);
    }


    public int createAnimatedTile(int staticTileIndex) {
        // checks static tile
        if (staticTileIndex < 0 || staticTileIndex >= numberOfTiles) {
            throw new IndexOutOfBoundsException();
        }

        if (anim_to_static == null) {
            anim_to_static = new int[4];
            numOfAnimTiles = 1;
        } else if (numOfAnimTiles == anim_to_static.length) {
            // grow anim_to_static table if needed
            int new_anim_tbl[] = new int[anim_to_static.length * 2];
            System.arraycopy(anim_to_static, 0, new_anim_tbl, 0,
                    anim_to_static.length);
            anim_to_static = new_anim_tbl;
        }
        anim_to_static[numOfAnimTiles] = staticTileIndex;
        numOfAnimTiles++;
        return (-(numOfAnimTiles - 1));
    }

    public void setAnimatedTile(int animatedTileIndex, int staticTileIndex) {
        // checks static tile
        if (staticTileIndex < 0 || staticTileIndex >= numberOfTiles) {
            throw new IndexOutOfBoundsException();
        }
        // do animated tile index check
        animatedTileIndex = -animatedTileIndex;
        if (anim_to_static == null || animatedTileIndex <= 0
                || animatedTileIndex >= numOfAnimTiles) {
            throw new IndexOutOfBoundsException();
        }

        anim_to_static[animatedTileIndex] = staticTileIndex;

    }

    public int getAnimatedTile(int animatedTileIndex) {
        animatedTileIndex = -animatedTileIndex;
        if (anim_to_static == null || animatedTileIndex <= 0
                || animatedTileIndex >= numOfAnimTiles) {
            throw new IndexOutOfBoundsException();
        }

        return anim_to_static[animatedTileIndex];
    }

    public void setCell(int col, int row, int tileIndex) {

        if (col < 0 || col >= this.columns || row < 0 || row >= this.rows) {
            throw new IndexOutOfBoundsException();
        }

        if (tileIndex > 0) {
            // do checks for static tile
            if (tileIndex >= numberOfTiles) {
                throw new IndexOutOfBoundsException();
            }
        } else if (tileIndex < 0) {
            // do animated tile index check
            if (anim_to_static == null || (-tileIndex) >= numOfAnimTiles) {
                throw new IndexOutOfBoundsException();
            }
        }

        cellMatrix[row][col] = tileIndex;

    }


    public int getCell(int col, int row) {
        if (col < 0 || col >= this.columns || row < 0 || row >= this.rows) {
            throw new IndexOutOfBoundsException();
        }
        return cellMatrix[row][col];
    }

    public void fillCells(int col, int row, int numCols, int numRows,
                          int tileIndex) {

        if (col < 0 || col >= this.columns || row < 0 || row >= this.rows
                || numCols < 0 || col + numCols > this.columns || numRows < 0
                || row + numRows > this.rows) {
            throw new IndexOutOfBoundsException();
        }

        if (tileIndex > 0) {
            // do checks for static tile
            if (tileIndex >= numberOfTiles) {
                throw new IndexOutOfBoundsException();
            }
        } else if (tileIndex < 0) {
            // do animated tile index check
            if (anim_to_static == null || (-tileIndex) >= numOfAnimTiles) {
                throw new IndexOutOfBoundsException();
            }
        }

        for (int rowCount = row; rowCount < row + numRows; rowCount++) {
            for (int columnCount = col; columnCount < col + numCols; columnCount++) {
                cellMatrix[rowCount][columnCount] = tileIndex;
            }
        }
    }

    /**
     * Gets the width of a single cell, in pixels.
     *
     * @return the width in pixels of a single cell in the
     *         <code>TiledLayer</code> grid
     */
    public final int getCellWidth() {
        return cellWidth;
    }

    /**
     * Gets the height of a single cell, in pixels.
     *
     * @return the height in pixels of a single cell in the
     *         <code>TiledLayer</code> grid
     */
    public final int getCellHeight() {
        return cellHeight;
    }

    /**
     * Gets the number of columns in the TiledLayer grid. The overall width of
     * the TiledLayer, in pixels, may be obtained by calling {@link #getWidth}.
     *
     * @return the width in columns of the <code>TiledLayer</code> grid
     */
    public final int getColumns() {
        return columns;
    }

    /**
     * Gets the number of rows in the TiledLayer grid. The overall height of the
     * TiledLayer, in pixels, may be obtained by calling {@link #getHeight}.
     *
     * @return the height in rows of the <code>TiledLayer</code> grid
     */
    public final int getRows() {
        return rows;
    }


    private void createStaticSet(Bitmap image, int noOfFrames, int tileWidth,
                                 int tileHeight, boolean maintainIndices) {

        cellWidth = tileWidth;
        cellHeight = tileHeight;

        int imageW = image.getWidth();
        int imageH = image.getHeight();

        sourceImage = image;

        numberOfTiles = noOfFrames;
        tileSetX = new int[numberOfTiles];
        tileSetY = new int[numberOfTiles];

        if (!maintainIndices) {
            // populate cell matrix, all the indices are 0 to begin with
            for (rows = 0; rows < cellMatrix.length; rows++) {
                int totalCols = cellMatrix[rows].length;
                for (columns = 0; columns < totalCols; columns++) {
                    cellMatrix[rows][columns] = 0;
                }
            }
            // delete animated tiles
            anim_to_static = null;
        }

        int currentTile = 1;

        for (int y = 0; y < imageH; y += tileHeight) {
            for (int x = 0; x < imageW; x += tileWidth) {

                tileSetX[currentTile] = x;
                tileSetY[currentTile] = y;

                currentTile++;
            }
        }
    }

    public final void paint(Canvas canvas) {

        if (canvas == null) {
            throw new NullPointerException();
        }

        if (visible) {
            int tileIndex = 0;

            // y-coordinate
            int ty = this.y;
            for (int row = 0; row < cellMatrix.length; row++, ty += cellHeight) {

                // reset the x-coordinate at the beginning of every row
                // x-coordinate to draw tile into
                int tx = this.x;
                int totalCols = cellMatrix[row].length;

                for (int column = 0; column < totalCols; column++, tx += cellWidth) {

                    tileIndex = cellMatrix[row][column];
                    // check the indices
                    // if animated get the corresponding
                    // static index from anim_to_static table
                    if (tileIndex == 0) { // transparent tile
                        continue;
                    } else if (tileIndex < 0) {
                        tileIndex = getAnimatedTile(tileIndex);
                    }

                    drawImage(canvas, tx, ty, sourceImage, tileSetX[tileIndex],
                            tileSetY[tileIndex], cellWidth, cellHeight);

                }
            }
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

    public void setStaticTileSet(Bitmap image, int tileWidth, int tileHeight) {
        // if img is null img.getWidth() will throw NullPointerException
        if (tileWidth < 1 || tileHeight < 1
                || ((image.getWidth() % tileWidth) != 0)
                || ((image.getHeight() % tileHeight) != 0)) {
            throw new IllegalArgumentException();
        }
        setWidthImpl(columns * tileWidth);
        setHeightImpl(rows * tileHeight);

        int noOfFrames = (image.getWidth() / tileWidth)
                * (image.getHeight() / tileHeight);

        // the zero th index is left empty for transparent tile
        // so it is passed in createStaticSet as noOfFrames + 1

        if (noOfFrames >= (numberOfTiles - 1)) {
            // maintain static indices
            createStaticSet(image, noOfFrames + 1, tileWidth, tileHeight, true);
        } else {
            createStaticSet(image, noOfFrames + 1, tileWidth, tileHeight, false);
        }
    }

}
