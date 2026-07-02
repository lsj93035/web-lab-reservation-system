@echo off
chcp 65001 >nul
echo ========================================
echo    实验室预约管理系统 - 启动脚本
echo ========================================
echo.

echo [1/4] 检查Java环境...
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-8.0.482.8-hotspot
"%JAVA_HOME%\bin\java" -version
if errorlevel 1 (
    echo Java环境检查失败！
    pause
    exit /b 1
)
echo Java环境正常！
echo.

echo [2/4] 启动Redis服务...
net start Redis 2>nul
if errorlevel 1 (
    echo Redis服务已在运行或启动失败
) else (
    echo Redis服务启动成功！
)
echo.

echo [3/4] 启动后端服务...
cd /d "%~dp0"
start "后端服务" cmd /k "set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-8.0.482.8-hotspot && java -jar ruoyi-admin\target\ruoyi-admin.jar"
echo 后端服务启动中，请等待约15秒...
timeout /t 15 /nobreak >nul
echo.

echo [4/4] 启动前端服务...
cd /d "%~dp0\ruoyi-ui"
start "前端服务" cmd /k "set NODE_OPTIONS=--openssl-legacy-provider && npm run dev"
echo 前端服务启动中，请等待约30秒...
timeout /t 30 /nobreak >nul
echo.

echo ========================================
echo    系统启动完成！
echo ========================================
echo.
echo 访问地址: http://localhost:80/
echo.
echo 测试账号:
echo   管理员: admin / admin123
echo   教师: teacher123 / 123456
echo   学生: student123 / 123456
echo.
echo 验证码已启用，请在登录页面输入验证码
echo.
echo 按任意键打开浏览器...
pause >nul
start http://localhost:80/
