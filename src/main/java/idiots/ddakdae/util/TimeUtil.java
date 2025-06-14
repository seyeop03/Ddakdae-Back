package idiots.ddakdae.util;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
public class TimeUtil {

    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter DATE_WITH_DAY_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd(E)").withLocale(Locale.KOREAN);

    public static String convertUtcIsoToKst(String isoUtcTimestamp) {
        if (isoUtcTimestamp == null || isoUtcTimestamp.isBlank()) return null;

        try {
            OffsetDateTime utcTime = OffsetDateTime.parse(isoUtcTimestamp);
            ZonedDateTime koreaTime = utcTime.atZoneSameInstant(ZoneId.of("Asia/Seoul"));
            return koreaTime.format(OUTPUT_FORMATTER);
        } catch (Exception e) {
            log.error("타임스탬프 변환 실패: {}", isoUtcTimestamp);
            return null;
        }
    }

    public static String formatDateWithDay(Instant instant) {
        if (instant == null) return null;

        try {
            ZonedDateTime koreaTime = instant.atZone(ZoneId.of("Asia/Seoul"));
            return koreaTime.format(DATE_WITH_DAY_FORMATTER);
        } catch (Exception e) {
            log.error("날짜 변환 실패: {}", instant);
            return null;
        }
    }
}
