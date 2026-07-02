@echo off
chcp 65001 >nul
echo ========================================
echo    实验室预约管理系统 - 数据库恢复脚本
echo ========================================
echo.

echo 警告：此操作将覆盖现有数据库数据！
echo.
set /p confirm=确定要恢复数据库吗？(Y/N):
if /i not "%confirm%"=="Y" (
    echo 操作已取消
    pause
    exit /b
)

echo.
echo [1/2] 正在恢复数据库...
cd /d "%~dp0"

REM 检查MySQL是否可用
mysql --version >nul 2>&1
if errorlevel 1 (
    echo 错误：未找到MySQL命令，请确保MySQL已安装并添加到PATH
    pause
    exit /b 1
)

REM 恢复数据库
mysql -u root -p123456 < "数据库完整备份_20260610.sql"
if errorlevel 1 (
    echo 错误：数据库恢复失败！
    pause
    exit /b 1
)

echo [2/2] 验证恢复结果...
mysql -u root -p123456 -e "USE meeting_hub; SELECT '实验室数量' AS '项目', COUNT(*) AS '数量' FROM sys_lab UNION ALL SELECT '用户数量', COUNT(*) FROM sys_user UNION ALL SELECT '预约记录', COUNT(*) FROM sys_reservation UNION ALL SELECT '报修记录', COUNT(*) FROM sys_repair UNION ALL SELECT '教学占用', COUNT(*) FROM sys_teaching_occupancy;"

echo.
echo ========================================
echo    数据库恢复完成！
echo ========================================
echo.
echo 测试账号:
echo   管理员: admin / admin123
echo   教师: teacher123 / 123456
echo   学生: student123 / 123456
echo.
pause
