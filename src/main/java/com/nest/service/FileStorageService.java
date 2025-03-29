package com.nest.service;

import com.nest.common.util.FormatDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
@Slf4j
public class FileStorageService {
    @Value("${file.default-dir}")
    private String defaultDir;
    FormatDate formatDate = new FormatDate();

    public String saveProfileImage(String email, MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String fileName = email + "/profile/" + formatDate.formatDate(LocalDateTime.now())+ "_" + file.getOriginalFilename()  ;
            Path path = Paths.get(defaultDir, fileName);
            Files.createDirectories(path.getParent()); // 폴더가 없는 경우 생성
            Files.write(path, bytes); // 파일 저장
            return path.toString();
        } catch (IOException e) {
            log.error("이미지 저장 중 오류 발생: {}", e.getMessage());
            throw new UncheckedIOException("이미지 저장 중 문제가 발생했습니다.", e);
        }
    }

    public String savePostImage(String email ,MultipartFile file){
        try {
            byte[] bytes = file.getBytes();
            String now = LocalDateTime.now().toString();
            String fileName = email + "/post/"  + formatDate.formatDate(LocalDateTime.now())+ "_"+ file.getOriginalFilename();
            Path path = Paths.get(defaultDir, fileName);
            Files.createDirectories(path.getParent());
            Files.write(path,bytes);
            log.info("!!FILE SAVE OK {}!!", path);
            return fileName;
        } catch (IOException e) {
            throw new UncheckedIOException("이미지 저장 중 오류가 발생하였습니다.",e);
        }
    }
    @Transactional
    public void deletePostImage(String ImagePath){
        try {
            Files.deleteIfExists(Paths.get(ImagePath));
        } catch (IOException e) {
            throw new UncheckedIOException(" 이미지 삭제 중 오류가 발생하였습니다.",e);
        }
    }
}
