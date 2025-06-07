package idiots.ddakdae.exception;

import com.google.api.Http;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Favorite
    NOTFOUND_USER(HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."),
    NOTFOUND_PKLT(HttpStatus.NOT_FOUND, "해당 주차장이 존재하지 않습니다."),
    NOTFOUND_FAVORITE(HttpStatus.NOT_FOUND, "해당 찜 내역이 존재하지 않습니다."),
    EXIST_PKLT_FAVORITE(HttpStatus.BAD_REQUEST, "이미 찜한 주차장입니다."),

    // Image upload
    IMAGE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 실패");

    private final HttpStatus httpStatus;
    private final String message;
}
