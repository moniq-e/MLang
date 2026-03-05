public final class u_byte {
    private short ubyte = 0;

    public u_byte() { }

    public u_byte(int value) {
        set(value);
    }

    public static u_byte of(int value) {
        return new u_byte(value);
    }

    public u_short u_short() {
        return new u_short(this);
    }

    public short get() {
        return (short) (ubyte & 0xFF);
    }

    public void set(int value) {
        this.ubyte = (short) (value & 0xFF);
    }

    public void set(u_byte ubyte) {
        set(ubyte.get());
    }
}