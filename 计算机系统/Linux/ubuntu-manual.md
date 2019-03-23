# Ubuntu Manual

- 上代理
- 基本系统设置
- 安装软件

## 代理

### v2ray

```shell
sudo mkdir -p /home/hdr/app/tools/v2ray

cd /home/hdr/app/tools/v2ray

sudo apt install python-pip -y
sudo pip install genpac
genpac --format pac --gfwlist-url=https://raw.githubusercontent.com/gfwlist/gfwlist/master/gfwlist.txt --pac-proxy="SOCKS5 127.0.0.1:1080"  --output="autoproxy.pac"

#开机启动
sudo cat > /etc/init.d/excu_on_start.sh <<EOL
#!/bin/bash
### BEGIN INIT INFO
# Provides:          svnd.sh
# Required-start:    $local_fs $remote_fs $network $syslog
# Required-Stop:     $local_fs $remote_fs $network $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: starts the svnd.sh daemon
# Description:       starts svnd.sh using start-stop-daemon
### END INIT INFO
sh /home/hdr/app/tools/v2ray/v2ray --config=/home/hdr/app/tools/v2ray/config.json
EOL

#使文件可执行
cd /etc/init.d/
sudo chmod 775 excu_on_start.sh
#设置启动顺序
sudo update-rc.d excu_on_start.sh defaults 95
#停止启动
#cd /etc/init.d
#sudo update-rc.d -f excu_on_start.sh remove

sudo chown -R hdr /home/hdr/app/tools
sudo chgrp -R hdr /home/hdr/app/tools

#设置系统使用
gsettings set org.gnome.system.proxy mode 'auto'
gsettings set org.gnome.system.proxy autoconfig-url "file:///home/hdr/app/tools/v2ray/autoproxy.pac"

echo "please install v2ray-core to /home/hdr/app/tools/v2ray"
```



## 系统基本设置

### gnome前期准备

```bash
# 下载浏览器插件
https://extensions.gnome.org/
# 下载chrome-gnome-shell
sudo apt install chrome-gnome-shell
sudo apt install gnome-shell-extensions
```

### 安装主题

```bash
mkdir ~/.themes
# 主题 现在用的是Macterial
```

### 安装papirus图标

```shell
sudo add-apt-repository ppa:papirus/papirus 
sudo apt-get update 
sudo apt-get install papirus-icon-theme
sudo apt install gnome-icon-theme
```

### 安装字体

```shell
下载ttf字体文件,然后在/usr/share/fonts下新建目录,把新字体放进该目录
进入新目录
sudo chmod 775 字体文件
sudo mkfontscale
sudo mkfontdir
sudo fc-cache -fv 
```

### 安装插件

```shell
# 安装系统监视器
sudo apt-get remove indicator-multiload
sudo apt-get install gir1.2-gtop-2.0 gir1.2-networkmanager-1.0  gir1.2-clutter-1.0
搜索system-monitor插件
# 安装dash to panle
```

### Termintor + oh my zsh

```
https://gnometerminator.blogspot.com/p/introduction.html
https://github.com/robbyrussell/oh-my-zsh
```

## 常用工具

### 安装VIm

```shell
sudo apt-get remove vim-common
sudo apt-get install vim
```

### 安装Typora

```shell
# or run:
# sudo apt-key adv --keyserver keyserver.ubuntu.com--recv-keys BA300B7755AFCFAE
wget -qO - https://typora.io/linux/public-key.asc | sudo apt-key add -

# add Typora's repository
sudo add-apt-repository 'deb https://typora.io/linux ./'
sudo apt-get update

# install typora
sudo apt-get install typora
```

### 安装Pomodoro

```shell
sudo apt-get install gnome-shell-pomodoro
```

### 安装XMIND

```shell
https://www.xmind.net/download/
```



### 安装网易云

```shell
wget http://d1.music.126.net/dmusic/netease-cloud-music_1.1.0_amd64_ubuntu.deb
sudo dpkg -i netease-cloud-music_1.1.0_amd64_ubuntu.deb
sudo apt-get -f install
sudo dpkg -i netease-cloud-music_1.1.0_amd64_ubuntu.deb
# 额外设置
sudo gedit /etc/sudoers
修改/etc/sudoers文件，在文件最下面加一行：
YOURNAME ALL = NOPASSWD: /usr/bin/netease-cloud-music
YOURNAME为你登录的用户名。

sudo gedit /usr/share/applications/netease-cloud-music.desktop
修改Exec=netease-cloud-music %U 为  Exec=sudo netease-cloud-music %U,
这样点击网易云音乐图标就是以管理员权限启动的了，且不用输入密码。

```

