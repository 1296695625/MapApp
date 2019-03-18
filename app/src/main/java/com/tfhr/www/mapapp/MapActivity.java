package com.tfhr.www.mapapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class MapActivity extends AppCompatActivity {
    private MapView mapView;
    private BaiduMap map;
    private Button location, navigate,btSearch;
    private LocationClient locationClient;
    private WalkNavigateHelper walkNavigateHelper;
    private EditText editText;
    private PoiSearch poiSearch;
    private ListView searchResult;
    private LatLng mylocation;
    private List<PoiInfo> poiInfos;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        editText=findViewById(R.id.poimsg);
        mapView = findViewById(R.id.mapview);
        searchResult=findViewById(R.id.poisearchlistview);
        btSearch=findViewById(R.id.poisearch);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(editText.getText())){
                    searchResult.setVisibility(View.GONE);
                }
            }
        });
        poiSearch=PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                poiInfos=poiResult.getAllPoi();
                Log.v("tfhr","poiresult-"+poiResult.getTotalPoiNum());
                if(null!=poiInfos){
                    searchResult.setVisibility(View.VISIBLE);
                    searchResult.setAdapter(new PoiListViewAdapter());
                }else {
                    searchResult.setVisibility(View.GONE);
                    Toast.makeText(MapActivity.this,"no data",Toast.LENGTH_SHORT).show();
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
        setMarker();
    }

    private void search() {
        if(null==mylocation){
            Toast.makeText(this," please get your location first",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.v("tfhr","mylocation lat"+mylocation.latitude);
        poiSearch.searchNearby(new PoiNearbySearchOption().location(mylocation).radius(1000).keyword(editText.getText()+"").pageNum(10).tag(editText.getText()+""));

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
        if(null!=poiSearch){poiSearch.destroy();}
        locationClient.stop();
        map.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (null == bdLocation) {
                return;
            }
            mylocation=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            MyLocationData locationData = new MyLocationData.Builder().accuracy(bdLocation.getRadius()).latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude()).
                    direction(bdLocation.getDirection()).build();
            map.setMyLocationData(locationData);
        }
    }
    class PoiListViewAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            if(null!=poiInfos){return poiInfos.size();}
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if(null!=poiInfos){
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
            if(null==convertView){
                convertView= LayoutInflater.from(MapActivity.this).inflate(R.layout.item_layout,parent, false);
                holder=new ViewHolder();
                holder.tv=convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(poiInfos.get(position).getName()+"");
            return convertView;
        }
        class ViewHolder{
            TextView tv;
        }
    }
}
