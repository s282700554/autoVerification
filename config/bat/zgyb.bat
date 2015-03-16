SET TNSNAMEABC=oratest
SET DATAFILEPATH=C:\oradata\oratest

CALL УмТыжижУ_UP.bat %TNSNAMEABC% ORACLE

CALL eosp_main_update.bat ES_DBA oracle %TNSNAMEABC% %DATAFILEPATH%

CALL УмТыжижУ_UP.bat %TNSNAMEABC% ORACLE

PAUSE