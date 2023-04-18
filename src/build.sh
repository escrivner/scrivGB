#!/bin/bash
#compile statements
javac addressBus/Motherboard.java
javac addressBus/InterruptRegisters.java
javac addressBus/Timer.java
javac cartridge/Cartridge.java

#CPU components
javac cpu/CPU.java
javac cpu/CPUMethods.java
javac cpu/Register.java
javac cpu/RegisterManager.java
javac cpu/DefaultOpcodes.java

javac other/BitManipulator.java
javac ram/RAMBank.java
javac Main.java

#execution
java Main $1