package idiots.ddakdae.service;

import idiots.ddakdae.config.GCSUploader;
import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.domain.ParkingLot;
import idiots.ddakdae.domain.Review;
import idiots.ddakdae.dto.request.ReviewRequestDto;
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
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        ParkingLot parkingLot = parkingLotRepository.findById(reviewRequestDto.getPlId()).orElseThrow();
        String imagePath = image != null ?
                uploader.upload(bucketName, UUID.randomUUID() + "-" + image.getOriginalFilename(), image) : null;

        if (image == null || image.isEmpty()) {
            log.warn("️업로드된 이미지가 없습니다.");
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
