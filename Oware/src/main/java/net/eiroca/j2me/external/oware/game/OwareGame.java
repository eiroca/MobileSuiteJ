/**
 * GPL >= 2.0
 * 
 * FIX use get factory for table and move.
 * 
 * Put in more factory calls
 * 
 * Based upon jtReversi game written by Jataka Ltd.
 *
 * This software was modified 2008-12-07. The original file was ReversiGame.java in
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
/**
 * This was modified no later than 2009-01-29
 */
package net.eiroca.j2me.external.oware.game;

import java.util.Vector;
import net.eiroca.j2me.debug.Debug;
import net.eiroca.j2me.external.oware.midlet.OwareMIDlet;
import net.eiroca.j2me.game.tpg.GameMove;
import net.eiroca.j2me.game.tpg.GameTable;
import net.eiroca.j2me.game.tpg.TwoPlayerGame;

/**
 * Oware two player game.
 */
public final class OwareGame extends BoardGame {

  public final static int NBR_MAX_STACK = 6;
  protected static int maxHouses = OwareTable.NBR_COL;
  public static final int GRAND_SLAM_ILLEGAL = 0;
  public static final int GRAND_SLAM_LEGAL_NO_CAPTURE = 1;
  public static final int GRAND_SLAM_LEGAL_OPPONENT = 2;
  public static final int GRAND_SLAM_LEGAL_LAST = 3;
  public static final int GRAND_SLAM_LEGAL_FIRST = 4;
  public static final int GRAND_SLAM_LEGAL_24 = 5;
  public static final int GRAND_SLAM_LEGAL_MAX = 24;
  public static final int MULTI_LAP_ONE = 0;
  public static final int MULTI_LAP_1ST = 1;
  public static final int MULTI_LAP_2ND = 2;
  public static final int START_NEXT = 0;
  public static final int START_FIRST = 1;
  public static final int SKIP_STARTING = 0;
  public static final int SOW_STARTING = 1;
  public static final int SKIP_STORE = 0;
  public static final int SOW_STORE = 1;
  public static final int CAPTURE_EMPTY = 0;
  public static final int CAPTURE_23 = 1;
  public static final int CAPTURE_4 = 2;

  /**
   * Use the table to determine if a move is possible, then create the simulation of the move and
   * put it into newTable (table is not modified) and return an array. If animated, return
   *
   * @param table
   * @param player
   * @param move
   * @param newTable
   * @param animated
   * @return GameTable[]
   * @author Irv Bunton
   */
  private static GameTable[] _turn(final OwareTable table, final byte player, final OwareMove move, final OwareTable newTable, final boolean animated, boolean captureAll, boolean noCapture, int captureIx) {
    GameTable tables[] = null;
    try {
      final int row = move.row;
      final int col = move.col;
      /* If the cup is not for the current player, we are not allowed to
      	 do anything with it.  Also, if there are no seeds, you cannot
      	 take the turn. */
      if ((row != table.nbrRow) &&
          ((table.getItem(row, col) != OwareTable.getPlayerItem(player)) ||
              (table.getSeeds(row, col) == 0))) { return null; }
      Vector vTables = null;
      if (animated) {
        vTables = new Vector();
      }
      newTable.copyDataFrom(table);
      if (row == table.nbrRow) {
        // pass.  Leave table as is
        newTable.setPassNum(newTable.getPassNum() + 1);
        tables = new GameTable[1];
        tables[0] = newTable;
        return tables;
      }
      newTable.setPassNum(0);
      boolean changed = false;
      int seeds = newTable.getSeeds(row, col);
      boolean capture4 = (OwareMIDlet.gsCapture[BoardGameApp.PD_CURR] == OwareGame.CAPTURE_4);
      newTable.setSeeds(row, col, (byte)0);
      move.setPoint((byte)seeds);
      newTable.setLastMove(player, move);
      int ccol = col;
      int crow = row;
      boolean first = true;
      boolean lastWasStore = false;
      int lastRow = -1;
      int lastCol = -1;
      int lastSeeds = -1;
      int lastBoardCol = table.nbrCol - 1;
      boolean sowStore = (OwareMIDlet.gsSowStore[BoardGameApp.PD_CURR] == OwareGame.SOW_STORE);
      boolean oneLap = (OwareMIDlet.gsMultiLap[BoardGameApp.PD_CURR] == OwareGame.MULTI_LAP_ONE);
      boolean skipStarting = (OwareMIDlet.gsSkipStarting[BoardGameApp.PD_CURR] == OwareGame.SKIP_STARTING);
      boolean sowFirst = (OwareMIDlet.gsStartFirst[BoardGameApp.PD_CURR] == OwareGame.START_FIRST);
      while (seeds > 0) {
        if (!first) {
          crow = 1 - crow;
          ccol = (crow == 1) ? 0 : lastBoardCol;
        }
        int dir = (crow == 0) ? -1 : 1;
        int nseeds = -1;
        for (; (ccol >= 0) && (ccol < table.nbrCol); ccol += dir) {
          boolean startingPos = ((crow == row) && (ccol == col));
          boolean procSquare;
          if (first) {
            first = false;
            procSquare = sowFirst;
          }
          else {
            if (skipStarting) {
              procSquare = !startingPos;
            }
            else {
              procSquare = true;
            }
          }

          if (procSquare) {
            nseeds = newTable.getSeeds(crow, ccol) + 1;
            if (capture4 && (nseeds == 4) && (seeds > 1)) {
              newTable.incrPoint((byte)crow, (byte)nseeds);
              newTable.setSeeds(crow, ccol, (byte)0);
              nseeds = 0;
            }
            else {
              newTable.setSeeds(crow, ccol, (byte)nseeds);
            }
            changed = true;
            // Sow to the store if there are enough seeds left.
            if (sowStore && (row == crow) && (seeds > 1) &&
                (((ccol == 0) && (dir == -1)) ||
                    ((ccol == lastBoardCol) && (dir == 1)))) {
              newTable.incrPoint((byte)crow, (byte)1);
              seeds--;
              // Set last row/col to -1 since we last sowed in store.
              lastRow = -1;
              lastCol = -1;
              lastWasStore = true;
            }
            else {
              lastRow = crow;
              lastCol = ccol;
              lastWasStore = false;
            }
            if (--seeds <= 0) {
              if (lastWasStore) {
                table.repeatNum += 1;
              }
              else {
                switch (OwareMIDlet.gsMultiLap[BoardGameApp.PD_CURR]) {
                  // Only 1 lap.
                  default:
                  case OwareGame.MULTI_LAP_ONE:
                    lastSeeds = nseeds;
                    break;
                  case OwareGame.MULTI_LAP_1ST:
                    if (nseeds == 1) {
                      lastSeeds = 1;
                      break;
                    }
                    else if (capture4 && (nseeds == 4)) {
                      seeds = 0;
                      lastSeeds = nseeds;
                      break;
                    }
                    else {
                      seeds = nseeds;
                      newTable.setSeeds(crow, ccol, (byte)0);
                    }
                    break;
                }
              }
              if (seeds <= 0) {
                break;
              }
            }
          }
        }
      }
      /* Score points/capture. */
      int holesCaptured = 0;
      int lastCaptureCol = lastCol;
      if (lastWasStore || (lastCol == -1)) {
        lastCaptureCol = -1;
      }
      else {
        lastCaptureCol = lastCol;
        if (captureAll || (captureIx >= 0)) {
          if (lastRow == row) {
            if ((lastSeeds == 1) &&
                (OwareMIDlet.gsCapture[BoardGameApp.PD_CURR] == OwareGame.CAPTURE_EMPTY)) {
              // Get points on opposite side of the board from the opponent if
              // we end up in an empty cup on our side.
              byte opponent = (byte)(1 - lastRow);
              int oseeds = (byte)newTable.getSeeds(opponent, lastCol);
              if (oseeds > 0) {
                newTable.incrPoint((byte)row, (byte)(oseeds + 1));
                newTable.setSeeds(opponent, lastCol, (byte)0);
                holesCaptured = 1;
                lastRow = opponent;
              }
            }
            else if ((lastSeeds == 4) && capture4) {
              newTable.incrPoint((byte)row, (byte)lastSeeds);
              newTable.setSeeds((byte)lastRow, lastCol, (byte)0);
              holesCaptured = 1;
            }
          }
          else if (lastRow != row) {
            boolean capture2or3 = (OwareMIDlet.gsCapture[BoardGameApp.PD_CURR] == OwareGame.CAPTURE_23);
            if (capture2or3) {
              // Move in the opposite direction this time
              crow = lastRow;
              ccol = lastCol;
              int dir = (crow == 0) ? 1 : -1;
              for (; (ccol >= 0) && (ccol < table.nbrCol); ccol += dir) {
                final int cseeds = newTable.getSeeds(crow, ccol);
                if ((cseeds == 2) || (cseeds == 3)) {
                  if ((captureIx >= 0) && (captureIx == ccol)) {
                    continue;
                  }
                  newTable.incrPoint(player, newTable.getSeeds(crow, ccol));
                  newTable.setSeeds(crow, ccol, (byte)0);
                  lastCaptureCol = ccol;
                  if (++holesCaptured > maxHouses) {
                    break;
                  }
                }
                else {
                  break;
                }
              }
            }
          }
        }
      }

      boolean emptyHoles = allEmptyHoles(newTable, (1 - player));
      // If last was a store, no grand slam.
      if (captureAll && changed && emptyHoles && !lastWasStore &&
          (holesCaptured > 0)) {
        switch (OwareMIDlet.gsGrandSlam[BoardGameApp.PD_CURR]) {
          case GRAND_SLAM_ILLEGAL:
          default:
            return null;
          case GRAND_SLAM_LEGAL_NO_CAPTURE:
            return _turn(table, player, move, newTable, animated, false, true, -1);
          case GRAND_SLAM_LEGAL_OPPONENT:
            // Give seeds to the opponent
            giveToPlayer(player, (byte)(1 - player), newTable);
            break;
          case GRAND_SLAM_LEGAL_LAST:
            return _turn(table, player, move, newTable, animated, false, false, lastCaptureCol);
          case GRAND_SLAM_LEGAL_FIRST:
            return _turn(table, player, move, newTable, animated, false, false, lastCol);
          case GRAND_SLAM_LEGAL_24:
            if (newTable.getPoint(player) <= GRAND_SLAM_LEGAL_MAX) { return null; }
        }
      }
      else if (emptyHoles) { return null; }
      if (animated) {
        vTables.addElement((OwareTable)newTable.getBoardGameTable(newTable));
      }
      if (changed) {
        if (animated) {
          tables = new GameTable[vTables.size()];
          try {
            vTables.copyInto(tables);
          }
          catch (Throwable e) {
            for (int i = 0; i < vTables.size(); ++i) {
              tables[i] = (GameTable)vTables.elementAt(i);
            }
          }
        }
        else {
          tables = new GameTable[1];
          tables[0] = newTable;
        }
        return tables;
      }
      return null;
    }
    catch (Throwable e) {
      Debug.ignore(e);
      return null;
    }
  }

