#关于
本项目为一个娱乐项目，使用Python [Scrapy](http://scrapy.org/)爬取 [糗事百科](http://www.qiushibaike.com/)前35页热门笑话，并将其保存到MySQL数据库中，然后使用Java编写的一个客户端读取其内容并显示的小应用；用于学习Python Scrapy插件。

#目录结构
*  Java ------- Java糗百客户端程序
*  Python ----- Python Scrapy糗百爬取程序

#Java编译
*  `cd QiuShiBaike/Java/src`
*  `javac -classpath . -encoding utf8  Main.java`

#Java运行
*  `java -classpath <mysql驱动>.jar;. Main`

#Python依赖模块
* service_identity
* pywin32(windows环境)
* mysql.connector
* PIL

#注意
1. Java糗百客户端程序使用[One-jar](http://one-jar.sourceforge.net/)打包，使用方法参见：http://lzqwebsoft.net/show/20140809085836.html
2. 在使用打包好的Java/bin/qiushiclient-v1.0.jar文件时，需修改qiushiclient-v1.0.jar/main/qiushiclient.jar/config.properties文件中的数据库连接配置文件。
3. 运行Python服务端应用时先修改tutorial/tutorial/settings.py数据库配置，及爬取的图片文件存放目录路径IMAGES_STORE：

<pre>
IMAGES_STORE = '/root/Images/QiuShiBaiKe'

DATABASE = {
     'host' : 'localhost',
     'port' : 3306,
     'user' : 'root',
     'password' : '123456',
     'database' : 'scrape',
     'raise_on_warnings': True,
}
</pre>

# 运行截图
<img src="http://ww4.sinaimg.cn/large/a3498d1egw1ej54fuxxpdj20rs0nnagu.jpg" width="800"/>
