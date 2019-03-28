package com.tfhr.www.mapapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.animation.Animation;
import com.baidu.mapapi.animation.Transformation;
import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements CheckboxRecycleAdapter.RecyclerViewItemClickListener, View.OnClickListener, MyPopmenu.PopItemClickListener {
    private MapView mapView;
    private BaiduMap map;
    private Button location, navigate, btSearch, btTuli;
    private LocationClient locationClient;
    private WalkNavigateHelper walkNavigateHelper;
    private EditText editText;
    private PoiSearch poiSearch;
    private ListView searchResult;
    private LatLng mylocation;
    private List<PoiInfo> poiInfos;
    private RecyclerView drawleft;
    private DrawerLayout drawerLayout;
    private LinearLayout bottomLL;
    private ArrayList<DrawlayoutItem> data = new ArrayList<>();
    private CheckboxRecycleAdapter adapter;
    private ActionBar actionBar;
    private TextView dianwang, peibian;
    private MyPopmenu peibianPop;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        editText = findViewById(R.id.poimsg);
        mapView = findViewById(R.id.mapview);
        bottomLL = findViewById(R.id.bottom_ll);
        searchResult = findViewById(R.id.poisearchlistview);
        btSearch = findViewById(R.id.poisearch);
        btTuli = findViewById(R.id.bt_tuli);
        initActionBar();
        btTuli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomLL.getVisibility() == View.INVISIBLE) {
                    bottomLL.setVisibility(View.VISIBLE);
                } else {
                    bottomLL.setVisibility(View.INVISIBLE);
                }
            }
        });
        drawleft = findViewById(R.id.left_layout_map);
        drawerLayout = findViewById(R.id.drawlayout_map);
//        drawerLayout.setDrawerElevation(0);
        drawerLayout.setScrimColor(getResources().getColor(R.color.lightwhite));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        });
        data.add(new DrawlayoutItem("配变分布", 0));
        data.add(new DrawlayoutItem("开关分布", 0));
        data.add(new DrawlayoutItem("变电站展示", 0));
        data.add(new DrawlayoutItem("线路展示", 0));
        data.add(new DrawlayoutItem("分段开关", 0));
        data.add(new DrawlayoutItem("联络开关", 0));
        data.add(new DrawlayoutItem("单主变变电站", 0));
        data.add(new DrawlayoutItem("营配校核", 0));
        data.add(new DrawlayoutItem("低压采集可视化", 0));
//        drawleft.addHeaderView();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        drawleft.setLayoutManager(manager);
//        drawleft.setAdapter(new ArrayAdapter(this,R.layout.checkbox_item,R.id.item_checkbox_tv,data));
        if (null == adapter) {
            adapter = new CheckboxRecycleAdapter(this, data);
        }
        drawleft.setAdapter(adapter);
//        drawleft.(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(editText.getText())) {
                    searchResult.setVisibility(View.GONE);
                }
            }
        });
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                poiInfos = poiResult.getAllPoi();
                Log.v("tfhr", "poiresult-" + poiResult.getTotalPoiNum());
                if (null != poiInfos) {
                    searchResult.setVisibility(View.VISIBLE);
                    searchResult.setAdapter(new PoiListViewAdapter());
                } else {
                    searchResult.setVisibility(View.GONE);
                    Toast.makeText(MapActivity.this, "no data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        mapView.showScaleControl(true);
        location = findViewById(R.id.location);
        navigate = findViewById(R.id.navigate);
        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("tfhr", "click");
                walkNavigateHelper = WalkNavigateHelper.getInstance();
                walkNavigateHelper.initNaviEngine(MapActivity.this, new IWEngineInitListener() {
                    @Override
                    public void engineInitSuccess() {
                        walkNaviagte();
                        Log.v("tfhr", "init engine success");
                    }

                    @Override
                    public void engineInitFail() {

                    }
                });
            }
        });

        map = mapView.getMap();
        map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);

        //定位图标设置
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        MyLocationConfiguration locationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null, 0xAAFFFF88, 0xAA00FF00);
        map.setMyLocationConfiguration(locationConfiguration);
        locationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd0911");
        option.setScanSpan(1000);
        option.setIgnoreKillProcess(true);
        option.setOpenAutoNotifyMode();
        locationClient.setLocOption(option);
        MyLocationListener listener = new MyLocationListener();
        locationClient.registerLocationListener(listener);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationClient.isStarted()) {
                    locationClient.stop();
                } else {
                    locationClient.start();
                }
            }
        });
        map.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                searchResult.setVisibility(View.GONE);
                Log.v("tfhr", "latlng-" + latLng.latitude + latLng.longitude);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        //39.903220001487554,116.38795626135766