  static public void giveToPlayer(final byte player, final byte opPlayer, OwareTable table) {
    for (int crow = 0; crow < table.nbrRow; crow++) {
      for (int ccol = 0; ccol < table.nbrCol; ccol++) {
        int ix = table.getItem(crow, ccol) - 1;
        if (ix == player) {
          table.incrPoint((byte)(1 - opPlayer), (byte)(table.getPoint((byte)(1 - opPlayer))));
          table.setSeeds(crow, ccol, (byte)0);
        }
      }
    }
  }

  public static boolean allEmptyHoles(OwareTable table, int player) {
    int crow = player;
    int ccol = 0;
    for (; (ccol >= 0) && (ccol < table.nbrCol); ccol++) {
      if (table.getSeeds(crow, ccol) > 0) { return false; }
    }
    return true;
  }

  /**
   * Use the table to determine if a move is possible, then create the simulation of the move and
   * put it into newTable (table is not modified) and return an array. If animated, return
   *
   * @param table
   * @param player
   * @param move
   * @param newTable
   * @param animated
   * @param animated
   * @return GameTable[]
   * @author Irv Bunton
   */
  private static GameTable[] _turn(final OwareTable table, final byte player, final OwareMove move, final OwareTable newTable, final boolean animated) {
    return _turn(table, player, move, newTable, animated, true, false, -1);
  }

