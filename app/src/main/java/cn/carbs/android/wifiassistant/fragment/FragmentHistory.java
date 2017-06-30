package cn.carbs.android.wifiassistant.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import cn.carbs.android.wifiassistant.R;
import cn.carbs.android.wifiassistant.adapter.AdapterNetwork;
import cn.carbs.android.wifiassistant.bean.BeanNetwork;
import cn.carbs.android.wifiassistant.constant.Constant;
import cn.carbs.android.wifiassistant.event.EventFAB;
import cn.carbs.android.wifiassistant.io.ReadNetworks;
import cn.carbs.android.wifiassistant.listeners.HidingScrollListener;
import cn.carbs.android.wifiassistant.listeners.OnRecyclerViewItemClickListener;
import cn.carbs.android.wifiassistant.util.DisplayUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Carbs.Wang on 2016/7/13.
 */
public class FragmentHistory extends FragmentBase implements OnRecyclerViewItemClickListener{

    private static final String TAG = "history";
    private static final int DEFAULT_AUTO_HIDE_THRESHOLD_DP = 64;
    //view
    private Context mContext;
    private RecyclerView mRecyclerView;
    private AdapterNetwork mAdapterNetwork;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //data
    private Bundle mArguments;
    private String mData;

    public FragmentHistory() {
    }

    //TODO
    public static FragmentHistory newInstance(String data) {
        FragmentHistory fragment = new FragmentHistory();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.History.DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mArguments = getArguments();
        if (mArguments != null) {
            mData = getArguments().getString(Constant.History.DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        setupRecyclerView(mRecyclerView);
        setupSwipeRefreshLayout(mSwipeRefreshLayout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewData();
    }

    private void initViewData(){
        Observable
                .create(new Observable.OnSubscribe<ArrayList<BeanNetwork>>() {
                    @Override
                    public void call(Subscriber<? super ArrayList<BeanNetwork>> subscriber) {
                        ArrayList<BeanNetwork> networksOrig = null;
                        ArrayList<BeanNetwork> networksSort = null;
                        int retCode = ReadNetworks.processSuAndCopyFile(Constant.FileConfig.HISTORY_STORAGE_FILE_ABS_NAME);
                        if(retCode == Constant.SuFeedbackCode.SUCCESS){
                            networksOrig = ReadNetworks.readNetworksHistory(Constant.FileConfig.HISTORY_STORAGE_FILE_ABS_NAME);
                            if(networksOrig!=null){
                                int size = networksOrig.size();
                                networksSort = new ArrayList<>();
                                BeanNetwork network;
                                for(int i = size - 1; i >= 0; i--){
                                    network = networksOrig.get(i);
                                    if(!TextUtils.isEmpty(network.psk)){
                                        networksSort.add(network);
                                    }
                                }
                            }
                        }
                        subscriber.onNext(networksSort);
                        subscriber.onCompleted();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ArrayList<BeanNetwork>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ArrayList<BeanNetwork> networks) {
                        //TODO MVP
                        if(networks == null){
                            mRecyclerView.setVisibility(View.GONE);
                        }else{
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                            Log.d(TAG,"networks size : " + networks.size());
                            mAdapterNetwork = new AdapterNetwork(mContext, networks);
                            mRecyclerView.setAdapter(mAdapterNetwork);
                            mAdapterNetwork.setOnItemClickListener(FragmentHistory.this);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onItemClick(View view, Object data) {
        if(data != null && (data instanceof BeanNetwork)){
            Toast.makeText(getContext(),"ssid:"+((BeanNetwork)data).psk, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView){
        recyclerView.addOnScrollListener(new HidingScrollListener(
                DisplayUtil.dp2px(getContext(),DEFAULT_AUTO_HIDE_THRESHOLD_DP), false){
            @Override
            public void onHide() {
                EventBus.getDefault().post(new EventFAB(false));
            }

            @Override
            public void onMoved(int distance) {
            }

            @Override
            public void onShow() {
                EventBus.getDefault().post(new EventFAB(true));
            }
        });
    }
    private void setupSwipeRefreshLayout(final SwipeRefreshLayout swipeRefreshLayout){
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_purple_amethyst_normal,
                                                    R.color.color_yellow_carrot_normal,
                                                    R.color.color_blue_river_normal);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViewData();
            }
        });
    }

    @Override
    public void update(Object object) {
        if(mSwipeRefreshLayout != null){
            mSwipeRefreshLayout.setRefreshing(true);
        }
        initViewData();
    }
}
