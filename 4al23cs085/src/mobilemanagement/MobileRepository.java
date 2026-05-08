package mobilemanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MobileRepository {
    private final List<Mobile> mobiles = Collections.synchronizedList(new ArrayList<>());

    public MobileRepository() {
        add(new Mobile("Apple", "iPhone 15", "iOS", 128, 69900));
        add(new Mobile("Samsung", "Galaxy S24", "Android", 256, 74999));
        add(new Mobile("OnePlus", "12R", "Android", 128, 39999));
    }

    public void add(Mobile mobile) {
        mobiles.add(mobile);
    }

    public List<Mobile> findAllNewestFirst() {
        synchronized (mobiles) {
            List<Mobile> copy = new ArrayList<>(mobiles);
            Collections.reverse(copy);
            return copy;
        }
    }

    public int activeCount() {
        return mobiles.size();
    }
}
