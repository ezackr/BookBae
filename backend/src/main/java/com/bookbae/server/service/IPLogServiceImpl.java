public class IPLogServiceImpl implements IPLogService {
    private final AtomicReferenceArray<IPLogEntry> ararray;
    public static final int BUFLEN = 50;
    public static final int TIME_WINDOW = 50;
    public static final int MAX_CONN;
    public IPLogServiceImpl() {
        this.ararray = new AtomicReferenceArray<>(BUFLEN);
    }
    public boolean addAndCheck(String remoteAddress, long nanoTime) {
        int oldIdx = 0;
        long oldTime = LONG_MAX;
        IPLogEntry oldElement = null;
        IPLogEntry newElement = new IPLogEntry(remoteAddress, nanoTime);
        int connectionCount = 0;
        do {
            for(int i = 0; i < BUFLEN; i++) {
                IPLogEntry cur = ararray.get(i);
                if(cur == null) {
                    oldElement = null;
                    oldTime = 0;
                    oldIdx = i;
                } else if(cur != null && cur.nanoTime < oldTime) {
                    oldElement = cur;
                    oldTime = cur.nanoTime;
                    oldIdx = i;
                }
                if(cur != null && cur.address.equals(remoteAddress) 
                    && nanoTime - cur.nanoTime < TIME_WINDOW) {
                    connectionCount++;
                }
            }
        } while (this.ararray.compareAndSet(oldIdx, oldElement, newElement));
        return connectionCount > MAX_CONN;
    }

    private static class IPLogEntry {
        public final String address;
        public final long nanoTime; 
        public IPLogEntry(String address, long nanoTime) {
            this.address = address;
            this.nanoTime = nanoTime;
        }
    }
}