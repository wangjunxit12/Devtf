package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devtf.devtf.R;

import bean.MenuItem;

/**
 * Created by Administrator on 2016/9/14.
 */
public  class Menuadapter extends RecyclerBaeAdapter<MenuItem, Menuadapter.MenuViewHolder> {

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int layoutId) {
        return new MenuViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.menu_item,parent,false))
                ;
    }

    @Override
    protected void bindDataToItemView(MenuViewHolder viewholder, MenuItem item) {
        viewholder.nameTextView.setText(item.getName());
        viewholder.userImageView.setImageResource(item.getResId());
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
