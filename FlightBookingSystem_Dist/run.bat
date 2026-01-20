@echo off
echo Compiling...
if not exist bin mkdir bin
javac -d bin -sourcepath src src/bcu/cmp5332/bookingsystem/main/Main.java
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b %errorlevel%
)

echo Running...
java -cp bin bcu.cmp5332.bookingsystem.main.Main
pause