  /*
   * Take the turn for the given move for the given player.
   *
   * @Override
   * @see net.eiroca.j2me.minmax.TwoPlayerGame#animatedTurn(net.eiroca.j2me.minmax.Table,
   *      byte, net.eiroca.j2me.minmax.Move, net.eiroca.j2me.minmax.Table)
   */
  public GameTable[] animatedTurn(final GameTable table, final byte player, final GameMove move, final GameTable newt) {
    try {
      return _turn((OwareTable)table, player, (OwareMove)move, (OwareTable)newt, true, true, false, -1);
    }
    catch (Throwable e) {
      Debug.ignore(e);
      return null;
    }
  }

  /**
   * Evaluate the table either with actual points or estimated goodness with heuristic (actually
   * currently there is no heuristic.)
   *
   * @param lazyProcess - If lazyProcess is true, it gets actual points else it get goodness of
   *        board using heuristic.
   * @param bg - Board game
   * @param t - Board table
   * @param player - Current player
   * @param endGame - End of game
   */
  public void eval(boolean lazyProcess, BoardGame bg, GameTable t, final byte player, boolean endGame) {
    try {
      OwareTable ctable = (OwareTable)t;
      int pointFirstPlayer = ctable.getPoint((byte)0);
      int pointSecondPlayer = ctable.getPoint((byte)1);
      if (endGame) {
        for (int i = 0; i < ctable.nbrRow; ++i) {
          for (int j = 0; j < ctable.nbrCol; ++j) {
            int ix = ctable.getItem(i, j) - 1;
            int cseeds = ctable.getSeeds(i, j);
            if (cseeds > 0) {
              if (endGame) {
                ctable.setPoint((byte)ix, (byte)(ctable.getPoint((byte)ix) + cseeds));
                ctable.setSeeds(i, j, (byte)0);
              }
            }
            if (ix == 0) {
              pointFirstPlayer += cseeds;
            }
            else {
              pointSecondPlayer += cseeds;
            }
          }
        }
      }
      point = pointFirstPlayer - pointSecondPlayer;
      if (player == 1) {
        point = -point;
      }
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  /**
   * Evaluate the table either with actual points or estimated goodness with heuristic (actually
   * currently there is no heuristic.)
   *
   * @param fullProcess - If true, use estimate of goodness with heuristic.
   * @param endGame - End of game
   * @param player - Current player
   */
  protected void eval(boolean fullProcess, boolean endGame, byte player) {
    boolean lazyProcess = !fullProcess || isGameEnded();
    eval(lazyProcess, this, rTable, player, endGame);
  }

  @Override
  public int getGameResult(byte player) {
    eval(true, true, player);
    int score = point;
    if (score > 0) {
      return TwoPlayerGame.WIN;
    }
    else if (score < 0) {
      return TwoPlayerGame.LOSS;
    }
    else {
      return TwoPlayerGame.DRAW;
    }
  }

  @Override
  public int getGameResult() {
    return getGameResult(rPlayer);
  }

  public int getTblPoint(final GameTable table, final byte player) {
    if (!(table instanceof OwareTable)) { return 0; }
    return (((OwareTable)rTable).getPoint(player) -
        ((OwareTable)rTable).getPoint((byte)(1 - player)));
  }

  public boolean hasPossibleMove(final GameTable table, final byte player) {
    try {
      final OwareMove[] moves = (OwareMove[])possibleMoves(table, player);
      return (moves != null) && ((moves.length > 1) || (moves[0].row != ((BoardGameTable)table).nbrRow));
    }
    catch (Throwable e) {
      Debug.ignore(e);
      return false;
    }
  }

  public boolean isGameEnded(BoardGame bg, BoardGameTable t, byte player) {
    if (!(bg instanceof OwareGame)) { return false; }
    if (!(t instanceof OwareTable)) { return false; }
    OwareTable ot = (OwareTable)t;
    if ((ot.getPoint((byte)0) >= OwareTable.WINNING_SCORE) || (ot.getPoint((byte)1) >= OwareTable.WINNING_SCORE) || (t.getPassNum() == 2) || (allEmptyHoles(ot, player) && allEmptyHoles(ot, (1 - player)))) { return true; }
    return false;
  }

  public boolean isGameEnded() {
    return isGameEnded(this, rTable, rPlayer);
  }

  /**
   * Get possible moves by simulating the move for the given row/col and if it has a result (is
   * allowed), return the moves. Except 2 passes is end of game
   *
   * @param table
   * @param player
   * @return GameMove[]
   * @author Irv Bunton
   */
  public GameMove[] possibleMoves(final GameTable table, final byte player) {
    try {
      if (!(table instanceof OwareTable)) { return null; }
      final Vector moves = new Vector();
      if (((OwareTable)table).getPassNum() == 2) {
        // two passes: end of the game
        return null;
      }
      // This has no values, but they are put in when _turn does a copy
      // to this table.
      OwareTable newTable = (OwareTable)((OwareTable)table).getEmptyTable();

      // Possible moves can only be in the current row.
      OwareMove move = null;
      boolean hasMove = false;
      int col = 0;
      for (int row = player; col < ((BoardGameTable)table).nbrCol; ++col) {
        if (move == null) {
          move = new OwareMove(row, col);
        }
        else {
          move.setCoordinates(row, col);
        }
        // FIX
        if (!hasMove && (((OwareTable)table).getSeeds(row, col) > 0)) {
          hasMove = true;
        }
        OwareTable oldTable = new OwareTable(newTable);
        final boolean goodMove = turn(table, player, move, newTable);
        if (goodMove) {
          moves.addElement(new OwareMove(move));
        }
        newTable = oldTable;
      }
      if (!hasMove) {
        if (OwareMIDlet.gsOpponentEmpty) {
          // Give seeds to the current player (who is the opponent of the
          // previous player).
          giveToPlayer((byte)(1 - player), player, (OwareTable)table);
        }
        else {
          // Give seeds to the current player.
          giveToPlayer(player, (byte)(1 - player), (OwareTable)table);
        }
        return null;
      }
      // Oware does not have pass.  May need later for other Mancala.
      final GameMove[] retMoves = new OwareMove[moves.size()];
      try {
        moves.copyInto(retMoves);
      }
      catch (Throwable e) {
        for (int m = 0; m < moves.size(); ++m) {
          retMoves[m] = (GameMove)moves.elementAt(m);
        }
      }
      return retMoves;
    }
    catch (Throwable e) {
      Debug.ignore(e);
      return null;
    }
  }

  /**
   * Set the table to the parameter and evauate points.
   *
   * @param table - Board table
   * @param player - current player
   * @param fullProcess - If true, use estimate of goodness with heuristic. (actually currently
   *        there is no heuristic.)
   */
  protected void setTable(final GameTable table, final byte player, final boolean fullProcess) {
    if (!(table instanceof OwareTable)) { throw new IllegalArgumentException(); }
    rTable = (OwareTable)table;
    rPlayer = player;
    ++evalNum;
    eval(fullProcess, false, player);
  }

  public void procEndGame(byte player) {
    try {
      eval(true, true, player);
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  public static boolean turn(final OwareTable table, final byte player, final OwareMove move, final OwareTable newt) {
    return OwareGame._turn(table, player, move, newt, false, true, false, -1) != null;
  }

  public boolean turn(final GameTable table, final byte player, final GameMove move, final GameTable newt) {
    return OwareGame._turn((OwareTable)table, player, (OwareMove)move, (OwareTable)newt, false, true, false, -1) != null;
  }

  public void setMaxHouses(int maxHouses) {
    OwareGame.maxHouses = maxHouses;
  }

  public int getMaxHouses() {
    return (OwareGame.maxHouses);
  }

}
