/**
 * FIX use baseap for commands, etc. Use confirm and settings FeatureMgr.java
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

import java.util.Hashtable;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.debug.Debug;

/*
 * Allow Form/List with optional commands added with addPromptCommand which if used, will give
 * prompt message with OK/Cancel. Also, perform commandAction in thread to prevent hangs.
 */

public class FeatureMgr implements CommandListener, Runnable {

  private Hashtable promptCommands = null;
  private Hashtable promptIndexes = null;
  public static final int DEFAULT_FONT_CHOICE = 0;
  protected int fontChoice = FeatureMgr.DEFAULT_FONT_CHOICE;
  protected Font font;
  final private Displayable disp;
  private Displayable promptDisp1 = null;
  private Displayable promptDisp2 = null;
  private Command origCmd = null;
  protected Command exCmd = null;
  private boolean foundDisp = false;
  private boolean foundPrompt = false;
  private Displayable exDisp = null;
  private boolean background = false; // Flag to continue looping
  private int loop = 0; // Number of times to loop
  private Thread netThread = null; // The thread for networking, etc

  private CommandListener cmdFeatureUser = null;
  private Runnable runFeatureUser = null;

  public FeatureMgr(final Displayable disp) {
    this.disp = disp;
  }

  public void setRunnable(final Runnable runFeatureUser, final boolean background) {
    synchronized (this) {
      if (background) {
        if (runFeatureUser != null) {
          if (!(runFeatureUser instanceof Runnable)) { throw new IllegalArgumentException("Listener must implement Runnable"); }
          this.runFeatureUser = runFeatureUser;
        }
        else {
          this.runFeatureUser = runFeatureUser;
        }
      }
      this.background = background;
    }
    if (background) {
      startWakeup(false);
    }
  }

  public void setCommandListener(final CommandListener cmdFeatureUser, final boolean background) {
    synchronized (this) {
      this.cmdFeatureUser = cmdFeatureUser;
      if (background) {
        if (cmdFeatureUser != null) {
          if (!(cmdFeatureUser instanceof Runnable)) { throw new IllegalArgumentException(
              "Listener must implement Runnable"); }
          runFeatureUser = (Runnable)cmdFeatureUser;
        }
        else {
          runFeatureUser = (Runnable)cmdFeatureUser;
        }
      }
      this.background = background;
    }
    if (background) {
      startWakeup(false);
    }
  }

  public void addPromptCommand(final Command cmd, final int prompt) {
    synchronized (this) {
      if (promptCommands == null) {
        promptCommands = new Hashtable();
      }
      promptCommands.put(cmd, new Integer(prompt));
    }
  }

  public void addPromptIndex(final int ps, final int prompt) {
    synchronized (this) {
      if (promptIndexes == null) {
        promptIndexes = new Hashtable();
      }
      promptIndexes.put(new Integer(ps), new Integer(prompt));
    }
  }

  public void removeCommand(final Command cmd) {
    removePrompt(cmd);
    disp.removeCommand(cmd);
  }

  public void removePrompt(final Command cmd) {
    synchronized (this) {
      if (promptCommands != null) {
        promptCommands.remove(cmd);
      }
    }
  }

