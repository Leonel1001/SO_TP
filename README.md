1.	Através da linha de comandos
Neste ponto pretende-se que o utilizador, já tenha a pasta do projeto e utilize a linha de comandos para compilar a aplicação executando os comandos que estão descritos a seguir:

Selecionar o projeto a compilar através do terminal e após isso executar o seguinte comando:

javac -cp .\lib\jfreechart.jar;./lib/gson.jar;./lib/jBCrypt.jar; -d .\SatelliteClass -encoding UTF-8 *.java

javac -cp ./lib/jfreechart.jar:./lib/gson.jar:./lib/jBCrypt.jar -d ./SatelliteClass -encoding UTF-8 *.java

Após realizar o comando “Javac”, todos os ficheiros .class devem ficar criados com sucesso dentro da pasta com o seguinte nome “SatelliteClass”. Com isto feito fica com o programa pronto a executar e para isso deve utilizar o seguinte comando:

No Windows:
java -cp .;.\lib\jfreechart.jar;./lib/gson.jar;./lib/jBCrypt.jar;.\SatelliteClass SatelliteInterface

No MacOS:
java -cp .:./lib/jfreechart.jar:./lib/gson.jar:./lib/jBCrypt.jar:./SatelliteClass SatelliteInterface


Por fim, quando os dois comandos forem executados com sucesso, a aplicação irá aparecer no ecrã e está pronta a ser utilizada.

2.	Executar o ficheiro Satelite.jar.

Através do ficheiro Satelite.jar o utilizador tem a opção de executar o programa sem ter de introduzir comandos, facilitando assim o processo de compilação.
 Para isto o utilizador apenas tem de abrir a pasta do projeto na linha de comandos e executar o seguinte comando:

java -jar Satellite.java

Por fim, a aplicação irá aparecer no ecrã e está pronta a ser utilizada.


3. Fazer login
Após iniciar a aplicação, a primeira página que irá surgir é a página para realizar o login ou registo, por defeito, foi criado um utilizador para que a aplicação possa ser utilizada:

Username: Teste

Password: Test12345

