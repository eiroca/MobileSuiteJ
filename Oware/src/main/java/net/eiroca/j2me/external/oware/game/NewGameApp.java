/**
 * GPL >= 2.0
 *
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package net.eiroca.j2me.external.oware.game;

import java.util.Vector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.debug.Debug;
import net.eiroca.j2me.external.presentation.FeatureList;
import net.eiroca.j2me.game.GameApp;
import net.eiroca.j2me.game.ScoreManager;

public abstract class NewGameApp extends GameApp {

  /**
   * Menu prompt message index into menu item definition
   */
  public static final int MD_PROMPTX = 3;

  public static Vector menuShown;
  public static Vector menuCombined;

  @Override
  public void init() {
    initialized = true;
    Application.messages = BaseApp.readStrings(GameApp.RES_MSGS);
    Application.icons = BaseApp.splitImages(GameApp.RES_MENUICON, 9, 12, 12);
    GameApp.highscore = new ScoreManager(GameApp.RMS_HIGHSCORE, GameApp.hsName, GameApp.hsMaxLevel, GameApp.hsMaxScore, true);
    GameApp.game = getGameScreen();
    Application.cOK = Application.newCommand(GameApp.MSG_LABEL_OK, Command.OK, 30, Application.AC_NONE);
    Application.cBACK = Application.newCommand(GameApp.MSG_LABEL_BACK, Command.BACK, 20, Application.AC_BACK);
    Application.cEXIT = Application.newCommand(GameApp.MSG_LABEL_EXIT, Command.EXIT, 10, Application.AC_EXIT);
    gameMenu = NewGameApp.newGetMenu(GameApp.game.name, GameApp.ME_MAINMENU, GameApp.GA_CONTINUE, Application.cEXIT);
    processGameAction(GameApp.GA_STARTUP);
  }

  /**
   * Get the menu as a list for the given menu id. Add command to the list.
   *
   * @param owner
   * @param title
   * @param menuID
   * @param menuAction
   * @param special
   * @param cmd
   * @return
   */
  public static List newGetMenu(final String title, final int menuID, final int special, final Command cmd) {
    final FeatureList list = new FeatureList(title, Choice.IMPLICIT);
    try {
      NewGameApp.menuShown = new Vector();
      NewGameApp.menuCombined = new Vector();
      short[] def;
      int ps = 0;
      for (int i = 0; i < Application.menu.length; i++) {
        def = Application.menu[i];
        final int action = def[Application.MD_MENUAC];
        if (def[Application.MD_MENUID] == menuID) {
          final Integer ix = new Integer(i);
          NewGameApp.menuCombined.addElement(ix);
          // FIX for special
          if (action == special) {
          }
          else {
            NewGameApp.newInsertMenuItem(list, ps, def);
            if (action != Application.AC_NONE) {
              Application.registerListItem(list, ps, action);
            }
            ps++;
          }
        }
      }

      Application.setup(list, cmd, null);
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
    return list;
  }

  /**
   * Insert the item into the menu (a list) at position ps with menu definition def. If the item has
   * an image, use that too.
   *
   * @param list
   * @param ps
   * @param def
   * @return
   */
  public static boolean newInsertMenuItem(final List list, final int ps, final short[] def) {
    try {
      Image icon = null;
      if (def[Application.MD_MENUIC] >= 0) {
        icon = Application.icons[def[Application.MD_MENUIC]];
      }
      short[] cdef;
      final int clen = NewGameApp.menuCombined.size();
      int mix = -1;
      int i = 0;
      for (; i < clen; i++) {
        mix = ((Integer)NewGameApp.menuCombined.elementAt(i)).intValue();
        cdef = Application.menu[mix];
        if (cdef[Application.MD_MENUAC] == def[Application.MD_MENUAC]) {
          break;
        }
      }
      // Item must be in combined menu list
      if (i >= clen) { throw new IllegalArgumentException("Definition must be in menuCombined"); }
      final Integer ix = new Integer(mix);
      if (NewGameApp.menuShown.indexOf(ix) >= 0) { return false; }
      if ((ps >= 0) && (ps <= NewGameApp.menuShown.size())) {
        NewGameApp.menuShown.insertElementAt(ix, ps);
      }
      else {
        NewGameApp.menuShown.addElement(ix);
      }
      list.insert(ps, Application.messages[def[Application.MD_MENUTX]], icon);
      if ((NewGameApp.MD_PROMPTX < def.length) && (def[NewGameApp.MD_PROMPTX] >= 0) &&
          (list instanceof FeatureList)) {
        ((FeatureList)list).insertPrompt(ps, def[NewGameApp.MD_PROMPTX]);
      }
      return true;
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
    return false;
  }

  /**
   * Insert the item for the action into the menu (a list) at position ps. If the item has an image,
   * use that too.
   *
   * @param list
   * @param ps
   * @param action
   * @return
   */
  public static boolean newInsertMenuItem(final List list, final int action) {
    try {
      short[] cdef = null;
      final int clen = NewGameApp.menuCombined.size();
      int mix = -1;
      int i = 0;
      for (; i < clen; i++) {
        mix = ((Integer)NewGameApp.menuCombined.elementAt(i)).intValue();
        cdef = Application.menu[mix];
        if (cdef[Application.MD_MENUAC] == action) {
          break;
        }
      }
      // Item must be in combined menu list
      if ((i >= clen) || (cdef == null)) { throw new IllegalArgumentException("Definition must be in menuCombined"); }
      final Integer ix = new Integer(mix);
      if (NewGameApp.menuShown.indexOf(ix) >= 0) { return false; }
      final int slen = NewGameApp.menuShown.size();
      int j = 0;
      for (; j < slen; j++) {
        final int cix = ((Integer)NewGameApp.menuShown.elementAt(j)).intValue();
        if (mix < cix) {
          break;
        }
      }
      if (j < slen) {
        NewGameApp.menuShown.insertElementAt(ix, j);
      }
      else {
        NewGameApp.menuShown.addElement(ix);
      }
      Image icon = null;
      if (cdef[Application.MD_MENUIC] >= 0) {
        icon = Application.icons[cdef[Application.MD_MENUIC]];
      }
      list.insert(j, Application.messages[cdef[Application.MD_MENUTX]], icon);
      if ((NewGameApp.MD_PROMPTX < cdef.length) &&
          (cdef[NewGameApp.MD_PROMPTX] >= 0) &&
          (list instanceof FeatureList)) {
        ((FeatureList)list).insertPrompt(j, cdef[NewGameApp.MD_PROMPTX]);
      }
      return true;
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
    return false;
  }

  /**
   * Remove command (implemented as list) for the action from the list.
   *
   * @param list to remove from
   * @param action
   * @return
   */
  public static boolean deleteMenuItem(final List list, final int action) {
    try {
      if (NewGameApp.menuShown.size() == 0) { return false; }
      final int clen = NewGameApp.menuShown.size();
      int i = 0;
      short[] cdef;
      for (; i < clen; i++) {
        cdef = Application.menu[((Integer)NewGameApp.menuShown.elementAt(i)).intValue()];
        if (cdef[Application.MD_MENUAC] == action) {
          NewGameApp.menuShown.removeElementAt(i);
          list.delete(i);
          return true;
        }
      }
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
    return false;
  }

}
