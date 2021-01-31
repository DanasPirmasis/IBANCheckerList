package seb.homework.iban.service.iban;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IBANService {
    Map<String, Boolean> checkFromFileByValidity(MultipartFile file);

    Map<String, String> checkFromFileByBank(MultipartFile file);

}
