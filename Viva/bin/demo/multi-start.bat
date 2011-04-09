@echo off
set CURRENT_PATH=%cd%
cd ..
FOR /L %%p IN (6660,1,6670) DO (
@start /b start.bat %%p
timeout /t 5
)
cd %CURRENT_PATH%