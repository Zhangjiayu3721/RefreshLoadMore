package zjy.com.zhoukaotwo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import zjy.com.zhoukaotwo.R;
import zjy.com.zhoukaotwo.bean.MyBean;

/**
 * Created by ZhangJiaYu on 2017/11/11.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    List<MyBean.DataBean> list = new ArrayList<>();
    Context context;

    public MyAdapter(List<MyBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    OnRecyclerViewItemClickListener onItemClickListener;
    OnRecyclerViewItemLongClickListener onItemLongClickListener;
    public interface OnRecyclerViewItemClickListener{
        void OnItemClick(View view,int position);
    }

    public interface OnRecyclerViewItemLongClickListener{
        void onItemLongClickListener(View view,int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void addData(List<MyBean.DataBean> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = null;
        View view = View.inflate(context, R.layout.activity_item,null);
        holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv.setText(list.get(position).getTitle());
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(list.get(position).getImg())
                .setOldController(holder.img.getController())
                .setTapToRetryEnabled(true)
                .build();
        holder.img.setController(controller);
        if(onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClick(holder.itemView,position);
                }
            });
        }
        if(onItemLongClickListener!=null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemLongClickListener.onItemLongClickListener(holder.itemView,position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView img;
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tv = itemView.findViewById(R.id.tv);
        }
    }
}
