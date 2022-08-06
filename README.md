# 项目简介
本项目主要是来自黑马的瑞吉外卖项目，前端采用的是Vue，项目注重于后端的接口开发，
本项目采用的是 SpringBoot和mybatis-plus框架进行开发，数据库用的是MySQL + Redis，MySQL主要是用来保存数据，Redis主要是用来做缓存，缓存的管理主要是用了Spring-Cache
接口文档的文档也是使用了Swagger来管理
# 环境准备
在运行该项目前，请先使用MySQL在根目录下运行db_reggie.sql，安装好Redis后，运行Redis
# 修改配置文件
application.yml
1. 可以修改运行端口，数据库的密码，Redis的密码等配置
2. 然后可以查看pom.xml，如果你需要进行修改成你本地的SpringBoot版本以及mybatis-plus等
# 运行
后端页面：
    http://localhost:8888/backend/page/login/login.html
    密码和账号默认自动填充了，可以自己手动修改
移动端页面:
    http://localhost:8888/front/page/login.html
    登录的验证码需要在运行窗口查看
接口文档页面:
    http://localhost:8888/doc.html
