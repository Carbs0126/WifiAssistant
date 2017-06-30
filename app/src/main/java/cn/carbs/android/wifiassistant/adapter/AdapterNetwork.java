package cn.carbs.android.wifiassistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.carbs.android.wifiassistant.bean.BeanNetwork;
import cn.carbs.android.wifiassistant.listeners.OnRecyclerViewItemClickListener;
import cn.carbs.android.wifiassistant.view.ViewNetwork;

/**
 * Created by Carbs.Wang on 2016/7/13.
 */
public class AdapterNetwork extends RecyclerView.Adapter<AdapterNetwork.ViewHolder>
        implements View.OnClickListener{

    private Context mContext;
    private List<BeanNetwork> mNetworks;
    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public AdapterNetwork(Context context, List<BeanNetwork> networks) {
        mContext = context;
        mNetworks = networks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = new ViewNetwork(mContext);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        BeanNetwork beanNetwork = mNetworks.get(position);
        viewHolder.itemView.setTag(beanNetwork);
        ((ViewNetwork)viewHolder.itemView).update(beanNetwork);
    }

    @Override
    public int getItemCount(){
        return mNetworks.size();
    }

    public BeanNetwork getItem(int position){
        return mNetworks.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}