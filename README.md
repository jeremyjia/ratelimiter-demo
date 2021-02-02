# ratelimiter-demo

说明:
基于SpringBoot实现了对API的限流功能，目前有一个可测试的API
http://localhost:8080/request?userlicense=jeremyjia
其中，userlicense参数代表对不同用户的限流
同时设计了JUnit单元测试用例, 对上面的API进行了并发下的限流测试,运行mvn clean install的时候会执行测试用例

执行程序:
1. 使用git clone https://github.com/jeremyjia/ratelimiter-demo.git 下载程序到本地
2. 进入pom.xml文件所在目录,运行命令
   mvn clean install
   执行JUnit测试用例，并在target目录下生成ratelimiter-demo-0.0.1-SNAPSHOT.jar
3. 运行命令启动应用
   jar -jar ratelimiter-demo-0.0.1-SNAPSHOT.jar
4. 在浏览器中输入
   http://localhost:8080 访问主页
   输入 localhost:8080/request?userlicense=jeremyjia
   访问测试API
