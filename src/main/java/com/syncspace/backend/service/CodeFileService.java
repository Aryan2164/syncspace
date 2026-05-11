package com.syncspace.backend.service;

import com.syncspace.backend.entity.CodeFile;
import com.syncspace.backend.entity.User;
import com.syncspace.backend.entity.Workspace;
import com.syncspace.backend.repository.CodeFileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class CodeFileService {

 private final CodeFileRepository codeFileRepository;

 public CodeFileService(CodeFileRepository codeFileRepository){
     this.codeFileRepository= codeFileRepository;
 }


 public CodeFile createCodeFile( String fileName,
                                 String language,
                                 Workspace workspace,
                                 User user,
                                 String content
                                 ){
     CodeFile codeFile = new CodeFile();
     codeFile.setFileName(fileName);
     codeFile.setLanguage(language);
     codeFile.setWorkspace(workspace);
     codeFile.setCreatedBy(user);
     codeFile.setContent(content);
     codeFile.setCreatedAt(LocalDateTime.now());
     codeFile.setUpdatedAt(LocalDateTime.now());
     return codeFileRepository.save(codeFile);
 }

}
