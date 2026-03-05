public class CPU implements Memory {
    private u_short pc = new u_short();

    public CPU(Bus bus) {
        
    }

    @Override
    public u_byte memRead(int addr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'memRead'");
    }

    @Override
    public void memWrite(int addr, u_byte value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'memWrite'");
    }
}
