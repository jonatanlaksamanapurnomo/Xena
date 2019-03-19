package org.xena.plugin.official;

import org.xena.Indexer;
import org.xena.Settings;
import org.xena.cs.ClientState;
import org.xena.cs.GameEntity;
import org.xena.cs.Me;
import org.xena.cs.Player;
import org.xena.plugin.Plugin;
import org.xena.plugin.PluginManifest;
import org.xena.plugin.utils.AngleUtils;
import org.xena.plugin.utils.Vector;
public class TriggerBot extends  Plugin {
    private final AngleUtils aimHelper = new AngleUtils(this, Settings.AIM_ASSIST_STRENGTH, 1.7F, 2.0F, 1.7F, 2.0F);
    private final Vector aim = new Vector();
    private long prevFired = 0;
    private Player lastTarget = null;
    @Override
    public void pulse(ClientState clientState, Me me, Indexer<GameEntity> entities) {


    }
}