### 安装Chrome

```shell
sudo wget https://repo.fdzh.org/chrome/google-chrome.list -P /etc/apt/sources.list.d/
wget -q -O - https://dl.google.com/linux/linux_signing_key.pub  | sudo apt-key add -
sudo apt-get update
sudo apt-get install google-chrome-stable
# 删除 firefox
sudo apt remove --purge firefox
```

### 安装WPS

```shell
wget http://kdl.cc.ksosoft.com/wps-community/download/6757/wps-office_10.1.0.6757_x86_64.tar.xz
xz -d wps-office_10.1.0.6757_x86_64.tar.xz
tar -xvf wps-office_10.1.0.6757_x86_64.tar
mv wps-office_10.1.0.6757_x86_64 WPS
```

### 安装sogo输入法

```
官网下载，然后直接双击安装
```

### 安装Franz

```
https://meetfranz.com/#download
```

## 开发工具

### Java8

```shell
# 安装
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
# 设置Java_HOME
sudo update-alternatives --config java
sudo vim /etc/environment
JAVA_HOME="/usr/lib/jvm/java-8-oracle"
PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/usr/lib/jvm/java-8-oracle/bin"
```

### MySQL

```shell
wget https://dev.mysql.com/get/mysql-apt-config_0.8.10-1_all.deb
安装执行，选择MySQL8.0，OK。
sudo dpkg -i mysql-apt-config_0.8.10-1_all.deb 
sudo apt update 
sudo apt install -y mysql-server
#注意
选择 Use Legacy Authentication Method
----------------------------------------------------------------------------
#全部卸载
sudo apt purge mysql-*
sudo rm -rf /etc/mysql/ /var/lib/mysql
sudo apt autoremove
sudo apt autoclean
```

### Redis

```shell
sudo apt install redis 
sudo vim /etc/redis/redis.conf
requirepass !@#$
```

### MongoDB

```shell
# 安装MongoDB Service
sudo apt install -y mongodb
# 安装MongoDB Compass
wget https://downloads.mongodb.com/compass/mongodb-compass_1.15.1_amd64.deb;
sudo dpkg -i mongodb-compass_1.15.1_amd64.deb;
# 检查MongoDB信息
mongo --eval 'db.runCommand({ connectionStatus: 1 })'
```

**管理MongoDB Service**

```shell
# 查看运行状态
sudo systemctl status mongodb
# 启动
sudo systemctl start mongodb
# 停止
sudo systemctl stop mongodb
# 重新启动
sudo systemctl restart mongodb
# 允许开机启动
sudo systemctl enable mongodb
# 禁止开机启动
sudo systemctl disable mongodb
```

### Tomcat

### Postman 

```shell
wget https://dl.pstmn.io/download/latest/linux64 -O postman.tar.gz
sudo tar -xzf postman.tar.gz -C ~/App
rm postman.tar.gz
sudo ln -s ~/App/Postman/Postman /usr/bin/postman
# 添加到启动器
cat > ~/.local/share/applications/postman.desktop <<EOL
[Desktop Entry]
Encoding=UTF-8
Name=Postman
Exec=postman
Icon=/home/hdr/App/Tools/Postman/app/resources/app/assets/icon.png
Terminal=false
Type=Application
Categories=Development;
EOL
sudo apt install libcanberra-gtk-module libcanberra-gtk3-module
```

### Maven

```shell
# 安装Maven
wget http://mirrors.advancedhosters.com/apache/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz
# 修改Maven源
    <mirror>
      <id>alimaven</id>
      <name>aliyun maven</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>        
    </mirror>
# MAVEN_HOME
参照JAVA_HOME
```

### Gradle

```shell
wget https://services.gradle.org/distributions/gradle-3.4.1-bin.zip
sudo unzip -d ~/App/JavaAbout/Gradle/ gradle-3.4.1-bin.zip 
rm gradle-3.4.1-bin-zip
sudo ln -s ~/App/JavaAbout/Gradle/gradle-3.4.1/bin/gradle /usr/bin/gradle
# 添加到启动器
cat > ~/.local/share/applications/gradle.desktop <<EOL
[Desktop Entry]
Encoding=UTF-8
Name=Gradle
Exec=gradle
Icon=/home/hdr/App/JavaAbout/Gradle/gradle-3.4.1/media/gradle-icon-128x128.png
Terminal=false
Type=Application
Categories=Development;
EOL

```



## 常见问题


