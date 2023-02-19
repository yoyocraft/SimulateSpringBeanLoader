# SimulateSpringBeanLoader
Use custom annotation and config class to simulate the process of spring load class to container

## 类说明
- MySpringApplicationContext => Spring容器
- MySpringConfig => 类似Spring的xml配置文件
- MyComponentScan => 指定扫描包

## 实现过程
- 搭建基本结构 && 获取扫描包
- 获取扫描包下所有的`.class`文件
- 获取全类名 && 反射创建对象 && 存入容器