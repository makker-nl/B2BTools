set JAVA_HOME="C:\CycloneVIP\jre_1.5.0_07\bin"
REM set BASE_DIR=U:\Mijn documenten\Source\Java\B2BFrontEnd\B2BFrontEnd\deploy\
set BASE_DIR=.
rem set CLASSPATH="%BASE_DIR%\B2BFrontEnd.jar;%BASE_DIR%\classes\b2bfrontend\b2bfe.properties;%BASE_DIR%\lib\jms.jar;%BASE_DIR%\jlib\aqapi.jar;%BASE_DIR%\lib\jta.jar;%BASE_DIR%\jdbc\lib\ojdbc14dms.jar;%BASE_DIR%\jdbc\lib\orai18n.jar;%BASE_DIR%\jdbc\lib\ocrs12.jar;%BASE_DIR%\diagnostics\lib\ojdl.jar;%BASE_DIR%\lib\dms.jar;%BASE_DIR%\lib\xmlparserv2.jar;%BASE_DIR%\lib\xml.jar"
set CLASSPATH="%BASE_DIR%\classes;%BASE_DIR%\config;%BASE_DIR%\lib\xmlparserv2.jar;%BASE_DIR%\lib\xml.jar"


%JAVA_HOME%\javaw.exe -client -classpath %CLASSPATH% cyclonefrontend.CycloneFrontEnd