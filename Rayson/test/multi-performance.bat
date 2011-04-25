@echo off
set HELP_INFO="usage: %0 process_count performance_paramters";
if /i "%1"=="" (
echo %HELP_INFO%
exit /b -1
)
set PROCESS_COUNT=%1
shift /1

FOR /L %%p IN (0,1,%PROCESS_COUNT%) DO (
@start /b performance.bat %1 %2 %3 %4 %5 %6 %7 %8
)