package org.anlntse.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.anlntse.bean.Role;

import java.util.List;

/**
 * Created by anlntse on 2017/12/17.
 */
@Mapper
public interface RolesMapper {
    public int addRoles(@Param("roles") String[] roles, @Param("uid") Long uid);

    public List<Role> getRolesByUid(Long uid);
}
