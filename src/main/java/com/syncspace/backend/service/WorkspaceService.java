package com.syncspace.backend.service;


import com.syncspace.backend.DTO.AddMemberRequest;
import com.syncspace.backend.entity.Workspace;
import com.syncspace.backend.entity.WorkspaceMember;
import com.syncspace.backend.enums.WorkspaceRole;
import com.syncspace.backend.repository.UserRepository;
import com.syncspace.backend.repository.WorkspaceMemberRepository;
import com.syncspace.backend.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import com.syncspace.backend.DTO.WorkspaceRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.syncspace.backend.entity.User;

import java.util.List;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository, UserRepository userRepository, WorkspaceMemberRepository workspaceMemberRepository){
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.workspaceMemberRepository=workspaceMemberRepository;
    }


    public Workspace createWorkspace(WorkspaceRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        Workspace workspace = new Workspace();
        workspace.setOwner(user);

        workspace.setName(request.getName());
        workspace.setDescription(request.getDescription());
        Workspace savedWorkspace = workspaceRepository.save(workspace);


        WorkspaceMember member = new WorkspaceMember();
        member.setWorkspace(savedWorkspace);
        member.setUser(user);
        member.setRole(WorkspaceRole.OWNER);
         workspaceMemberRepository.save(member);
         return savedWorkspace;
    }

    public List<Workspace> getMyWorkspaces(Authentication authentication){
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        return  workspaceRepository.findByOwnerId(user.getId());
    }
    public Workspace getWorkspaceById(Long id, Authentication authentication){

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(()->  new RuntimeException("Workspace not found"));
        if(!workspace.getOwner().getId().equals(user.getId())){
            throw new RuntimeException("Access denied");
        }
        return workspace;
    }
    public Workspace updateWorkspace(Long id, WorkspaceRequest request, Authentication authentication){
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(()->  new RuntimeException("Workspace not found"));
        if(!workspace.getOwner().getId().equals(user.getId())){
            throw new RuntimeException("Access denied");
        }
        workspace.setName(request.getName());
        workspace.setDescription(request.getDescription());
        return workspaceRepository.save(workspace);
    }
    public void deleteWorkspace(Long id, Authentication authentication){
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(()->  new RuntimeException("Workspace not found"));
        if(!workspace.getOwner().getId().equals(user.getId())){
            throw new RuntimeException("Access denied");
        }
        workspaceRepository.delete(workspace);
    }
    public String addMember(Long id,
                            AddMemberRequest request,
                            Authentication authentication){

        String email = authentication.getName();

        User user = userRepository.findByEmail(email);

        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        if(!workspace.getOwner().getId().equals(user.getId())){
            throw new RuntimeException("Access denied");
        }

        String memberEmail = request.getEmail();

        User memberUser = userRepository.findByEmail(memberEmail);

        WorkspaceMember workspaceMember = new WorkspaceMember();

        workspaceMember.setWorkspace(workspace);

        workspaceMember.setUser(memberUser);

        workspaceMember.setRole(request.getRole());

        workspaceMemberRepository.save(workspaceMember);

        return "Member added successfully";
    }
}
