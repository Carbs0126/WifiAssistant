package cn.carbs.android.wifiassistant.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.carbs.android.wifiassistant.R;
import cn.carbs.android.wifiassistant.bean.BeanNetwork;
import cn.carbs.android.wifiassistant.interfaces.IUpdate;

/**
 * Created by Carbs.Wang on 2016/7/13.
 */
public class ViewNetwork extends RelativeLayout implements IUpdate<BeanNetwork>{

    private static final String TAG = "history";
    //View
    private TextView mTVSSID;
    private TextView mTVPSK;

    //Data
    private BeanNetwork mBeanNetwork;

    public ViewNetwork(Context context) {
        super(context);
        init(context);
    }

    public ViewNetwork(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ViewNetwork(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View contentView = inflate(context, R.layout.view_item_network, this);
        mTVSSID = (TextView)contentView.findViewById(R.id.ssid_content);
        mTVPSK = (TextView)contentView.findViewById(R.id.psk_content);
    }

    @Override
    public void update(BeanNetwork beanNetwork) {
        mBeanNetwork = beanNetwork;
        Log.d(TAG,"update mBeanNetwork : " + mBeanNetwork);
        mTVSSID.setText(mBeanNetwork.ssid);
        mTVPSK.setText(mBeanNetwork.psk);
    }
}