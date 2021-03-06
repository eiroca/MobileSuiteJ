/**
 * FIX font size FIX insertPrompt FeatureList.java
 *
 * Copyright (C) 2007 Irving Bunton http://code.google.com/p/mobile-rss-reader/
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 * have received a copy of the GNU General Public License along with this program; if not, write to
 * the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.eiroca.j2me.external.presentation;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;

/*
 * List with optional commands added with addPromptCommand which if used, will give prompt message
 * with OK/Cancel.
 */
public class FeatureList extends List {

  protected FeatureMgr featureMgr;

  private Font font = null;

  public FeatureList(final String title, final int listType) {
    super(title, listType);
    init();
  }

  public void init() {
    featureMgr = new FeatureMgr(this);
  }

  public FeatureList(final String title, final int listType, final String[] stringElements, final Image[] imageElements) {
    super(title, listType, stringElements, imageElements);
    init();
  }

  final public void addPromptCommand(final Command cmd, final int prompt) {
    super.addCommand(cmd);
    featureMgr.addPromptCommand(cmd, prompt);
  }

  final public void insertPrompt(final int ps, final int prompt) {
    featureMgr.addPromptIndex(ps, prompt);
  }

  @Override
  final public void removeCommand(final Command cmd) {
    super.removeCommand(cmd);
    featureMgr.removePrompt(cmd);
  }

  final public void removePrompt(final Command cmd) {
    super.removeCommand(cmd);
    featureMgr.removePrompt(cmd);
  }

  @Override
  final public void setCommandListener(final CommandListener cmdListener) {
    super.setCommandListener(featureMgr);
    featureMgr.setCommandListener(cmdListener, false);
  }

  final public void setCommandListener(final CommandListener cmdListener, final boolean background) {
    super.setCommandListener(featureMgr);
    featureMgr.setCommandListener(cmdListener, background);
  }

  @Override
  final public int append(final String stringPart, final Image imagePart) {
    int rtn = -1;
    try {
      rtn = super.append(stringPart, imagePart);
    }
    catch (final RuntimeException e) {
      handleError(e);
      rtn = super.append(stringPart, imagePart);
    }
    if (font != null) {
      setFont(rtn, font);
    }
    return rtn;
  }

  @Override
  final public void insert(final int elementnum, final String stringPart, final Image imagePart) {
    try {
      super.insert(elementnum, stringPart, imagePart);
    }
    catch (final RuntimeException e) {
      handleError(e);
      super.insert(elementnum, stringPart, imagePart);
    }
    final int newElement = (elementnum < 0) ? 0 : elementnum;
    if (font != null) {
      setFont(newElement, font);
    }
  }

  @Override
  final public void set(final int elementnum, final String stringPart, final Image imagePart) {
    try {
      super.set(elementnum, stringPart, imagePart);
    }
    catch (final RuntimeException e) {
      handleError(e);
      super.set(elementnum, stringPart, imagePart);
    }
    if (font != null) {
      setFont(elementnum, font);
    }
  }

  private void handleError(final RuntimeException e) {
    // Using emulator, this can throw array out of bounds, but
    // this is not in the
    if (e instanceof ArrayIndexOutOfBoundsException) {
      font = null;
      final int last = super.size() - 1;
      if (last >= 0) {
        super.setFont(last, Font.getDefaultFont());
      }
    }
    else {
      throw e;
    }
  }

  public Font getFont() {
    return (font);
  }

  public void setFont(final Font font) {
    this.font = font;
  }

  public FeatureMgr getFeatureMgr() {
    return (featureMgr);
  }

}
