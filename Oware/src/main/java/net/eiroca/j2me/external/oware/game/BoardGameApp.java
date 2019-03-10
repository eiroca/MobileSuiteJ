/**
 * GPL >= 2.0
 * 
 * FIX arc piece shape and size OwareScreen, no vibrate,flashBacklight for 1.0 for GameApp
 * 
 * FIX Piece image
 * 
 * FIX game menu
 * 
 * FIX rows/columns
 * 
 * TODO do Riversi
 * 
 * FIX no getGraphics for GameScreen 1.0 for GameScreen
 * 
 * FIX no suppress keys for 1.0 for GameApp
 * 
 * FIX take out fromRowString from OwareTable
 * 
 * Based upon jtReversi game written by Jataka Ltd.
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
 * This was modified no later than 2009-01-29
 */
package net.eiroca.j2me.external.oware.game;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.debug.Debug;
import net.eiroca.j2me.external.oware.midlet.AppConstants;
import net.eiroca.j2me.external.presentation.FeatureForm;
import net.eiroca.j2me.game.GameApp;
import net.eiroca.j2me.rms.Settings;

/**
 * Oware game application. Handle game options in addition to standard app options on form. Save
 * options.
 */
abstract public class BoardGameApp extends NewGameApp {

  public static String GRAPHICS_PRECALCULATE = "boardgame-precalculate";
  public static boolean precalculate = true;
  public static String storeName = "BOARD_GAME_STORE";

  public static int ACTION_OFFSET = 0;
  public static final int GA_UNDO = GameApp.GA_USERDEF + 0;
  public static final int GA_REDO = GameApp.GA_USERDEF + 1;
  public static final int GA_ENDGAME = GameApp.GA_USERDEF + 2;

  public static String[] playerNames;
  private boolean first = true;
  private byte[] bsavedRec = new byte[0];

  protected ChoiceGroup opPlayers;
  protected ChoiceGroup opLevel = null;
  protected ChoiceGroup opDept = null;
  protected ChoiceGroup opRow = null;
  protected ChoiceGroup opCol = null;
  protected ChoiceGroup opNbrPlayers = null;

  public static String BOARD_GAME_PLAYER = "board-game-player";
  public static String BOARD_GAME_FIRST = "board-game-first";
  public static String BOARD_GAME_ROW = "board-game-row";
  public static String BOARD_GAME_COL = "board-game-col";
  public static String BOARD_GAME_NBR_PLAYERS = "board-game-nbrplayers";
  public static String BOARD_GAME_TEXT_ROW = "board-game-text-row";
  public static String BOARD_GAME_LEVEL = "board-game-level";
  public static String BOARD_GAME_DEPT = "board-game-dept";
  /* How many human players. */
  public static int gsPlayer = 1;
  public static int gsFirst = 1;
  static public String[] gsSquareImages = new String[0];
  static public String[] gsPiece1Images = new String[0];
  static public String[] gsPiece2Images = new String[0];
  static public int[] gsLevelMsg = new int[0];
  /**
   * Initialization index into parameter definition
   */
  public static final int PD_CURR = 0;
  public static final int PD_DFLT = 1;
  public static final int PD_INIT = 2;
  public static final int PD_LIMIT = 3;
  public static final int PD_INCR = 4;
  public static int[] gsDepth;
  public static int[] gsRow;
  public static int[] gsCol;
  public static int[] gsNbrPlayers;
  public static int gsTextRow = 0;
  /* Skill level. */
  final static public int gsLevelNormal = 0;
  final static public int gsLevelDifficult = 1;
  final static public int gsLevelHard = 2;
  public static int[] gsLevel = new int[] {
      0, 0, 0, 0, 0
  };

