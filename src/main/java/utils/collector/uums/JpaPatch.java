package utils.collector.uums;

import javax.persistence.Query;
import java.util.List;

/**
 * 
 * @author yuli
 *
 */
@SuppressWarnings("unchecked")
public class JpaPatch {

	/**
	 * Patch for javax.persistence.Query.getSingleResult
	 * 
	 * @param query
	 * @return
	 */
	public static <T> T uniqueResult(Query query) {
		List<T> list = query.getResultList();
		if (list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

}
