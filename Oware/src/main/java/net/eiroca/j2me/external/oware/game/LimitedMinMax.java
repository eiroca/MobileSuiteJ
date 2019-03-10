/**
 * LGPL >= 2.1
 * 
 * Copyright (C) 2002-2004 Salamon Andras
 * 
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 *
 * This software was modified 2008-12-14. The original file was GameMinMax.java in
 * mobilesuite.sourceforge.net project.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
/**
 * This was modified no later than 2009-01-29
 */
package net.eiroca.j2me.external.oware.game;

import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.debug.Debug;
import net.eiroca.j2me.game.tpg.GameMinMax;
import net.eiroca.j2me.game.tpg.GameMove;
import net.eiroca.j2me.game.tpg.GameTable;
import net.eiroca.j2me.game.tpg.TwoPlayerGame;


/**
 * Min max algorithm with basic computation.
 */
public class LimitedMinMax extends GameMinMax {

  Thread cthread = Thread.currentThread();


  public GameMove minimax(final int depth, final GameTable state, final byte player, final TwoPlayerGame tpg, final boolean alphabeta, final int alpha, final boolean order, final boolean kill, final GameMove killerMove) {
    try {
      if (cancelled) {
        cancelled = false;
        return null;
      }
      GameMove bestMove;
      if (depth == 0) {
        /* Should not happen. */
        return null;
      }
      GameMove actMove;
      GameMove kMove;
      boolean cut = false;
      int actPoint;
      int maxPoint = -GameMinMax.MAX_POINT; /* -Integer.MIN_VALUE ?? */
      int bestNum = 0;
      final GameMove pMoves[] = tpg.possibleMoves(state, player);
      if ((pMoves == null) || (pMoves.length == 0)) {
        // game ended or player cannot move.
        return null;
      }
      final int[] bestMoves = new int[pMoves.length];
      final GameTable newState = state.copyFrom();
      if ((depth > 2) && order && (pMoves.length > 1)) {
        /* For each potential move, get the points for that move after
        	 the turn is taken. */
        final int points[] = new int[pMoves.length];
        for (int oindex = 0; oindex < pMoves.length; ++oindex) {
          tpg.turn(state, player, pMoves[oindex], newState);
          points[oindex] = ((OwareGame)tpg).getTblPoint(newState, player);
        }
        int oindex3 = 0;
        for (int oindex1 = 0; oindex1 < pMoves.length - 1; ++oindex1) {
          // Get the index of the move with max points
          for (int oindex2 = oindex1; oindex2 < pMoves.length; ++oindex2) {
            if ((oindex2 == oindex1) || (points[oindex2] > points[oindex3])) {
              oindex3 = oindex2;
            }
          }
          if (oindex3 != oindex1) {
            final GameMove swapMove = pMoves[oindex3];
            pMoves[oindex3] = pMoves[oindex1];
            pMoves[oindex1] = swapMove;
          }
        }
      }
      if (kill && (killerMove != null) && (pMoves.length > 1)) {
        int kindex = 0;
        while ((kindex < pMoves.length) && !pMoves[kindex].equals(killerMove)) {
          ++kindex;
        }
        if ((kindex < pMoves.length) && (kindex != 0)) {
          // we have a killermove, but it's not the first one
          final GameMove swapMove = pMoves[0];
          pMoves[0] = pMoves[kindex];
          pMoves[kindex] = swapMove;
        }
      }
      actMove = null;
      for (int i = 0; !cut && (i < pMoves.length); ++i) {
        tpg.turn(state, player, pMoves[i], newState);
        if (depth <= 1) {
          actPoint = ((OwareGame)tpg).getTblPoint(newState, player);
        }
        else {
          if (kill && (i != 0)) {
            kMove = actMove;
          }
          else {
            kMove = null;
          }
          cthread.yield();
          synchronized (this) {
            try {
              wait(1L);
            }
            catch (InterruptedException e) {
            }
          }
          actMove = minimax(depth - 1, newState, (byte)(1 - player), tpg, alphabeta, -maxPoint, order, kill, kMove);
          if (actMove == null) { return null; }
          actPoint = -actMove.getPoint();
        }
        if ((i == 0) || (actPoint > maxPoint)) {
          // better move
          maxPoint = actPoint;
          if (alphabeta && (alpha < maxPoint)) {
            cut = true;
          }
          bestNum = 1;
          bestMoves[0] = i;
        }
        else if (actPoint == maxPoint) {
          // same as the better
          bestMoves[bestNum++] = i;
        }
      }
      int bestIndex;
      if (bestNum > 1) {
        bestIndex = bestMoves[BaseApp.rand(bestNum)];
      }
      else {
        bestIndex = bestMoves[0];
      }
      bestMove = pMoves[bestIndex];
      bestMove.setPoint(maxPoint);
      return bestMove;
    }
    catch (Throwable e) {
      Debug.ignore(e);
      return null;
    }
  }

}
