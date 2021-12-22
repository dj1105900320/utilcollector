package utils.collector.daenerys;


import com.xueliman.iov.daenerys_admin.api.dto.DepLineTree;
import com.xueliman.iov.daenerys_admin.api.dto.LineTreeDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yujingwei
 */
public class TreeUtil {

    private static final String TOP_NODE_ID = "0";


    public static <T> List<DepLineTree<T>> buildDepTree(List<DepLineTree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<DepLineTree<T>> result = new ArrayList<>();
        nodes.forEach(children -> {
            String pid = children.getParentId();
            if (pid == null || TOP_NODE_ID.equals(pid)) {
                result.add(children);
                return;
            }
            for (DepLineTree<T> n : nodes) {
                String id = n.getId();
                if (id != null && id.equals(pid)) {
                    if (n.getChildren() == null) {
                        n.initChildren();
                    }
                    n.getChildren().add(children);
                    children.setHasParent(true);
                    n.setHasChild(true);
                    return;
                }
            }
        });

        return result;
    }

    public static <T> List<LineTreeDTO<T>> buildLineTree(List<LineTreeDTO<T>> nodes) {
        if (nodes == null) {
            return Collections.EMPTY_LIST;
        }
        List<LineTreeDTO<T>> result = new ArrayList<>();
        nodes.forEach(children -> {
            String pid = children.getParentId();
            if (pid == null || TOP_NODE_ID.equals(pid)) {
                result.add(children);
                return;
            }
            for (LineTreeDTO<T> n : nodes) {
                String id = n.getId();
                if (id != null && id.equals(pid)) {
                    if (n.getChildren() == null) {
                        n.initChildren();
                    }
                    n.getChildren().add(children);
                    children.setHasParent(true);
                    n.setHasChild(true);
                    return;
                }
            }
        });
        return result;
    }

}