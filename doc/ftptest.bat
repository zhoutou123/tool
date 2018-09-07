::本地电脑ftp上传文件至服务器
@echo off
:y
set /p a=输入项目:
d:
cd D:\项目
if %a% == member (
	ftp -s:member.txt
)
if %a% == market (
	ftp -s:market.txt
)
set /p b=是否继续(y/n):
if %b% == y goto y
:n
exit



