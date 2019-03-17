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
 * This was modified no later than 2009-01-29
 */
package net.eiroca.j2me.external.oware.game;

/**
 * Oware game move
 */
public final class OwareMove extends BoardGameMove {

  public static final int OWARE_MOVE_STORE_SIZE = 3;
  protected int point;

  public OwareMove(final int row, final int col) {
    super(row, col);
  }

  public OwareMove(final int row, final int col, final int point) {
    super(row, col);
    this.point = point;
  }

  public OwareMove(final OwareMove move) {
    super(move.row, move.col);
    point = move.point;
  }

  @Override
  public BoardGameMove getBoardGameMove(final BoardGameMove move) {
    if (!(move instanceof OwareMove)) { return null; }
    return new OwareMove((OwareMove)move);
  }

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof OwareMove)) { return false; }
    final OwareMove r = (OwareMove)o;
    return (row == r.row) && (col == r.col);
  }

  @Override
  public int hashCode() {
    return row + col + getPoint();
  }

  /**
   * Get the value of point.
   * @return Value of point.
   */
  @Override
  public int getPoint() {
    return point;
  }

  /**
   * Set the value of point.
   * @param v Value to assign to point.
   */
  @Override
  public void setPoint(final int v) {
    point = v;
  }

  @Override
  public String toString() {
    return new StringBuffer(32).append("OwareMove(").append(row).append(", ").append(col).append(")").toString();
  }

}
