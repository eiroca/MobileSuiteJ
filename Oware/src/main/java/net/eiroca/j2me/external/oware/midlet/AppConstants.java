/**
 * GPL >= 2.0 Based upon jtReversi game written by Jataka Ltd.
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
package net.eiroca.j2me.external.oware.midlet;

/**
 * Game application constants.
 */
public class AppConstants {

  public final static int MSG_LABEL_OK = 0;
  public final static int MSG_LABEL_BACK = 1;
  public final static int MSG_LABEL_EXIT = 2;
  public final static int MSG_LABEL_YES = 3;
  public final static int MSG_LABEL_NO = 4;
  public final static int MSG_TEXT_GAMEOVER_01 = 5;
  public final static int MSG_TEXT_GAMEOVER_02 = 6;
  public final static int MSG_TEXT_GAMEOVER_03 = 7;
  public final static int MSG_TEXT_HIGHSCORE_01 = 8;
  public final static int MSG_TEXT_HIGHSCORE_02 = 9;
  public final static int MSG_TEXT_HIGHSCORE_03 = 10;
  public final static int MSG_TEXT_HIGHSCORE_04 = 11;
  public final static int MSG_MENU_MAIN_CONTINUE = 12;
  public final static int MSG_MENU_MAIN_NEWGAME = 13;
  public final static int MSG_MENU_MAIN_HIGHSCORE = 14;
  public final static int MSG_MENU_MAIN_SETTINGS = 15;
  public final static int MSG_MENU_MAIN_OPTIONS = 16;
  public final static int MSG_MENU_MAIN_HELP = 17;
  public final static int MSG_MENU_MAIN_ABOUT = 18;
  public final static int MSG_MENU_SETTINGS_VOLUME = 19;
  public final static int MSG_MENU_SETTINGS_VIBRATE = 20;
  public final static int MSG_MENU_SETTINGS_BACKLIGHT = 21;
  public final static short MSG_GAMEAPP_USERDEF = 22;
  public static short msgOffset = 0;
  final public static short MSG_MENU_MAIN_UNDO = (short)(AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++);
  final public static short MSG_MENU_MAIN_REDO = (short)(AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++);
  final public static short MSG_MENU_MAIN_ENDGAME = (short)(AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++);
  final public static short MSG_MENU_MAIN_PAUSE = (short)(AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++);
  final public static short MSG_MENU_MAIN_TEST_ENDGAME = (short)(AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++);
  final public static short MSG_MENU_MAIN_TEST = (short)(AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++);
  final public static short MSG_MENU_MAIN_LOGGING = (short)(AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++);
  final public static short MSG_MENU_OPTIONS_DEAULT = (short)(AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++);
  final public static int MSG_GAMEMODE = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_GAMEMODE1 = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_GAMEMODE2 = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_AILEVEL = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++; //A.I. Difficulty
  final public static int MSG_AILEVEL1 = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_AILEVEL2 = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  // FIX use messages for numbers
  final public static int MSG_AILEVEL3 = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_AILEVEL4 = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++; // 10
  final public static int MSG_SKILL_LEVEL = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++; // 10
  final public static int MSG_NAMEPLAYER1 = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_NAMEPLAYER2 = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_ROW = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_COL = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_NBR_PLAYERS = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_GOODLUCK = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_THINKING = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_INVALIDMOVE = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_EXTRAMOVE = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_WONCOMPUTER = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_HUMANWON = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_PLAYERWON = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_DRAW = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_HUMAN = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_COMPUTER = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static int MSG_PASS = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  final public static short MSG_SURE_END = (short)(AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++);
  // The 3 messages are used for level with offset form MSG_LEVELPREFIX
  final public static int MSG_LEVELPREFIX = AppConstants.MSG_GAMEAPP_USERDEF + (AppConstants.msgOffset += 3);
  final public static int MSG_BOARDAPP_USERDEF = AppConstants.MSG_GAMEAPP_USERDEF + AppConstants.msgOffset++;
  public static short boardMsgOffset = 0;
  final public static int MSG_OWARE_NAME = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++; // 0
  final public static int MSG_INIT_SEEDS = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_MAX_HOUSES = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_MULTI_LAP = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_ONE_LAP = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_1ST_LAP = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_2ND_LAP = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_START_SOWING = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_SOW_NEXT = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_SOW_FIRST = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_LOOP_SKIP = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_SKIP_STARTING = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_SOW_STARTING = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_SOW_STORE_RULE = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_SKIP_STORE = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_SOW_STORE = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_CAPTURE_RULES = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_CAPTURE_EMPTY = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_CAPTURE_2_3 = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_CAPTURE_4 = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_GRAND_SLAM = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_GRAND_SLAM1 = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_GRAND_SLAM2 = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_GRAND_SLAM3 = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_GRAND_SLAM4 = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_GRAND_SLAM5 = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_GRAND_SLAM6 = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_OPPONENT_EMPTY = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_OPPONENT_EMPTY1 = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static int MSG_OPPONENT_EMPTY2 = AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++;
  final public static short MSG_OWARE_USERDEF = (short)(AppConstants.MSG_BOARDAPP_USERDEF + AppConstants.boardMsgOffset++);
  public static short owareMsgOffset = 0;
  final public static int MSG_REVERSI_NAME = AppConstants.MSG_OWARE_USERDEF + AppConstants.owareMsgOffset++;
  final public static int MSG_UPPER = AppConstants.MSG_OWARE_USERDEF + AppConstants.owareMsgOffset++;
  final public static int MSG_LOWER = AppConstants.MSG_OWARE_USERDEF + AppConstants.owareMsgOffset++;
  final public static short MSG_REVERSI_USERDEF = (short)(AppConstants.MSG_OWARE_USERDEF + AppConstants.owareMsgOffset++);
  public static short reversiMsgOffset = 0;
  public static final int MSG_MINE_NAME = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;
  public static final int MSG_MENU_OPTIONS_LEVEL = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;

