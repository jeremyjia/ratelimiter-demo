# ratelimiter-demo

说明：
基于SpringBoot实现了对API的限流功能，目前有一个可测试的API
http://localhost:8080/request?userlicense=jeremyjia
其中，userlicense参数代表对不同用户的限流
同时设计了JUnit单元测试用例, 运行mvn clean install的时候会执行测试用例

执行程序
1. 使用git clone <link>下载程序到本地
2. 进入pom.xml文件所在目录允许命令
   mvn clean install
   执行JUnit测试用例，并在target目录生成ratelimiter-demo-0.0.1-SNAPSHOT.jar
3. 运行
   jar -jar ratelimiter-demo-0.0.1-SNAPSHOT.jar
4. 在浏览器中
   http://localhost:8080 访问主页
   localhost:8080/request?userlicense=jeremy
   访问测试API
