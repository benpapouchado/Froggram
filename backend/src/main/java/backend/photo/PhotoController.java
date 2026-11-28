package backend.photo;

import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.amazonaws.HttpMethod;

import backend.S3.S3Service;

@RestController
@RequestMapping("/photo")
public class PhotoController {

    private final S3Service s3Service;

    public PhotoController(S3Service s3Service){
        this.s3Service = s3Service;
    }

    @PostMapping("/generate-pre-signed-url")
    public ResponseEntity<String> generatePreSignedUrl(@RequestBody String username) {
        String url = s3Service.generateUrl(username, HttpMethod.POST);

        return ResponseEntity.ok().body(url);
    }
}
