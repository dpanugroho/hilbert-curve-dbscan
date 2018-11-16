package hilbert;

public class Preconditions {
    private Preconditions() {
    }

    public static void checkNotNull(Object o) {
        checkNotNull(o, (String)null);
    }

    public static void checkNotNull(Object o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
    }

    public static void checkArgument(boolean b, String message) {
        if (!b) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkArgument(boolean b) {
        if (!b) {
            throw new IllegalArgumentException();
        }
    }
}
