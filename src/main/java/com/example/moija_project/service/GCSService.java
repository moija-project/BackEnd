package com.example.moija_project.service;


import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Optional;

@Service
@Slf4j
public class GCSService {
    @Value("${gcs-resource-test-bucket}")
    private String bucketname;

    @Value("classpath:/pictures/no_profile.png")
    private Resource defaultFile;

    private Storage storage;

    private GCSService() {
        this.storage = StorageOptions.getDefaultInstance().getService();;
    }

    public String readGcsFile(Optional<String> filename) throws IOException {
        return StreamUtils.copyToString(
                filename.isPresent()
                        ? fetchResource(filename.get()).getInputStream()
                        : this.defaultFile.getInputStream(),
                Charset.defaultCharset())
                + "\n";
    }

    public String writeGcsFile(String fileName, String data) throws IOException {
        GoogleStorageResource resource = fetchResource(fileName);

        try (OutputStream os = ((WritableResource) resource).getOutputStream()) {
            os.write(data.getBytes());
        }
        return resource.getFilename();
    }

    private GoogleStorageResource fetchResource(String filename) {
        return new GoogleStorageResource(
                this.storage, String.format("gs://%s/%s", this.bucketname, filename));
    }
}
