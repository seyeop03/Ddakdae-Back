package idiots.ddakdae.service;

import idiots.ddakdae.config.GCSUploader;
import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.dto.request.SignUpRequestDto;
import idiots.ddakdae.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final GCSUploader uploader;

    @Value("${spring.cloud.gcp.storage.bucket-name}")
    private String bucketName;

    public void signup(SignUpRequestDto signUpRequestDto, MultipartFile profileImage) throws IOException {

        String objectName = UUID.randomUUID() + "-" + profileImage.getOriginalFilename();
        uploader.upload(bucketName, objectName, profileImage);
        String imagePath = objectName;

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
}
