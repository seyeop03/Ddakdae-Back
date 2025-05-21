package idiots.ddakdae.infra.redis;

public class RedisCacheKeyGenerator {

    public static String generateKey(double swLat, double swLot, double neLat, double neLot, int zoomLevel) {
        return String.format("cluster:%.4f_%.4f:%.4f_%.4f:zoomLevel-%d",
                swLat, swLot, neLat, neLot, zoomLevel);
    }
}
