package com.spring.security.config;

import com.spring.security.adapter.SmsSecurityConfigureAdapter;
import com.spring.security.filter.JsonLoginAuthenticationFilter;
import com.spring.security.filter.SmsLoginAuthenticationFilter;
import com.spring.security.handler.WebAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private WebAuthenticationSuccessHandler webAuthenticationSuccessHandler;

    @Autowired
    private SmsSecurityConfigureAdapter smsSecurityConfigureAdapter;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("java")
                .password("{noop}java")
                .roles("admin")
                ;
    }

    /*@Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }*/

    @Bean
    public JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter() throws Exception {
        JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter = new JsonLoginAuthenticationFilter();
        jsonLoginAuthenticationFilter.setAuthenticationSuccessHandler(webAuthenticationSuccessHandler);
        //jsonLoginAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        jsonLoginAuthenticationFilter.setAuthenticationManager(authenticationManager());
        jsonLoginAuthenticationFilter.setFilterProcessesUrl("/customer/login");
        return jsonLoginAuthenticationFilter;
    }

    //@Bean
    public SmsLoginAuthenticationFilter smsLoginAuthenticationFilter() throws Exception {
        SmsLoginAuthenticationFilter smsLoginAuthenticationFilter = new SmsLoginAuthenticationFilter();
        smsLoginAuthenticationFilter.setAuthenticationSuccessHandler(webAuthenticationSuccessHandler);
        smsLoginAuthenticationFilter.setAuthenticationManager(authenticationManager());
        smsLoginAuthenticationFilter.setFilterProcessesUrl("/sms/login");

        return smsLoginAuthenticationFilter;
    }

    //@Bean
    JdbcTokenRepositoryImpl jdbcTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    /**
     * JSON登录
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //.and()
                //.rememberMe()
                //.tokenRepository(jdbcTokenRepository())
                .and()
                /**
                 * 1、浏览器访问用户信息接口，由于地址需要认证才能访问，此时服务器会返回302，要求重定向到2中的地址
                 * http://localhost:8080/user
                 * 2、当浏览器请求该地址时，检测到这是一个授权请求，于是再次返回302，要求浏览器重定向到3中的github授权页面地址
                 * http://localhost:8080/oauth2/authorization/github
                 * 3、浏览器请求该地址，用户可能会输入用户密码进行登录认证，完成认证后github授权服务器要求浏览器重定向到4中的提前配置好的回调地址上
                 * https://github.com/login/oauth/authorize?response_type=code&client_id=82d432900dc2e077c8b4&scope=read:user&state=5Aj_CN3rDHQCtsc0NbF12l6iuDXslxe5fjCLCBdF8z4%3D&redirect_uri=http://localhost:8080/login/oauth2/code/github
                 * 4、浏览器通过guthub返回的code后请求服务器，将code传给服务器
                 * http://localhost:8080/login/oauth2/code/github?code=08d69dd70eec6d93801e&state=5Aj_CN3rDHQCtsc0NbF12l6iuDXslxe5fjCLCBdF8z4%3D
                 * 5、服务器通过code再去请求github授权服务器来获取access_token
                 * https://github.com/login/oauth/access_token
                 * 6、服务器拿到access_token后就可以再向该地址发送请求返回github用户信息
                 * https://api.github.com/user
                 */
                .oauth2Login()
                .loginProcessingUrl("/oauth2/code/github")
                .successHandler(webAuthenticationSuccessHandler)
                .and()
                .csrf().disable()
        ;
                //去除默认登录页面
        //http.removeConfigurer(DefaultLoginPageConfigurer.class);
        //将LoginFilter添加到UsernamePasswordAuthenticationFilter位置
        http.addFilterAt(jsonLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.apply(smsSecurityConfigureAdapter);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/js/**")
                .antMatchers("/*.html");
    }
}
