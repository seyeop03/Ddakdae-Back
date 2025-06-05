package idiots.ddakdae.util;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class GCSUtil {

    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket-name}")
    private String bucketName;

    public String generateSignedUrl(String objectName) {
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName).build();

        URL signedUrl = storage.signUrl(blobInfo, 15, TimeUnit.MINUTES,
                Storage.SignUrlOption.withV4Signature());

        return signedUrl.toString();
    }
}
