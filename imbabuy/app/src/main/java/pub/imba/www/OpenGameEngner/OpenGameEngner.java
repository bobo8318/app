package pub.imba.www.OpenGameEngner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by My on 2017/10/12.
 */
public class OpenGameEngner {

    private List<Bitmap> tile_map = new ArrayList<Bitmap>();//显示素材
    private List<Integer> map_matrix =  new ArrayList<Integer>();//地图矩阵

    private int width;
    private int height;

    private int tile_width;
    private int tile_height;

    private int offset_x;//矩阵显示偏移量 左右偏移
    private int offset_y;//矩阵显示偏移量 上下偏移

    private int show_x_tile = 1;//屏幕显示块数量
    private int show_y_tile = 1;//屏幕显示块数量

    //显示内容
    private BackGround backGround;//背景层 绘制背景矩阵
    private Decotator[] decotators;//装饰层 绘制装饰 有碰撞体积

    private Sprit[] sprits;//精灵层 绘制精灵

    public OpenGameEngner(int width, int height){
        this.width = width;
        this.height = height;
    }


    //控制内容

    //音频内容

    public BackGround getBackGround(){
        if( backGround == null){
            backGround = new BackGround();

            backGround.setWidth(this.width);
            backGround.setHeight(this.height);

        }
        return backGround;
    }

    private void init(){

    }
    //根据初始平铺背景取得
    private int[] getTileSize(int x, int y){
        int[] size = new int[2];
        size[0] = width/x;
        size[1] = height/y;
        return size;
    }
    //绘制背景 生成地图矩阵

    //绘制装饰 在地图矩阵中
    private void addDecotator(){

    }

    //绘制精灵 在地图矩阵中
    private void addSprit(){

    }
    //根据地图矩阵 生成画面
    public void draw(Canvas canvas,Paint paint){
        if(backGround != null)
            backGround.adaptBitmap().draw(canvas,paint);
    }


    /**
     * 初始化一张矩阵地图
     */
    public void buildMap(){
        show_x_tile = width/this.tile_width;
        show_y_tile = height/this.tile_height;
        for(int i=0;i<show_x_tile*show_y_tile;i++){
            map_matrix.add(i,0);//0 表示空地图
        }

    }

    public void setTileSize(int width, int height) {
        this.tile_width = width;
        this.tile_height = height;
    }

}
