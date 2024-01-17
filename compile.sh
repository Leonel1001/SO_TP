#!/bin/bash

# Diretório de saída para os arquivos .class
output_directory="SatelliteClass"

# Certifique-se de que o diretório de saída exista
mkdir -p "$output_directory"

# Compilação dos arquivos .java e colocação no diretório de saída
javac -d "$output_directory" *.java

# Execução da classe SatelliteInterface
java -cp "$output_directory" SatelliteInterface
-