  /* Create prompt alert. */
  @Override
  public void run() {
    /* Use networking if necessary */
    long lngStart;
    long lngTimeTaken;
    do {
      try {
        Command ccmd = null;
        Displayable cdisp = null;
        Command corigCmd = null;
        boolean cfoundDisp = false;
        boolean cfoundPrompt = false;
        synchronized (this) {
          cfoundDisp = foundDisp;
          cfoundPrompt = foundPrompt;
          if ((cfoundDisp || cfoundPrompt) && (exCmd != null)) {
            ccmd = exCmd;
            cdisp = exDisp;
            corigCmd = origCmd;
          }
          corigCmd = origCmd;
        }
        if ((ccmd != null) && (cdisp != null)) {
          try {
            Hashtable cpromptCommands = null;
            synchronized (this) {
              cpromptCommands = promptCommands;
            }
            if (cfoundDisp && (cpromptCommands != null)
                && cpromptCommands.containsKey(ccmd)) {
              synchronized (this) {
                origCmd = ccmd;
              }
              final int promptMsg = ((Integer)cpromptCommands.get(ccmd)).intValue();
              // Due to a quirk on T637 (MIDP 1.0), we need to create a form
              // before the alert or the alert will not be seen.
              // FIX
              final Form formAlert = new Form(ccmd.getLabel());
              formAlert.append(Application.messages[promptMsg]);
              formAlert.addCommand(Application.cOK);
              formAlert.addCommand(Application.cBACK);
              formAlert.setCommandListener(this);
              BaseApp.setDisplay(formAlert);
              BaseApp.setDisplay(formAlert);
              synchronized (this) {
                promptDisp1 = formAlert;
                /* Change if using alerts promptDisp2 = promptAlert; */
              }
            }
            else if (cfoundDisp && cdisp.equals(disp)) {
              cmdFeatureUser.commandAction(ccmd, cdisp);
              if (background && (runFeatureUser != null)) {
                runFeatureUser.run();
              }
            }
            if (cfoundPrompt && !cdisp.equals(disp)) {
              try {
                if ((ccmd == Application.cOK) || ccmd.equals(Alert.DISMISS_COMMAND)) {
                  BaseApp.setDisplay(disp);
                  cmdFeatureUser.commandAction(corigCmd, disp);
                  if (background && (runFeatureUser != null)) {
                    runFeatureUser.run();
                  }
                }
                else if (ccmd == Application.cBACK) {
                  BaseApp.setDisplay(disp);
                }
              }
              finally {
                synchronized (this) {
                  origCmd = null;
                  promptDisp1 = disp;
                  promptDisp2 = disp;
                }
              }
            }
          }
          catch (final Throwable e) {
            System.out.println("commandAction caught " + e + " " + e.getMessage());
          }
          finally {
            synchronized (this) {
              foundDisp = false;
              foundPrompt = false;
              exCmd = null;
              exDisp = null;
            }
          }
        }
        else {
          if (background && (runFeatureUser != null)) {
            runFeatureUser.run();
          }
        }
        lngStart = System.currentTimeMillis();
        lngTimeTaken = System.currentTimeMillis() - lngStart;
        if (lngTimeTaken < 100L) {
          synchronized (this) {
            if (loop-- <= 0) {
              super.wait(75L - lngTimeTaken);
            }
          }
        }
      }
      catch (final InterruptedException e) {
        break;
      }
    }
    while (background);
  }

  /* Prompt if command is in prompt camands.  */
  @Override
  public void commandAction(final Command cmd, final Displayable cdisp) {
    synchronized (this) {
      foundDisp = (cdisp == disp);
      foundPrompt = (cdisp != promptDisp1) &&
          ((cdisp == promptDisp1) || (cdisp == promptDisp2));
      exCmd = cmd;
      exDisp = cdisp;
    }
    startWakeup(true);
  }

  public void startWakeup(final boolean wakeupThread) {
    if ((netThread == null) || !netThread.isAlive()) {
      try {
        netThread = new Thread(this, "T" + disp.getClass().getName());
        netThread.start();
      }
      catch (final Exception e) {
        System.err.println("Could not restart thread.");
        Debug.ignore(e);
      }
    }
    else if (wakeupThread) {
      wakeup(3);
    }
  }

  /* Notify us that we are finished. */
  public void wakeup(final int loop) {

    synchronized (this) {
      this.loop += loop;
      super.notify();
    }
  }

  public void setBackground(final boolean background) {
    this.background = background;
  }

  /* Get the font size. This is the actual size of the font */
  final public int getFontSize(final int fontChoice) {
    int fontSize;
    switch (fontChoice) {
      case 1:
        fontSize = Font.SIZE_SMALL;
        break;
      case 2:
        fontSize = Font.SIZE_MEDIUM;
        break;
      case 3:
        fontSize = Font.SIZE_LARGE;
        break;
      case DEFAULT_FONT_CHOICE:
      default:
        fontSize = Font.getDefaultFont().getSize();
        break;
    }
    return fontSize;
  }

  public void initFont() {
    if (fontChoice == FeatureMgr.DEFAULT_FONT_CHOICE) {
      font = Font.getDefaultFont();
    }
    else {
      final Font defFont = Font.getDefaultFont();
      font = Font.getFont(Font.FACE_SYSTEM, defFont.getStyle(), getFontSize(fontChoice));
    }
  }

}
