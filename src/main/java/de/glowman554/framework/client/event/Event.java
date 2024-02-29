package de.glowman554.framework.client.event;

import java.util.ArrayList;

public class Event {
    public Event call() {
        final ArrayList<EventData> dataList = EventManager.get(this.getClass());

        if (dataList != null) {
            // toArray needed because of ConcurrentModificationException
            for (EventData data : dataList.toArray(EventData[]::new)) {
                try {
                    data.target.invoke(data.source, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }
}
