---
title : 
categories : 
- JavaSE
date : 1997-01-25
---

# 简介

# 镜像

```bash
# 查看本地镜像
docker iamges ls
# 搜索镜像
docker search ubuntu
# 获取镜像
docker pull [opt] [Docker Registry address[:port]/] repositoryName[:label]
docker pull ubuntu
# 删除镜像
docker image rm [选项] <镜像1> [<镜像2> ...]
# 其中，<镜像> 可以是 镜像短 ID、镜像长 ID、镜像名 或者 镜像摘要。
```

# 容器

```bash
# 创建容器
docker run ubuntu
# 查看容器
docker container ls -a
# 启动容器 
docker start  容器ID
# 进入容器
docker attach 容器ID
# 终止容器
docker container stop 容器ID
# 删除容器
docker container rm 容器ID
```

# 网络配置

```bash
# 端口映射 本地的 5000 端口映射到容器的 80 端口
docker run  -p 5000:80 ubuntu
```

