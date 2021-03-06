package pub.imba.www.imbabuy;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by My on 2017/9/7.
 */
public abstract class EndLessOnScrollListener extends  RecyclerView.OnScrollListener {
    //声明一个LinearLayoutManager
    private LinearLayoutManager mLinearLayoutManager;

    //当前页，从0开始    private int currentPage = 0;
    //已经加载出来的Item的数量
    private int totalItemCount;

    //主要用来存储上一个totalItemCount
    private int previousTotal = 0;

    //在屏幕上可见的item数量
    private int visibleItemCount;

    //在屏幕可见的Item中的第一个
    private int firstVisibleItem;

    //是否正在上拉数据
    private boolean loading = false;

    public EndLessOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        visibleItemCount = recyclerView.getChildCount();
        //totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        totalItemCount = recyclerView.getAdapter().getItemCount();
        if(loading){
           // Log.i("wnwn","firstVisibleItem: " +firstVisibleItem);
            //Log.i("wnwn","totalPageCount:" +totalItemCount);
            Log.i("openui", "visibleItemCount:" + visibleItemCount);

            Log.i("openui","totalPageCount:" +totalItemCount);
            Log.i("openui","previousTotal:" +previousTotal);
            if(totalItemCount > previousTotal){
                //说明数据已经加载结束
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        Log.i("openui", "status:" + loading);
        //这里需要好好理解
        if (!loading && totalItemCount-visibleItemCount <= firstVisibleItem){
            Log.i("openui", "load new data:" );
            onLoadMore();
            loading = true;
        }

    }

    public abstract void onLoadMore();
}
