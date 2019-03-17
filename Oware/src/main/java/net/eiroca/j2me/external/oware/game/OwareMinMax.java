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

import net.eiroca.j2me.debug.Debug;
import net.eiroca.j2me.game.tpg.GameMinMax;
import net.eiroca.j2me.game.tpg.GameMove;
import net.eiroca.j2me.game.tpg.GameTable;
import net.eiroca.j2me.game.tpg.TwoPlayerGame;

/**
 * Oware wizard min/max algorithm.
 */
public final class OwareMinMax extends GameMinMax {

  final public static int MAXDEPTH = 16;
  final public static int ENDGAMEDEPTH = 20;
  final public static int MAX = 1;
  final public static int MIN = 2;
  final public static int HEURISTIC_DFLT = 0;
  /* Array of heuristics.  Each elmement is a different skill level. */
  static private OwareHeuristic[] heuristics = new OwareHeuristic[256];
  static private int gheuristic = 0;
  Thread cthread = Thread.currentThread();

  public OwareMinMax(final int heuristic) {
    OwareMinMax.heuristics[0] = new DefaultHeuristic();
    OwareMinMax.heuristics[1] = new DefenseHeuristic();
    OwareMinMax.gheuristic = heuristic;
  }

  void alphabetaSetHeuristic(final int id, final OwareHeuristic h) {
    OwareMinMax.heuristics[id] = h;
  }

  private OwareMove alphabetaPly(final int depth, final OwareTable table, final byte player, final OwareGame g, final int heuristic, int bestmax, int bestmin) {
    OwareMove bestmove = null;
    try {
      if (cancelled) { return null; }
      final OwareMove pMoves[] = (OwareMove[])g.possibleMoves(table, player);
      if ((pMoves == null) || (pMoves.length == 0)) { return null; }
      int result = bestmin;
      for (int i = 0; (i < pMoves.length); ++i) {
        final OwareTable testTable = new OwareTable(table);
        if (cancelled) { return null; }
        if (!OwareGame.turn(table, player, pMoves[i], testTable)) {
          continue; /*An illegal move*/
        }
        if (bestmove == null) {
          bestmove = new OwareMove(pMoves[i]);
          bestmove.setPoint(0);
        }
        if ((depth <= 0) || g.isGameEnded(g, testTable, player)) {
          final int tmpResult = OwareMinMax.heuristics[heuristic].getResult(player, testTable);
          if (tmpResult > (byte)result) {
            bestmove = new OwareMove(pMoves[i]);
            bestmove.setPoint(tmpResult);
            result = tmpResult;
          }
        }
        else {
          Thread.yield();
          synchronized (this) {
            try {
              wait(1L);
            }
            catch (final InterruptedException e) {
            }
          }
          final OwareMove tmpmove = alphabetaPly(depth - 1, testTable, (byte)(1 - player), g, heuristic, bestmax, bestmin);
          if (tmpmove == null) {
            continue;
          }
          if (-tmpmove.getPoint() > result) {
            bestmove = new OwareMove(pMoves[i]);
            bestmove.setPoint(-tmpmove.getPoint());
            result = bestmove.getPoint();
          }
        }
        // FIX?
        if (player == 0) {
          if (result >= bestmin) {
            bestmove.setCoordinates(pMoves[i].row, pMoves[i].col);
            bestmove.setPoint(bestmin);
            return bestmove;
          }
          if (result > bestmax) {
            bestmove.setCoordinates(pMoves[i].row, pMoves[i].col);
            bestmax = result;
            bestmove.setPoint(bestmax);
          }
        }
        else {
          if (result <= bestmax) {
            bestmove.setCoordinates(pMoves[i].row, pMoves[i].col);
            bestmove.setPoint(bestmax);
            return bestmove;
          }
          if (result < bestmin) {
            bestmove.setCoordinates(pMoves[i].row, pMoves[i].col);
            bestmin = result;
          }
        }
      }
      if (bestmove == null) {
        return null;
      }
      else if (player == 0) {
        bestmove.setPoint(bestmax);
        return bestmove;
      }
      else {
        bestmove.setPoint(bestmin);
        return bestmove;
      }
    }
    catch (final Throwable e) {
      Debug.ignore(e);
      return null;
    }
  }

  @Override
  public GameMove minimax(final int depth, final GameTable state, final byte player, final TwoPlayerGame tpg, final boolean alphabeta, final int alpha, final boolean order, final boolean kill, final GameMove killerMove) {
    OwareMove bestmove = null;
    try {
      final OwareTable testTable = new OwareTable((OwareTable)state);
      if (depth == 0) { return null; }
      bestmove = alphabetaPly(depth - 1, testTable, player, (OwareGame)tpg, OwareMinMax.gheuristic, -GameMinMax.MAX_POINT, GameMinMax.MAX_POINT);
      if (cancelled) {
        cancelled = false;
        return null;
      }
      if (bestmove == null) { return null; }
      if (bestmove.getPoint() == -GameMinMax.MAX_POINT) {
        return null;
      }
      else {
        return bestmove;
      }
    }
    catch (final Throwable e) {
      Debug.ignore(e);
      return null;
    }
  }

}
