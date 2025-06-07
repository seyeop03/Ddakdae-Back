package idiots.ddakdae.service;

import idiots.ddakdae.config.GCSUploader;
import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.domain.ParkingLot;
import idiots.ddakdae.domain.Review;
import idiots.ddakdae.dto.request.ReviewRequestDto;
import idiots.ddakdae.exception.BizException;
import idiots.ddakdae.exception.ErrorCode;
import idiots.ddakdae.repository.CustomerRepository;
import idiots.ddakdae.repository.ParkingLotRepository;
import idiots.ddakdae.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GCSUploader uploader;
    private final CustomerRepository customerRepository;
    private final ParkingLotRepository parkingLotRepository;

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
                .build());
    }

    public List<Review> getReviews(Long plId) {
        return reviewRepository.findByParkingLotPlId(plId);
    }
}
