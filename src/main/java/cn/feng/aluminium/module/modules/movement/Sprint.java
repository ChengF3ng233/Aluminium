package cn.feng.aluminium.module.modules.movement;

import cn.feng.aluminium.event.annotations.EventTarget;
import cn.feng.aluminium.event.events.EventUpdate;
import cn.feng.aluminium.module.Module;
import cn.feng.aluminium.module.ModuleCategory;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", ModuleCategory.MOVEMENT);
    }

    @EventTarget
    private void onUpdate(EventUpdate e) {
        if (!mc.thePlayer.isCollidedHorizontally && mc.thePlayer.movementInput.moveForward > 0f && !mc.thePlayer.isSneaking()) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
