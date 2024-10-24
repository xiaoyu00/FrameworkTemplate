package com.framework.template.simple.gismap.gaode;

import android.content.Context;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 高德加载wms
 * 加载自定义wms在你的Activity 中加载 amap为mapview.getAmap();
 * HeritageScopeTileProvider tileProvider = newHeritageScopeTileProvider();
 * aMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
 *
 * UrlTileProvider 高德地图api
 */
public class HeritageScopeTileProvider extends UrlTileProvider {
    private String mRootUrl;
    //默认瓦片大小
    private static int titleSize = 256;//a=6378137±2（m）
    //基本参数
    private final double initialResolution= 156543.03392804062;//2*Math.PI*6378137/titleSize;
    private final double originShift      = 20037508.342789244;//2*Math.PI*6378137/2.0; 周长的一半

    private final double HALF_PI = Math.PI / 2.0;
    private final double RAD_PER_DEGREE = Math.PI / 180.0;
    private final double HALF_RAD_PER_DEGREE = Math.PI / 360.0;
    private final double METER_PER_DEGREE = originShift / 180.0;//一度多少米
    private final double DEGREE_PER_METER = 180.0 / originShift;//一米多少度
    private Context context;

    public HeritageScopeTileProvider(Context context) {
        super(titleSize, titleSize);
        this.context=context;
        //地址写你自己的wms地址
        mRootUrl = "http://xxxxxx自己的/wms?LAYERS=cwh:protect_region_38_20160830&FORMAT=image%2Fpng&TRANSPARENT=TRUE&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&STYLES=&SRS=EPSG:3587&BBOX=";
        // 超图
//        mRootUrl = "http://support.supermap.com.cn:8090/iserver/services/map-china400/wms130/ChinaDark?LAYERS=ChinaDark&FORMAT=image%2Fpng&TRANSPARENT=TRUE&SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&STYLES=&CRS=CRS%3A84&BBOX=";
    }


    public HeritageScopeTileProvider(Context context,int i, int i1) {
        super(i, i1);
        this.context=context;
    }

    @Override
    public URL getTileUrl(int x, int y, int level) {

        try {
            String url = mRootUrl + TitleBounds(x, y, level);
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 根据像素、等级算出坐标
     *
     * @param p
     * @param zoom
     * @return
     */
    private double Pixels2Meters(int p, int zoom) {
        return p * Resolution(zoom) - originShift;
    }

    /**
     * 根据瓦片的x/y等级返回瓦片范围
     *
     * @param tx
     * @param ty
     * @param zoom
     * @return
     */
    private String TitleBounds(int tx, int ty, int zoom) {
        double minX = Pixels2Meters(tx * titleSize, zoom);
        double maxY = -Pixels2Meters(ty * titleSize, zoom);
        double maxX = Pixels2Meters((tx + 1) * titleSize, zoom);
        double minY = -Pixels2Meters((ty + 1) * titleSize, zoom);

        //转换成经纬度
        minX = Meters2Lon(minX);
        minY = Meters2Lat(minY);
        maxX = Meters2Lon(maxX);
        maxY = Meters2Lat(maxY);
        //经纬度转换米
        //        minX=Lon2Meter(minX);
        //        minY=Lat2Meter(minY);
        //        maxX=Lon2Meter(maxX);
        //        maxY=Lat2Meter(maxY);
        //坐标转换工具类构造方法 GPS( WGS-84) 转 为高德地图需要的坐标
//        CoordinateConverter converter = new CoordinateConverter(context);
//        converter.from(CoordinateConverter.CoordType.GPS);
//        converter.coord(new LatLng(minY, minX));
//        LatLng min = converter.convert();
//        converter.coord(new LatLng(maxY, maxX));
//        LatLng max = converter.convert();
//        minX = Lon2Meter(-min.longitude + 2 * minX);
//        minY = Lat2Meter(-min.latitude + 2 * minY);
//        maxX = Lon2Meter(-max.longitude + 2 * maxX);
//        maxY = Lat2Meter(-max.latitude + 2 * maxY);
        return Double.toString(minX) + "," + Double.toString(minY) + "," + Double.toString(maxX) + "," + Double.toString(maxY) + "&WIDTH=256&HEIGHT=256";
    }
    /**
     * 计算分辨率
     *
     * @param zoom
     * @return
     */
    private double Resolution(int zoom) {
        return initialResolution / (Math.pow(2, zoom));
    }

    /**
     * X米转经纬度
     */
    private double Meters2Lon(double mx) {
        double lon = mx * DEGREE_PER_METER;
        return lon;
    }
    /**
     * Y米转经纬度
     */
    private double Meters2Lat(double my) {
        double lat = my * DEGREE_PER_METER;
        lat = 180.0 / Math.PI * (2 * Math.atan(Math.exp(lat * RAD_PER_DEGREE)) - HALF_PI);
        return lat;
    }
    /**
     * X经纬度转米
     */
    private double Lon2Meter(double lon) {
        double mx = lon * METER_PER_DEGREE;
        return mx;
    }
    /**
     * Y经纬度转米
     */
    private double Lat2Meter(double lat) {
        double my = Math.log(Math.tan((90 + lat) * HALF_RAD_PER_DEGREE)) / (RAD_PER_DEGREE);
        my = my * METER_PER_DEGREE;
        return my;
    }

}

