package idiots.ddakdae.controller;

import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.domain.Review;
import idiots.ddakdae.dto.request.ReviewRequestDto;
import idiots.ddakdae.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성 API", description = "리뷰 텍스트 및 이미지 업로드 하나만")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createReview(@AuthenticationPrincipal Customer customer,
                                          @RequestPart("reviewRequestDto") ReviewRequestDto reviewRequestDto,
                                          @Parameter(schema = @Schema(type = "string", format = "binary"), description = "이미지 파일")
                                          @RequestPart(value = "reviewImage", required = false) MultipartFile image) throws IOException {

        if (Objects.isNull(customer)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }
        Review savedReview = reviewService.saveReview(customer.getId(), reviewRequestDto, image);
        return ResponseEntity.ok(savedReview.getReviewId());
    }

    @GetMapping("/{plId}")
    public ResponseEntity<?> getReviews(@PathVariable Long plId) {
        return ResponseEntity.ok(reviewService.getReviews(plId));
    }
}
