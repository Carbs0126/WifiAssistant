package cn.carbs.android.wifiassistant.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import cn.carbs.android.wifiassistant.R;
import cn.carbs.android.wifiassistant.event.EventFAB;
import cn.carbs.android.wifiassistant.fragment.FragmentBase;
import cn.carbs.android.wifiassistant.fragment.FragmentHistory;

/**
 * Created by Carbs.Wang on 2016/7/13.
 */
public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //view
    private Toolbar mToolbar;
    private FloatingActionButton mFloatingActionButton;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    //listener
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    //data CanonicalName & Fragment
    private HashMap<String, WeakReference<FragmentBase>> mFragments = new HashMap<>();
    private int mFABDistance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String fragmentName = null;
        switch (item.getItemId()) {
            case R.id.nav_histories:
                fragmentName = FragmentHistory.class.getCanonicalName();
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
        }

        WeakReference<FragmentBase> reference = mFragments.get(fragmentName);
        FragmentBase fragment;
        if (reference == null || reference.get() == null) {
            fragment = newFragmentByItemId(item.getItemId());
        } else {
            fragment = reference.get();
        }
        if (fragment != null) {
            commitFragment(R.id.fragment_container, fragment);
            cacheFragment(fragment);
            updateFragment(fragment, null);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private FragmentBase newFragmentByItemId(int id){
        switch (id) {
            case R.id.nav_histories:
                return FragmentHistory.newInstance("");
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private void initViews(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        FragmentBase fragmentBase = FragmentHistory.newInstance("");
        commitFragment(R.id.fragment_container, fragmentBase);
        cacheFragment(fragmentBase);
    }

    private void commitFragment(int containerID, FragmentBase fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerID, fragment)
                .commitAllowingStateLoss();
    }

    private void cacheFragment(FragmentBase fragment){
        mFragments.put(fragment.getClass().getCanonicalName(), new WeakReference<>(fragment));
    }

    private void updateFragment(FragmentBase fragment, Object object){
        fragment.update(object);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventFAB event) {
        if(event != null){
            if(event.show){
                showFAB();
            }else{
                hideFAB();
            }
        }
    }

    public void showFAB() {
        getFABTranslationDistance();
        if(mFloatingActionButton.getTranslationY() != 0) {
            mFloatingActionButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        }
    }

    public void hideFAB() {
        getFABTranslationDistance();
        if(mFloatingActionButton.getTranslationY() != mFABDistance) {
            mFloatingActionButton.animate().translationY(mFABDistance).setInterpolator(new AccelerateInterpolator(2)).start();
        }
    }

    private void getFABTranslationDistance(){
        if(mFABDistance == 0){
            int height = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
            int[] location = new int[2];
            mFloatingActionButton.getLocationOnScreen(location);
            mFABDistance = height - location[0];
        }
    }
}
