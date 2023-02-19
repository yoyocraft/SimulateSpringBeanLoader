package com.juzi.spring.context;

import com.juzi.spring.annotation.MyComponentScan;
import com.juzi.spring.config.MySpringConfig;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模拟Spring加载Bean过程
 *
 * @author codejuzi
 */
public class MySpringApplicationContext {
    /**
     * 保存配置类对象，获取扫描包
     */
    private final Class<MySpringConfig> configClass;

    /**
     * 存放反射创建的对象的容器
     */
    private final ConcurrentHashMap<String, Object> singletonObjects;

    {
        singletonObjects = new ConcurrentHashMap<>();
    }

    public MySpringApplicationContext(Class<MySpringConfig> configClass) {
        this.configClass = configClass;

        //获取扫描包
        MyComponentScan componentScan
                = this.configClass.getDeclaredAnnotation(MyComponentScan.class);

        String originalPath = componentScan.value();

        // 得到类加载器
        ClassLoader classLoader = this.getClass().getClassLoader();
        // 得到扫描包的资源URL
        String proPath = originalPath.replace(".", "/");
        URL resource = classLoader.getResource(proPath);
        // 遍历需要加载的资源路径下的文件
        assert resource != null;
        File resourceFile = new File(resource.getFile());
        if (resourceFile.isDirectory()) {
            File[] files = resourceFile.listFiles();
            assert files != null;
            for (File file : files) {
                String absoluteFilePath = file.getAbsolutePath();
                // 过滤.class文件
                if (absoluteFilePath.endsWith(".class")) {
                    // 获取到类名
                    String className =
                            absoluteFilePath.substring(absoluteFilePath.lastIndexOf("/") + 1, absoluteFilePath.indexOf(".class"));
                    // 获取类的完整路径（全类名）
                    String classFullName = originalPath + "." + className;
                    // 判断该类是否需要被注入容器
                    try {
                        Class<?> aClass = classLoader.loadClass(classFullName);
                        if (aClass.isAnnotationPresent(Component.class) ||
                                aClass.isAnnotationPresent(Controller.class) ||
                                aClass.isAnnotationPresent(Service.class) ||
                                aClass.isAnnotationPresent(Repository.class)) {


                            // 反射生成对象，注入容器
                            Class<?> clazz = Class.forName(classFullName);
                            Object instance = clazz.newInstance();
                            singletonObjects.put(StringUtils.uncapitalize(className), instance);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 获取bean
     */
    public Object getBean(String className) {
        return singletonObjects.get(className);
    }
}
