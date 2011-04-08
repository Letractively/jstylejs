@echo off
set CURRENT_PATH=%cd%
cd ..
FOR /L %%p IN (4460,1,4470) DO @start start.bat %%p
cd %CURRENT_PATH%