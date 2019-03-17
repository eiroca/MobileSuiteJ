/**
 * GPL >= 2.0
 * 
 *
 * This software was modified 2008-12-07. The original file was Reversi.java in
 * mobilesuite.sourceforge.net project.
 *
 * Copyright (C) 2002-2004 Salamon Andras
 * 
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
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
 * not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 *
 */
/**
 * FIX arc piece shape and size OwareScreen, no vibrate,flashBacklight for 1.0 for GameApp
 * 
 * TODO optional skip first hole
 * 
 * FIX game menu
 * 
 * FIX Don't assume that a player owns the pits in his role to allow capture versions.
 * 
 * TODO do Riversi modifications FIX no getGraphics for GameScreen 1.0 for GameScreen
 * 
 * FIX no suppress keys for 1.0 for GameApp
 * 
 * FIX take out fromRowString from OwareTable Based upon jtReversi game written by Jataka Ltd.
 * 
 */
package net.eiroca.j2me.external.oware.midlet;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.app.SplashScreen;
import net.eiroca.j2me.debug.Debug;
import net.eiroca.j2me.external.oware.game.BoardGameApp;
import net.eiroca.j2me.external.oware.game.OwareGame;
import net.eiroca.j2me.external.oware.game.OwareTable;
import net.eiroca.j2me.external.oware.game.ui.OwareScreen;
import net.eiroca.j2me.game.GameApp;
import net.eiroca.j2me.game.GameScreen;
import net.eiroca.j2me.rms.Settings;

/**
 * Oware game application. Handle game options in addition to standard app options on form. Save
 * options.
 */
public class OwareMIDlet extends BoardGameApp {

  protected ChoiceGroup opInitSeeds;
  protected ChoiceGroup opMaxHouses;
  protected ChoiceGroup opMultiLap;
  protected ChoiceGroup opStartFirst;
  protected ChoiceGroup opSkipStarting;
  protected ChoiceGroup opSowStore;
  protected ChoiceGroup opCapture;
  protected ChoiceGroup opGrandSlam;
  protected ChoiceGroup opOpponentEmpty;

  final static public String OWARE_INIT_SEEDS = "oware-init-seeds";
  final static public String OWARE_GRAND_SLAM = "oware-grand-slam";
  final static public String OWARE_MAX_HOUSES = "oware-max-houses";
  final static public String OWARE_MULTI_LAP = "oware-multi-lap";
  final static public String OWARE_START_FIRST = "oware-start-first";
  final static public String OWARE_SKIP_STARTING = "oware-skip-starting";
  final static public String OWARE_SOW_STORE = "oware-sow-store";
  final static public String OWARE_CAPTURE = "oware-capture";
  final static public String OWARE_OPP_NO_SEEDS = "oware-opp-no-seeds";
  /* How many human players. */
  /* Skill level. */
  final static public int LEVEL_NORMAL = 0;
  final static public int LEVEL_DIFFICULT = 1;
  final static public int LEVEL_HARD = 2;
  public static int[] gsInitSeeds = new int[] {
      OwareTable.INIT_SEEDS,
      OwareTable.INIT_SEEDS, 1, -9, 1
  };
  public static int[] gsMultiLap = new int[] {
      0, 0, 0, -2, 1
  };
  public static int[] gsStartFirst = new int[] {
      0, 0, 0, -1, 1
  };
  public static int[] gsSkipStarting = new int[] {
      0, 0, 0, -1, 1
  };
  public static int[] gsSowStore = new int[] {
      0, 0, 0, -1, 1
  };
  public static int[] gsCapture = new int[] {
      1, 1, 0, -2, 1
  };
  public static int[] gsMaxHouses = new int[] {
      6, 6, 1, -8, 1
  };
  public static int[] gsGrandSlam = new int[] {
      0, 0, 0, -OwareGame.GRAND_SLAM_LEGAL_24, 1
  };
  public static boolean gsOpponentEmpty = true;

  public OwareMIDlet() {
    super();
    BaseApp.resPrefix = "ow";
    BoardGameApp.storeName = "OWARE_GAME_STORE";
    BoardGameApp.gsLevelMsg = new int[] {
        AppConstants.MSG_AILEVEL1, //
        AppConstants.MSG_AILEVEL2, //
        AppConstants.MSG_AILEVEL3
    };
    BoardGameApp.gsSquareImages = new String[0];
    BoardGameApp.gsPiece1Images = new String[] {
        "oware_icon12.png", //
        "oware_icon14.png", //
        "oware_icon16.png", //
        "oware_icon18.png", //
        "oware_icon20.png"
    };
    BoardGameApp.gsPiece2Images = BoardGameApp.gsPiece1Images;
    GameApp.hsName = "Oware";
    BoardGameApp.gsTextRow = 2;
    BoardGameApp.gsDepth = new int[] {
        3, 3, 1, -14, 1
    };
    BoardGameApp.gsRow = new int[] {
        2, 2, 2, -4, 2
    };
    // Unee has only 3 columns.
    BoardGameApp.gsCol = new int[] {
        6, 6, 3, -9, 1
    };
    BoardGameApp.gsNbrPlayers = new int[] {
        2, 2, 2, 2, 1
    };
    setGameDefaults();
  }

