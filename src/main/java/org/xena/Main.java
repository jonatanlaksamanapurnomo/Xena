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

package org.xena;

import org.xena.offsets.OffsetManager;

import java.lang.management.ManagementFactory;

public final class Main {
	
//	private static final String logo = "__  __                \n" + "\\ \\/ /___ _ __   __ _ \n" + " \\  // _ \\ '_ \\ / _` |\n" + " /  \\  __/ | | | (_| |\n" + "/_/\\_\\___|_| |_|\\__,_|\n" + "A free, open-source CS:GO cheating platform\n";
	private static final String logo = "Charlatano lite";
	private static final String desc = "A simple and light weight  csgo cheat , made with java";
	public static void main(String... args) throws InterruptedException {
		System.setProperty("jna.nosys", "true");
		System.out.println(ManagementFactory.getRuntimeMXBean().getName());
		System.out.println(logo);
		System.out.println(desc);
		OffsetManager.initAll();
		Xena.run(Xena.CYCLE_TIME);
	}
	
}
