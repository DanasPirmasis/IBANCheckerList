package seb.homework.iban.service.archive;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
class ArchiveServiceImpl implements ArchiveService {
    @Override
    public File filesToZip(File... files) {
        try {
            File zipFile = new File("Iban Validator Results.zip");
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            for (File file : files) {
                zipOut.putNextEntry(new ZipEntry(file.getName()));
                byte[] bytes = Files.readAllBytes(file.toPath());
                zipOut.write(bytes, 0, bytes.length);
                zipOut.closeEntry();
            }
            zipOut.close();
            return zipFile;
        } catch (Exception e) {
            String msg = String.format("Could not zip to file, cause: [%s]", e.getMessage());
            throw new RuntimeException(msg);
        }
    }
}
