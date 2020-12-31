package com.example.mask_app_java;

public class LocationDistance {

    /**
     * 두 지점간의 거리 계산
     *
     * @param lat1 지점 1 위도
     * @param lon1 지점 1 경도
     * @param lat2 지점 2 위도
     * @param lon2 지점 2 경도
     * @param unit 거리 표출단위
     * @return
     */
    
    // Static(정적)은 고정된이란 의미로, 정적 필드와 메소드는 객체(인스턴스)에 소속된 멤버가 아닌
    // 클래스에 고정된 멤버다. 클래스 로더가 클래스를 로딩하여 메소드 메모리 영역에 적재할 때
    // 클래스별로 관리되기 때문에 로딩이 끝나면 즉시 사용이 가능하다.
    // 즉, static으로 선언하면 호출 시 클래스를 이용해 호출이 가능하지만,
    // static으로 선언하지 않으면 객체를 생성하여 호출해야한다.
    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
         
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
         
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
         
        if (unit.equals("kilometer")) {
            dist = dist * 1.609344;
        } else if(unit.equals("meter")){
            dist = dist * 1609.344;
        }
 
        return (dist);
    }
     
 
    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
     
    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


        /*
        // 호출하는 코드여서 뺌

     public static void main(String[] args) {

        // 마일(Mile) 단위
        double distanceMile =
            distance(37.504198, 127.047967, 37.501025, 127.037701, "");

        // 미터(Meter) 단위
        double distanceMeter =
            distance(37.504198, 127.047967, 37.501025, 127.037701, "meter");

        // 킬로미터(Kilo Meter) 단위
        double distanceKiloMeter =
            distance(37.504198, 127.047967, 37.501025, 127.037701, "kilometer");

        System.out.println(distanceMile) ;
        System.out.println(distanceMeter) ;
        System.out.println(distanceKiloMeter) ;


    }
    
         */
 
}