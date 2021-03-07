package com.spring.security.mapper;

import com.spring.security.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PermissionMapper {

    Permission getPermissionById(@Param("customerId") Integer customerId);
}
