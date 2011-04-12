@echo off

set HELP_INFO=usage: %0 fromport toport
@rem paramter count.
set COUNT=0

set /a FROM_PORT=%1+0
if /i %FROM_PORT% GTR 1 set /a COUNT=%COUNT%+1

set /a TO_PORT=%2+0
if /i %TO_PORT% GTR 1 set /a COUNT=%COUNT%+1

if /i %COUNT% LSS 2 (
echo %HELP_INFO%
exit /b -1
)
set CURRENT_PATH=%cd%
cd ..
FOR /L %%p IN (%FROM_PORT%,1,%TO_PORT%) DO (
@start /b start.bat %%p
timeout /t 5
)
cd %CURRENT_PATH%