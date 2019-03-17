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

package org.xena.gui;


import org.xena.Xena;
import org.xena.plugin.Plugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public final class Overlay extends JWindow {
	
	private static final int WIDTH = 200;
	private static int HEIGHT = 400;
	
	private static Point mouseDownPoint;
	
	private JLabel[] plugins = null;
	private JLabel status;
	
	private Xena xena;
	private boolean minimized;
	
	private Overlay(Xena xena) {
		init(xena);
	}
	
	public static Overlay open(Xena xena) {
		return new Overlay(xena);
	}
	
	private void init(Xena xena) {
		this.xena = xena;
		
		plugins = new JLabel[xena.getPluginManager().size()];
		
		setAlwaysOnTop(true);
		setLayout(null);
		setBackground(new Color(71, 71, 71, 253));
		setBounds(350, 0, WIDTH, HEIGHT);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseDownPoint = e.getPoint();
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				Point mousePos = e.getLocationOnScreen();
				setLocation(mousePos.x - mouseDownPoint.x, mousePos.y - mouseDownPoint.y);
				repaint();
			}
		});
		
		int height = 0;
		JLabel title = new JLabel("Charlatano Lite", SwingConstants.CENTER);
		title.setFont(new Font("Sans Serif", Font.BOLD, 16));
		title.setOpaque(true);
		title.setBackground(new Color(61, 61, 61));
		title.setForeground(Color.WHITE);
		title.setBounds(0, height, getWidth(), 40);
		add(title);
		
		height += 40;
		JLabel splitter = new JLabel("", SwingConstants.CENTER);
		splitter.setOpaque(true);
		splitter.setBackground(new Color(232, 76, 61));
		splitter.setBounds(0, height, getWidth(), 5);
		add(splitter);
		
		JLabel label;
		for (Plugin plugin : xena.getPluginManager()) {
			height += 20;
			label = new JLabel(plugin.toString());
			label.setFont(new Font("Sans Serif", Font.BOLD, 14));
			label.setForeground(plugin.isEnabled() ? Color.GREEN : Color.RED);
			label.setBounds(10, height, getWidth(), 20);
			add(label);
			plugins[plugin.uid()] = label;
		}
		
		height += 30;
		
		status = new JLabel(getStatus(), SwingConstants.LEFT);
		status.setFont(new Font("Sans Serif", Font.BOLD, 12));
		status.setForeground(Color.WHITE);
		status.setBounds(3, height, getWidth() - 5, 70);
		add(status);
		
		height += 70;
		setSize(getWidth(), height);
		
		HEIGHT = height;
		setVisible(true);
	}
	
	@Override
	public void repaint() {
		for (int i = 0; i < plugins.length; i++) {
			JLabel label = plugins[i];
			Plugin plugin = xena.getPluginManager().get(i);
			label.setForeground(plugin.isEnabled() ? Color.GREEN : Color.RED);
		}
		status.setText(getStatus());
	}
	
	public void minimize() {
		minimized = !minimized;
		setSize(getWidth(), minimized ? 45 : HEIGHT);
		repaint();
	}
	
	private String getStatus() {
		return "<html>Status: Running<br>Cycle: " + xena.getLastCycle() + "(max=" + Xena.CYCLE_TIME + ")<br>Game mode: " + xena.getGameMode() + "<br>State: " + xena.getState().toString() + "</html>".intern();
	}
}
