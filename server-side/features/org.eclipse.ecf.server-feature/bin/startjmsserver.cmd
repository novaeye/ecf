setlocal
cd %~dp0
set RP=..\..\..\plugins
set JMS=%RP%\org.eclipse.ecf.provider.jms_0.8.3
set ECF=%RP%\org.eclipse.ecf_0.8.3\ecf.jar
set JMSP=%JMS%\jmsprovider.jar
set UI=%RP%\org.eclipse.ecf.ui_0.8.3\ui.jar
set SDO=%RP%\org.eclipse.ecf.sdo_0.8.3\ecf.sdo.jar
set DS=%RP%\org.eclipse.ecf.datashare_0.8.3\datashare.jar
set DSP=%RP%\org.eclipse.ecf.provider.datashare_0.8.3\dsprovider.jar
set FS=%RP%\org.eclipse.ecf.fileshare_0.8.3\fileshare.jar
set FSP=%RP%\org.eclipse.ecf.provider.fileshare_0.8.3\fsprovider.jar
set PROVIDER=%RP%\org.eclipse.ecf.provider_0.8.3\provider.jar
set PRESENCE=%RP%\org.eclipse.ecf.presence_0.8.3\presence.jar
set GED=%RP%\org.eclipse.ecf.example.sdo.gefeditor_0.8.3\editor.jar
set ED=%RP%\org.eclipse.ecf.example.sdo.editor_0.8.3\editor.jar
set LIBRARY=%RP%\org.eclipse.ecf.example.sdo.library_0.8.3\runtime\org.eclipse.ecf.example.library.jar
set DISCOVERY=%RP%\org.eclipse.ecf.discovery_0.8.3\discovery.jar
set HELLO=%RP%\org.eclipse.ecf.example.hello_0.8.3\hello.jar
set COLLAB=%RP%\org.eclipse.ecf.example.collab_0.8.3\client.jar
set CED=%RP%\org.eclipse.ecf.example.collab.editor_0.8.3\collabeditor.jar

set JMSL=%JMS%\lib\activemq3.1M5

set JMSLIB=%JMSL%\activeio-1.1.jar;%JMSL%\activemq-core-3.1-M5.jar;%JMSL%\commons-logging-1.0.3.jar;%JMSL%\concurrent-1.3.4.jar;%JMSL%\geronimo-spec-j2ee-jacc-1.0-rc4.jar;%JMSL%\geronimo-spec-j2ee-management-1.0-rc4.jar;%JMSL%\geronimo-spec-jms-1.1-rc4.jar;%JMSL%\geronimo-spec-jta-1.0.1B-rc4.jar;%JMSL%\log4j-1.2.8.jar;%JMSL%\spring-1.2.2.jar

set CP="..\lib\core.jar;..\lib\runtime.jar;..\lib\osgi.jar;%JMSLIB%;%ECF%;%PROVIDER%;%PRESENCE%;%JMSP%;%DISCOVERY%;%UI%;%SDO%;%DS%;%DSP%;%FS%;%FSP%;%GED%;%ED%;%LIBRARY%;%HELLO%;%COLLAB%;%CED%;."

rem set TRACE=-Dorg.eclipse.ecf.provider.jms.Trace=true -Dorg.eclipse.ecf.provider.Trace=true
set TRACE=-Dorg.eclipse.ecf.provider.Trace -Dorg.eclipse.ecf.Trace -Dorg.eclipse.ecf.provider.jms.Trace

set OPTIONS=%TRACE%

set MAINCLASS=org.eclipse.ecf.provider.jms.app.ServerApplication
set ARGS=tcp://localhost:3240/server

rem Start server
echo "Starting server with options: %OPTIONS% and args: %ARGS%"
java -cp %CP% %OPTIONS% %MAINCLASS% %ARGS% 

endlocal
