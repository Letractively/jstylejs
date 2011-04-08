@echo off
setlocal EnableDelayedExpansion
set CURRENT_DIR=%cd%
cd ..
set PATENT_PATH=%cd%
set CLASS_PATH=%PATENT_PATH%\build\

for /R %PATENT_PATH%\lib\ %%i in (*.jar) do @set CLASS_PATH=!CLASS_PATH!;%%i

cd %CURRENT_DIR%
java -cp %CLASS_PATH%  org.creativor.viva.Viva %*
