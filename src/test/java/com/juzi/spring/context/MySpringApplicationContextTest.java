package com.juzi.spring.context;

import com.juzi.spring.component.UserService;
import com.juzi.spring.config.MySpringConfig;
import org.junit.Test;

/**
 * @author codejuzi
 */
public class MySpringApplicationContextTest {

    @Test
    public void testIOC() {
        MySpringApplicationContext ioc = new MySpringApplicationContext(MySpringConfig.class);
        UserService userService = (UserService) ioc.getBean("myUserService");
        System.out.println("userService = " + userService);
    }

}