  public static final int MSG_TEXT_LEVEL_01 = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;
  public static final int MSG_TEXT_LEVEL_02 = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;
  public static final int MSG_TEXT_LEVEL_03 = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;
  public static final int MSG_TEXT_LEVEL_04 = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;
  public static final int MSG_CUSTOMLEVEL = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;
  public static final int MSG_CL_HEIGTH = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;
  public static final int MSG_CL_WIDTH = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;
  public static final int MSG_CL_BOMBS = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;
  public static final int MSG_CL_ERR_HEIGHT = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;
  public static final int MSG_CL_ERR_WIDTH = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;
  public static final int MSG_CL_ERR_BOMBS = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;
  public static final int MSG_CL_ERR_FRM = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;
  public static final int MSG_CL_ERR_TO = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;

  public static final int MSG_HS_LEVEL = AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++;
  public static final short MSG_MINE_USERDEF = (short)(AppConstants.MSG_REVERSI_USERDEF + AppConstants.reversiMsgOffset++);
  public static int mineMsgOffset = 0;
  public static final int MSG_SECURESMS_NAME = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_SAVE = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_SEND = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_REPLY = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_DELETE = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_ADD = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_CANCEL = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_INBOX = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_SENDNEW = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_SENTITEMS = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_ADDRESSBOOK = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_MESSAGE = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_ADDRESS = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_NEWADDRESS = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_NEWMESSAGE = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_TEXT = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_TO = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_FROM = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_NUMBER = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_NAME = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_KEY = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_MESSAGESENT = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_MESSAGEHASNOTBEENSENT = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_MESSAGEDELETED = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_MESSAGEHASNOTBEENDELETED = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_MESSAGERECEIVED = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_ADDRESSSAVED = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_ADDRESSHASNOTBEENSAVED = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_ADDRESSDELETED = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_ADDRESSHASNOTBEENDELETED = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_ERROR = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_INFO = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_MESSAGESTOREERROR = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_ADDRESSSTOREERROR = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_CANNOTSTART = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_CHANGEPIN = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_INSERTPINTEXT = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_PIN = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_WRONGPIN = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_INSERTPIN = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_INVALINDPIN = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_CLEANUP = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_CLEANUP1 = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_CLEANUP2 = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_CLEANUP3 = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_CONFIRM = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_ARESURE = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_NUMPREFIX = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_INVALID = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_MESSAGEIVALID = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_ADDRESSBOOKEMPTY = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;
  public static final int MSG_SECURESMS_USERDEF = AppConstants.MSG_MINE_USERDEF + AppConstants.mineMsgOffset++;

}