  @Override
  public void setGameDefaults() {
    super.setGameDefaults();
    OwareMIDlet.gsMaxHouses[BoardGameApp.PD_CURR] = OwareMIDlet.gsMaxHouses[BoardGameApp.PD_DFLT];
    OwareMIDlet.gsMultiLap[BoardGameApp.PD_CURR] = OwareMIDlet.gsMultiLap[BoardGameApp.PD_DFLT];
    OwareMIDlet.gsStartFirst[BoardGameApp.PD_CURR] = OwareMIDlet.gsStartFirst[BoardGameApp.PD_DFLT];
    OwareMIDlet.gsSkipStarting[BoardGameApp.PD_CURR] = OwareMIDlet.gsSkipStarting[BoardGameApp.PD_DFLT];
    OwareMIDlet.gsSowStore[BoardGameApp.PD_CURR] = OwareMIDlet.gsSowStore[BoardGameApp.PD_DFLT];
    OwareMIDlet.gsCapture[BoardGameApp.PD_CURR] = OwareMIDlet.gsCapture[BoardGameApp.PD_DFLT];
    OwareMIDlet.gsInitSeeds[BoardGameApp.PD_CURR] = OwareMIDlet.gsInitSeeds[BoardGameApp.PD_DFLT];
    BoardGameApp.gsLevel[BoardGameApp.PD_CURR] = BoardGameApp.gsLevelDifficult;
  }