  public BoardGameApp() {
    super();
    Application.menu = new short[][] {
        {
            GameApp.ME_MAINMENU, AppConstants.MSG_MENU_MAIN_CONTINUE, GameApp.GA_CONTINUE, 0
        }, {
            GameApp.ME_MAINMENU, AppConstants.MSG_MENU_MAIN_NEWGAME, GameApp.GA_NEWGAME, 1
        }, {
            GameApp.ME_MAINMENU, AppConstants.MSG_MENU_MAIN_UNDO, (short)BoardGameApp.GA_UNDO, 7
        }, {
            GameApp.ME_MAINMENU, AppConstants.MSG_MENU_MAIN_REDO, (short)BoardGameApp.GA_REDO, 8
        }, {
            GameApp.ME_MAINMENU, AppConstants.MSG_MENU_MAIN_ENDGAME, (short)BoardGameApp.GA_ENDGAME, -1, AppConstants.MSG_SURE_END
        }, {
            GameApp.ME_MAINMENU, AppConstants.MSG_MENU_MAIN_OPTIONS, GameApp.GA_OPTIONS, 4
        }, {
            GameApp.ME_MAINMENU, AppConstants.MSG_MENU_MAIN_HELP, GameApp.GA_HELP, 5
        }, {
            GameApp.ME_MAINMENU, AppConstants.MSG_MENU_MAIN_ABOUT, GameApp.GA_ABOUT, 6
        }
    };
  }

