package lt.dstulgis.metasite;

/**
 * Replacement for file locking from read operations until computation finishes.
 * Meant to be used by frontend services.
 *
 * @author dstulgis
 */
public class CountingServiceStatus {

    private static boolean completed = false;

    /**
     * Meant to be used only by computation service in same package.
     *
     * @param newStatus to update to, like {@code false} for computation start and {@code true} for computation end.
     */
    protected static void updateStatus(boolean newStatus) {
        completed = newStatus;
    }

    /**
     * @return current computation status
     */
    public static boolean isCompleted() {
        return completed;
    }
}
