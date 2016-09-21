package view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/9/20.
 */

public class AutoLoadRecyclerview extends RecyclerView {
     OnLoadListener mLoadListener;
     boolean isloading=false;
    boolean isValidDelay = true;
    public AutoLoadRecyclerview(Context context) {
        super(context);
    }

    public AutoLoadRecyclerview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadRecyclerview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(isInEditMode()){
            return;
        }
        init();
    }
    private void init(){
      setOnScrollListener(new OnScrollListener() {
          @Override
          public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
              checkLoadMore(dx,dy);
          }
      });
    }
    private void checkLoadMore(int dx,int dy){
        if(isBottom(dx,dy)&&
                isloading&&
                isValidDelay&&
                mLoadListener!=null){
           postDelayed(new Runnable() {
               @Override
               public void run() {
               isValidDelay=true;
               }
           },1000);
        }
    }
    private boolean isBottom(int dx,int dy){
        LinearLayoutManager layoutManager= (LinearLayoutManager) getLayoutManager();
        int lastVisbleItem=layoutManager.findLastVisibleItemPosition();
        int totleCountItem=layoutManager.getItemCount();
        return lastVisbleItem>=totleCountItem-4&&dy>0;
    }
    public void setLoadListener(OnLoadListener listener){
        this.mLoadListener=listener;
    }
    public void setLoading(boolean loading){
        this.isloading=loading;
    }
    public static interface OnLoadListener{
      void  onLoad();
    }
}
