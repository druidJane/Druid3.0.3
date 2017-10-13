@REM #########################################################
@REM  Name: �ݹ�ɾ��ָ����Ŀ¼����Ѵ��ļ�������ϣ��ִ�е��Ǹ�Ŀ¼
@REM  Desciption:
@REM  Author: amosryan
@REM  Date: 2010-11-01
@REM  Version: 1.0
@REM  Copyright: Up to you.
@REM platform: MOS5.0
@REM #########################################################

@echo off
setlocal enabledelayedexpansion

del /s *.project

del /s *.classpath

del /s *.iml

del /s *.rebel.xml.*

del /s build.properties

@REM ��������ɾ����Ŀ¼
set WHAT_SHOULD_BE_DELETED=.settings .idea

for /r . %%a in (!WHAT_SHOULD_BE_DELETED!) do (
  if exist %%a (
  echo "ɾ��"%%a
  rd /s /q "%%a"
 )
)

set WHAT_SHOULD_BE_DELETED=bin

for /r . %%a in (!WHAT_SHOULD_BE_DELETED!) do (
  if exist %%a (
  echo "ɾ��"%%a
  rd /s /q "%%a"
 )
)

set WHAT_SHOULD_BE_DELETED=target

for /r . %%a in (!WHAT_SHOULD_BE_DELETED!) do (
  if exist %%a (
  echo "ɾ��"%%a
  rd /s /q "%%a"
 )
)

pause