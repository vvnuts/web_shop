package com.vvnuts.shop.utils.validators;

import com.vvnuts.shop.exceptions.FileIsEmptyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageValidator {
    public void validate(MultipartFile file) {
        if (file.isEmpty()){
            throw new FileIsEmptyException("Файл пуст.");
        }
    }
}
