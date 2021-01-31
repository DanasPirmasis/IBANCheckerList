package seb.homework.iban.service.iban;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import seb.homework.iban.service.file.FileService;
import seb.homework.iban.types.BankValuesEnum;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
class IBANServiceImpl implements IBANService {

    private final FileService fileService;

    public IBANServiceImpl(@Autowired FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public Map<String, Boolean> checkFromFileByValidity(MultipartFile file) {
        List<String> ibanFileLines = fileService.readFileLines(file);

        Map<String, Boolean> ibanValidityResponse = new HashMap<>();
        for (String iban : ibanFileLines) {
            ibanValidityResponse.put(iban, ibanChecker(iban));
        }
        return ibanValidityResponse;
    }

    @Override
    public Map<String, String> checkFromFileByBank(MultipartFile file) {
        List<String> ibanFileLines = fileService.readFileLines(file);

        Map<String, String> ibanBankResponse = new HashMap<>();
        for (String iban : ibanFileLines) {
            ibanBankResponse.put(iban, bankChecker(iban));
        }
        return ibanBankResponse;
    }

    private Boolean ibanChecker(String ibanNumber) {

        String modifiedIBAN = removeSpacesAndMoveIBANCharacters(ibanNumber);

        if (ibanNumber.length() < 15 || ibanNumber.length() > 34) {
            return false;
        }
        System.out.println("IBAN: " + modifiedIBAN + " " + modifiedIBAN.length());

        modifiedIBAN = lettersToIntegers(modifiedIBAN);

        if (modifiedIBAN == null) {
            return false;
        }

        BigInteger ibanNumberToBigInt = new BigInteger(modifiedIBAN);
        return ibanNumberToBigInt.mod(BigInteger.valueOf(97)).intValue() == 1;
    }

    private String removeSpacesAndMoveIBANCharacters(String ibanNumber) {

        String modifiedIBAN = ibanNumber.replaceAll("\\s", "");
        if (modifiedIBAN.length() > 4) {
            modifiedIBAN = modifiedIBAN.substring(4) + modifiedIBAN.substring(0, 4);
        }

        return modifiedIBAN;
    }

    private String lettersToIntegers(String ibanNumber) {
        StringBuilder ibanNumberToIntegers = new StringBuilder();

        for (int i = 0; i < ibanNumber.length(); i++) {
            int numericValue = Character.getNumericValue(ibanNumber.charAt(i));
            if (!checkForNegatives(numericValue)) return null;
            ibanNumberToIntegers.append(numericValue);
        }

        return ibanNumberToIntegers.toString();
    }

    private boolean checkForNegatives(int number) {
        return number >= 0;
    }

    private String bankChecker(String ibanNumber) {
        String code = "0";
        if (ibanNumber.length() > 9) {
            code = ibanNumber.substring(4, 9);
        }
        BankValuesEnum bankEnum = BankValuesEnum.getByBankCode(code);
        return bankEnum.getBankDescription();
    }
}
