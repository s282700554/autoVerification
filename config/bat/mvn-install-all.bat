@echo off
echo ���ڰ�װ������maven�ֿ���ȴ�...

set SAVE_PATH="%cd%"
set log=%SAVE_PATH%\"install.log"
@echo %date% %time% > %log%
@echo start install Emofs >> %log%
for /f %%i in (pkgOrder.txt) do (
	@echo **********************************************************************************
	@echo install %%i
	@echo **********************************************************************************
	cd %%i
 	@rem �������̫������ɾ��javadoc:jar����Ҫ��ʱ�䶼�ǻ��ڴ�javadoc�ϣ����Ҫ�������õĻ���һ��Ҫ��javadoc:jar�ſ��õ�ע��
	call mvn clean install -DskipTests=true
	cd %SAVE_PATH%
) >> %log%
echo �����Ѿ���ɣ�������־�ļ�%log%�е�"] BUILD "�Ƿ���ʧ�ܡ�
