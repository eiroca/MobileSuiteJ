/**
 * GPL >= 2.0 Based upon jtReversi game written by Jataka Ltd.
 *
 * This software was modified 2008-12-07. The original file was ReversiScreen.java in
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
 */
package net.eiroca.j2me.external.oware.game.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.debug.Debug;
import net.eiroca.j2me.external.oware.game.BoardGameApp;
import net.eiroca.j2me.external.oware.game.BoardGameMove;
import net.eiroca.j2me.external.oware.game.BoardGameScreen;
import net.eiroca.j2me.external.oware.game.BoardGameTable;
import net.eiroca.j2me.external.oware.game.LimitedMinMax;
import net.eiroca.j2me.external.oware.game.OwareGame;
import net.eiroca.j2me.external.oware.game.OwareMinMax;
import net.eiroca.j2me.external.oware.game.OwareMove;
import net.eiroca.j2me.external.oware.game.OwareTable;
import net.eiroca.j2me.external.oware.midlet.AppConstants;
import net.eiroca.j2me.external.oware.midlet.OwareMIDlet;
import net.eiroca.j2me.game.GameApp;

/**
 * Oware game screen (game canvas).
 */
public final class OwareScreen extends BoardGameScreen {

  public OwareScreen(final GameApp midlet, final boolean suppressKeys, final boolean fullScreen) {
    /* Do not suppress keys.  However, do full screen. */
    super(midlet, false, true, AppConstants.MSG_OWARE_NAME);
    BoardGameScreen.rgame = new OwareGame();
    // FIX for different AIs and skill
    switch (OwareMIDlet.gsLevel[BoardGameApp.PD_CURR]) {
      case OwareMIDlet.gsLevelNormal:
        gMiniMax = new LimitedMinMax();
        break;
      case OwareMIDlet.gsLevelDifficult:
        gMiniMax = new OwareMinMax(0);
        break;
      case OwareMIDlet.gsLevelHard:
      default:
        gMiniMax = new OwareMinMax(1);
        break;
    }
  }

  public void init() {
    try {
      synchronized (this) {
        BoardGameScreen.table = new OwareTable(BoardGameApp.gsRow[BoardGameApp.PD_CURR], BoardGameApp.gsCol[BoardGameApp.PD_CURR], BoardGameApp.gsNbrPlayers[BoardGameApp.PD_CURR], OwareMIDlet.gsInitSeeds[BoardGameApp.PD_CURR]);
      }
      super.init();
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  /**
   * Draw a piece on the board at the row/col for the player. If onBoard is true, the piece is in
   * the playing area. If false, it is on the right to identify the players.
   *
   * @param row
   * @param col
   * @param player
   * @param onBoard
   */
  protected void drawPiece(BoardGameTable bgt, final int row, final int col, final int player, boolean onBoard, Image cupImage, int yadjust, int lastMovePoint) {
    try {
      OwareTable ot = (OwareTable)bgt;
      final int x = off_x + col * sizex + piece_offx;
      int y = off_y + row * sizey + piece_offy + yadjust;
      if (y < 0) {
        y = 0;
      }
      int lastMove = onBoard ? lastMovePoint : (byte)0;
      int seeds = onBoard ? ot.getSeeds(row, col) : 0;
      if (onBoard && (seeds == 0)) {
        // Reverse the square
        screen.setColor(Application.foreground);
        screen.drawRect(x, y, cupWidth, cupHeight);
        screen.fillRect(x, y, cupWidth, cupHeight);
        screen.setColor(Application.background);
        screen.fillArc(x, y, cupWidth, cupHeight, 0, 360);
      }
      else {
        if (player == OwareMIDlet.gsFirst) {
          screen.setColor(BoardGameScreen.COLOR_P1);
        }
        else {
          screen.setColor(BoardGameScreen.COLOR_P2);
        }
        if (cupImage == null) {
          screen.fillArc(x, y, cupWidth, cupHeight, 0, 360);
        }
        else {
          screen.drawImage(cupImage, x + cupImagexOffset, y, Graphics.TOP | Graphics.LEFT);
        }
      }
      if (onBoard) {
        screen.setColor(Application.foreground);
        screen.drawString(" " + String.valueOf(
            (int)seeds),
            x, y + cupHeight + 1, Graphics.TOP | Graphics.HCENTER);
        if (lastMove > 0) {
          screen.drawString(" " + String.valueOf(
              (int)-lastMove),
              x, y + cupHeight + 2 + fontHeight, Graphics.TOP | Graphics.HCENTER);
        }
      }
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  protected void drawTable(BoardGameTable bgt) {
    try {
      OwareTable ot = (OwareTable)bgt;
      int item;
      for (int i = 0; i < ot.nbrRow; ++i) {
        for (int j = 0; j < ot.nbrCol; ++j) {
          item = ot.getItem(i, j);
          if (item != 0) {
            int lastCol = -10;
            int lastRow = -10;
            int lastPoint = 10;
            OwareMove clastMove = (OwareMove)ot.getLastMove(item - 1);
            if (clastMove != null) {
              lastRow = clastMove.row;
              lastCol = clastMove.col;
              lastPoint = clastMove.getPoint();
            }
            drawPiece(bgt, i, j, item, true, (item == 1) ? piece1Image : piece2Image, 0, ((i == lastRow) && (j == lastCol)) ? lastPoint : 0);
          }
        }
      }
      for (int i = 0; i < ot.nbrPlayers; ++i) {
        infoLines[i] = " " + Integer.toString(ot.getPoint((byte)i));
      }
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  public void drawVertInfo(BoardGameTable bgt) {
    try {
      // two pieces
      drawPiece(bgt, 0, bgt.nbrCol, 1, false, ((BoardGameScreen.actPlayer == 0) ? piece2Image : piece1Image), 0, 0); /* y, x */
      if (BoardGameScreen.actPlayer == 0) {
        drawSelectionBox(bgt.nbrCol, 0, 0);
      }
      // FIX
      int cadjust = -pieceHeight - piece_offy;
      drawPiece(bgt, bgt.nbrRow, bgt.nbrCol, 0, false, ((BoardGameScreen.actPlayer == 1) ? piece2Image : piece1Image), cadjust, 0); /* y, x */
      if (BoardGameScreen.actPlayer == 1) {
        drawSelectionBox(bgt.nbrCol, bgt.nbrRow - 1, 0);
      }
      // numbers
      screen.setColor(Application.foreground);
      screen.drawString(infoLines[0], width + vertWidth, off_y + pieceHeight + 1 + piece_offy, Graphics.TOP | Graphics.RIGHT);
      cadjust = off_y + cadjust + (bgt.nbrRow * sizey) - 1;
      screen.drawString(infoLines[1], width + vertWidth, cadjust, Graphics.BOTTOM | Graphics.RIGHT);
      // active player screen.
      // FIX if height problem as we could put the image in this square
      if (turnImage == null) {
        screen.drawRect(width + vertWidth - sizex, off_y + BoardGameScreen.getActPlayer() * ((bgt.nbrRow - 1) * sizey), sizex, sizey);
      }
      cadjust -= (2 * fontHeight) - 2;
      // skill
      // Put at middle of height.
      if (infoLines[BoardGameApp.gsNbrPlayers[BoardGameApp.PD_CURR]] != null) {
        screen.drawString(infoLines[BoardGameApp.gsNbrPlayers[BoardGameApp.PD_CURR]], width + vertWidth, cadjust, Graphics.BASELINE | Graphics.RIGHT);
      }
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  public void nextTurn(final int row, final int col) {
    if (mtt != null) {
      mtt.cancel();
      while (mtt.ended == false) {
        synchronized (this) {
          try {
            wait(50);
          }
          catch (final Exception e) {
            //
            Debug.ignore(e);
          }
        }
      }
    }
    if (gameEnded) { return; }
    final OwareMove move = new OwareMove(row, col);
    if (!processMove(move, BoardGameApp.precalculate)) { return; }
    updatePossibleMoves();
    // If the game is over, or still human player, return.  If we get another
    // turn, the player will be human.
    if (gameEnded || isHuman[BoardGameScreen.actPlayer]) { return; }
    if (BoardGameApp.precalculate) {
      mtt = new MinimaxTimerTask();
    }
    final OwareMove computerMove = (OwareMove)computerTurn((OwareGame)BoardGameScreen.rgame, move);
    if (computerMove == null) { return; }
    selx = computerMove.col;
    sely = computerMove.row;
    processMove(computerMove, BoardGameApp.precalculate);
    updatePossibleMoves();
    gMiniMax.clearPrecalculatedMoves();
  }

  /**
   * Process the move. Change the player to the other player
   *
   * @param move
   * @param startForeThinking
   */
  protected boolean processMove(final BoardGameMove move, final boolean startForeThinking) {
    try {
      final OwareTable newTable = (OwareTable)table.getEmptyTable();
      /* Simulate the results of taking the move and put results in newTable. */
      tables = BoardGameScreen.rgame.animatedTurn(BoardGameScreen.table, BoardGameScreen.actPlayer, move, newTable);
      boolean goodMove = (tables != null);
      if (!goodMove) {
        setMessage(Application.messages[AppConstants.MSG_INVALIDMOVE], 60);
        return false;
      }
      else {
        if (startForeThinking) {
          mtt.setStartGame(gMiniMax, BoardGameScreen.rgame, tables[tables.length - 1], getActSkill(), getActPlayer());
          timer.schedule(mtt, 0);
        }
        synchronized (this) {
          for (int i = 0; i < tables.length; ++i) {
            BoardGameScreen.table = (OwareTable)tables[i];
            if (i < tables.length - 1) {
              try {
                wait(300);
              }
              catch (final InterruptedException e) {
                // do something
              }
            }
          }
        }
        boolean nonPass = false;
        /* Make current table the simulated move. */
        synchronized (this) {
          BoardGameScreen.table = newTable;
        }
        BoardGameScreen.rgame.saveLastTable(BoardGameScreen.table, BoardGameScreen.actPlayer, BoardGameScreen.turnNum);
        while (!nonPass && !gameEnded) {
          /* Process the move. */
          BoardGameScreen.rgame.process(newTable, BoardGameScreen.actPlayer);
          if (BoardGameScreen.rgame.isGameEnded(BoardGameScreen.rgame, newTable, (byte)(1 - BoardGameScreen.actPlayer))) {
            gameEnded = true;
            procEndGame((byte)(1 - BoardGameScreen.actPlayer));
          }
          else {
            BoardGameScreen.turnNum++;
            if (BoardGameScreen.table.getRepeatNum() > 0) {
              BoardGameScreen.table.setRepeatNum(BoardGameScreen.table.getRepeatNum() - 1);
              setMessage(Application.messages[AppConstants.MSG_EXTRAMOVE], 60);
              return true;
            }
            /* Change to other player. */
            BoardGameScreen.actPlayer = (byte)(1 - BoardGameScreen.actPlayer);
            if (!BoardGameScreen.rgame.hasPossibleMove(BoardGameScreen.table, BoardGameScreen.actPlayer)) {
              String message;
              if (isHuman[BoardGameScreen.actPlayer]) {
                if (BoardGameScreen.twoplayer) {
                  message = OwareMIDlet.playerNames[BoardGameScreen.actPlayer];
                }
                else {
                  message = Application.messages[AppConstants.MSG_HUMAN];
                }
              }
              else {
                message = Application.messages[AppConstants.MSG_COMPUTER];
              }
              setMessage(message + AppConstants.MSG_PASS, 30);
              BoardGameScreen.table.setPassNum(BoardGameScreen.table.getPassNum() + 1);
              // just to be sure
              gMiniMax.clearPrecalculatedMoves();
              break;
            }
            else {
              nonPass = true;
            }
          }
        }
      }
      return true;
    }
    catch (Throwable e) {
      Debug.ignore(e);
      return false;
    }
    finally {
      Thread.yield();
    }
  }

  public void procEndGame(byte player) {
    try {
      byte secondPlayer = (byte)(1 - BoardGameApp.gsFirst);
      if (player != secondPlayer) {
        BoardGameScreen.rgame.procEndGame(player);
      }
      BoardGameScreen.rgame.procEndGame(secondPlayer);
      OwareTable ot = (OwareTable)BoardGameScreen.table;
      super.procEndGame(ot.getPoint((byte)0), ot.getPoint((byte)1),
          secondPlayer);
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

}
