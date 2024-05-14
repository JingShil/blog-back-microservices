package com.ccsu.img.controller;

import com.ccsu.feign.clients.ArticleClient;
import com.ccsu.feign.clients.UserClient;
import com.ccsu.feign.entity.Article;
import com.ccsu.feign.entity.User;
import com.ccsu.img.entity.FileData;
import com.ccsu.img.entity.FileFormData;
import com.ccsu.img.entity.Img;
import com.ccsu.img.entity.Result;
import com.ccsu.img.mapper.ImgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/img")
public class ImgController {
    @Autowired
    private ImgMapper imgMapper;

    @Value("${file.path}")
    private String filePath;

    @Value("${img.downloadPath}")
    private String downloadPath;

    @Autowired
    private ArticleClient articleClient;

    @Autowired
    private UserClient userClient;

    @PostMapping("/upload")
    public FileFormData imgUpload(@RequestBody MultipartFile file){

        try {
            String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
//            LocalDateTime currentDateTime = LocalDateTime.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String fileName = UUID.randomUUID() + "." + fileExtension;

//            String uploadDir = filePath;
//            File uploadPath = new File(uploadDir);
//            if (!uploadPath.exists()) {
//                uploadPath.mkdirs();
//            }
            String finalFilePath = filePath + fileName;
            File dest = new File(finalFilePath);
            file.transferTo(dest);
            Img img = new Img();
            img.setFilePath(fileName);
            imgMapper.insert(img);
            FileFormData fileFormData = new FileFormData();
            FileData fileData = new FileData();
            Map<String,String> map=new HashMap<>();
            map.put(fileName,downloadPath + fileName);
            fileData.setSuccMap(map);
            fileFormData.setData(fileData);
            fileFormData.setMsg("");
            fileFormData.setCode(1);
            return fileFormData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/upload/article")
    public Result<String> imgArticleUpload(@RequestHeader("ArticleId") String articleId, @RequestBody MultipartFile file){
        if(articleId==null){
            return Result.error("错误");
        }

        try {
            String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

            String fileName = articleId + "." + fileExtension;

            String finalFilePath = filePath + fileName;
            File dest = new File(finalFilePath);
            file.transferTo(dest);
//            Article article = articleService.getById(articleId);
//            article.setImg(fileName);
//            articleService.saveOrUpdate(article);
            Article article = articleClient.apiGetArticle(articleId);
            article.setImg(fileName);
            articleClient.apiSaveArticle(article);
            return Result.success(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/upload/avatar")
    public Result<String> saveAvatar(@RequestHeader("UserId") String userId,@RequestParam("file") MultipartFile file){
//        String id = tokenUtil.extractSubjectFromToken(token);

        try {
            String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
//            LocalDateTime currentDateTime = LocalDateTime.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String fileName = userId + "." + fileExtension;
            // 指定要保存文件的路径

            // 确保上传目录存在，如果不存在则创建
            File uploadPath = new File(filePath);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            // 将文件保存到指定路径
            String filePath1 = filePath + fileName;
            File dest = new File(filePath1);
            file.transferTo(dest);


            // 更新用户的头像信息为文件路径或文件名
            User user = userClient.apiGetUser(userId);
            user.setAvatar(fileName);
            userClient.apiSaveUser(user);

            return Result.success("保存成功");
        } catch (IOException e) {
            return Result.error("保存失败");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("fileName") String fileName) {
        String uploadDir = filePath;
        String finalFilePath = uploadDir + fileName;
        // 使用绝对路径创建文件对象
        File file = new File(finalFilePath);

        try {
            // 从文件对象创建资源
            Resource resource = new org.springframework.core.io.UrlResource(file.toURI());

            // 返回文件资源
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            // 处理文件下载失败的逻辑
            return ResponseEntity.notFound().build();
        }
    }

}
