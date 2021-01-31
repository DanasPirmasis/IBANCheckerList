package seb.homework.iban.service.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface FileService {

    List<String> readFileLines(MultipartFile file);

    File mapToFile(Map<?, ?> map, String separator, String fileName);
}
