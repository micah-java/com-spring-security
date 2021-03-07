package com.spring.security.mapper;

import com.spring.security.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomerMapper {

    Customer getUserByName(@Param("username") String username);
}
