package idiots.ddakdae.service;

import idiots.ddakdae.config.GCSUploader;
import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.domain.ParkingLot;
import idiots.ddakdae.domain.Review;
import idiots.ddakdae.dto.request.ReviewRequestDto;
import idiots.ddakdae.dto.response.ReviewResponseDto;
import idiots.ddakdae.exception.BizException;
import idiots.ddakdae.exception.ErrorCode;
import idiots.ddakdae.repository.CustomerRepository;
import idiots.ddakdae.repository.ParkingLotRepository;
import idiots.ddakdae.repository.ReviewRepository;
import idiots.ddakdae.util.GCSUtil;
import idiots.ddakdae.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GCSUploader uploader;
    private final CustomerRepository customerRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final GCSUtil gcsUtil;

    @Value("${spring.cloud.gcp.storage.bucket-name}")
    private String bucketName;

    public Review saveReview(Long customerId, ReviewRequestDto reviewRequestDto, MultipartFile image) throws IOException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new BizException(ErrorCode.NOT_FOUND_USER));

        ParkingLot parkingLot = parkingLotRepository.findById(reviewRequestDto.getPlId()).orElseThrow(() ->
                new BizException(ErrorCode.NOT_FOUND_PKLT));

        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            try {
                String objectName = UUID.randomUUID() + "-" + image.getOriginalFilename();
                uploader.upload(bucketName, objectName, image);
                imagePath = objectName;
            } catch (IOException e) {
                log.warn("리뷰 이미지 업로드 실패: {}", e.getMessage());
                throw new BizException(ErrorCode.IMAGE_UPLOAD_ERROR);
            }
        }

        return reviewRepository.save(Review.builder()
                .customer(customer)
                .parkingLot(parkingLot)
                .comment(reviewRequestDto.getComment())
                .star(reviewRequestDto.getStar())
                .description(reviewRequestDto.getDesc())
                .reviewImagePath(imagePath)
                .createdAt(LocalDateTime.now())
                .build());
    }

    public Page<ReviewResponseDto> getReviews(Long plId, int page, int size) {
        ParkingLot parkingLot = parkingLotRepository.findById(plId)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_PKLT));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")); // 최신순

        Page<ReviewResponseDto> reviews = reviewRepository.findAllByParkingLotPlId(parkingLot.getPlId(), pageable);


        reviews.forEach(reviewResponseDto -> {
            if (Objects.nonNull(reviewResponseDto.getReviewImagePath())) {
                reviewResponseDto.setReviewImagePath(gcsUtil.generateSignedUrl(reviewResponseDto.getReviewImagePath()));
            }

            if (Objects.nonNull(reviewResponseDto.getProfileImagePath())) {
                reviewResponseDto.setProfileImagePath(gcsUtil.generateSignedUrl(reviewResponseDto.getProfileImagePath()));
            }

            if (Objects.nonNull(reviewResponseDto.getCreatedAt())) {
                Instant instant = reviewResponseDto.getCreatedAt()
                        .atZone(ZoneId.of("Asia/Seoul"))
                        .toInstant();
                reviewResponseDto.setCreatedAtFormatted(TimeUtil.formatDateWithDay(instant));
            }
        });

        return reviews;
    }
}