### 添加分辨率
```shell
cvt 1440 900
sudo xrandr --newmode "1440x900_60.00"  106.50  1440 1528 1672 1904  900 903 909 934 -hsync +vsync
sudo xrandr --addmode VGA-1 "1440x900_60.00"
# 永久保存
vim .profile
xrandr --newmode "1440x900_60.00"  106.50  1440 1528 1672 1904  900 903 909 934 -hsync +vsync
xrandr --addmode VGA-1 "1440x900_60.00"
```

### 默认使用前置耳机

```bash
# 使用耳机
pacmd list-sinks
pacmd set-sink-port 2 analog-output-headphones
pacmd set-sink-port 5 analog-output-lineout
# 开机默认使用使用耳机 在/etc/pulse/default.pa 文件最后添加
set-sink-port 2 analog-output-headphones
# 音频管理工具
sudo apt install pavucontrol
```

### 设置开机启动项

```shell
sudo systemctl disable mysql #停止mysql开机运行
systemd-analyze blame # 分析开机时间
```

### N卡驱动

The objective is to install the NVIDIA drivers on Ubuntu 18.04 Bionic Beaver Linux. This article will discuss three methods of Nvidia driver installation in the following order:

- Automatic Install using standard Ubuntu Repository
- Automatic Install using PPA repository to install Nvidia Beta drivers
- Manual Install using the Official nvidia.com driver

```bash
 $ ubuntu-drivers devices
== /sys/devices/pci0000:00/0000:00:03.1/0000:1c:00.0 ==
modalias : pci:v000010DEd00001C82sv00007377sd00000000bc03sc00i00
vendor   : NVIDIA Corporation
model    : GP107 [GeForce GTX 1050 Ti]
driver   : nvidia-driver-390 - distro non-free recommended
driver   : xserver-xorg-video-nouveau - distro free builtin
```

```bash
sudo ubuntu-drivers autoinstall
# Once the installation is concluded, reboot your system and you are done.
```



### 进入单用户模式

```shell
- 开机的时候按住shift
- 上下移动到Ubuntu高级模式然后按e
- 上下移动到recovery模式然后按e
- 把 ro recovery nomodest 改为 rw single init=/bin/bash
- 按ctrl+x进入单用户模式
- 退出单用户模式 ctrl+alt+delete
```

### 中文输入法突然实效

```shell
rm -r /home/用户名/.cache/ibus/*pinyin
```

### 添加环境变量

