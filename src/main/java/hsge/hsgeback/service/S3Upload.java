package hsge.hsgeback.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import hsge.hsgeback.dto.request.PetImgDto;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.PetImg;
import hsge.hsgeback.repository.PetImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class S3Upload {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    private final PetImgRepository petImgRepository;

    public PetImg upload(MultipartFile multipartFile, Pet pet) throws IOException {
        PetImgDto petImgDto = new PetImgDto();
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());
        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);
        petImgDto.setOriImgName(multipartFile.getOriginalFilename());
        String savedFileName = amazonS3.getUrl(bucket, s3FileName).toString();
        petImgDto.setUuid(s3FileName);
        petImgDto.setS3Url(savedFileName);
        return petImgRepository.save(petImgDto.toPetImgEntity(pet));
    }

    public void update(MultipartFile multipartFile, Long petId) throws IOException {
        Optional<PetImg> optional = petImgRepository.findByPetId(petId);
        PetImg petImg = optional.orElseThrow();
        log.info("uuid : {}",petImg.getUuid());
        delete(petImg.getUuid());
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());
        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);
        petImg.setOriImgName(multipartFile.getOriginalFilename());
        String savedFileName = amazonS3.getUrl(bucket, s3FileName).toString();
        petImg.setS3Url(savedFileName);
        petImg.setUuid(s3FileName);
    }

    public void delete(String filePath) {
        amazonS3.deleteObject(bucket, filePath);
    }

}