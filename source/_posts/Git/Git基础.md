---
title : 2.Git基础
categories : 
- Git
- 
date : 2018-11-29
---

# Git基础

## 仓库

### 创建仓库

```git
git init
```

### 获取仓库

```git
# git clone [url]
git clone https://github.com/libgit2/libgit2
```

## 记录每个更新到仓库

在Git仓库里面，文件有两种类型，分别是已跟踪和未跟踪。

对于已跟踪的文件又有三种类型，分别是为修改、已修改、暂存

![](https://git-scm.com/book/en/v2/images/lifecycle.png)

## 查看提交历史

`git log` 会按提交时间列出所有的更新，最近的更新排在最上面。 

`git config --global log.date iso` 修改时间样式

`-p`，用来显示每次提交的内容差异。 你也可以加上 `-2` 来仅显示最近两次提交

```console
git log -p -2
```

`--stat` 选项在每次提交的下面列出所有被修改过的文件、有多少文件被修改了以及被修改过的文件的哪些行被移除或是添加了。 在每次提交的最后还有一个总结。

## 撤销操作

