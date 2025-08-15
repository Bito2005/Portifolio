@echo off  
REM AutoFacil Java Edition  
echo ========================================  
echo    AutoFacil Java Edition v1.0  
echo    Sistema de Locadora de Veiculos  
echo ========================================  
echo.  
jdk\bin\javac.exe -cp lib\* -d bin -encoding UTF-8 src\domain\*.java src\persistence\*.java src\util\*.java src\auth\*.java src\ui\*.java src\Main.java  
if errorlevel 1 (  
  echo Erro na compilacao!  
  pause  
  exit /b 1  
)  
jdk\bin\java.exe -cp bin;lib\* Main  
pause 
