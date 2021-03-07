package com.spring.security.service;

import com.spring.security.entity.Customer;
import com.spring.security.entity.Permission;
import com.spring.security.mapper.CustomerMapper;
import com.spring.security.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LubanUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private CustomerMapper userMapper;
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Customer customer = userMapper.getUserByName(username);
        if(customer == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        Permission permission = permissionMapper.getPermissionById(customer.getId());
        //密码
        String encode = passwordEncoder.encode(customer.getPassword());
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permission.getEnname());

        return new User(username,encode, grantedAuthorities);
    }
}
