package idiots.ddakdae.util;

import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class TimeUtil {

    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
}
