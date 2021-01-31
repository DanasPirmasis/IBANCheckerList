package seb.homework.iban.service.archive;

import java.io.File;

public interface ArchiveService {

    File filesToZip(File... files);

}