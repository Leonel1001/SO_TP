#!/bin/bash

# Compilação dos arquivos .java e colocação no diretório de saída com o JAR do JFreeChart no classpath
javac -cp ./lib/jfreechart.jar:./lib/gson.jar:./lib/jBCrypt.jar -d ./SatelliteClass -encoding UTF-8 *.java


# Execução da classe SatelliteInterface com o JAR do JFreeChart no classpath
java -cp .:./lib/jfreechart.jar:./lib/gson.jar:./lib/jBCrypt.jar:./SatelliteClass SatelliteInterface



