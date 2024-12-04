package com.example.datadynamo;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @Value("${upload.dir}")
    private String uplDir;

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile mf) {
        if (mf.isEmpty()) {
            return new ResponseEntity<>("No file selected", HttpStatus.BAD_REQUEST);
        }

        try {
            // Debugging output for paths
            System.out.println("Configured upload directory: " + uplDir);

            // Create the directory if it does not exist
            File tgtDir = new File(uplDir);
            if (!tgtDir.exists()) {
                boolean created = tgtDir.mkdirs();
                if (!created) {
                    throw new IOException("Failed to create directory: " + uplDir);
                }
            }

            // Ensure that the filename is safe and does not contain path traversal characters
            String fn = mf.getOriginalFilename();
            if (fn == null || fn.contains("..")) {
                return new ResponseEntity<>("Invalid file name", HttpStatus.BAD_REQUEST);
            }

            // Construct the file path
            File destFile = new File(tgtDir, fn);
            System.out.println("Saving file to: " + destFile.getAbsolutePath());

            // Save the file
            mf.transferTo(destFile);

            return new ResponseEntity<>("File uploaded successfully!", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
