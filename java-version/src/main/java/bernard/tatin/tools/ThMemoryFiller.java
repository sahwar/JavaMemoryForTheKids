package bernard.tatin.tools;

import bernard.tatin.common.Constants;
import bernard.tatin.common.IThPrinterClient;
import bernard.tatin.threads.AThConsumer;
import bernard.tatin.threads.ProtectedValue;
import bernard.tatin.threads.ThPrinter;

import java.util.Arrays;
import java.util.stream.Stream;

public class ThMemoryFiller extends AThConsumer implements IThPrinterClient {
    private static final ThMemoryFiller mainMemoryFiller = new ThMemoryFiller();
    private Byte[] memory = null;
    private ProtectedValue<Long> memory_size = new ProtectedValue<Long>(0L);
    private final Byte[] memory_unit = fillMemory(Constants.MEMORY_INCREMENT).
            toArray(Byte[]::new);
    private ThPrinter mainPrinter = ThPrinter.getMainInstance();


    private ThMemoryFiller() {
    }

    public static ThMemoryFiller getMainInstance() {
        return mainMemoryFiller;
    }

    @Override
    public String getName() {
        return "ThMemoryFiller";
    }

    @Override
    public void innerLoop() {
        try {
            memory = memory != null ?
                    Stream.concat(Arrays.stream(memory), Arrays.stream(memory_unit)).
                            toArray(Byte[]::new) :
                    memory_unit;

        } catch (OutOfMemoryError e) {
            printError("ERROR (ThMemoryFiller::consume): " + e.toString());
            memory = null;
        }

        setMemorySize();
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            printError("ERROR InterruptedException : " + e.getMessage());
        }
    }

    public long getMemorySize() {
        return memory_size.get();
    }

    private void setMemorySize() {
        if (memory == null) {
            memory_size.set(0L);
        } else {
            memory_size.set(new Long((long)memory.length));
        }
    }

    private Stream<Byte> fillMemory(int bytes) {
        return Stream.iterate((byte) 0,
                b -> (byte) ((b + 1) % 253)).limit(bytes);
    }

    @Override
    public void printString(String str) {
        mainPrinter.printString(str);
    }

    @Override
    public void printStrings(String[] strings) {
        mainPrinter.printStrings(strings);
    }

    @Override
    public void printError(String str) {
        mainPrinter.printError(str);
    }
}