  public void init() {
    try {
      // Need to do this before game screen and table are created.
      if (first) {
        loadBoardGameCustomization();
      }
      super.init();
      final String gval = super.readAppProperty(GRAPHICS_PRECALCULATE, "true");
      BoardGameApp.precalculate = gval.equals("true");
      if (first) {
        bsavedRec = ((BoardGameScreen)game).getSavedGameRecord();
      }
      prepGameMenu(bsavedRec.length > 0);
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
    finally {
      first = false;
    }
  }

  public void setGameDefaults() {
    BoardGameApp.gsDepth[PD_CURR] = BoardGameApp.gsDepth[PD_DFLT];
    BoardGameApp.gsRow[PD_CURR] = BoardGameApp.gsRow[PD_DFLT];
    BoardGameApp.gsCol[PD_CURR] = BoardGameApp.gsCol[PD_DFLT];
    BoardGameApp.gsNbrPlayers[PD_CURR] = BoardGameApp.gsNbrPlayers[PD_DFLT];
    BoardGameApp.gsLevel[PD_CURR] = BoardGameApp.gsLevel[PD_DFLT];
  }

  protected Displayable getOptions() {
    try {
      final Form form = new FeatureForm(Application.messages[AppConstants.MSG_MENU_MAIN_OPTIONS]);
      Command cdefault = new Command(Application.messages[AppConstants.MSG_MENU_OPTIONS_DEAULT], Command.SCREEN, 9);
      form.addCommand(cdefault);
      opPlayers = Application.createChoiceGroup(AppConstants.MSG_GAMEMODE, Choice.EXCLUSIVE, new int[] {
          AppConstants.MSG_GAMEMODE1, AppConstants.MSG_GAMEMODE2
      });
      if (BoardGameApp.gsLevelMsg.length > 0) {
        opLevel = Application.createChoiceGroup(AppConstants.MSG_AILEVEL, Choice.EXCLUSIVE, BoardGameApp.gsLevelMsg);
      }
      opDept = BoardGameApp.createNumRangePD(gsDepth, AppConstants.MSG_SKILL_LEVEL);
      opRow = BoardGameApp.createNumRangePD(gsRow, AppConstants.MSG_ROW);
      opCol = BoardGameApp.createNumRangePD(gsCol, AppConstants.MSG_COL);
      opNbrPlayers = BoardGameApp.createNumRangePD(gsNbrPlayers, AppConstants.MSG_NBR_PLAYERS);
      form.append(opPlayers);
      if (opLevel != null) {
        form.append(opLevel);
      }
      if (opDept != null) {
        form.append(opDept);
      }
      if (opRow != null) {
        form.append(opRow);
      }
      if (opCol != null) {
        form.append(opCol);
      }
      if (opNbrPlayers != null) {
        form.append(opNbrPlayers);
      }
      Application.setup(form, Application.cBACK, Application.cOK);
      return form;
    }
    catch (Throwable e) {
      Debug.ignore(e);
      return null;
    }
  }

  public void doShowOptions() {
    try {
      super.doShowOptions();
      opPlayers.setSelectedIndex(BoardGameApp.gsPlayer - 1, true);
      if (opLevel != null) {
        BoardGameApp.setSelectedChoicePD(opLevel, gsLevel);
      }
      if (opDept != null) {
        BoardGameApp.setSelectedChoicePD(opDept, gsDepth);
      }
      if (opRow != null) {
        BoardGameApp.setSelectedChoicePD(opRow, gsRow);
      }
      if (opCol != null) {
        BoardGameApp.setSelectedChoicePD(opCol, gsCol);
      }
      if (opNbrPlayers != null) {
        BoardGameApp.setSelectedChoicePD(opNbrPlayers, gsNbrPlayers);
      }
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  public void doApplyOptions() {
    try {
      BoardGameApp.gsPlayer = settingsGameUpd(opPlayers.getSelectedIndex() + 1, BoardGameApp.BOARD_GAME_PLAYER, BoardGameApp.gsPlayer);
      if (opLevel != null) {
        settingsGameUpdPD(opLevel, BoardGameApp.gsLevel, BoardGameApp.BOARD_GAME_LEVEL);
      }
      if (opDept != null) {
        settingsGameUpdPD(opDept, BoardGameApp.gsDepth, BoardGameApp.BOARD_GAME_DEPT);
      }
      if (opRow != null) {
        settingsGameUpdPD(opRow, BoardGameApp.gsRow, BoardGameApp.BOARD_GAME_ROW);
      }
      if (opCol != null) {
        settingsGameUpdPD(opCol, BoardGameApp.gsCol, BoardGameApp.BOARD_GAME_COL);
      }
      if (opNbrPlayers != null) {
        settingsGameUpdPD(opNbrPlayers, BoardGameApp.gsNbrPlayers, BoardGameApp.BOARD_GAME_NBR_PLAYERS);
      }
      super.doApplyOptions();
      Settings.save();
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  private static int settingsGameUpd(int newValue, String settingsKey, int prevValue) {
    return Application.settingsUpd(newValue, GameApp.hsName + "_" + settingsKey, prevValue);
  }

  /**
   * Command dispatcher
   */
  public void commandAction(final Command c, final Displayable d) {
    try {
      if (d == gameOptions) {
        // The options have only 1 command that is not back or OK.  This is of type screen.
        if (c.getCommandType() == Command.SCREEN) {
          setGameDefaults();
          doShowOptions();
          doApplyOptions();
        }
        else {
          super.commandAction(c, d);
        }
      }
      else {
        super.commandAction(c, d);
      }

    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  /**
   * Pause the game
   */
  public void doGamePause() {
    super.doGamePause();
    prepGameMenu(true);
  }

  public void doGameAbort() {
    try {
      super.doGameAbort();
      ((BoardGameScreen)GameApp.game).gMiniMax.cancel(false);
      ((BoardGameScreen)GameApp.game).gMiniMax.clearPrecalculatedMoves();
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  public List getGameMenu() {
    List gameMenu = newGetMenu(GameApp.game.name, GameApp.ME_MAINMENU, GameApp.GA_CONTINUE, Application.cEXIT);
    return gameMenu;
  }

  protected void prepGameMenu(boolean canContinue) {
    try {
      boolean canUndo = false;
      boolean canRedo = false;
      if (canContinue) {
        newInsertMenuItem(gameMenu, GA_CONTINUE);
        if (GameApp.game != null) {
          if (BoardGameScreen.table != null) {
            if (((BoardGameScreen)GameApp.game).checkLastTable()) {
              newInsertMenuItem(gameMenu, GA_UNDO);
              canUndo = true;
            }
            if (((BoardGameScreen)GameApp.game).checkLastRedoTable()) {
              newInsertMenuItem(gameMenu, GA_REDO);
              canRedo = true;
            }
            if (canUndo || canRedo) {
              newInsertMenuItem(gameMenu, GA_ENDGAME);
            }
          }
        }
      }
      else {
        deleteMenuItem(gameMenu, GA_CONTINUE);
      }
      if (!canUndo) {
        deleteMenuItem(gameMenu, GA_UNDO);
      }
      if (!canRedo) {
        deleteMenuItem(gameMenu, GA_REDO);
      }
      if (!canUndo && !canRedo) {
        deleteMenuItem(gameMenu, GA_ENDGAME);
      }
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  public void processGameAction(final int action) {
    try {
      switch (action) {
        case GA_STARTUP: // Startup
          doStartup();
          break;
        case GA_CONTINUE: // Continue
          if (bsavedRec.length > 0) {
            ((BoardGameScreen)game).bsavedRec = bsavedRec;
            bsavedRec = new byte[0];
            doGameStart();
          }
          else {
            doGameResume();
          }
          break;
        case GA_NEWGAME: // New game
          if (bsavedRec.length > 0) {
            bsavedRec = new byte[0];
            ((BoardGameScreen)game).bsavedRec = bsavedRec;
          }
          doGameStart();
          break;
        case GA_UNDO: // Undo last move
          ((BoardGameScreen)GameApp.game).undoTable();
          doGameResume();
          break;
        case GA_REDO: // Redo last move
          ((BoardGameScreen)GameApp.game).redoTable();
          doGameResume();
          break;
        case GA_ENDGAME: // Force end game
          ((BoardGameScreen)GameApp.game).procEndGame();
          doGameResume();
          break;
        case GA_OPTIONS:
          doShowOptions();
          break;
        case GA_HELP:
          doHelp();
          break;
        case GA_ABOUT:
          doAbout();
          break;
        case GA_APPLYOPTIONS:
          doApplyOptions();
          break;
        default:
          break;
      }
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  public void loadBoardGameCustomization() {
    try {
      Settings.load();
      BoardGameApp.gsPlayer = getIntGame(BoardGameApp.BOARD_GAME_PLAYER, BoardGameApp.gsPlayer);
      BoardGameApp.gsFirst = getIntGame(BoardGameApp.BOARD_GAME_FIRST, BoardGameApp.gsFirst);
      getIntGamePD(BoardGameApp.BOARD_GAME_LEVEL, BoardGameApp.gsLevel);
      getIntGamePD(BoardGameApp.BOARD_GAME_DEPT, BoardGameApp.gsDepth);
      getIntGamePD(BoardGameApp.BOARD_GAME_ROW, BoardGameApp.gsRow);
      getIntGamePD(BoardGameApp.BOARD_GAME_COL, BoardGameApp.gsCol);
      getIntGamePD(BoardGameApp.BOARD_GAME_NBR_PLAYERS, BoardGameApp.gsNbrPlayers);
      BoardGameApp.gsTextRow = getIntGame(BoardGameApp.BOARD_GAME_TEXT_ROW, BoardGameApp.gsTextRow);
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  public int getIntGame(String name, int defaultValue) {
    return Settings.getInt(GameApp.hsName + "_" + name, defaultValue);
  }

  public void getIntGamePD(String name, int[] pdef) {
    pdef[BoardGameApp.PD_CURR] = Settings.getInt(GameApp.hsName + "_" + name, pdef[BoardGameApp.PD_CURR]);
  }

  public void getIntPD(String name, int[] pdef) {
    pdef[BoardGameApp.PD_CURR] = Settings.getInt(name, pdef[BoardGameApp.PD_CURR]);
  }

  /**
   * Destroy the game
   */
  public void done() {
    Settings.save();
    super.done();
  }

  public static ChoiceGroup createNumRangePD(int[] pdef, int msgNbr) {
    if (pdef[BoardGameApp.PD_LIMIT] < 0) {
      return Application.createNumRange(msgNbr, pdef[BoardGameApp.PD_INIT], pdef[BoardGameApp.PD_LIMIT], pdef[BoardGameApp.PD_INCR]);
    }
    else {
      return null;
    }
  }

  public static void setSelectedChoicePD(Choice choice, int[] pdef) {
    choice.setSelectedIndex(pdef[BoardGameApp.PD_CURR] - pdef[BoardGameApp.PD_INIT], true);
  }

  private static void settingsGameUpdPD(Choice choice, int[] pdef, String settingsKey) {
    pdef[BoardGameApp.PD_CURR] = settingsGameUpd(choice.getSelectedIndex() + pdef[BoardGameApp.PD_INIT], settingsKey, pdef[BoardGameApp.PD_CURR]);
  }

  public static void settingsUpdPD(Choice choice, int[] pdef, String settingsKey) {
    pdef[BoardGameApp.PD_CURR] = Application.settingsUpd(choice.getSelectedIndex() + pdef[BoardGameApp.PD_INIT], settingsKey, pdef[BoardGameApp.PD_CURR]);
  }

}
