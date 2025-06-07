package idiots.ddakdae.exception;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private final ErrorCode errorCode;



}
