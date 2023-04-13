#!/bin/bash
#compile statements
javac AddressBus.java
javac other/BitManipulator.java
javac CPU.java
javac Cartridge.java
javac Main.java
javac Register.java

#execution
java Main $1