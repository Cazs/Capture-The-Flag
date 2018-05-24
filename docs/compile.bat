@echo off

set path=%path%;C:\Program Files\Java\jdk1.7.0_75\bin

set SRC=../src
set BIN=../bin
set DOCS=../docs

javac -d %BIN% %SRC%/Main.java -sourcepath %SRC%

javadoc -classpath %BIN% -sourcepath %SRC% -use -author -d %DOCS%/JavaDocs %SRC%/Main.java %SRC%./za/ac/uj/acsse/practicalx/flagcapture/Assets/*.java %SRC%./za/ac/uj/acsse/csc2a/pta/ui/*.java %SRC%./za/ac/uj/acsse/practicalx/flagcapture/GUI/*.java %SRC%./za/ac/uj/acsse/practicalx/flagcapture/Model/*.java %SRC%./za/ac/uj/acsse/practicalx/flagcapture/States/*.java

java -cp %BIN% Main
 
PAUSE