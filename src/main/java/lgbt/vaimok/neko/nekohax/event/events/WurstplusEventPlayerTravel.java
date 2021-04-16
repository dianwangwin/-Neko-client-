package lgbt.vaimok.neko.nekohax.event.events;

import lgbt.vaimok.neko.nekohax.event.WurstplusEventCancellable;

public class WurstplusEventPlayerTravel extends WurstplusEventCancellable {
    
    public float Strafe;
    public float Vertical;
    public float Forward;

    public WurstplusEventPlayerTravel(float p_Strafe, float p_Vertical, float p_Forward) {
        Strafe = p_Strafe;
        Vertical = p_Vertical;
        Forward = p_Forward;
    }

}