//        39.90382885310239116.38506371761555
//        39.89948374716881116.3819735466612
        //设置标点
        setMarker();
    }
    private String[] names=new String[]{"变电站","线路","馈线","配变"} ;
    private void initActionBar() {
        actionBar = getSupportActionBar();
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER | Gravity.CENTER;
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setElevation(0);
        View v = LayoutInflater.from(this).inflate(R.layout.layout_actionbar, null);
        TextView centerTitle = v.findViewById(R.id.centerTitle);
        centerTitle.setText("map");
        dianwang = v.findViewById(R.id.dianwang);
        dianwang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        peibian = v.findViewById(R.id.peibian);
        peibian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == peibianPop) {
                    peibianPop=new MyPopmenu(MapActivity.this);
                }
                if(peibianPop.isShow()){
                    peibianPop.dismiss();
                }
                peibianPop.setData(names);
                peibianPop.setDropDownView(peibian);
                peibianPop.setView(R.layout.custom_pop);
                peibianPop.initView();
            }
        });
        ImageView showDrawlayout = v.findViewById(R.id.showdrawlayout);
        showDrawlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawer(Gravity.START);
                } else {
                    drawerLayout.openDrawer(Gravity.START);
                }
            }
        });
        Log.v("tfhr", "actionbar");
        actionBar.setCustomView(v, layoutParams);
        actionBar.show();
        Toolbar parent = (Toolbar) v.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        Window window = getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(getResources().getColor(R.color.lightwhite));
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
//            ViewCompat.setFitsSystemWindows(mChildView, false);
//            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    private void search() {
        if (null == mylocation) {
            Toast.makeText(this, " please get your location first", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.v("tfhr", "mylocation lat" + mylocation.latitude);
        poiSearch.searchNearby(new PoiNearbySearchOption().location(mylocation).radius(1000).keyword(editText.getText() + "").pageNum(10).tag(editText.getText() + ""));

    }

    //步行导航
    private void walkNaviagte() {
        LatLng startpoint = new LatLng(39.90382885310239, 116.38506371761555);
        LatLng endpoint = new LatLng(39.89948374716881, 116.3819735466612);
        WalkNaviLaunchParam param = new WalkNaviLaunchParam().stPt(startpoint).endPt(endpoint);
        Log.v("tfhr", "navigate");
        walkNavigateHelper.routePlanWithParams(param, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.v("tfhr", "route plan start");
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.v("tfhr", "route plan success ");
                startActivity(new Intent(MapActivity.this, NavigateGuidActivity.class));
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError walkRoutePlanError) {
                Log.v("tfhr", "route plan fail" + walkRoutePlanError.toString());
            }
        });
    }

    private void setMarker() {
        LatLng position1 = new LatLng(39.903220001487554, 116.38795626135766);
        LatLng position2 = new LatLng(39.90382885310239, 116.38506371761555);
        LatLng position3 = new LatLng(39.89948374716881, 116.3819735466612);
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        OverlayOptions options = new MarkerOptions().icon(descriptor).position(position2).alpha(0.5f).title("xxx");
        final Marker m = (Marker) map.addOverlay(options);
        Transformation transformation = new Transformation(position1, position3);
        transformation.setDuration(500);
        transformation.setRepeatCount(1);
        transformation.setRepeatMode(Animation.RepeatMode.RESTART);
        m.setAnimation(transformation);
        map.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.v("tfhr", "marker.title" + marker.getTitle());
                m.startAnimation();
                return true;
            }
        });
        List<LatLng> points = new ArrayList<>();
        points.add(position1);
        points.add(position2);
        points.add(position3);
        OverlayOptions line = new PolylineOptions().color(0xAAFF0000).width(5).points(points);
        map.addOverlay(line);

        //39.89814833238639116.38979778765311
        //39.89450867499631116.38401270016885
        //39.891982942717085116.39225016169536
        //弧线
        LatLng p1 = new LatLng(39.89814833238639, 116.38979778765311);
        LatLng p2 = new LatLng(39.89450867499631, 116.38401270016885);
        LatLng p3 = new LatLng(39.891982942717085, 116.39225016169536);
        OverlayOptions arc = new ArcOptions().color(Color.GREEN).width(5).points(p1, p2, p3);
        map.addOverlay(arc);
    }

    @Override
    protected void onResume() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(btSearch.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (null != poiSearch) {
            poiSearch.destroy();
        }
        locationClient.stop();
        map.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    @Override
    public void recyclerItemClick(int position) {
        //根据选择的item，刷新地图的逻辑
        drawerLayout.closeDrawer(Gravity.START);
        Log.v("tfhr", data.get(position).getName() + "");
        Log.v("tfhr", "position" + position);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void popItemClick(int position, String name) {
        Log.v("tfhr","pei bian"+name);
    }

    class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (null == bdLocation) {
                return;
            }
            mylocation = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MyLocationData locationData = new MyLocationData.Builder().accuracy(bdLocation.getRadius()).latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude()).
                    direction(bdLocation.getDirection()).build();
            map.setMyLocationData(locationData);
        }
    }

    class PoiListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (null != poiInfos) {
                return poiInfos.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (null != poiInfos) {
                poiInfos.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(MapActivity.this).inflate(R.layout.item_layout, parent, false);
                holder = new ViewHolder();
                holder.tv = convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(poiInfos.get(position).getName() + "");
            return convertView;
        }

        class ViewHolder {
            TextView tv;
        }
    }
}
