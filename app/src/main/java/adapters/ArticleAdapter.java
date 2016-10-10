package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devtf.devtf.R;

import bean.Article;

/**
 * Created by Administrator on 2016/9/20.
 */

public class ArticleAdapter extends RecyclerBaeAdapter<Article , ArticleAdapter.ArticleViewHolder> {


    @Override
    protected void bindDataToItemView(ArticleViewHolder viewholder, Article item) {
        viewholder.title.setText(item.title);
        viewholder.author.setText(item.author);
        viewholder.time.setText(item.publishTime);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int layoutId) {
        return new ArticleViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.article_item,parent,false));
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView author;
        public TextView time;
        public ArticleViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.i_title);
            author= (TextView) itemView.findViewById(R.id.i_author);
            time=(TextView) itemView.findViewById(R.id.i_time);
        }
    }
}
