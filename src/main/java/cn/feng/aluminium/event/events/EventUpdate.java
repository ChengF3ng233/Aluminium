package cn.feng.aluminium.event.events;

import cn.feng.aluminium.event.impl.Event;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EventUpdate implements Event {
    private double x, y, z;
    private float yaw, pitch;
    private boolean ground;
}
