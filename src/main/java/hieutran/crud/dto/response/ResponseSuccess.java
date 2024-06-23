package hieutran.crud.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor //! Tự động tạo constructor với tất cả tham số dùng get, post
public class ResponseSuccess<T> {
    private final int status;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL) //! không trả về giá trị null nếu giá trị data = null
    private T data;

    //! constructor dùng cho put, path, delete
    public ResponseSuccess(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
