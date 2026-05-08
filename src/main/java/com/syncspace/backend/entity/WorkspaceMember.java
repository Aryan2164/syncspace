package com.syncspace.backend.entity;


import com.syncspace.backend.enums.WorkspaceRole;
import jakarta.persistence.*;

@Entity
public class WorkspaceMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    private Workspace workspace;

    @ManyToOne
    private User user;

   @Enumerated(EnumType.STRING)
    private WorkspaceRole role;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Workspace getWorkspace(){
        return workspace;
    }
    public  void setWorkspace(Workspace workspace){
        this.workspace=workspace;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WorkspaceRole getRole() {
        return role;
    }

    public void setRole(WorkspaceRole role) {
        this.role=role;

    }

    public WorkspaceMember() {
    }
}
