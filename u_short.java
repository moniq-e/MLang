public final class u_short {
    private int ushort = 0;

    public u_short() { }

    public u_short(int value) {
        set(value);
    }

    public u_short(u_byte ubyte) {
        set(ubyte.get());
    }

    public static u_short of(int value) {
        return new u_short(value);
    }

    public int get() {
        return ushort & 0xFFFF;
    }

    public void set(int value) {
        this.ushort = value & 0xFFFF;
    }
}