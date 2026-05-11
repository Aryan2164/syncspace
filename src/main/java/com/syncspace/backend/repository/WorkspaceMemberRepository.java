package com.syncspace.backend.repository;

import com.syncspace.backend.entity.WorkspaceMember;
import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {

List<WorkspaceMember> findByWorkspaceId(Long workspaceId);
    boolean existsByWorkspaceIdAndUserId(Long workspaceId, Long userId);
    @Query("SELECT wm FROM WorkspaceMember wm WHERE wm.workspace.id = :workspaceId AND wm.user.id = :userId")
    WorkspaceMember findMember(Long workspaceId, Long userId);
}
