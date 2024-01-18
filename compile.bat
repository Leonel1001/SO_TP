@echo off
rem Compilação dos arquivos .java e colocação no diretório de saída com o JAR do JFreeChart no classpath
javac -cp .\lib\jfreechart.jar;./lib/gson.jar;./lib/jBCrypt.jar; -d .\SatelliteClass -encoding UTF-8 *.java

rem Execução da classe SatelliteInterface com o JAR do JFreeChart no classpath
java -cp .;.\lib\jfreechart.jar;./lib/gson.jar;./lib/jBCrypt.jar;.\SatelliteClass SatelliteInterface

rem Aguarde a execução para visualizar mensagens de erro se houver
pause