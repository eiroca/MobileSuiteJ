/**
 * GPL >= 2.0
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
 * This software was modified 2008-12-09. The original file was alphabet.c in
 * http://oware.ivorycity.com/. Also, some code was taken from GameMinMax.java in
 * mobilesuite.sourceforge.net project.
 */
package net.eiroca.j2me.external.oware.game;

public class DefenseHeuristic extends OwareHeuristic {

  public int getResult(byte player, OwareTable table) {
    if (player == 0) {
      return table.getPoint((byte)0) - table.getPoint((byte)1);
    }
    else {
      return (3 * table.getPoint((byte)1)) - table.getPoint((byte)0);
    }
  }
}
