package ui.status;

public abstract class StatusMessage {
    private static String STATUS;

    public static boolean isInWater() {
        return STATUS.toLowerCase().equals("water");
    }

    public static boolean isFinished() {
        return STATUS.toLowerCase().equals("finished");
    }

    public static boolean isSuccess() {
        return STATUS.toLowerCase().equals("success");
    }

    public static void setSTATUS(String STATUS) {
        StatusMessage.STATUS = STATUS;
    }

}