  @Override
  public void init() {
    try {
      super.init();
      // UNDO Fix pre calculate
      if (BoardGameApp.precalculate) {
        BoardGameApp.precalculate = false;
      }
      BoardGameApp.playerNames = new String[] {
          Application.messages[AppConstants.MSG_NAMEPLAYER1], //
          Application.messages[AppConstants.MSG_NAMEPLAYER2]
      };
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
  }

  @Override
  public GameScreen getGameScreen() {
    try {
      final OwareScreen ows = new OwareScreen(this, false, true);
      return ows;
    }
    catch (final Throwable e) {
      Debug.ignore(e);
      return null;
    }
  }

  @Override
  protected Displayable getOptions() {
    try {
      final Form form = (Form)super.getOptions();
      opInitSeeds = BoardGameApp.createNumRangePD(OwareMIDlet.gsInitSeeds, AppConstants.MSG_INIT_SEEDS);
      opMaxHouses = BoardGameApp.createNumRangePD(OwareMIDlet.gsMaxHouses, AppConstants.MSG_MAX_HOUSES);
      opMultiLap = Application.createChoiceGroup(AppConstants.MSG_MULTI_LAP, Choice.EXCLUSIVE, new int[] {
          AppConstants.MSG_ONE_LAP, AppConstants.MSG_1ST_LAP, AppConstants.MSG_2ND_LAP
      });
      opStartFirst = Application.createChoiceGroup(AppConstants.MSG_START_SOWING, Choice.EXCLUSIVE, new int[] {
          AppConstants.MSG_SOW_NEXT, AppConstants.MSG_SOW_FIRST
      });
      opSkipStarting = Application.createChoiceGroup(AppConstants.MSG_LOOP_SKIP, Choice.EXCLUSIVE, new int[] {
          AppConstants.MSG_SKIP_STARTING, AppConstants.MSG_SOW_STARTING
      });
      opSowStore = Application.createChoiceGroup(AppConstants.MSG_SOW_STORE_RULE, Choice.EXCLUSIVE, new int[] {
          AppConstants.MSG_SKIP_STORE, AppConstants.MSG_SOW_STORE
      });
      opCapture = Application.createChoiceGroup(AppConstants.MSG_CAPTURE_RULES, Choice.EXCLUSIVE, new int[] {
          AppConstants.MSG_CAPTURE_EMPTY,
          AppConstants.MSG_CAPTURE_2_3, AppConstants.MSG_CAPTURE_4
      });
      opGrandSlam = Application.createChoiceGroup(AppConstants.MSG_GRAND_SLAM, Choice.EXCLUSIVE, new int[] {
          AppConstants.MSG_GRAND_SLAM1,
          AppConstants.MSG_GRAND_SLAM2, AppConstants.MSG_GRAND_SLAM3,
          AppConstants.MSG_GRAND_SLAM4, AppConstants.MSG_GRAND_SLAM5
      });
      opOpponentEmpty = Application.createChoiceGroup(AppConstants.MSG_OPPONENT_EMPTY, Choice.EXCLUSIVE, new int[] {
          AppConstants.MSG_OPPONENT_EMPTY1,
          AppConstants.MSG_OPPONENT_EMPTY2
      });
      form.append(opInitSeeds);
      form.append(opMaxHouses);
      form.append(opMultiLap);
      form.append(opSkipStarting);
      form.append(opSowStore);
      form.append(opCapture);
      form.append(opGrandSlam);
      form.append(opOpponentEmpty);
      return form;
    }
    catch (final Throwable e) {
      Debug.ignore(e);
      return null;
    }
  }

  @Override
  public void doShowOptions() {
    try {
      super.doShowOptions();
      BoardGameApp.setSelectedChoicePD(opInitSeeds, OwareMIDlet.gsInitSeeds);
      BoardGameApp.setSelectedChoicePD(opMaxHouses, OwareMIDlet.gsMaxHouses);
      BoardGameApp.setSelectedChoicePD(opMultiLap, OwareMIDlet.gsMultiLap);
      BoardGameApp.setSelectedChoicePD(opSkipStarting, OwareMIDlet.gsSkipStarting);
      BoardGameApp.setSelectedChoicePD(opSowStore, OwareMIDlet.gsSowStore);
      BoardGameApp.setSelectedChoicePD(opCapture, OwareMIDlet.gsCapture);
      BoardGameApp.setSelectedChoicePD(opGrandSlam, OwareMIDlet.gsGrandSlam);
      opOpponentEmpty.setSelectedIndex(OwareMIDlet.gsOpponentEmpty ? 1 : 0, true);
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
  }

  @Override
  public void doApplyOptions() {
    try {
      super.doApplyOptions();
      BoardGameApp.settingsUpdPD(opInitSeeds, OwareMIDlet.gsInitSeeds, OwareMIDlet.OWARE_INIT_SEEDS);
      BoardGameApp.settingsUpdPD(opMaxHouses, OwareMIDlet.gsMaxHouses, OwareMIDlet.OWARE_MAX_HOUSES);
      BoardGameApp.settingsUpdPD(opMultiLap, OwareMIDlet.gsMultiLap, OwareMIDlet.OWARE_MULTI_LAP);
      BoardGameApp.settingsUpdPD(opSkipStarting, OwareMIDlet.gsSkipStarting, OwareMIDlet.OWARE_SKIP_STARTING);
      BoardGameApp.settingsUpdPD(opSowStore, OwareMIDlet.gsSowStore, OwareMIDlet.OWARE_SOW_STORE);
      BoardGameApp.settingsUpdPD(opCapture, OwareMIDlet.gsCapture, OwareMIDlet.OWARE_CAPTURE);
      BoardGameApp.settingsUpdPD(opGrandSlam, OwareMIDlet.gsGrandSlam, OwareMIDlet.OWARE_GRAND_SLAM);
      OwareMIDlet.gsOpponentEmpty = (Application.settingsUpd(opOpponentEmpty.getSelectedIndex(), OwareMIDlet.OWARE_OPP_NO_SEEDS, (OwareMIDlet.gsOpponentEmpty ? 1 : 0)) == 1);
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
  }

  @Override
  public void loadBoardGameCustomization() {
    try {
      super.loadBoardGameCustomization();
      getIntPD(OwareMIDlet.OWARE_INIT_SEEDS, OwareMIDlet.gsInitSeeds);
      getIntPD(OwareMIDlet.OWARE_GRAND_SLAM, OwareMIDlet.gsGrandSlam);
      getIntPD(OwareMIDlet.OWARE_MAX_HOUSES, OwareMIDlet.gsMaxHouses);
      getIntPD(OwareMIDlet.OWARE_MULTI_LAP, OwareMIDlet.gsMultiLap);
      getIntPD(OwareMIDlet.OWARE_SKIP_STARTING, OwareMIDlet.gsSkipStarting);
      getIntPD(OwareMIDlet.OWARE_SOW_STORE, OwareMIDlet.gsSowStore);
      getIntPD(OwareMIDlet.OWARE_CAPTURE, OwareMIDlet.gsCapture);
      OwareMIDlet.gsOpponentEmpty = (Settings.getInt(OwareMIDlet.OWARE_OPP_NO_SEEDS, (OwareMIDlet.gsOpponentEmpty ? 1 : 0)) == 1);
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
  }

  /**
   * Game Shutdown
   */
  public void doShutdown() {
  }

  @Override
  protected Displayable getSplash() {
    return new SplashScreen(GameApp.resSplash, gameMenu, 3000, 0xFFFFFF);
  }

}
