#!/bin/sh
#服务器springboot项目的重启动
kill -9 $(ps -ef|grep member.jar|grep -v grep|awk '{print $2}')
sleep 2
sh run-member.sh
