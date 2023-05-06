#!/bin/bash

rm addressBus/Motherboard.class
rm addressBus/InterruptRegisters.class
rm addressBus/Timer.class
rm cartridge/Cartridge.class

rm cpu/CPU.class
rm cpu/CPUMethods.class
rm cpu/Register.class
rm cpu/RegisterManager.class
rm cpu/DefaultOpcodes.class

rm other/Debugger.class
rm other/BitManipulator.class
rm ram/RAMBank.class
rm Main.class

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
javac cpu/PrefixOpcodes.java

javac ppu/PPU.java
javac ppu/Screen.java

javac other/BitManipulator.java
javac other/Debugger.java
javac ram/RAMBank.java
javac Main.java

#execution
java Main /home/ethan/Programming/GB_Emu/individual/02-interrupts.gb /home/ethan/Programming/GB_Emu/Blargg2.txt