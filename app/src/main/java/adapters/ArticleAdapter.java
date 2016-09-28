package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devtf.devtf.R;

import java.util.ArrayList;
import java.util.List;

import bean.Article;
import listener.OnItemClickListener;

/**
 * Created by Administrator on 2016/9/20.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
private List<Article> mDataSet=new ArrayList<>();
    private OnItemClickListener<Article> itemClickListener;

    public ArticleAdapter(List<Article> mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ArticleViewHolder( LayoutInflater.from(parent.getContext()).inflate(
                R.layout.article_item,parent,false
        ));
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
         final   Article item=getItem(position);
        holder.title.setText(item.title);
        holder.author.setText(item.author);
        holder.time.setText(item.publishTime);
        setupItemViewClickListener(holder,item);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
    protected Article getItem(int postion){
        return mDataSet.get(postion);
    }
    public void setOnItemClickListener(OnItemClickListener<Article> itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    protected void setupItemViewClickListener(ArticleViewHolder holder,
                                              final Article item){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.OnClick(item);
            }
        });

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
