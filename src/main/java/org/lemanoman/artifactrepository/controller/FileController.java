package org.lemanoman.artifactrepository.controller;

import org.lemanoman.artifactrepository.dto.ArtifactDTO;
import org.lemanoman.artifactrepository.dto.ResponseDTO;
import org.lemanoman.artifactrepository.model.Artifact;
import org.lemanoman.artifactrepository.service.ArtifactService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/file")
public class FileController {
    private ArtifactService service;

    public FileController(ArtifactService service) {
        this.service = service;
    }

    final private Logger logger = org.slf4j.LoggerFactory.getLogger(FileController.class);


    @Value("${artifact-repository.path}")
    private String repositoryPath;

    @GetMapping("/")
    public ResponseDTO listFiles() {
        return ResponseDTO.ok(service.findAll().stream().map(ArtifactDTO::fromModel).toList());
    }

    @GetMapping("/groups/")
    public ResponseDTO listGroup() {
        return ResponseDTO.ok(service.findGroups());
    }

    @GetMapping("/groups/{group}")
    public ResponseDTO listArtifactByGroup(@PathVariable String group) {
        return ResponseDTO.ok(service.findByArtifactGroup(group).stream().map(ArtifactDTO::fromModel).toList());
    }

    @GetMapping("/groups/{group}/{version}")
    public ResponseDTO findLatestFileByGroupAndVersion(@PathVariable String group, @PathVariable String version) {
        if(group==null || group.isEmpty()){
            logger.error("Group is required");
            return ResponseDTO.fail("Group is required");
        }
        Artifact artifact = service.findFirstByVersion(version);
        if (artifact == null) {
            logger.error("Arquivo nao encontrado {}", group);
            return ResponseDTO.fail("Arquivo nao encontrado");
        }
        return ResponseDTO.ok(ArtifactDTO.fromModel(artifact));
    }

    @GetMapping("/groups/{group}/{version}/download")
    public ResponseEntity<Resource> downloadLatestFileByGroupAndVersion(@PathVariable String group, @PathVariable String version) {
        if(group==null || group.isEmpty()){
            logger.error("Group is required");
            return ResponseEntity.notFound().build();
        }
        Artifact artifact = service.findFirstByVersion(version);
        if (artifact == null) {
            logger.error("Arquivo nao encontrado {}", group);
            return ResponseEntity.notFound().build();
        }
        return getResourceResponseEntity(artifact);
    }

    @GetMapping("/groups/{group}/download")
    public ResponseEntity<Resource> downloadLatestFileByGroup(@PathVariable String group) {
        if(group==null || group.isEmpty()){
            logger.error("Group is required");
            return ResponseEntity.notFound().build();
        }
        Artifact artifact = service.findFirstByVersion("latest");
        if (artifact == null) {
            artifact = service.findLatestUpload();
        }
        if (artifact == null) {
            logger.error("Arquivo nao encontrado {}", group);
            return ResponseEntity.notFound().build();
        }
        return getResourceResponseEntity(artifact);
    }

