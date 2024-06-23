package hieutran.crud.exception;

import hieutran.crud.dto.response.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

//! lỗi conflict nếu tài nguyên đã tồn tại
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceConflictException extends RuntimeException{

    private ResponseError responseError;

    public ResourceConflictException(String message) {
        super(message);
        responseError = ResponseError.builder()
                .status(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(message)
                .build();
    }

    public ResponseError getErrorResponse() {
        return responseError;
    }
}
