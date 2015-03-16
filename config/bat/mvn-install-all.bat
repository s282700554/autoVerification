@echo off
echo 正在安装到本地maven仓库请等待...

set SAVE_PATH="%cd%"
set log=%SAVE_PATH%\"install.log"
@echo %date% %time% > %log%
@echo start install Emofs >> %log%
for /f %%i in (pkgOrder.txt) do (
	@echo **********************************************************************************
	@echo install %%i
	@echo **********************************************************************************
	cd %%i
 	@rem 如果觉得太慢可以删除javadoc:jar，主要的时间都是花在打javadoc上，如果要给别人用的话就一定要有javadoc:jar才看得到注释
	call mvn clean install -DskipTests=true
	cd %SAVE_PATH%
) >> %log%
echo 部署已经完成，请检查日志文件%log%中的"] BUILD "是否有失败。
