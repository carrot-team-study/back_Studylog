package com.studylog.api.domain.example;

import com.studylog.api.global.common.code.ErrorCode;
import com.studylog.api.global.common.code.SuccessCode;
import com.studylog.api.global.common.response.SuccessResponse;
import com.studylog.api.global.exception.BusinessException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/*
* 성공/실패 응답 사용
*
* - 성공응답 : SuccessResponse + SuccessCode
* - 실패응답 : BusinessException(ErrorCode) 던지면 GlobalExceptionHandler가 ErrorResponse로 변환
* - 검증 에러(@Valid): MethodArgumentNotValidException 발생 -> GlobalExceptionHandler가 ErrorResponse(errors 포함)로 변환
* */
@RestController
public class exampleController {

    /*
    성공 응답
    - data 있음
    반환예시
    {
       "success": true,
       "code": "SUCCESS_200",
       "message": "요청 성공",
       "data": { "id": 12, "email": "test@test.com" },
       "timestamp": "..."
    }

    */
    @GetMapping("/ok")
    public ResponseEntity<SuccessResponse<UserRes>> ok() {
       //성공코드선택
        SuccessCode sc = SuccessCode.OK;
        //내려줄 데이터
        UserRes data = new UserRes(12, "test@test.com");

        //리턴
        return ResponseEntity
                .status(sc.getHttpStatus())//상태코드는 SuccessCode에 있는 HttpStatus 사용
                .body(SuccessResponse.success(sc, data)); //body는 SuccessResponse.success(sc, data)로 통일
    }

    /*
    성공응답(데이터 없음)
    * */
    @PostMapping("/ok-no-data")
    public ResponseEntity<SuccessResponse<Void>> okNoData() {
        SuccessCode sc = SuccessCode.OK;

        //data 없는 버전은 SuccessResponse.success(sc) 사용
        return ResponseEntity
                .status(sc.getHttpStatus())
                .body(SuccessResponse.success(sc));
    }

    /*
    비지니스에러 응답 예시
    로직상 에러
    실제 응답은 GlobalExceptionHandler가 잡아서 ErrorResponse로 내려줌

    **/
    @GetMapping("/biz-error")
    public void bizError() {
        //컨트롤러/서비스 어디서든 throw로 던지면 됨
        throw new BusinessException(ErrorCode.USER_NOT_FOUND);
    }

    /*
     검증 에러
     요청 바디가 SimpleReq 검증 조건을 만족 못하면 MethodArgumentNotValidException이 자동 발생
    GlobalExceptionHandler가 잡아서 ErrorResponse(errors 포함)로 내려줌
    * */
    @PostMapping("/valid")
    public ResponseEntity<SuccessResponse<Void>> valid(@RequestBody @Valid SimpleReq req) {
        SuccessCode sc = SuccessCode.OK;

        return ResponseEntity
                .status(sc.getHttpStatus())
                .body(SuccessResponse.success(sc));
    }

    // ---------------- DTO (예시용) ----------------

    /**
     * ok()에서 내려줄 데이터 예시 DTO
     * - record: 간단 DTO 만들 때 편해서 데모에 적합
     */

    public record UserRes(int id, String email) {}

    public record SimpleReq(
            @NotBlank(message = "name은 필수입니다.")
            String name
    ) {}
}
