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

import net.eiroca.j2me.debug.Debug;

/**
 * Oware game move
 */
abstract public class BoardGameTableSquare {

  final public static int BOARD_GAME_TABLE_SQ_SIZE = 2;

  public int x;
  public int y;

  public int hashCode() {
    return x + y;
  }

  public BoardGameTableSquare(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  public BoardGameTableSquare(final BoardGameTableSquare move) {
    this.x = move.x;
    this.y = move.y;
  }

  public BoardGameTableSquare(final byte[] byteArray, final int offset) {
    int coffset = offset;
    this.x = byteArray[coffset++];
    this.y = byteArray[coffset++];
  }

  public void toByteArray(final byte[] byteArray, final int offset) {
    try {
      int coffset = offset;
      this.x = byteArray[coffset++];
      this.y = byteArray[coffset++];
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  public void setCoordinates(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  public String toString() {
    return new StringBuffer(32).append("BoardGameTableSquare(").append(x).append(", ").append(y).append(")").toString();
  }

}
