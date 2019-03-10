/**
 * FIX use message catalog FeatureForm.java
 * 
 * Copyright (C) 2007 Irving Bunton
 * 
 * http://code.google.com/p/mobile-rss-reader/
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 *
 */
package net.eiroca.j2me.external.presentation;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;

/*
 * Form with optional commands added with addPromptCommand which if used, will give prompt message
 * with OK/Cancel.
 */
public class FeatureForm extends Form {

  protected FeatureMgr featureMgr;

  public FeatureForm(String title) {
    super(title);
    init();
  }

  private void init() {
    featureMgr = new FeatureMgr(this);
  }

  public FeatureForm(String title, Item[] items) {
    super(title, items);
    init();
  }

  final public void addPromptCommand(Command cmd, int prompt) {
    super.addCommand(cmd);
    featureMgr.addPromptCommand(cmd, prompt);
  }

  final public void removeCommand(Command cmd) {
    super.removeCommand(cmd);
    featureMgr.removePrompt(cmd);
  }

  final public void removePrompt(Command cmd) {
    super.removeCommand(cmd);
    featureMgr.removePrompt(cmd);
  }

  final public void setCommandListener(CommandListener cmdListener) {
    super.setCommandListener(featureMgr);
    featureMgr.setCommandListener(cmdListener, false);
  }

  final public void setCommandListener(CommandListener cmdListener, boolean background) {
    super.setCommandListener(featureMgr);
    featureMgr.setCommandListener(cmdListener, background);
  }

  public FeatureMgr getFeatureMgr() {
    return (featureMgr);
  }

}
