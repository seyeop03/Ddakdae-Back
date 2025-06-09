package idiots.ddakdae.service;

import idiots.ddakdae.config.GCSUploader;
import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.dto.request.CustomerUpdateRequestDto;
import idiots.ddakdae.dto.request.SignUpRequestDto;
import idiots.ddakdae.exception.BizException;
import idiots.ddakdae.exception.ErrorCode;
import idiots.ddakdae.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final GCSUploader uploader;

    @Value("${spring.cloud.gcp.storage.bucket-name}")
    private String bucketName;

    public void signup(SignUpRequestDto signUpRequestDto, MultipartFile profileImage) throws IOException {

        String imagePath = uploadImage(profileImage);

        Customer customer = Customer.builder()
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .nickName(signUpRequestDto.getNickName())
                .phone(signUpRequestDto.getPhone())
                .carNumber(signUpRequestDto.getCustCarNumber())
                .carKnd(signUpRequestDto.getCustCarKind())
                .manuCompany(signUpRequestDto.getManuCompany())
                .fuelType(signUpRequestDto.getFuelType())
                .carModel(signUpRequestDto.getCarModel())
                .profileImage(imagePath)
                .createdAt(LocalDateTime.now())
                .build();

        customerRepository.save(customer);
    }

    public void updateCustomer(Long custId, CustomerUpdateRequestDto requestDto, MultipartFile profileImage) throws IOException {
        Customer customer = customerRepository.findById(custId)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_USER));

        String imagePath = uploadImage(profileImage);
        if (imagePath != null) {
            customer.setProfileImage(imagePath);
        }

        customer.setNickName(requestDto.getNickName());
        customer.setPhone(requestDto.getPhone());
        customer.setCarKnd(requestDto.getCarKnd());
        customer.setCarNumber(requestDto.getCarNumber());
        customer.setFuelType(requestDto.getFuelType());
        customer.setManuCompany(requestDto.getManuCompany());
        customer.setUpdatedAt(LocalDateTime.now());

        customerRepository.save(customer);
    }

    private String uploadImage(MultipartFile profileImage) {
        String imagePath = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String objectName = UUID.randomUUID() + "-" + profileImage.getOriginalFilename();
                uploader.upload(bucketName, objectName, profileImage);
                imagePath = objectName;
            } catch (IOException e) {
                log.warn("프로필 이미지 업로드 실패: {}", e.getMessage());
                throw new BizException(ErrorCode.IMAGE_UPLOAD_ERROR);
            }
        }

        return imagePath;
    }
}
