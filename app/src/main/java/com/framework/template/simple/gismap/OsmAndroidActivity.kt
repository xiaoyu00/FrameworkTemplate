package com.framework.template.simple.gismap

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.framework.template.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TMSOnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.wms.WMSTileSource


/**
 * osmAndroid 开源免费库(有缺陷不建议使用)
 */
class OsmAndroidActivity : AppCompatActivity() {
    private var mapView: MapView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(getApplicationContext(),
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        //TODO check permissions
        setContentView(R.layout.activity_osm_android)
        initMapView()
    }
    override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        if (mapView != null) {
            mapView!!.onResume()
        }
    }
    override fun onPause() {
        super.onPause()
        Configuration.getInstance().save(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        if (mapView != null) {
            mapView!!.onPause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mapView != null) {
            mapView!!.onDetach()
        }
    }
    private fun initMapView() {
        mapView?.maxZoomLevel = 23.0
        mapView?.minZoomLevel = 0.0
        mapView?.controller?.setZoom(12.0)
        //让瓦片适应不同像素密度:默认地图显示的字体小，图片像素高，可设置以下代码，使地图适应不同像素密度，更美观
        mapView?.isTilesScaledToDpi = true
        //设置缩放按钮可见
        val zoomController = mapView?.zoomController
        zoomController?.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapView?.setMultiTouchControls(true) // 触控放大缩小
        mapView?.overlayManager?.tilesOverlay?.isEnabled = true
        mapView?.isSelected = true
        var dm = resources.displayMetrics
        //指南针
        var mCompassOverlay = CompassOverlay(
            this, InternalCompassOrientationProvider(this),
            mapView
        )
        mCompassOverlay.enableCompass()
        mapView?.getOverlays()?.add(mCompassOverlay)
        //比例尺配置
        var mScaleBarOverlay = ScaleBarOverlay(mapView)
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        mapView?.getOverlays()?.add(mScaleBarOverlay)
        mapView?.overlays?.add(mScaleBarOverlay)
        //定位
        var mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mapView)
        mapView?.overlays?.add(mLocationOverlay)
        mLocationOverlay.enableMyLocation()
        //地图移动到该点
        var startPoint = GeoPoint(34.360284, 108.859602)
        mapView?.controller?.setCenter(startPoint)

        mapView?.setUseDataConnection(true)
//        mapView?.setTileSource(tianDiTuImgTileSource)

    }
    var superMapSource: OnlineTileSourceBase = object : TMSOnlineTileSourceBase(
        "地图",
        0,
        18,
        256,
        ".png",
        arrayOf("http://58.18.173.240:8084/datapublish_service/tms?year=2022&theme=R&layers=9TO11")
    ) {
        override fun getTileURLString(pMapTileIndex: Long): String {
            Log.e(
                "url",
                baseUrl + "&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(
                    pMapTileIndex
                ) + "&l=" + MapTileIndex.getZoom(pMapTileIndex) + "&cnvt=tdt"
            )
            return baseUrl + "&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(
                pMapTileIndex
            ) + "&l=" + MapTileIndex.getZoom(pMapTileIndex) + "&cnvt=tdt"
        }
    }
    var wmsMapSource: OnlineTileSourceBase = WMSTileSource(
        "China_4326",
        arrayOf<String>("http://support.supermap.com.cn:8090/iserver/services/map-china400/wms130/China_4326?"),
        "China_4326",
        "1.1.1",
        "EPSG:4326",
        null,
        256
    )
    var wz =
        "tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=自己申请的token"

    //影像地图 _W是墨卡托投影  _c是国家2000的坐标系
    var tianDiTuImgTileSource: OnlineTileSourceBase = object : XYTileSource(
        "Tian Di Tu Img", 1, 18, 256, "", arrayOf<String>(
            "https://t0.$wz",
            "https://t1.$wz",
            "https://t2.$wz",
            "https://t3.$wz",
            "https://t4.$wz",
            "https://t5.$wz",
            "https://t6.$wz",
            "https://t7.$wz"
        )
    ) {
        override fun getTileURLString(pMapTileIndex: Long): String {
            Log.d(
                "url",
                baseUrl + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILECOL=" + MapTileIndex.getX(
                    pMapTileIndex
                )
                        + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex)
            )
            return (baseUrl + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILECOL=" + MapTileIndex.getX(
                pMapTileIndex
            )
                    + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex))
        }
    }

}