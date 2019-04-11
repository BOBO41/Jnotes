## 简介

nginx拥有一个主进程和一组工作进程。主进程负责读取和解析配置以及维护工作进程。工作进程则用来处理请求。

## 运行nginx

通过可执行文件启动nginx，nginx启动后，你可以通过以下命令来控制它。

```shell
nginx -s option
# stop — fast shutdown
# quit — graceful shutdown
# reload — reloading the configuration file
# 读取并验证配置文件，如果通过则创建新的工作进程，并且通知旧的工作进程退出
# reopen — reopening the log files
```

更多详情请看[控制nginx](http://nginx.org/en/docs/control.html)

## 配置文件基本结构

nginx的配置文件默认叫做`nginx.conf`，分别存放在`/usr/local/nginx/conf`, `/etc/nginx`,  `/usr/local/etc/nginx`等目录下。

nginx由多个模块所组成，模块的行为通知配置文件中编写的指令来控制。指令分为两种，简单指令和块指令。

```nginx
# 简单指令 name parameters;
worker_processes auto;
# 块指令
events {
    worker_connections 768;
    # multi_accept on;
}
# If a block directive can have other directives inside braces, it is called a context (examples: events, http, server, and location).
# Directives placed in the configuration file outside of any contexts are considered to be in the main context.The events and http directives reside in the main context, server in http, and location in server.
```

## 提供静态资源

Web服务器的一个基本功能就是提供静态资源。

```shell
mkdir -p /data/www # 存放html
mkdir -p /data/images # 存放图片
```

修改nginx.conf，在`http{}`中添加以下内容

```nginx
server {
    location / {
        root /data/www; 
        # root 设置请求的根目录
        # location /i/ {
 		#   root /data/w3;
		# } 
        # 请求 “/i/top.gif” 返回 /data/w3/i/top.gif
    }

    location /images/ {
        root /data/images;
    }
}
```

```nginx
# 把http中的这两行指令注释掉，它们是用来导入配置的
# include /etc/nginx/conf.d/*.conf;
# include /etc/nginx/sites-enabled/*;
```

```shell
# 重新读取配置
nginx -s reload
```

## 代理服务器

We will configure a basic proxy server, which serves requests of images with files from the local directory and sends all other requests to a proxied server.

```shell
server {
	location / {
		proxy_pass http://localhost:8080;
	}

	# ~ 后面跟的是正则表达式
	location ~ \.(gif|jpg|png)$ {
 	   root /data/images;
	}
	
}

#添加一个新的server
server {
	# 监听8080端口，不设置的话默认80端口
    listen 8080; 
    # 如果location中没有设置root指令，默认使用这个
    root /data/tomcat; 
    # 所有请求映射到/data/tomcat目录
    location / {
    }
}
```

[更多代理相关的指令](http://nginx.org/en/docs/http/ngx_http_proxy_module.html)

