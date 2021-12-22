//package utils.collector.pump.pumpserver;
//
//import com.xueliman.iov.protocol.ProtocolMessage;
//import org.apache.mina.core.future.WriteFuture;
//import org.apache.mina.core.session.IoSession;
//
///**
// * Utilities - 终端
// *
// * @author yuli
// */
//public class TermUtil {
//
//    static final long TIMEOUT_MILLIS = 5000L;
//
//    /**
//     * Send data to terminal using specified session.
//     *
//     * @param s
//     * @param m
//     */
//    public static boolean write(final IoSession s, final ProtocolMessage<?> m) {
//        if (check(s)) {
//            WriteFuture wf = s.write(m);
//            wf.awaitUninterruptibly(TIMEOUT_MILLIS);
//            return wf.isWritten();
//        }
//        return Boolean.FALSE;
//    }
//
//    /**
//     * @param s
//     * @return
//     */
//    public static boolean check(final IoSession s) {
//        return s != null && s.isActive();
//    }
//
//}
