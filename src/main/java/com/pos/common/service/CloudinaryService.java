package com.pos.common.service;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map uploadFile(MultipartFile file, String folderName) {
        Map<String, String> options = new HashMap<>();
        options.put("folder", folderName);
        try {
            Map<String, String> result = cloudinary.uploader().upload(file.getBytes(), options);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
