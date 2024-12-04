
package org.example.services.interfaces;

import org.example.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ImageService {
    ImageDto uploadImage(Long productId, MultipartFile file);
    ImageDto uploadImageByUrl(Long productId, String imageUrl);
    List<ImageDto> getImagesByProductId(Long productId);
    void deleteImage(Long imageId);
}
