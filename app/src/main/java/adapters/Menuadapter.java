package adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devtf.devtf.R;

import java.util.ArrayList;
import java.util.List;

import bean.MenuItem;
import listener.OnItemClickListener;

/**
 * Created by Administrator on 2016/9/14.
 */
public  class Menuadapter extends Adapter<Menuadapter.MenuViewHolder> {
    List<MenuItem> mDataSet=new ArrayList<>();
    OnItemClickListener<MenuItem> mItemClickListener;
    private static final  String TAG="Menuadapter";
    public Menuadapter(List<MenuItem> mDataSet) {
        this.mDataSet = mDataSet;
        Log.i(TAG, "Menuadapter: "+mDataSet.size());
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MenuViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.menu_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        final MenuItem item=mDataSet.get(position);
        Log.i(TAG, "Menuadapter: "+item.getName());
        holder.nameTextView.setText(item.getName());
        holder.userImageView.setImageResource(item.getResId());
        setupItemViewClickListener(holder,item);
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

   public void setOnItemClickListener(OnItemClickListener<MenuItem> mItemClickListener){
       this.mItemClickListener=mItemClickListener;
   }
   protected void setupItemViewClickListener(MenuViewHolder viewHolder,final MenuItem itme){
       viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mItemClickListener!=null){
                   mItemClickListener.OnClick(itme);
               }
           }
       });
   }
   protected  MenuItem getItem(int postion){
       return mDataSet.get(postion);
   }
     static class MenuViewHolder extends RecyclerView.ViewHolder{
        public ImageView userImageView;
        public TextView nameTextView;
        public MenuViewHolder(View itemView) {
            super(itemView);
            userImageView = (ImageView) itemView.findViewById(R.id.menu_icon_imageview);
            nameTextView = (TextView) itemView.findViewById(R.id.menu_text_tv);
        }

    }
}
