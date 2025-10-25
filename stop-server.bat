@echo off
ECHO Stopping services on ports 9998 and 4200...

FOR /F "tokens=5" %%T IN ('netstat -aon ^| findstr :9998') DO (
    IF NOT "%%T"=="0" (
        ECHO Killing process with PID %%T on port 9998.
        taskkill /F /PID %%T
    ) ELSE (
        ECHO No process found on port 9998.
    )
)

FOR /F "tokens=5" %%T IN ('netstat -aon ^| findstr :4200') DO (
    IF NOT "%%T"=="0" (
        ECHO Killing process with PID %%T on port 4200.
        taskkill /F /PID %%T
    ) ELSE (
        ECHO No process found on port 4200.
    )
)

ECHO Done.