```shell
sudo vim /etc/environment
#添加
JAVA_HOME="/usr/lib/jvm/java-8-oracle"
MAVEN_HOME="/home/hdr/App/JavaAbout/Maven"

PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/usr/lib/jvm/java-8-oracle/bin:/home/hdr/App/JavaAbout/Maven/bin"

Ubuntu Linux系统环境变量配置文件分为两种：系统级文件和用户级文件，下面详细介绍环境变量的配置文件。
1.系统级文件：

/etc/profile:在登录时,操作系统定制用户环境时使用的第一个文件，此文件为系统的每个用户设置环境信息,当用户第一次登录时,该文件被执行。并从/etc/profile.d目录的配置文件中搜集shell的设置。这个文件一般就是调用/etc/bash.bashrc文件。

/etc/bash.bashrc：系统级的bashrc文件，为每一个运行bash shell的用户执行此文件.当bash shell被打开时,该文件被读取.

/etc/environment: 在登录时操作系统使用的第二个文件,系统在读取你自己的profile前,设置环境文件的环境变量。
2.用户级文件：

~/.profile: 在登录时用到的第三个文件 是.profile文件,每个用户都可使用该文件输入专用于自己使用的shell信息,当用户登录时,该文件仅仅执行一次!默认情况下,他设置一些环境变量,执行用户的.bashrc文件。

~/.bashrc:该文件包含专用于你的bash shell的bash信息,当登录时以及每次打开新的shell时,该该文件被读取。不推荐放到这儿，因为每开一个shell，这个文件会读取一次，效率 上讲不好。

~/.bash_profile：每个用户都可使用该文件输入专用于自己 使用的shell信息,当用户登录时,该文件仅仅执行一次!默认情况下,他设置一些环境变量,执行用户的.bashrc文件。~/.bash_profile 是交互式、login 方式进入 bash 运行的~/.bashrc是交互式 non-login 方式进入 bash 运行的通常二者设置大致相同，所以通常前者会调用后者。

~./bash_login:不推荐使用这个，这些不会影响图形界面。而且.bash_profile优先级比bash_login高。当它们存在时，登录shell启动时会读取它们。

~/.bash_logout:当每次退出系统(退出bash shell)时,执行该文件.

~/.pam_environment：用户级的环境变量设置文件。

另外,/etc/profile中设定的变量(全局)的可以作用于任何用户,而~/.bashrc等中设定的变量(局部)只能继承 /etc/profile中的变量,他们是"父子"关系。 
二、/etc/profile与/etc /enviroment的比较

首先来做一个实验:

先将export LANG=zh_CN加入/etc/profile ,退出系统重新登录，登录提示显示英文。将/etc/profile中的export LANG=zh_CN删除，将LNAG=zh_CN加入/etc/environment，退出系统重新登录，登录提示显示中文。 

用户环境建立的过程中总是先执行/etc/profile然后在读取/etc/environment。为什么会有如上所叙的不同呢？ 

应该是先执行/etc/environment，后执行/etc/profile。 

/etc/environment是设置整个系统的环境，而/etc/profile是设置所有用户的环境，前者与登录用户无关，后者与登录用户有关。

系统应用程序的执行与用户环境可以是无关的，但与系统环境是相关的，所以当你登录时，你看到的提示信息，比如日期、时间信息的显示格式与系统环境的LANG是相关的，缺省LANG=en_US，如果系统环境LANG=zh_CN，则提示信息是中文的，否则是英文的。 

对于用户的SHELL初始化而言是先执行/etc/profile, 再读取文件/etc/environment. 

对整个系统而言是先执行/etc/environment。这样理解正确吗？ 

/etc/enviroment -->/etc/profile --> $HOME/.profile -->$HOME/.env (如果存在) 

/etc/profile 是所有用户的环境变量

/etc/enviroment是系统的环境变量 

登陆系统时shell读取的顺序应该是

/etc/profile ->/etc/enviroment -->$HOME/.profile-->$HOME/.env 

原因应该是用户环境和系统环境的区别了 

如果同一个变量在用户环境(/etc/profile)和系统环境(/etc/environment) 有不同的值那应该是以用户环境为准了。 

备注：在shell中执行程序时，shell会提供一组环境变量。export可新增，修改或删除环境变量，供后续执行的程序使用。export的效力仅及于该此登陆操作。 

在登录Linux时要执行文件的过程如下：

在刚登录Linux时，首先启动/etc/profile 文件，然后再启动用户目录下的 ~/.bash_profile、 ~/.bash_login或 ~/.profile文件中的其中一个，执行的顺序为：~/.bash_profile、 ~/.bash_login、 ~/.profile。如果 ~/.bash_profile文件存在的话，一般还会执行 ~/.bashrc文件。因为在 ~/.bash_profile文件中一般会有下面的代码：

 

if[ -f ~/.bashrc ] ; then
　../bashrc
　　　　　　　　　　　fi
　　~/.bashrc中，一般还会有以下代码：
if[ -f /etc/bashrc ] ; then
　./bashrc
fi

 

所以，~/.bashrc会调用/etc/bashrc文件。最后，在退出shell时，还会执行~/.bash_logout文件。 

执行顺序为：/etc/profile -> (~/.bash_profile | ~/.bash_login | ~/.profile) -> ~/.bashrc-> /etc/bashrc -> ~/.bash_logout 
三、设置环境变量的方法

由以上分析可知：

/etc/profile全局的，随系统启动设置【设置这个文件是一劳永逸的办法】

/root/.profile和/home/myname/.profile只对当前窗口有效。

/root/.bashrc和 /home/yourname/.bashrc随系统启动，设置用户的环境变量【平时设置这个文件就可以了】

那么要配置Ubuntu的环境变量，就是在这几个配置文件中找一个合适的文件进行操作了；如想将一个路径加入到$PATH中，可以由下面这样几种添加方法：
1.控制台中：

$PATH="$PATH:/my_new_path"    （关闭shell，会还原PATH）
2.修改profile文件：

$sudo gedit /etc/profile

在里面加入:

exportPATH="$PATH:/my_new_path"
3.修改.bashrc文件：

$ sudo gedit /root/.bashrc

在里面加入：

export PATH="$PATH:/my_new_path"

后两种方法一般需要重新注销系统才能生效，最后可以通过echo命令测试一下：

$ echo $PATH

输出已经是新路径了。

举个列子，如果想把当前路径加入到环境变量中去，就可以这样做：

$  PATH ="$PATH:."

这样运行自己编写的shell脚本时就可以不输入./了
四、小结

综上所述，在Ubuntu 系统中/etc/profile文件是全局的环境变量配置文件，它适用于所有的shell。在我们登陆Linux系统时，首先启动/etc/profile文件，然后再启动用户目录下的~/.bash_profile、~/.bash_login或~/.profile文件中的其中一个，执行的顺序和上面的排序一样。如果~/.bash_profile文件存在的话，一般还会执行~/.bashrc文件。
```