/** GPL >= 2.0
 * Based upon jtReversi game written by Jataka Ltd.
 *
 * This software was modified 2008-12-07.  The original file was MinimaxTimerTask.java
 * in mobilesuite.sourceforge.net project.
 *
 * Copyright (C) 2002-2004 Salamon Andras
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
/**
 * This was modified no later than 2009-01-29
 */
package net.eiroca.j2me.external.oware.game.ui;

import java.util.TimerTask;
import net.eiroca.j2me.external.oware.game.BoardGame;
import net.eiroca.j2me.game.tpg.GameMinMax;
import net.eiroca.j2me.game.tpg.GameTable;

/**
	* TimerTask to run min/max algorithms.
	*/
public class MinimaxTimerTask extends TimerTask {

  public boolean ended;
  protected GameMinMax gminMax;
  protected int startActSkill;
  protected byte player;
  protected GameTable startTable;
  protected BoardGame startBoardGame;

  public MinimaxTimerTask() {
    //
  }

  public boolean cancel() {
    gminMax.cancel(true);
    return true;
  }

  public void run() {
    ended = false;
    gminMax.foreMinimax(startActSkill, startTable, player, startBoardGame, true, 0, true, true);
    System.gc();
    ended = true;
  }

  public void setStartGame(GameMinMax gminMax, BoardGame bg, GameTable startTable,
			int startActSkill, byte player) {
		this.gminMax = gminMax;
		this.startBoardGame = bg;
    this.startTable = startTable;
    this.startActSkill = startActSkill;
    this.player = player;
  }

}
