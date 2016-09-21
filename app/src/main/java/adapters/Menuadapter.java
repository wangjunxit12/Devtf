package adapters;

import android.support.v7.widget.RecyclerView;
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
public  class Menuadapter extends RecyclerView.Adapter<Menuadapter.MenuViewHolder> {
    List<MenuItem> mDataSet=new ArrayList<>();
    OnItemClickListener<MenuItem> mItemClickListener;

    public Menuadapter(List<MenuItem> mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MenuViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.menu_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        final MenuItem item=mDataSet.get(position);
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
    protected static class MenuViewHolder extends RecyclerView.ViewHolder{
        public ImageView userImageView;
        public TextView nameTextView;
        public MenuViewHolder(View itemView) {
            super(itemView);
            userImageView = (ImageView) itemView.findViewById(R.id.main_user_icon);
            nameTextView = (TextView) itemView.findViewById(R.id.main_username);
        }

    }
}
