package com.syncspace.backend.DTO;

import com.syncspace.backend.enums.WorkspaceRole;

public class AddMemberRequest {
    private String email;
    private WorkspaceRole role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public WorkspaceRole getRole() {
        return role;
    }

    public void setRole(WorkspaceRole role) {
        this.role = role;
    }
}
