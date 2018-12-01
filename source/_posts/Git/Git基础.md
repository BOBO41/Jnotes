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

- 提交完了，发现漏掉了几个文件没有添加，或者提交信息写错了

```console
git commit --amend
```

这个命令会将暂存区中的文件提交。 如果自上次提交以来你还未做任何修改（例如，在上次提交后马上执行了此命令），那么快照会保持不变，而你所修改的只是提交信息。

- 取消暂存的文件，使用 `git reset HEAD <file>...` 来取消暂存。

```console
git reset HEAD CONTRIBUTING.md
```

- 取消对尚未暂存的文件的修改

如果你并不想保留对 `CONTRIBUTING.md` 文件的修改怎么办？ 你该如何方便地撤消修改 - 将它还原成上次提交时的样子（或者刚克隆完的样子，或者刚把它放入工作目录时的样子）？

```console
git checkout -- CONTRIBUTING.md
```

 `git checkout -- [file]` 是一个危险的命令。 你对那个文件做的任何修改都会消失 - 你只是拷贝了另一个文件来覆盖它。 除非你确实清楚不想要那个文件了，否则不要使用这个命令。