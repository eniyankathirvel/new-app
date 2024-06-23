package hieutran.crud.exception;

import hieutran.crud.dto.response.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private ResponseError responseError;
    //! tạo một constructor với tham số là message để truyền vào thông báo lỗi
    public ResourceNotFoundException(String message) {
        super(message);
        responseError = ResponseError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(message)
                .build();
    }

    public ResponseError getErrorResponse() {
        return responseError;
    }
}
