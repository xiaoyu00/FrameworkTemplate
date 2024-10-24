package com.framework.template.simple.gismap.gaode;

import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.UrlTileProvider;
import com.framework.template.R;

import java.net.URL;

/**
 * 高德地图 加载wms地图（无法去掉底图）
 */
public class GaoDeActivity extends AppCompatActivity {
    private MapView mapView;
    private AMap aMap;
    private String url1="http://t0.tianditu.gov.cn/img_c/wmts?" +
            "service=wmts" +
            "&request=gettile" +
            "&version=1.0.0" +
            "&layer=img" +
            "&format=tiles" +
            "&STYLE=default" +
            "&tilematrixset=c" +
            "&tk=cc02148045661e676713366ea192d5f8"+
            "&TILEMATRIX=%s"+
            "&TILEROW=%s" +
            "&TILECOL=%s";
    private String url2="http://t0.tianditu.gov.cn/cia_c/wmts?" +
            "service=wmts" +
            "&request=gettile" +
            "&version=1.0.0" +
            "&layer=cia" +
            "&format=tiles" +
            "&STYLE=default" +
            "&tilematrixset=c" +
            "&tk=cc02148045661e676713366ea192d5f8"+
            "&TILEMATRIX=%s"+
            "&TILEROW=%s" +
            "&TILECOL=%s";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gao_de);
        MapsInitializer.updatePrivacyShow(this,true,true);
        MapsInitializer.updatePrivacyAgree(this,true);
        mapView=findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        aMap=mapView.getMap();
        loadTianDiTuMap();
    }
    // 天地图
    private void loadTianDiTuMap(){
        // 天地图1
        UrlTileProvider urlTileProvider1= new UrlTileProvider(256,256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                try {
                    String realUrl=String.format(url1,zoom,y,x);
                    LatLng mLatLng=aMap.getProjection().fromScreenLocation(new Point(x,y));
                    return new URL(realUrl);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        };
        TileOverlayOptions tileOverlayOptions1=new TileOverlayOptions().tileProvider(urlTileProvider1);
        // 天地图2
        UrlTileProvider urlTileProvider2= new UrlTileProvider(256,256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                try {
                    String realUrl=String.format(url2,zoom,y,x);
                    LatLng mLatLng=aMap.getProjection().fromScreenLocation(new Point(x,y));
                    return new URL(realUrl);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        };
        TileOverlayOptions tileOverlayOptions2=new TileOverlayOptions().tileProvider(urlTileProvider2);

        // wms地图（超图）
        HeritageScopeTileProvider heritageScopeTileProvider =new HeritageScopeTileProvider(this);
        TileOverlayOptions tileOverlayOptions3=new TileOverlayOptions().tileProvider(heritageScopeTileProvider);


        aMap.addTileOverlay(tileOverlayOptions1).setZIndex(1);
        aMap.addTileOverlay(tileOverlayOptions2).setZIndex(1);

        TileOverlay tileOverlay= aMap.addTileOverlay(tileOverlayOptions3);
        // 移除
//        tileOverlay.remove();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}