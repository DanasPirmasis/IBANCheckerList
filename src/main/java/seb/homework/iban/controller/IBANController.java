package seb.homework.iban.controller;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import seb.homework.iban.service.archive.ArchiveService;
import seb.homework.iban.service.file.FileService;
import seb.homework.iban.service.iban.IBANService;
import seb.homework.iban.util.CustomMediaType;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;


@Controller
public class IBANController {

    private final IBANService ibanService;
    private final FileService fileService;
    private final ArchiveService archiveService;

    public IBANController(@Autowired IBANService ibanService,
                          @Autowired FileService fileService,
                          @Autowired ArchiveService archiveService) {
        this.ibanService = ibanService;
        this.fileService = fileService;
        this.archiveService = archiveService;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload")
    public ResponseEntity<InputStreamResource> uploadAndGetFiles(@RequestParam("file") MultipartFile file) throws RuntimeException {
        try {
            Map<String, Boolean> ibanByValidity = ibanService.checkFromFileByValidity(file);
            Map<String, String> ibanByBank = ibanService.checkFromFileByBank(file);

            File ibanByValidityFile = fileService.mapToFile(ibanByValidity, ";", "_valid.csv");
            File ibanByBankFile = fileService.mapToFile(ibanByBank, ";", "_bank.csv");

            File zipFile = archiveService.filesToZip(ibanByValidityFile, ibanByBankFile);

            InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + zipFile.getName())
                    .contentType(CustomMediaType.APPLICATION_ZIP)
                    .contentLength(zipFile.length())
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
