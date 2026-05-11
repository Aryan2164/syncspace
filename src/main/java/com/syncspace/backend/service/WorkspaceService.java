package com.syncspace.backend.service;


import com.syncspace.backend.DTO.AddMemberRequest;
import com.syncspace.backend.DTO.WorkspaceMemberResponse;
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


import java.util.ArrayList;
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

    public List<WorkspaceMemberResponse> getWorkspaceMembers(Long workspaceId,
                                                             Authentication authentication){

        String email = authentication.getName();

        User user = userRepository.findByEmail(email);

        System.out.println("Logged user id: " + user.getId());
        System.out.println("Workspace id: " + workspaceId);


        WorkspaceMember existingMember =
                workspaceMemberRepository.findMember(
                        workspaceId,
                        user.getId()
                );

        boolean isMember = existingMember != null;

        System.out.println("Is member: " + isMember);

        if(!isMember){
            throw new RuntimeException("Access denied");
        }

        List<WorkspaceMember> members =
                workspaceMemberRepository.findByWorkspaceId(workspaceId);

        System.out.println("Members size: " + members.size());

        List<WorkspaceMemberResponse> responses = new ArrayList<>();

        for (WorkspaceMember member : members){

            System.out.println(member.getUser().getEmail());

            WorkspaceMemberResponse response =
                    new WorkspaceMemberResponse();

            response.setUserId(member.getUser().getId());

            response.setName(member.getUser().getName());

            response.setEmail(member.getUser().getEmail());

            response.setRole(member.getRole());

            responses.add(response);
        }

        return responses;
    }

    public String removeMember(Long workspaceId,
                               Long userId,
                               Authentication authentication){
    String email = authentication.getName();
    User user = userRepository.findByEmail(email);
    Workspace workspace =  workspaceRepository.findById(workspaceId)
            .orElseThrow(()-> new RuntimeException("member not deleted"));
    if(!workspace.getOwner().getId().equals(user.getId())){
        throw new RuntimeException("Access denied");
    }
        WorkspaceMember workspaceMember =
                workspaceMemberRepository.findMember(
                        workspaceId,
                        userId
                );
        if(workspaceMember == null){
            throw new RuntimeException("Member not found");
        }
        workspaceMemberRepository.delete(workspaceMember);
        return "member removed successfully";
    }

}
