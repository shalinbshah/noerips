chdir /d %~dp0
cd libs
del SpireonKeywordsLib.jar
cd..
call mvn clean
call ant
call mvn robotframework:run