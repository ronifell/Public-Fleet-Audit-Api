package com.example.seagri.infra.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.seagri.infra.model.Group;
import com.example.seagri.infra.model.Permission;
import com.example.seagri.infra.model.User;

public record UserDTO (
    Long id,
    String fullName,
    String userName,
    String registrationNumber,
    String status,
    Boolean active,
    Set<Permission> permissions,
    List<Group> groups
) {

    public static UserDTO DTOFromEntity (User user) {
        if(user == null) return null;
        Set<Permission> permissions = new HashSet<Permission>();
        for(Group group : user.getGroups()) {
            group.getPermissions().forEach(perm -> permissions.add(perm));
        }
        UserDTO dto = new UserDTO(
            user.getId(),
            user.getFullName(),
            user.getUserName(),
            user.getRegistrationNumber(),
            user.getStatus(),
            user.getActive(),
            permissions,
            user.getGroups()
        );
        return dto;
    }

    public static String getPermissionsString (UserDTO dto) {
        Set<String> perms = new HashSet<String>();

        for(Permission perm: dto.permissions()){
            perms.add(perm.getName());
        };
        
        return perms.toString();
    }
}
