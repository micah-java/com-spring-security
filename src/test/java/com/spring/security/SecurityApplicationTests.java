package com.spring.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootTest
class SecurityApplicationTests {

    @Test
    void contextLoads() {

        String gensalt = BCrypt.gensalt();
        String hashpw = BCrypt.hashpw("123456", gensalt);
        System.out.println("gensalt: " + gensalt);//$2a$10$UuPnepiqk2x7qBMVkXQhru
        System.out.println("hashpw: " + hashpw);//$2a$10$UuPnepiqk2x7qBMVkXQhruhMVRBcUKQjMdweutnqjPtJYFnS60fc.

        boolean checkpw = BCrypt.checkpw("123456", "$2a$10$UuPnepiqk2x7qBMVkXQhruhMVRBcUKQjMdweutnqjPtJYFnS60fc.");
        System.out.println("checkpw: " + checkpw);

    }

}
