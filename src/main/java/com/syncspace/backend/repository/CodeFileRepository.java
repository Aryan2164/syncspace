package com.syncspace.backend.repository;

import com.syncspace.backend.entity.CodeFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeFileRepository extends JpaRepository<CodeFile ,Long> {


    List<CodeFile> findByWorkspaceId(Long WorkspaceId);
}
