package com.syncspace.backend.controller;


import com.syncspace.backend.DTO.AddMemberRequest;
import com.syncspace.backend.DTO.WorkspaceMemberResponse;
import com.syncspace.backend.DTO.WorkspaceRequest;
import com.syncspace.backend.entity.Workspace;
import com.syncspace.backend.service.WorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService){
        this.workspaceService = workspaceService;
    }

    @PostMapping
    public Workspace createWorkspace(@RequestBody WorkspaceRequest request){
        return workspaceService.createWorkspace(request);
    }

    @GetMapping("/my")
    public List<Workspace> getMyWorkspaces(Authentication authentication){
        return workspaceService.getMyWorkspaces(authentication);
    }

    @GetMapping("/{id}")
    public Workspace getWorkspace(@PathVariable Long id,
                                  Authentication authentication){

        return workspaceService.getWorkspaceById(id, authentication);
    }

    @PutMapping("/{id}")
    public Workspace updateWorkspace(@PathVariable Long id,
                                     @RequestBody WorkspaceRequest request,
                                     Authentication authentication){

        return workspaceService.updateWorkspace(id, request, authentication);
    }

    @DeleteMapping("/{id}")
    public String deleteWorkspace(@PathVariable Long id,
                                  Authentication authentication){

        workspaceService.deleteWorkspace(id, authentication);

        return "Workspace deleted successfully";
    }

    @PostMapping("/{id}/members")
    public String member(@PathVariable Long id,
                         @RequestBody AddMemberRequest request,
                         Authentication authentication
                         ){
      return workspaceService.addMember(id, request, authentication);
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<WorkspaceMemberResponse>> getWorkspaceMembers(@PathVariable Long id,
                                                                             Authentication authentication
                                                                             ){
        return ResponseEntity.ok(workspaceService.getWorkspaceMembers(id, authentication));
    }

    @DeleteMapping("/{workspaceId}/members/{userId}")
    public String removeMember(@PathVariable Long workspaceId,
                               @PathVariable Long userId,
                               Authentication authentication
                               ){
            return workspaceService.removeMember(
                    workspaceId,
                    userId,
                    authentication
            );
    }
}
