package bernard.tatin.ProcFS;

import bernard.tatin.Constants.ApplicationConstants;
import bernard.tatin.Tools.ForFiles;
import bernard.tatin.Tools.ForStrings;

import java.util.Arrays;

public class StatM {
    private final static StatM ourInstance = new StatM();
    private String lineOfTitles = null;

    private final int FPROGRAM_SIZE = 0;
    private final int FRESIDENT = 1;
    private final int FDATA = 2;
    private final int FJTOTAL = 3;
    private final int FJMAX = 4;
    private final int FJFREE = 5;
    private final int FALLOCATED = 6;

    public static StatM getMainInstance() {
        return ourInstance;
    }

    public String getStatsTitle() {
        if (lineOfTitles == null) {
            String[] statsTitles = new String[ApplicationConstants.FIELD_COUNT];
            statsTitles[FPROGRAM_SIZE] = "Prog. Size";
            statsTitles[FRESIDENT] = "Resident";
            statsTitles[FDATA] = "Data";
            statsTitles[FJTOTAL] = "JVM total memory";
            statsTitles[FJMAX] = "JVM max memory";
            statsTitles[FJFREE] = "JVM free mem";
            statsTitles[FALLOCATED] = "Allocated memory";
            lineOfTitles = Arrays.stream(statsTitles).map(s ->
                    ForStrings.rightFormat(s,
                            ApplicationConstants.FIELD_LENGTH - 2) +
                            " |").reduce("", String::concat);
        }
        return lineOfTitles;
    }

    public String getStats(long allocatedMemory) {
        Long[] lstats = new Long[ApplicationConstants.FIELD_COUNT];
        String[] strStats = ForFiles.loadLinesFromFiles(
                LinuxProc.procPathName("statm"),
                "[ \n]");
        if (strStats != null) {
            lstats[FALLOCATED] = allocatedMemory;
            lstats[FJFREE] = Runtime.getRuntime().freeMemory();
            lstats[FJMAX] = Runtime.getRuntime().maxMemory();
            lstats[FJTOTAL] = Runtime.getRuntime().totalMemory();
            lstats[FPROGRAM_SIZE] = (Long.parseLong(strStats[0]) * ApplicationConstants.PAGE_SIZE);
            lstats[FRESIDENT] = (Long.parseLong(strStats[1]) * ApplicationConstants.PAGE_SIZE);
            lstats[FDATA] = (Long.parseLong(strStats[5]) * ApplicationConstants.PAGE_SIZE);
            return Arrays.stream(lstats).map(v ->
                    ForStrings.leftFormat(longToMB(v),
                            ApplicationConstants.FIELD_LENGTH - 3) + "M |").
                    reduce("", String::concat);
        } else {
            return null;
        }
    }

    private String longToMB(Long l) {
        Double h = l.doubleValue() / ApplicationConstants.MEGABYTE;
        return String.format("%.3f", h);
    }

    private StatM() {
    }
}
