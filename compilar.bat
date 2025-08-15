@echo off
echo Compilando AutoFacil Java Edition...
cd /d "%~dp0"
jdk\bin\javac.exe -cp lib\* -d bin -encoding UTF-8 src\domain\*.java src\persistence\*.java src\util\*.java src\auth\*.java src\ui\*.java src\Main.java
if errorlevel 1 (
  echo Erro na compilacao!
  pause
  exit /b 1
)
echo Compilacao concluida com sucesso!
pause
