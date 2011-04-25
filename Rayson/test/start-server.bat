@echo off
setlocal EnableDelayedExpansion
set CURRENT_DIR=%cd%
cd ..
set PATENT_PATH=%cd%
set CLASS_PATH=%PATENT_PATH%\bin\

for /R %PATENT_PATH%\lib\ %%i in (*.jar) do @set CLASS_PATH=!CLASS_PATH!;%%i


java -cp %CLASS_PATH%  org.creativor.rayson.demo.main.DemoServer $*
cd %CURRENT_DIR%
