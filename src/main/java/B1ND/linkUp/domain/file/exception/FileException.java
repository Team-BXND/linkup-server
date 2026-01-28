package B1ND.linkUp.domain.file.exception;

import lombok.Getter;

@Getter
public class FileException extends RuntimeException {
    private FileErrorCode fileErrorCode;

    public FileException (FileErrorCode fileErrorCode) {
        super(fileErrorCode.getMessage());
        this.fileErrorCode = fileErrorCode;
    }

    public FileException (FileErrorCode fileErrorCode, String messgae) {
        super(messgae);
        this.fileErrorCode = fileErrorCode;
    }
}
