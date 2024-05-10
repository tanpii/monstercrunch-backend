package edu.mirea.cookie_shop.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class PictureService {
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String product;
    @Value("${minio.comment}")
    private String comment;

    @SneakyThrows
    public void deleteProductPicture(String name) {
        minioClient.removeObject(RemoveObjectArgs
                .builder()
                .bucket(product)
                .object(name)
                .build());
    }

    @SneakyThrows
    public String getLinkOnPicture(String picture) {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs
                .builder()
                .bucket(product)
                .object(picture.replaceAll("\\s", "_").toLowerCase() + ".jpg")
                .expiry(3600)
                .method(Method.GET)
                .build());
    }

    @SneakyThrows
    public String getLinkOnCommentPicture(Long id) {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs
                .builder()
                .bucket(comment)
                .object(id + ".jpg")
                .expiry(3600)
                .method(Method.GET)
                .build());
    }

    @SneakyThrows
    public void putCommentPicture(Long id, InputStream stream) {
        minioClient.putObject(PutObjectArgs
                .builder()
                .bucket(comment)
                .object(id + ".jpg")
                .stream(stream, -1, 10485760)
                .build());
    }

    @SneakyThrows
    public void putProductPicture(String picture, InputStream stream) {
        minioClient.putObject(PutObjectArgs
                .builder()
                .bucket(product)
                .object(picture.replaceAll(" ", "_").toLowerCase() + ".jpg")
                .stream(stream, -1, 10485760)
                .build());
    }
}
