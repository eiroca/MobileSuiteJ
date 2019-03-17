/**
 * GPL >= 2.0
 *
 * FIX length of store Bao (Bawo Malawi) (Omweso Uganda) Single lap Oware Multiple-lap last Bao and
 * Ayoayo Multiple-lap next Pallanguzhi, Kisolo, Pallum Kuzhi FIX empty move Based upon jtReversi
 * game written by Jataka Ltd.
 *
 * This software was modified 2008-12-07. The original file was ReversiTable.java in
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
package net.eiroca.j2me.external.oware.game;

import net.eiroca.j2me.debug.Debug;
import net.eiroca.j2me.game.tpg.GameMove;
import net.eiroca.j2me.game.tpg.GameTable;

/**
 * Oware board table.
 */
public final class OwareTable extends BoardGameTable {

  /**
   * Two bits for every place: 00: nothing 01: 1 10: 2 11: oops
   */
  // Many of this is partially implemented
  public final static int INIT_SEEDS = 4;
  public final static int NBR_ROW = 2;
  public final static int NBR_COL = 6;
  public final static int NBR_ATTRIBUTES = 2;
  private final static int MAX_TABLE_STORE_SIZE = (OwareTable.NBR_ATTRIBUTES * OwareTable.NBR_COL * OwareTable.NBR_ROW) + (BoardGameTable.NBR_PLAYERS * 1) + (BoardGameTable.NBR_PLAYERS * BoardGameTable.BOARD_TABLE_STORE_SIZE) + 1 + 200;
  private final static int TABLE_STORE_SIZE = (OwareTable.NBR_ATTRIBUTES * OwareTable.NBR_COL * OwareTable.NBR_ROW) + (BoardGameTable.NBR_PLAYERS * 1) + (BoardGameTable.NBR_PLAYERS * BoardGameTable.BOARD_TABLE_STORE_SIZE) + 1 + 200; //UNDO
  public final static int WINNING_SCORE = 25;
  protected int initSeeds = OwareTable.INIT_SEEDS;
  protected byte[][] board;
  protected byte[] point;
  protected byte[] reserve;

  public OwareTable(final int nbrRow, final int nbrCol, final int nbrPlayers, final int initSeeds) {
    super(nbrRow, nbrCol, nbrPlayers);
    this.initSeeds = initSeeds;
    board = new byte[OwareTable.NBR_ATTRIBUTES][nbrCol * nbrRow];
    point = new byte[nbrPlayers];
    reserve = new byte[nbrPlayers];
    for (int i = 0; i < nbrPlayers; i++) {
      setPoint((byte)i, (byte)0);
      setReserve((byte)i, (byte)0);
    }
    final int middle = nbrRow / 2;
    for (int i = 0; i < nbrRow; ++i) {
      for (int j = 0; j < nbrCol; j++) {
        setItem(i, j, (byte)((i < middle) ? 1 : 2));
        setSeeds(i, j, (byte)initSeeds);
      }
    }
  }

