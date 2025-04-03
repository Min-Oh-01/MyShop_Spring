package trade.trade;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    // 이미지 파일 저장 메서드
    public String imageSave(MultipartFile image) throws IOException {

        Files.createDirectories(Paths.get("./upload/images/"));
        String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
        Path filePath = Paths.get("./upload/images/" + fileName);
        Files.copy(image.getInputStream(), filePath);

        return "/upload/images/" + fileName;
    }

    // 파일 삭제 메서드
    public void fileDelete(String file) throws IOException {
        Files.deleteIfExists(Paths.get("./upload/images").resolve(file));
    }
}
