/**
 * GPL >= 2.0
 *
 * Based upon jtReversi game written by Jataka Ltd.
 *
 * This software was modified 2008-12-07. The original file was ReversiMove.java in
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
 * This was modified no later than 2009-01-29. It was based on GameMove
 */
package net.eiroca.j2me.external.oware.game;

import net.eiroca.j2me.game.tpg.GameMove;

/**
 * Oware game move
 */
abstract public class BoardGameMove implements GameMove {

  public int col;
  public int row;

  public static boolean valid(final BoardGameTable bgt, final int row, final int col) {
    return (row >= 0) && (row < bgt.nbrRow) && (col >= 0) && (col < bgt.nbrCol);
  }

  public static boolean valid(final BoardGameTable bgt, final BoardGameMove bgm) {
    return BoardGameMove.valid(bgt, bgm.row, bgm.col);
  }

  abstract public BoardGameMove getBoardGameMove(final BoardGameMove move);

  @Override
  abstract public boolean equals(final Object o);

  @Override
  public int hashCode() {
    return row + col;
  }

  public BoardGameMove(final int row, final int col) {
    this.row = row;
    this.col = col;
  }

  public BoardGameMove(final BoardGameMove move) {
    row = move.row;
    col = move.col;
  }

  public void setCoordinates(final int row, final int col) {
    this.row = row;
    this.col = col;
  }

  @Override
  public String toString() {
    return new StringBuffer(32).append("BoardGameMove(").append(row).append(", ").append(col).append(")").toString();
  }

}
