Oracle connection tester

This is a simple tool to check if your oracle database is up and running.

Compiling:

  javac tools/Check.java
  jar cvfm check.jar manifest.txt tools


Usage:

  java -jar check.jar <parameters>

Parameters

  user=[user] - username's database - default: "sys"
  
  password=[password] - password's database - default: null
  
  database=[database] - oracle SID - default: "XE"
  
  server=[name or ip] - database server address - default: "localhost"
  
  port=[port] - database port - default: "1521"
  
  attempts=[attempts] - How many retries should be run - default: 30
  
  sleepTime=[seconds] - How much time should wait before retry - default: 5


Sample:

  java -jar check.jar user=sys password=secret server=oracle-server attempts=1
