package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import listener.OnItemClickListener;

/**
 * Created by Administrator on 2016/9/28.
 */

public abstract class RecyclerBaeAdapter<D,V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    private  final List<D> mDataSet=new ArrayList<>();
    private OnItemClickListener<D> itemClickListener;
    @Override
    public V onCreateViewHolder(ViewGroup parent, int layoutId) {
        return null;

    }

    @Override
    public void onBindViewHolder(V holder, int position) {
           final  D item=getItem(position);
           bindDataToItemView(holder,item);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
    protected  abstract void bindDataToItemView(V viewholder,D item);
    public void setOnItemClickListener(OnItemClickListener<D> itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    private  void setupItemViewClickListener(V viewholder,final D item){
        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener!=null){
                    itemClickListener.OnClick(item);
                }
            }
        });
    }
    protected D getItem(int postion){
        return  mDataSet.get(postion);
    }
    public void addItems(List<D> items){
        items.removeAll(mDataSet);
        mDataSet.addAll(items);
        notifyDataSetChanged();
    }
}
