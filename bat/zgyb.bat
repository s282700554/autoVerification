SET TNSNAMEABC=oratest
SET DATAFILEPATH=C:\oradata\oratest

CALL ��������_UP.bat %TNSNAMEABC% ORACLE

CALL eosp_main_update.bat ES_DBA oracle %TNSNAMEABC% %DATAFILEPATH%

CALL ��������_UP.bat %TNSNAMEABC% ORACLE

PAUSE