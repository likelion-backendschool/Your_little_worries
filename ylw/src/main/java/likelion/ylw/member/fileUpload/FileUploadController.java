package likelion.ylw.member.fileUpload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/upload")
public class FileUploadController {
    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;
}

