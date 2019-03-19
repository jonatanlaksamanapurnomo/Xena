/*
 *    Copyright 2016 Jonathan Beaudoin
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.xena.plugin.official;

import org.xena.Indexer;
import org.xena.Settings;
import org.xena.cs.ClientState;
import org.xena.cs.GameEntity;
import org.xena.cs.Me;
import org.xena.cs.Player;
import org.xena.keylistener.NativeKeyUtils;
import org.xena.plugin.Plugin;
import org.xena.plugin.PluginManifest;
import org.xena.plugin.utils.AngleUtils;
import org.xena.plugin.utils.Vector;

import java.awt.*;
import java.awt.event.InputEvent;

@PluginManifest(name = "Spin Bot", description = "Helps you to stay on target.")
public final class SpinBotPlugin extends Plugin {
	
	private final AngleUtils aimHelper;
	private final Vector aim = new Vector();
	private Player lastTarget = null;
	private int lastIdx;
	private Robot robot;
	
	public SpinBotPlugin() {
		aimHelper = new AngleUtils(this, Settings.SPIN_BOT_STRENGTH, 2F, 2F, 2F, 2F);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void pulse(ClientState clientState, Me me, Indexer<GameEntity> entities) {
		if (NativeKeyUtils.isKeyDown(Settings.SPIN_BOT_TOGGLE) && !me.getCursorEnabled()) {
//			recaulcate target
			if (lastTarget == null) {
				while (lastTarget == null) {
//					klo gk ada orang  /main sendiri jomblo gk usah di calculate lgsung di close aja
					if (lastIdx  >= entities.size()) {
						lastIdx = 0;
						break;
					}
//					klo gk main sendiri index muush terdekat
					GameEntity entity = entities.get(lastIdx++);

					try {
//						liat jarak antara musuh
						if (aimHelper.delta(me.getViewOrigin(), entity.getBones()) > 3000) {
							continue;
						}
//						klo bisa nembak masukin last target ke dalam variable
						if (aimHelper.canShoot(me, entity)) {
							lastTarget = (Player) entity;
						} else {
							lastTarget = null;
						}
					} catch (Throwable throwable) {
						throwable.printStackTrace();
					}
				}
			}
//			cross check biar gk salah
			if (lastTarget == null) {
				return;
			}
//			klo ada target cek bisa nembak ?
			else if (aimHelper.canShoot(me, lastTarget) ) {
//				laksananakan
//				snip mode
				aimHelper.velocityComp(me, lastTarget, lastTarget.getBones());
				aimHelper.calculateAngle(me, me.getViewOrigin(), lastTarget.getBones(), aim);
				aimHelper.setAngleSmooth(aim, lastTarget.getViewAngles());
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			} else {
//				print kenapa gk bisa nembak
				System.out.println(me.getActiveWeapon().getClip1() + ", " + me.isDead() + ", " + lastTarget.getTeam() + ", " + me.getTeam());
//				last target di buat null supaya next round gk ngincer yg itu
				lastTarget = null;
			}
		} else {

			lastTarget = null;
		}
		sleep(5);
	}
	
}
