package com.framework.template.simple.gismap.leaflet;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.framework.template.R;

/**
 * leaflet 地图库
 * 核心：使用webview 加载asset本地html实现
 */
public class LeaftActivity extends AppCompatActivity {
    private MapView mapView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaft);
        mapView=findViewById(R.id.mapView);
        mapView.setListener(new AMapListener());
        mapView.switchBaseLayer("谷歌","卫星图");
//        mapView.switchBaseLayer("天地图","道路图");



    }
    class AMapListener implements MapListener {
        /**
         * 地图加载完成执行操作
         */
        @Override
        public void onMapLoaded() {
            mapView.setCenter(35,120);
            mapView.addIcon(35,45,"test.png");
            mapView.switchBaseLayer("谷歌","卫星图");
            mapView.addWMSLayer("http://support.supermap.com.cn:8090/iserver/services/map-china400/wms130/China_4326?","China_4326","image/png","测试图层",1,40);
            mapView.addWMSLayer("http://support.supermap.com.cn:8090/iserver/services/map-china400/wms130/ChinaDark?","ChinaDark","image/png","测试图层",1,40);
        }

        /**
         * 点击marker监听
         * @param lat 图标纬度
         * @param lng 图标经度
         */
        @Override
        public void onIconClick(double lat,double lng ) {

        }
    }

}