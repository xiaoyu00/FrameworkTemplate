package com.framework.base.utils

//import com.amap.api.maps.model.LatLng
//
//
//const val MIN_LAT = -90.0
//const val MAX_LAT = 90.0
//const val MIN_LNG = -180.0
//const val MAX_LNG = 180.0
//
///**
// * 获取不规则多边形几何中心点
// *
// * @param mPoints
// * @return
// */
//fun getCenterPoint(mPoints: List<LatLng>): LatLng? {
////     1 自己计算
//    val latitude = (getMinLatitude(mPoints) + getMaxLatitude(mPoints)) / 2
//    val longitude = (getMinLongitude(mPoints) + getMaxLongitude(mPoints)) / 2
//    return LatLng(latitude, longitude)
//    // 2 使用Google map API提供的方法（推荐）
////    val boundsBuilder = LatLngBounds.builder()
////    for (ll in mPoints) boundsBuilder.include(ll)
////    return boundsBuilder.build().getCenter()
//}
//
//// 经度最小值
//fun getMinLongitude(mPoints: List<LatLng>): Double {
//    var minLongitude = MAX_LNG
//    if (mPoints.size > 0) {
//        minLongitude = mPoints[0].longitude
//        for (latlng in mPoints) {
//            // 经度最小值
//            if (latlng.longitude < minLongitude) minLongitude = latlng.longitude
//        }
//    }
//    return minLongitude
//}
//
//// 经度最大值
//fun getMaxLongitude(mPoints: List<LatLng>): Double {
//    var maxLongitude = MIN_LNG
//    if (mPoints.size > 0) {
//        maxLongitude = mPoints[0].longitude
//        for (latlng in mPoints) {
//            // 经度最大值
//            if (latlng.longitude > maxLongitude) maxLongitude = latlng.longitude
//        }
//    }
//    return maxLongitude
//}
//
//// 纬度最小值
//fun getMinLatitude(mPoints: List<LatLng>): Double {
//    var minLatitude = MAX_LAT
//    if (mPoints.size > 0) {
//        minLatitude = mPoints[0].latitude
//        for (latlng in mPoints) {
//            // 纬度最小值
//            if (latlng.latitude < minLatitude) minLatitude = latlng.latitude
//        }
//    }
//    return minLatitude
//}
//
//// 纬度最大值
//fun getMaxLatitude(mPoints: List<LatLng>): Double {
//    var maxLatitude = MIN_LAT
//    if (mPoints.size > 0) {
//        maxLatitude = mPoints[0].latitude
//        for (latlng in mPoints) {
//            // 纬度最大值
//            if (latlng.latitude > maxLatitude) maxLatitude = latlng.latitude
//        }
//    }
//    return maxLatitude
//}
//
///**
// * 获取不规则多边形重心点
// *
// * @param mPoints
// * @return
// */
//fun getCenterOfGravityPoint(mPoints: List<LatLng>): LatLng? {
//    var area = 0.0 //多边形面积
//    var Gx = 0.0
//    var Gy = 0.0 // 重心的x、y
//    for (i in 1..mPoints.size) {
//        val iLat = mPoints[i % mPoints.size].latitude
//        val iLng = mPoints[i % mPoints.size].longitude
//        val nextLat = mPoints[i - 1].latitude
//        val nextLng = mPoints[i - 1].longitude
//        val temp = (iLat * nextLng - iLng * nextLat) / 2.0
//        area += temp
//        Gx += temp * (iLat + nextLat) / 3.0
//        Gy += temp * (iLng + nextLng) / 3.0
//    }
//    Gx = Gx / area
//    Gy = Gy / area
//    return LatLng(Gx, Gy)
//}
//
///**
// * 判断多边形是否合法
// */
//fun isPolygonMethod(lats: List<LatLng>): Boolean {
//    val lines = mutableMapOf<Int, List<LatLng>>()
//    lats.forEachIndexed { index, lat ->
//        if (index != lats.size - 1) {
//            lines[index] = listOf<LatLng>(lat, lats[index + 1])
//        } else {
//            lines[index] = listOf<LatLng>(lat, lats.first())
//        }
//    }
//    var boolean = true
//    lines.forEach { (t, u) ->
//        for (l in t until lines.size) {
//            if (isIntersect(u, lines[l]!!)) {
//                boolean = false
//                break
//            }
//        }
//    }
//    return boolean
//}
//
//private fun isIntersect(line1: List<LatLng>, line2: List<LatLng>): Boolean {
//    val p1 = line1.first()
//    val p2 = line1.last()
//    val q1 = line2.first()
//    val q2 = line2.last()
//
//    val a1 =
//        (p2.longitude - p1.longitude) * (q1.latitude - p1.latitude) - (q1.longitude - p1.longitude) * (p2.latitude - p1.latitude)
//    val a2 =
//        (p2.longitude - p1.longitude) * (q2.latitude - p1.latitude) - (q2.longitude - p1.longitude) * (p2.latitude - p1.latitude)
//
//    val b1 =
//        (q2.longitude - q1.longitude) * (p1.latitude - q1.latitude) - (p1.longitude - q1.longitude) * (q2.latitude - q1.latitude)
//    val b2 =
//        (q2.longitude - q1.longitude) * (p2.latitude - q1.latitude) - (p2.longitude - q1.longitude) * (q2.latitude - q1.latitude)
//
//    if (a1 * a2 < 0 && b1 * b2 < 0) {
//        return true
//    }
//    return false
//}
//
////计算俩点之间的距离
//fun getDistance(start: LatLng, end: LatLng): Double {
//    val lon1: Double = Math.PI / 180 * start.longitude
//    val lon2: Double = Math.PI / 180 * end.longitude
//    val lat1: Double = Math.PI / 180 * start.latitude
//    val lat2: Double = Math.PI / 180 * end.latitude
//
//    // 地球半径
//    val R = 6371.0
//
//    // 两点间距离 km，如果想要米的话，结果*1000就可以了
//    val d = (Math.acos(
//        Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(
//            lat2
//        ) * Math.cos(lon2 - lon1)
//    )
//            * R)
//    return d * 1000
//}