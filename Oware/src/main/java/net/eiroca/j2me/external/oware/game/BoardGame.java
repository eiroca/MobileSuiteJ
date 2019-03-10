/**
 * GPL >= 2.0
 * 
 * FIX use get factory for table and move. Put in more factory calls
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
 * This was modified no later than 2009-01-29. Based on TwoPlayerGame
 */
package net.eiroca.j2me.external.oware.game;

import java.util.Stack;
import net.eiroca.j2me.debug.Debug;
import net.eiroca.j2me.game.tpg.GameMove;
import net.eiroca.j2me.game.tpg.GameTable;
import net.eiroca.j2me.game.tpg.TwoPlayerGame;

/**
 * Two player game board game.
 */
abstract public class BoardGame extends TwoPlayerGame {

  public final static int NBR_MAX_STACK = 6;
  protected int evalNum = 0;
  protected byte rPlayer;
  protected BoardGameTable rTable;
  protected int redoTop = 0;
  protected Stack prevTbls = new Stack();
  protected int point;

  abstract public int getGameResult(final byte player);

  public BoardGame() {
  }

  public BoardGame(final BoardGame bg) {
    this.evalNum = bg.evalNum;
    this.rPlayer = bg.rPlayer;
    this.rTable = bg.rTable;
    this.redoTop = bg.redoTop;
    this.prevTbls = new Stack();
    final int len = bg.prevTbls.size();
    for (int i = 0; i < len; i++) {
      this.prevTbls.addElement(bg.prevTbls.elementAt(i));
    }
    this.point = bg.point;
  }

  public int getPoint() {
    return point;
  }

  public boolean hasPossibleMove(final GameTable table, final byte player) {
    if (!(table instanceof BoardGameTable)) { return false; }
    try {
      final BoardGameMove[] moves = (BoardGameMove[])possibleMoves(table, player);
      return (moves != null) && ((moves.length > 1) || (moves[0].row != ((BoardGameTable)table).nbrRow));
    }
    catch (Throwable e) {
      Debug.ignore(e);
      return false;
    }
  }

  abstract public boolean isGameEnded(BoardGame bg, BoardGameTable t, byte player);

  abstract public boolean isGameEnded();

  /**
   * Calculates the point (goodness) of the table.
   * @param t Table (position) to be checked.
   * @param player Player.
   * @return Goodness of the position.
   */
  public int point(final BoardGame bg, final GameTable t, final byte player) {
    eval(true, bg, t, player, false);
    return getPoint();
  }

  abstract public void eval(boolean lazyProcess, BoardGame bg, GameTable t, final byte player, boolean endGame);

  /**
   * Get possible moves by simulating the move for the given row/col and if it has a result (is
   * allowed), return the moves. Except 2 passes is end of game
   *
   * @param table
   * @param player
   * @return GameMove[]
   * @author Irv Bunton
   */
  abstract public GameMove[] possibleMoves(final GameTable table, final byte player);

  public void resetEvalNum() {
    evalNum = 0;
  }

  public int getEvalNum() {
    return evalNum;
  }

  abstract public void procEndGame(byte player);

  protected GameTable getTable() {
    return rTable;
  }

  /**
   * This gets the current rPlayer, but this is NOT the same as the actual current player because
   * after this is set, the player may change.
   *
   * @return byte
   * @author Irv Bunton
   */
  protected byte getPlayer() {
    return rPlayer;
  }

  public void resetTables() {
    prevTbls.removeAllElements();
  }

  public void saveLastTable(BoardGameTable bgt, byte player, int turnNum) {
    if (prevTbls.size() >= NBR_MAX_STACK) {
      prevTbls.removeElementAt(0);
    }
    int psize = prevTbls.size();
    while ((redoTop < psize) && (psize > 0)) {
      prevTbls.removeElementAt(psize-- - 1);
    }
    prevTbls.push(new RedoInfo(bgt.getBoardGameTable(bgt), player, turnNum));
    redoTop = prevTbls.size();
  }

  private BoardGameTable undoTable(BoardGameTable bgt, byte player, int ix, boolean removeEntry) {
    synchronized (this) {
      int undoTop = redoTop - 2 - ix;
      if (undoTop < 0) { return null; }
      if (player != ((RedoInfo)prevTbls.elementAt(redoTop - 1 - ix)).player) { return null; }
      RedoInfo ri = (RedoInfo)prevTbls.elementAt(undoTop);
      // If remove entry, do not update.
      if (!removeEntry) { return bgt.getBoardGameTable(ri.tbl); }
      redoTop--;
      setTable(ri.tbl.copyFrom(), player, false);
      BoardGameScreen.turnNum = ri.turnNum;
      return bgt;
    }
  }

  public BoardGameTable undoTable(byte player) {
    return undoTable(rTable, player, 0, true);
  }

  private BoardGameTable redoTable(BoardGameTable bgt, byte player, int ix, boolean removeEntry) {
    int newRedo = redoTop + 1 - ix;
    if (newRedo > prevTbls.size()) { return null; }
    if (newRedo < 0) { return null; }
    RedoInfo ri = (RedoInfo)prevTbls.elementAt(redoTop - ix);
    if (ri.player != player) { return null; }
    if (!removeEntry) { return bgt.getBoardGameTable(ri.tbl); }
    redoTop++;
    bgt.copyDataFrom(ri.tbl);
    setTable(bgt, player, false);
    BoardGameScreen.turnNum = ri.turnNum;
    return (BoardGameTable)bgt;
  }

  public BoardGameTable redoTable(byte player) {
    return redoTable(rTable, player, 0, true);
  }

  public boolean checkLast(byte player, byte ix) {
    return undoTable(rTable, player, ix, false) != null;
  }

  public boolean checkLastRedo(byte player, byte ix) {
    return redoTable(rTable, player, ix, false) != null;
  }

  private class RedoInfo {

    BoardGameTable tbl;
    byte player;
    int turnNum;

    RedoInfo(BoardGameTable tbl, byte player, int turnNum) {
      this.tbl = tbl;
      this.player = player;
      this.turnNum = turnNum;
    }
  }

}
