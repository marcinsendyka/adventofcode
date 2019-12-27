package com.msendyka.adventofcode;

import io.vavr.collection.List;

public class OpcodeComputer {

    private long inputSignal;
    private boolean halt = false;
    private long[] instructions;

    public OpcodeComputer(long inputSignal, long[] instructions) {
        this.inputSignal = inputSignal;
        this.instructions = instructions;
    }

    public OpcodeComputer(long[] instructions) {
        this.instructions = instructions.clone();

    }

    public OpcodeComputer(String inputFile) {
        String[] insArr = Functions.readInput(inputFile).head().split(",");
        long[] arr = Functions.stringArrayToLongArray(insArr);
        instructions = arr;

    }

    public Long processInstruction() {
        for (int i = 0; i < instructions.length; ) {
            Long instructionCode = instructions[i];
            if (instructionCode.intValue() == 99) break;
            Instruction instruction = createInstruction(i, instructionCode);
            InstructionResult result = instruction.execute();
            instructions[result.address] = result.value;
            i += 4;
        }
        return instructions[0];
    }

    public List<Long> getInstructions() {
        return List.ofAll(instructions);
    }

    private Instruction createInstruction(int programCounter, Long instructionCode) {
        long firstParameter = instructions[(int) instructions[programCounter + 1]];
        long secondParameter = instructions[(int) instructions[programCounter + 2]];
        int thirdParameter = (int) instructions[programCounter + 3];
        switch (instructionCode.intValue()) {
            case 1:
                return new Instruction(Opcode.ADD, firstParameter,
                        secondParameter,
                        thirdParameter);
            case 2:
                return new Instruction(Opcode.MULTIPLY, firstParameter,
                        secondParameter,
                        thirdParameter);
            default:
                throw new IllegalStateException();

        }


    }

    private static class Instruction {
        private Opcode opcode;
        private long param1;
        private long param2;
        private int param3;

        public Instruction(Opcode opcode, long param1, long param2, int param3) {
            this.opcode = opcode;
            this.param1 = param1;
            this.param2 = param2;
            this.param3 = param3;
        }

        InstructionResult execute() {
            return new InstructionResult(param3, opcode.execute.apply(param1, param2));
        }
    }

    private static class InstructionResult {
        final int address;
        final long value;

        public InstructionResult(int address, long value) {
            this.address = address;
            this.value = value;
        }
    }
}
