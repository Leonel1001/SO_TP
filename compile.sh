#!/bin/bash

# Compilação dos arquivos .java e colocação no diretório de saída com o JAR do JFreeChart no classpath
javac -cp ./lib/jfreechart.jar -d ./SatelliteClass *.java

# Execução da classe SatelliteInterface com o JAR do JFreeChart no classpath
java -cp .:./lib/jfreechart.jar:./SatelliteClass SatelliteInterface

