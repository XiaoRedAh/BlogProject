package com.xiaored.vo;

import com.xiaored.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleInfoVo {
    List<Long> roleIds;
    List<Role> roles;
    UpdateUserVo user;
}
