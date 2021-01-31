package seb.homework.iban.service.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service("fileService")
class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public List<String> readFileLines(MultipartFile file) {
        List<String> lines = new ArrayList<>();
        try (InputStream is = file.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (Exception e) {
            String msg = String.format("Could not read file [%s] lines, cause: [%s]", file.getName(), e.getMessage());
            throw new RuntimeException(msg);
        }
    }

    @Override
    public File mapToFile(Map<?, ?> map, String separator, String fileName) {
        try {
            File newFile = new File(makeFileName(fileName));
            try (FileWriter fileWriter = new FileWriter(newFile, true)) {
                for (Map.Entry<?, ?> pair : map.entrySet()) {
                    String key = pair.getKey().toString();
                    String value = pair.getValue().toString();
                    logger.debug(String.format("Adding entry to file [%s] = [%s]", key, value));
                    fileWriter
                            .append(key)
                            .append(separator)
                            .append(value)
                            .append(System.lineSeparator());
                }
            }
            return newFile;
        } catch (IOException e) {
            String msg = String.format("Could not map to file [%s], cause: [%s]", fileName, e.getMessage());
            throw new RuntimeException(msg);
        }
    }

    private String makeFileName(String fileName) {
        Random r = new Random();

        StringBuilder fileNameWithRandomCharsAtStart = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            fileNameWithRandomCharsAtStart.append((char) (r.nextInt(26) + 'a'));
        }
        fileNameWithRandomCharsAtStart.append(fileName);
        return fileNameWithRandomCharsAtStart.toString();
    }
}