  public OwareTable(final byte[] byteArray, final int offset) {
    super(byteArray, offset);
    board = new byte[OwareTable.NBR_ATTRIBUTES][nbrCol * nbrRow];
    point = new byte[nbrPlayers];
    reserve = new byte[nbrPlayers];
    try {
      int coffset = offset + BoardGameTable.BOARD_TABLE_STORE_SIZE;
      for (int i = 0; i < nbrPlayers; i++) {
        if (byteArray[coffset] == 255) {
          lastMove[i] = null;
          coffset += 3;
        }
        else {
          lastMove[i] = new OwareMove(byteArray[coffset++], byteArray[coffset++]);
          lastMove[i].setPoint(byteArray[coffset++]);
        }
      }
      initSeeds = byteArray[coffset++];
      for (int i = 0; i < nbrPlayers; i++) {
        point[i] = byteArray[coffset++];
      }
      for (int i = 0; i < nbrPlayers; i++) {
        reserve[i] = byteArray[coffset++];
      }
      for (int i = 0; i < OwareTable.NBR_ATTRIBUTES; i++) {
        System.arraycopy(byteArray, coffset, board[i], 0, nbrCol * nbrRow);
        coffset += (nbrCol * nbrRow);
      }
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
  }

  public OwareTable(final OwareTable table) {
    super(table);
    board = new byte[OwareTable.NBR_ATTRIBUTES][table.nbrCol * table.nbrRow];
    point = new byte[nbrPlayers];
    reserve = new byte[nbrPlayers];
    initSeeds = table.initSeeds;
    try {
      for (int i = 0; i < OwareTable.NBR_ATTRIBUTES; i++) {
        System.arraycopy(table.board[i], 0, board[i], 0, board[i].length);
      }
      for (int i = 0; i < nbrPlayers; i++) {
        point[i] = table.point[i];
      }
      for (int i = 0; i < nbrPlayers; i++) {
        reserve[i] = table.reserve[i];
      }
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
  }

  @Override
  public BoardGameTable getEmptyTable() {
    return new OwareTable(nbrRow, nbrCol, nbrPlayers, initSeeds);
  }

  @Override
  public BoardGameMove getBoardGameMove(final int row, final int col) {
    return new OwareMove(row, col);
  }

  @Override
  public BoardGameTable getBoardGameTable(final BoardGameTable table) {
    if (!(table instanceof OwareTable)) { return null; }
    return new OwareTable((OwareTable)table);
  }

  @Override
  public BoardGameTable getBoardGameTable(final byte[] byteArray, final int offset) {
    return new OwareTable(byteArray, offset);
  }

  @Override
  public void convertToIntArray(final int[][] array) {
    for (int i = 0; i < nbrRow; ++i) {
      for (int j = 0; j < nbrCol; ++j) {
        array[i][j] = getItem(i, j);
      }
    }
  }

  @Override
  public void copyDataFrom(final GameTable table) {
    try {
      final OwareTable rtable = (OwareTable)table;
      super.copyDataFrom(rtable);
      initSeeds = rtable.initSeeds;
      for (int i = 0; i < OwareTable.NBR_ATTRIBUTES; i++) {
        System.arraycopy(rtable.board[i], 0, board[i], 0, rtable.nbrCol * rtable.nbrRow);
      }
      for (int i = 0; i < nbrPlayers; i++) {
        point[i] = rtable.getPoint((byte)i);
      }
      for (int i = 0; i < nbrPlayers; i++) {
        reserve[i] = rtable.reserve[i];
      }
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
  }

  @Override
  public GameTable copyFrom() {
    final OwareTable rtable = new OwareTable(this);
    return rtable;
  }

  @Override
  public GameMove getEmptyMove() {
    return new OwareMove(0, 0);
  }

  /**
   * Get the player for the coordinates
   * @param row
   * @param col
   * @param value
   * @author Irv Bunton
   */
  @Override
  public byte getItem(final int row, final int col) {
    try {
      return board[0][(row * nbrCol) + col];
    }
    catch (final Throwable e) {
      Debug.ignore(e);
      return 0;
    }
  }

  /**
   * Get the number of seeds for the coordinates
   * @param row
   * @param col
   * @param value
   * @author Irv Bunton
   */
  public byte getSeeds(final int row, final int col) {
    try {
      return board[1][(row * nbrCol) + col];
    }
    catch (final Throwable e) {
      Debug.ignore(e);
      return 0;
    }
  }

  /**
   * Get the number of points for the player
   * @param player
   * @author Irv Bunton
   */
  public byte getPoint(final byte player) {
    try {
      return point[player];
    }
    catch (final Throwable e) {
      Debug.ignore(e);
      return 0;
    }
  }

  /**
   * Get the number of points for the player
   * @param player
   * @author Irv Bunton
   */
  public byte getReserve(final byte player) {
    try {
      return reserve[player];
    }
    catch (final Throwable e) {
      Debug.ignore(e);
      return 0;
    }
  }

  /**
   * Set the player for the coordinates
   * @param row
   * @param col
   * @param value
   * @author Irv Bunton
   */
  @Override
  public void setItem(final int row, final int col, final byte value) {
    try {
      board[0][(row * nbrCol) + col] = value;
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
  }

  /**
   * Set the number of seeds for the coordinates
   * @param row
   * @param col
   * @param value
   * @author Irv Bunton
   */
  public void setSeeds(final int row, final int col, final byte value) {
    try {
      board[1][(row * nbrCol) + col] = value;
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
  }

  /**
   * Set the number of point for the player
   * @param player
   * @author Irv Bunton
   */
  public void setPoint(final byte player, final byte score) {
    try {
      point[player] = score;
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
  }

  /**
   * Increment the number of point for the player
   * @param player
   * @author Irv Bunton
   */
  public void incrPoint(final byte player, final byte score) {
    try {
      point[player] += score;
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
  }

  /**
   * Set the number of reserve for the player
   * @param player
   * @author Irv Bunton
   */
  public void setReserve(final byte player, final byte score) {
    try {
      reserve[player] = score;
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
  }

  public int tableMaxStoreSize() {
    return OwareTable.TABLE_STORE_SIZE;
  }

  @Override
  public int tableStoreSize() {
    return BoardGameTable.tableStoreSize(nbrPlayers) + 1 + (2 * nbrPlayers) + (OwareTable.NBR_ATTRIBUTES * nbrRow * nbrCol);
  }

  @Override
  public byte[] toByteArray() {
    final byte[] byteArray = new byte[tableStoreSize()];
    toByteArray(byteArray, 0);
    return byteArray;
  }

  @Override
  public void toByteArray(final byte[] byteArray, final int offset) {
    super.toByteArray(byteArray, offset);
    int coffset = offset + BoardGameTable.BOARD_TABLE_STORE_SIZE;
    try {
      for (int i = 0; i < nbrPlayers; i++) {
        if (lastMove[i] != null) {
          byteArray[coffset++] = (byte)lastMove[i].row;
          byteArray[coffset++] = (byte)lastMove[i].col;
          byteArray[coffset++] = (byte)lastMove[i].getPoint();
        }
        else {
          byteArray[coffset++] = (byte)255;
          byteArray[coffset++] = (byte)255;
          byteArray[coffset++] = (byte)255;
        }
      }
      byteArray[coffset++] = (byte)initSeeds;
      for (int i = 0; i < nbrPlayers; i++) {
        byteArray[coffset++] = point[i];
      }
      for (int i = 0; i < nbrPlayers; i++) {
        byteArray[coffset++] = reserve[i];
      }
      for (int i = 0; i < OwareTable.NBR_ATTRIBUTES; i++) {
        System.arraycopy(board[i], 0, byteArray, coffset, board[i].length);
        coffset += board[0].length;
      }
    }
    catch (final ArrayIndexOutOfBoundsException e) {
      Debug.ignore(e);
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
  }

  /**
   * Should use StringBuffer instead of String, but this method is only for debug purposes.
   */
  @Override
  public String toString() {
    final StringBuffer ret = new StringBuffer(80);
    for (int i = 0; i < nbrRow; ++i) {
      ret.append(toRowString(i));
      ret.append('\n');
    }
    ret.append("pass: ").append(getPassNum()).append('\n');
    return ret.toString();
  }

  /**
   * Convert string to seeds
   */
  @Override
  public void fromRowString(final int i, final String nums)
      throws IllegalArgumentException {
    final byte[] bnums = nums.getBytes();
    final int len = bnums.length;
    int j = 0;
    try {
      for (; (j < len) && (j < nbrCol); ++j) {
        final byte seeds = (j < len) ? (byte)(bnums[j] - '0') : (byte)0;
        if (seeds > 9) {
          final Exception e = new IllegalArgumentException("Seed # invalid " + j);
          Debug.ignore(e);
          throw e;
        }
        setSeeds(i, j, seeds);
      }
      if (len >= (nbrCol + 1)) {
        final String spoint = new String(bnums, nbrCol, len - nbrCol);
        final byte point = (byte)((Integer.valueOf(spoint)).intValue());
        setPoint((byte)i, point);
      }
      else {
        setPoint((byte)i, (byte)0);
      }
    }
    catch (final IllegalArgumentException e) {
      throw e;
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
  }

  public boolean equals(final OwareTable table) {
    if ((nbrCol != table.nbrCol) || (nbrRow != table.nbrRow)) { return false; }
    boolean isEquals = true;
    try {
      final int len = nbrCol * nbrRow;
      for (int i = 0; i < OwareTable.NBR_ATTRIBUTES; i++) {
        for (int j = 0; j < len; j++) {
          if (table.board[i][j] != board[i][j]) {
            isEquals = false;
          }
        }
      }
      if (passNum != table.passNum) {
        isEquals = false;
      }
      if (initSeeds != table.initSeeds) {
        isEquals = false;
      }
      for (int i = 0; i < nbrPlayers; i++) {
        if (point[i] != table.point[i]) {
          isEquals = false;
        }
      }
      for (int i = 0; i < nbrPlayers; i++) {
        if (reserve[i] != table.reserve[i]) {
          isEquals = false;
        }
      }
      for (int i = 0; i < nbrPlayers; i++) {
        if (lastMove[i].row != table.lastMove[i].row) {
          isEquals = false;
        }
        if (lastMove[i].col != table.lastMove[i].col) {
          isEquals = false;
        }
      }
    }
    catch (final Throwable e) {
      Debug.ignore(e);
    }
    return isEquals;
  }

  /**
   * Convert items to string
   */
  @Override
  public String toRowItemString(final int i) {
    final StringBuffer ret = new StringBuffer(80);
    for (int j = 0; j < nbrCol; ++j) {
      ret.append(getItem(i, j));
    }
    return ret.toString();
  }

  /**
   * Convert items to string
   */
  @Override
  public String toItemString() {
    final StringBuffer ret = new StringBuffer(80);
    for (int i = 0; i < nbrRow; ++i) {
      ret.append(toRowItemString(i));
      ret.append('\n');
    }
    ret.append("pass: ").append(getPassNum()).append('\n');
    return ret.toString();
  }

  /**
   * Convert seeds to string
   */
  @Override
  public String toRowString(final int i) {
    final StringBuffer ret = new StringBuffer(80);
    for (int j = 0; j < nbrCol; ++j) {
      ret.append(getSeeds(i, j));
    }
    ret.append(getPoint((byte)i));
    return ret.toString();
  }

}
