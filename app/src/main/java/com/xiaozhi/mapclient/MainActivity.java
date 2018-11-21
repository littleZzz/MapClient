package com.xiaozhi.mapclient;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.xiaozhi.mapclient.View.MyMapView;
import com.xiaozhi.mapclient.app.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : xiaozhi
 * desc   :地图主界面 完成定位跳转  TODO 集成中的一些简单问题用 TODO查看
 */
public class MainActivity extends BaseActivity {


    /*地图缩放级别*/
    private static int MAP_ZOOM_LEVEL = 14;
    private static final String TAG = "MainActivity";
    private Context mContext = this;
    @BindView(R.id.mapView)
    MyMapView mMapView;
    @BindView(R.id.ll_bgn_red)
    LinearLayout ll_bgn_red;


    //获取到map对象
    private AMap aMap;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    private Marker centerMarker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

        aMap = mMapView.getMap();

        //初始化地图定位到指定位置跳转到指定位置
        LatLng latLng = new LatLng(30.67, 104.06);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM_LEVEL));

        //初始化中心marker
        initMaker(latLng);
        //循环创建marker
        creatMarker();

        //设置地图改变监听
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLng target = cameraPosition.target;
                centerMarker.setPosition(target);
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                Log.e(TAG, "onCameraChangeFinish=" + cameraPosition.toString());
                //为marker 添加 动画
                Animation animation = new ScaleAnimation(1, 1.5f, 1, 1.5f);
                animation.setDuration(300);
                animation.setInterpolator(new LinearInterpolator());
                centerMarker.setAnimation(animation);
                centerMarker.startAnimation();
            }
        });

        //启动定位
        initLocation();


    }

    private void creatMarker() {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(30.5862310470482, 104.04248625864564));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_start_branch));
        aMap.addMarker(markerOptions);

        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(new LatLng(30.582663548546005, 104.05210868332196));
        markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_start_branch));
        aMap.addMarker(markerOptions1);

        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(new LatLng(30.589524805307846, 104.0544167241398));
        markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_start_branch));
        aMap.addMarker(markerOptions2);

        MarkerOptions markerOptions3 = new MarkerOptions();
        markerOptions3.position(new LatLng(30.595732190544194, 104.0665926117546));
        markerOptions3.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_start_branch));
        aMap.addMarker(markerOptions3);


    }


    @Override
    public void initData() {

    }

    /**
     * 初始化定位maker
     *
     * @param
     * @param target
     */
    private void initMaker(LatLng target) {

        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(target);
        markerOptions.draggable(true);//设置Marker可拖动
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.icon_position)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOptions.setFlat(true);//设置marker平贴地图效果
        centerMarker = aMap.addMarker(markerOptions);


        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.e(TAG, "onMapClick");
                ll_bgn_red.setVisibility(View.GONE);
            }
        });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.equals(centerMarker)) {
                    Log.e(TAG, "onMarkerClick");
                } else {
                    ll_bgn_red.setVisibility(View.VISIBLE);
                    //查询全部marker
                    List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
                    for (int i = 0; i < mapScreenMarkers.size(); i++) {
                        if ("123".equals(mapScreenMarkers.get(i).getObject())) {
                            mapScreenMarkers.get(i).setObject("");
                            mapScreenMarkers.get(i).setIcon(BitmapDescriptorFactory
                                    .fromResource(R.mipmap.icon_start_branch));
                            break;
                        }
                    }
                    marker.setObject("123");
                    marker.setIcon(BitmapDescriptorFactory
                            .fromResource(R.mipmap.branch_icon_like_has_free_car_n));
                }
                /* TODO 返回true 事件不在向下传递  false继续传递 触发map的click事件*/
                return true;
            }
        });
    }

    /**
     * 启动定位
     */
    private void initLocation() {

        //初始化定位蓝点样式类
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //TODO 定位蓝点的type 也会有类似定位的切换地图的功能
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_map_default_position));
        //取消定位圆形区域
        myLocationStyle.strokeColor(Color.parseColor("#00000000"));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.parseColor("#00000000"));// 设置圆形i的填充颜色
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        //初始化定位Client
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocation(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);


        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), MAP_ZOOM_LEVEL));
                        aMap.moveCamera(CameraUpdateFactory.changeBearing(0));

                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }

        });

        //启动定位
        mlocationClient.startLocation();
    }


    @OnClick({R.id.img_location})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.img_location://定位按钮
                //再次启动定位
                mlocationClient.startLocation();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
