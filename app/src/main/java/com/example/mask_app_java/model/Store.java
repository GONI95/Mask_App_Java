
package com.example.mask_app_java.model;

import com.squareup.moshi.Json;

//http://www.tcpschool.com/java/java_collectionFramework_comparable
public class Store implements Comparable<Store>{
    // Comparable 는 객체 간의 비교를 가능하게 해주는 인터페이스이다.
    // Comparable 인터페이스는 객체를 정렬하는데 사용되는 메소드인 compareTo() 메소드를 정의하고 있다.

    @Json(name = "addr")
    private String addr;
    @Json(name = "code")
    private String code;
    @Json(name = "created_at")
    private String createdAt;
    @Json(name = "lat")
    private double lat;
    @Json(name = "lng")
    private double lng;
    @Json(name = "name")
    private String name;
    @Json(name = "remain_stat")
    private String remainStat;
    @Json(name = "stock_at")
    private String stockAt;
    @Json(name = "type")
    private String type;
    private double distance_unit;

    public double getDistance_unit() { return distance_unit; }

    public void setDistance_unit(double distance_unit) { this.distance_unit = distance_unit; }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemainStat() {
        /*
        if (remainStat == null) {
             return "null";
        }
         */

            return remainStat;
    }

    public void setRemainStat(String remainStat) {
        this.remainStat = remainStat;
    }

    public String getStockAt() {
        return stockAt;
    }

    public void setStockAt(String stockAt) {
        this.stockAt = stockAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(Store o) {
        // o로 들어온 정보와 자신의 정보를 비교해 더 작으면 음수, 같으면 0, 크면 양수 반환
        return Double.compare(distance_unit, o.distance_unit);
    }
}
