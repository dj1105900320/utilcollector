package utils.collector.pump.pumpserver;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public class IdTable {

    static final Table<String, Long, Long> TABLE = HashBasedTable.create(); // TermID - SignID - SessionID
    static final Long DEFAULT_SESSION_ID = -1L;

    public static void put(String termId, Long signId, Long sessionId) {
        TABLE.put(termId, signId, sessionId);
    }

    public static Long sessionId(String termId) {
        return max(TABLE.row(termId).values());
    }

    public static Long sessionId(Long signId) {
        return max(TABLE.column(signId).values());
    }

    public static void remove(Long sessionId) {
        TABLE.values().remove(sessionId);
    }

    static Long max(Collection<Long> values) {
        Optional<Long> max = values.stream().max(Comparator.naturalOrder());
        return max.isPresent() ? max.get() : DEFAULT_SESSION_ID;
    }

}