    private ResponseEntity<Resource> getResourceResponseEntity(Artifact artifact) {
        File file = new File(artifact.getPath());
        if (!file.exists()) {
            logger.error("Arquivo nao encontrado {}", file.getAbsolutePath());
            return ResponseEntity.notFound().build();
        }
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + file.getName())
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{key}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable String key) {
        if(key==null || key.isEmpty()){
            logger.error("Key is required");
            return ResponseEntity.notFound().build();
        }
        Artifact artifact = service.findById(key);
        if (artifact == null) {
            logger.error("Arquivo nao encontrado {}", key);
            return ResponseEntity.notFound().build();
        }
        return getResourceResponseEntity(artifact);
    }

    private ResponseDTO uploadFile(InputStream inputStream, String originalName,  String name, String description, String version, String buildNumber, String key, String group) {
        if (key == null || key.isEmpty()) {
            logger.error("Key is required");
            return ResponseDTO.fail("Key is required");
        }
        if(group==null || group.isEmpty()){
            logger.error("Group is required");
            return ResponseDTO.fail("Group is required");
        }


        if (originalName == null || originalName.isEmpty() && (name == null || name.isEmpty())) {
            logger.error("Original name is empty, name is required");
            return ResponseDTO.fail("Original name is empty, name is required");
        }
        if (name != null && !name.isEmpty()) {
            originalName = name;
        }

        File destinationMain = new File(repositoryPath);
        if (!destinationMain.exists() && !destinationMain.mkdirs()) {
            logger.error("Nao foi possivel criar o diretorio {}", destinationMain.getAbsolutePath());
            return ResponseDTO.fail("Diretorio invalido");
        }
        File targetDir = new File(destinationMain, group);
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            logger.error("Nao foi possivel criar o diretorio {}", targetDir.getAbsolutePath());
            return ResponseDTO.fail("Diretorio invalido");
        }
        File targetFile = new File(targetDir, originalName);

        try (
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
                BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

        ) {

            byte[] buffer = new byte[1048576];
            logger.info("Tentando com buff de " + buffer.length);
            int count;
            while ((count = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, count);
            }
            logger.info("Dando um flush");
            bos.flush();


        } catch (IOException e) {
            logger.error("Nao foi possivel salvar o arquivo {}", targetFile.getAbsolutePath(), e);
            return ResponseDTO.fail("Nao foi possivel salvar o arquivo");
        }

        Artifact artifact = new Artifact();
        artifact.setId(key);
        artifact.setArtifactGroup(group);
        artifact.setName(originalName);
        artifact.setDateCreated(new Date());
        artifact.setDescription(description);
        artifact.setPath(targetFile.getAbsolutePath());
        artifact.setVersion(version);
        artifact.setBuildNumber(buildNumber);

        try {
            artifact.setChecksum(getChecksum(targetFile.getAbsolutePath()));
        } catch (Exception ex) {
            logger.warn("Nao foi possivel calcular o checksum do arquivo {}", targetFile.getAbsolutePath());
            artifact.setChecksum("invalid");
        }

        try {
            service.saveOrUpdate(artifact);
        } catch (Exception ex) {
            logger.error("Nao foi possivel salvar registro no banco {}", ArtifactDTO.fromModel(artifact), ex);
            return ResponseDTO.fail("Nao foi possivel salvar o arquivo");
        }
        return ResponseDTO.ok(artifact);
    }

    @PostMapping("/upload/{key}")
    public ResponseDTO handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "group") String group,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "version", required = false) String version,
            @RequestParam(value = "buildNumber", required = false) String buildNumber,
            @PathVariable String key) {
        if (file.isEmpty()) {
            logger.error("File is required");
            return ResponseDTO.fail("File is required");
        }

        ResponseDTO response = null;
        try {
            response = uploadFile(file.getInputStream(), file.getOriginalFilename(), name, description, version, buildNumber, key, group);
        } catch (IOException e) {
            return ResponseDTO.fail("Nao foi possivel ler o arquivo");
        }
        /**
        if(response.isSuccess()){
            ObjectMapper mapper = new ObjectMapper();
            try {
                byte[] byteArrayJson = mapper.writeValueAsBytes(response.getData());
                InputStream inputStream = new ByteArrayInputStream(byteArrayJson);
                uploadFile(inputStream, "metadata.json", name, description, version, buildNumber, key+".metadata", group);
            } catch (Exception e) {
                logger.error("Nao foi possivel salvar o metadata",e);
            }
        }
        **/
        return response;
    }

    public static String getChecksum(String filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath))) {
            byte[] bytes = new byte[1048576];
            int bytesRead;
            while ((bytesRead = bis.read(bytes)) != -1) {
                digest.update(bytes, 0, bytesRead);
            }
        }
        byte[] hash = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
