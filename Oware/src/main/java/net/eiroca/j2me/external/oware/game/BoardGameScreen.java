/**
 * GPL >= 2.0 Based upon jtReversi game written by Jataka Ltd.
 *
 * This software was modified 2008-12-07. The original file was ReversiScreen.java in
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

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.util.Timer;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordStore;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.debug.Debug;
import net.eiroca.j2me.external.oware.game.ui.MinimaxTimerTask;
import net.eiroca.j2me.external.oware.midlet.AppConstants;
import net.eiroca.j2me.external.presentation.FeatureMgr;
import net.eiroca.j2me.game.GameApp;
import net.eiroca.j2me.game.GameScreen;
import net.eiroca.j2me.game.tpg.GameMinMax;
import net.eiroca.j2me.game.tpg.GameTable;
import net.eiroca.j2me.game.tpg.TwoPlayerGame;

/**
 * Board game screen (game canvas).
 */
abstract public class BoardGameScreen extends GameScreen implements Runnable {

  protected static final byte STORE_VERS = 4;
  protected static final String IGNORE_KEYCODES = "boardgame-ignore-keycodes";
  protected static final byte SCREEN_STORE_BYTES = 6;
  protected static final char NL = '\n';
  protected static final int WIDTH_SEPARATERS = 2;
  protected static final int HEIGHT_SEPARATERS = 2;
  protected static final int COLOR_TEXT_BG = 0xEEEEEE;
  protected static final int COLOR_TEXT_FG = 0x000000;
  protected static final int COLOR_BG = 0xFFFFD0;
  protected static final int COLOR_FG = 0x000000;
  protected static final int COLOR_P1 = 0xFF0000;
  protected static final int COLOR_P2 = 0x0000FF;
  protected static final String SEP = ": ";
  protected static final int COLOR_DARKBOX = 0x000000;
  protected static final int ASPECT_LIMIT_A = 400; // 1.5
  protected static final int ASPECT_LIMIT_B = 300; // 1.5
  protected String infoLines[];
  protected String message = null;
  protected Image squareImage = null;
  protected int squareWidth = 0;
  protected Image piece1Image = null;
  protected Image piece2Image = null;
  protected Image turnImage;
  protected BoardGameMove[] possibleMoves;
  final protected FeatureMgr featureMgr;
  protected int sizex;
  protected int sizey;
  protected int vertWidth;
  protected int width;
  protected int height;
  public int selx;
  public int sely;
  public static final int INVALID_KEY_CODE = -2000;
  public int keyCode = INVALID_KEY_CODE;
  public int[] pointerPress = new int[] {
      -1, -1
  };
  final protected String ignoreKeycodes;

  public long messageEnd;

  protected MinimaxTimerTask mtt;
  protected final Timer timer = new Timer();

  public static byte actPlayer;
  public boolean gameEnded = true;
  public boolean[] isHuman = new boolean[2];
  public GameTable[] tables;
  public static int turnNum;
  public static BoardGameTable table;
  public GameMinMax gMiniMax;
  public static BoardGame rgame;
  public static boolean twoplayer;

  protected int fontHeight;
  protected int off_y;
  protected int off_x;
  protected int pieceWidth;
  protected int pieceHeight;
  protected int cupWidth = 0;
  protected int cupHeight = 0;
  protected int cupImagexOffset = 0;
  protected int piece_offx;
  protected int piece_offy;
  public byte[] bsavedRec = new byte[0];

  public BoardGameScreen(final GameApp midlet, final boolean suppressKeys, final boolean fullScreen, int appName) {
    /* Do not suppress keys.  However, do full screen. */
    super(midlet, false, true, 20);
    try {
      name = Application.messages[appName];
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
    finally {
      featureMgr = new FeatureMgr(this);
      featureMgr.setRunnable(this, true);
      ignoreKeycodes = "," + BaseApp.midlet.readAppProperty(IGNORE_KEYCODES, "").trim() + ",";
    }
  }

  /**
   * Initialize graphics portion only. This is for MIDP 1.0 devices which do not get screen info
   * until paint. So, we allow it to be called from 2 places.
   */
  public void initGraphics() {
    super.initGraphics();
    // FIX turnImage = BaseApp.createImage(TURN_ICON);
    /* Leave space to the right of the board for putting other info. */
    int bcol = BoardGameApp.gsCol[BoardGameApp.PD_CURR];
    int brow = BoardGameApp.gsRow[BoardGameApp.PD_CURR];
    width = screenWidth * bcol / (bcol + 1);
    vertWidth = screenWidth - width;
    height = screenHeight;
    sizex = (width - 1) / bcol;
    sizey = (height - 1) / brow;
    final int origSizex = sizex;
    final int origSizey = sizey;
    if (BoardGameScreen.ASPECT_LIMIT_B * sizex > BoardGameScreen.ASPECT_LIMIT_A * sizey) {
      sizex = sizey * BoardGameScreen.ASPECT_LIMIT_A / BoardGameScreen.ASPECT_LIMIT_B;
    }
    if (BoardGameScreen.ASPECT_LIMIT_B * sizey > BoardGameScreen.ASPECT_LIMIT_A * sizex) {
      sizey = sizex * BoardGameScreen.ASPECT_LIMIT_A / BoardGameScreen.ASPECT_LIMIT_B;
    }
    fontHeight = screen.getFont().getHeight();
    height = sizey * brow;
    width = sizex * bcol;
    if (BoardGameApp.gsSquareImages.length > 0) {
      sizex = Math.min(sizex, sizey);
      sizey = sizex;
      squareImage = getImageFit(BoardGameApp.gsSquareImages, sizex);
      if (squareImage != null) {
        if (squareImage.getWidth() < sizex) {
          sizex = squareImage.getWidth();
          sizey = sizex;
        }
        piece_offy = 0;
        piece_offx = 0;
        squareWidth = sizex * table.nbrRow;
        piece1Image = getImageFit(BoardGameApp.gsPiece1Images, sizex);
        piece2Image = getImageFit(BoardGameApp.gsPiece2Images, sizex);
        pieceWidth = piece1Image.getWidth();
        pieceHeight = piece1Image.getHeight();
      }
    }
    if ((BoardGameApp.gsSquareImages.length == 0) || (squareImage == null)) {
      if (BoardGameApp.gsTextRow > 0) {
        pieceHeight = sizey - fontHeight;
      }
      else {
        pieceHeight = sizey - 2 * HEIGHT_SEPARATERS;
      }
      pieceWidth = sizex - 2 * WIDTH_SEPARATERS;
      // See if # text rows of text height plus the pieceWidth is < sizey
      if ((BoardGameApp.gsTextRow > 0) && (sizey < ((BoardGameApp.gsTextRow * fontHeight + HEIGHT_SEPARATERS) + pieceWidth + piece_offy + 1))) {
        int newSizey = (BoardGameApp.gsTextRow * (fontHeight + HEIGHT_SEPARATERS)) + pieceHeight + 1;
        if (newSizey < origSizey) {
          sizey = newSizey;
        }
      }
      piece_offx = (sizex - pieceWidth) / 2;
      piece_offy = (sizex - pieceHeight) / 2;
      cupWidth = pieceWidth;
      cupHeight = pieceHeight;
      piece1Image = getImageFit(BoardGameApp.gsPiece1Images, cupWidth);
      piece2Image = getImageFit(BoardGameApp.gsPiece2Images, cupWidth);
      if ((piece1Image != null) && (piece2Image != null) && (piece1Image.getWidth() == piece1Image.getHeight()) && (piece1Image.getWidth() == piece2Image.getHeight()) && (piece2Image.getWidth() == piece2Image.getHeight())) {
        if (cupWidth > cupHeight) {
          cupWidth = cupHeight;
        }
        else {
          cupHeight = cupWidth;
        }
      }
      turnImage = piece1Image;
      if (piece1Image != null) {
        int imageWidth = piece1Image.getWidth();
        if (imageWidth <= cupWidth) {
          cupImagexOffset = (cupWidth - imageWidth) / 2;
        }
        else {
          piece1Image = null;
          cupImagexOffset = 0;
        }
      }
    }
    height = sizey * brow;
    selx = 0;
    sely = 0;
    off_y = (screenHeight - height) / 2;
    off_x = 2;
  }

  private Image getImageFit(String[] imageNames, int width) {
    for (int ix = imageNames.length - 1; ix >= 0; ix--) {
      final Image cimage = BaseApp.createImage(imageNames[ix]);
      if ((cimage != null) && (cimage.getWidth() < width)) { return cimage; }
    }
    return null;
  }

  /**
   * This does init. Must create new table before calling this from screen.
   */
  public void init() {
    super.init();
    try {
      infoLines = new String[BoardGameApp.gsNbrPlayers[BoardGameApp.PD_CURR] + 1];
      Application.background = 0x00FFFFFF;
      Application.foreground = 0x00000000;
      score.beginGame(1, 0, 0);
      // FIX allow to set
      int rtn = 0;
      if (bsavedRec.length > 0) {
        rtn = loadRecordStore(bsavedRec);
        bsavedRec = new byte[0];
      }
      if (rtn == 0) {
        BoardGameScreen.turnNum = 1;
        gameEnded = false;
      }
      BoardGameScreen.actPlayer = (byte)BoardGameApp.gsFirst;
      if (BoardGameApp.gsPlayer == 1) {
        isHuman[BoardGameScreen.actPlayer] = true;
        isHuman[1 - BoardGameScreen.actPlayer] = false;
        BoardGameScreen.twoplayer = false;
      }
      else {
        isHuman[0] = true;
        isHuman[1] = true;
        BoardGameScreen.twoplayer = true;
      }
      BoardGameScreen.rgame.process(BoardGameScreen.table, BoardGameScreen.actPlayer);
      BoardGameScreen.rgame.resetEvalNum();
      BoardGameScreen.rgame.resetTables();
      BoardGameScreen.rgame.saveLastTable(BoardGameScreen.table, (byte)(1 - BoardGameScreen.actPlayer), BoardGameScreen.turnNum);
      updateSkillInfo();
      updatePossibleMoves();
      setMessage(Application.messages[AppConstants.MSG_GOODLUCK]);
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  public boolean tick() {
    try {
      screen.setColor(Application.background);
      screen.fillRect(0, 0, screenWidth, screenHeight);
      drawBoard();
      BoardGameTable bgt = null;
      synchronized (this) {
        bgt = BoardGameScreen.table;
      }
      if (bgt != null) {
        drawTable(bgt);
        drawSelectionBox(selx, sely, 0);
        drawPossibleMoves();
        drawVertInfo(bgt);
      }
      drawMessage();
      return true;
    }
    catch (Throwable e) {
      Debug.ignore(e);
      return true;
    }
  }

  /**
   * Executed when hidden. Stop animation thread, turn off full screen.
   */
  public void hide() {
    try {
      storeRecordStore();
      super.hide();
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  /**
   * Executed when hidden. Stop animation thread, turn off full screen.
   */
  public void storeRecordStore() {
    try {
      byte[] gameRec = saveRecordStore();
      if (gameRec != null) {
        RecordStore gstore = BaseApp.getRecordStore(BoardGameApp.storeName, true, true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(gameRec, 0, gameRec.length);
        BaseApp.writeData(gstore, bos);
        BaseApp.closeRecordStores();
      }
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  protected void drawMessage() {
    // Avoid synchronization errors.
    String cmessage = null;
    long cmessageEnd;
    synchronized (this) {
      cmessage = message;
      cmessageEnd = messageEnd;
    }
    if ((cmessage == null) || ((cmessageEnd != 0) && (cmessageEnd < System.currentTimeMillis()))) { return; }
    int startIndex;
    int endIndex = -1;
    final int breaks = BaseApp.lineBreaks(cmessage);
    final int maxWidth = BaseApp.maxSubWidth(screen.getFont(), cmessage) + 10;
    int cornerX = (width - maxWidth) / 2;
    if (cornerX < 0) {
      cornerX = (screenWidth - maxWidth) / 2;
    }
    else {
      cornerX += off_x;
    }
    int cornerY = off_y + (height - (breaks + 1) * fontHeight - 6) / 2;
    screen.setColor(BoardGameScreen.COLOR_TEXT_BG);
    screen.fillRect(cornerX - 1, cornerY - 1, maxWidth, (breaks + 1) * fontHeight + 6);
    screen.setColor(BoardGameScreen.COLOR_TEXT_FG);
    screen.drawRect(cornerX - 1, cornerY - 1, maxWidth, (breaks + 1) * fontHeight + 6);
    screen.drawRect(cornerX, cornerY, maxWidth - 2, (breaks + 1) * fontHeight + 4);
    while (endIndex < cmessage.length()) {
      startIndex = endIndex + 1;
      endIndex = cmessage.indexOf(NL, startIndex);
      if (endIndex == -1) {
        endIndex = cmessage.length();
      }
      final String submessage = cmessage.substring(startIndex, endIndex);
      screen.drawString(submessage, cornerX + 5, cornerY + 2, Graphics.TOP | Graphics.LEFT);
      cornerY += fontHeight;
    }
  }

  protected void drawBoard() {
    screen.setColor(BoardGameScreen.COLOR_BG);
    if (squareImage != null) {
      screen.fillRect(off_x, off_y, squareWidth, squareWidth);
    }
    else {
      screen.fillRect(off_x, off_y, width, height);
    }
    screen.setColor(BoardGameScreen.COLOR_FG);
    if (squareImage != null) {
      /* Draw horizontal lines of rows. */
      for (int i = 0; i < table.nbrRow; ++i) {
        for (int j = 0; j < table.nbrCol; ++j) {
          screen.drawImage(squareImage, off_x + j * sizex, off_y + i * sizey, Graphics.TOP | Graphics.LEFT);
        }
      }
    }
    else {
      /* Draw horizontal lines of rows. */
      for (int i = 0; i <= table.nbrRow; ++i) {
        screen.drawLine(off_x, off_y + i * sizey, off_x + width, off_y + i * sizey);
      }
      /* Draw vertical lines of cols. */
      for (int i = 0; i <= table.nbrCol; ++i) {
        screen.drawLine(off_x + i * sizex, off_y, off_x + i * sizex, off_y + height);
      }
    }
  }

  /**
   * Draw a piece on the board at the row/col for the player. If onBoard is true, the piece is in
   * the playing area. If false, it is on the right to identify the players.
   *
   * @param row
   * @param col
   * @param player
   * @param onBoard
   */
  abstract protected void drawPiece(final BoardGameTable bgt, final int row, final int col, final int player, boolean onBoard, Image cupImage, int yadjust, int lastMovePoint);

  protected void drawPossibleMoves() {
    try {
      BoardGameMove[] cpossibleMoves = null;
      synchronized (this) {
        cpossibleMoves = possibleMoves;
      }
      if (cpossibleMoves == null) {
        // end of the game
        return;
      }
      int x;
      int y;
      screen.setColor(BoardGameScreen.COLOR_DARKBOX);
      for (int i = 0; i < cpossibleMoves.length; ++i) {
        x = off_x + cpossibleMoves[i].col * sizex + sizex / 2;
        y = off_y + cpossibleMoves[i].row * sizey + sizey / 2;
        screen.fillRect(x, y, 2, 2);
      }
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  protected void drawSelectionBox(int x, int y, int yadjust) {
    if (BoardGameScreen.getActPlayer() == 0) {
      screen.setColor(BoardGameScreen.COLOR_P1);
    }
    else {
      screen.setColor(BoardGameScreen.COLOR_P2);
    }
    screen.drawRect(off_x + x * sizex, off_y + yadjust + y * sizey, sizex,
        sizey);
    screen.drawRect(off_x + x * sizex + 1, off_y + yadjust + y * sizey + 1,
        sizex - 2, sizey - 2);
  }

  abstract protected void drawTable(BoardGameTable bgt);

  public void drawVertInfo(BoardGameTable bgt) {
    try {
      // two pieces
      drawPiece(bgt, 0, bgt.nbrCol, 1, false, ((BoardGameApp.gsFirst == 0) ? piece2Image : piece1Image), 0, 0); /* y, x */
      drawPiece(bgt, bgt.nbrRow - 1, bgt.nbrCol, 0, false, ((BoardGameApp.gsFirst == 1) ? piece2Image : piece1Image), sizey - pieceHeight, 0); /* y, x */
      // numbers
      screen.setColor(Application.foreground);
      screen.drawString(infoLines[0], width + vertWidth, off_y + cupHeight + 1 + piece_offy, Graphics.TOP | Graphics.RIGHT);
      screen.drawString(infoLines[1], width + vertWidth, off_y + ((bgt.nbrRow - 1) * sizey) + cupHeight + 1 + piece_offy, Graphics.BOTTOM | Graphics.RIGHT);
      // active player screen.
      // FIX if height problem as we could put the image in this square
      if (turnImage == null) {
        screen.drawRect(width + vertWidth - sizex, off_y + BoardGameScreen.getActPlayer() * ((bgt.nbrRow - 1) * sizey), sizex, sizey);
      }
      // skill
      // Put at middle of height.
      if (infoLines[2] != null) {
        screen.drawString(infoLines[2], width + vertWidth, screenHeight / 2, Graphics.BASELINE | Graphics.RIGHT);
      }
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  /**
   * This is just a quick procedure to prevent a hang due to something taking too long.
   *
   * @param keyCode
   */
  public void keyPressed(final int keyCode) {
    synchronized (this) {
      if (this.keyCode == INVALID_KEY_CODE) {
        this.keyCode = keyCode;
        featureMgr.wakeup(3);
      }
    }
  }

  /**
   * This is just a quick procedure to prevent a hang due to something taking too long.
   *
   * @param keyCode
   */
  public void pointerPressed(final int x, final int y) {
    synchronized (this) {
      if ((this.pointerPress[0] == -1) && (this.pointerPress[1] == -1)) {
        this.pointerPress[0] = x;
        this.pointerPress[1] = y;
        featureMgr.wakeup(3);
      }
    }
  }

  /**
   * This is just a quick procedure to prevent a hang due to something taking too long.
   *
   * @param keyCode
   */
  public void pointerDragged(final int x, final int y) {
    pointerPressed(x, y);
  }

  public void procKeyPressed(final int keyCode) {
    try {
      if (gameEnded) {
        midlet.doGameStop();
      }
      else {
        try {
          switch (super.getGameAction(keyCode)) {
            case Canvas.UP:
              sely = (sely + table.nbrRow - 1) % table.nbrRow;
              setMessage(null);
              break;
            case Canvas.DOWN:
              sely = (sely + 1) % table.nbrRow;
              setMessage(null);
              break;
            case Canvas.LEFT:
              selx = (selx + table.nbrCol - 1) % table.nbrCol;
              setMessage(null);
              break;
            case Canvas.RIGHT:
              selx = (selx + 1) % table.nbrCol;
              setMessage(null);
              break;
            case Canvas.FIRE:
              String cmessage = null;
              synchronized (this) {
                cmessage = message;
              }
              if (cmessage != null) {
                setMessage(null);
              }
              else {
                nextTurn(sely, selx);
              }
              break;
            default:
              if (!acceptKeyCode(keyCode)) {
                gMiniMax.cancel(true);
                midlet.doGamePause();
              }
              break;
          }
        }
        catch (IllegalArgumentException e) {
          if (!acceptKeyCode(keyCode)) {
            gMiniMax.cancel(true);
            midlet.doGamePause();
          }
        }
      }
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
    finally {
    }
  }

  public boolean acceptKeyCode(int keyCode) {
    boolean rtn = (ignoreKeycodes.indexOf("," + Integer.toString(keyCode) + ",") >= 0);
    return rtn;
  }

  public void procPointerPressed(final int x, final int y) {
    try {
      if (gameEnded) {
        midlet.doGameStop();
      }
      else {
        if ((off_x < x) && (x < ((table.nbrCol * sizex) - off_x)) &&
            (off_y < y) && (y < ((table.nbrRow * sizex) - off_y))) {
          selx = (selx - off_x) / table.nbrCol;
          sely = (sely - off_y) / table.nbrRow;
          setMessage(null);
        }
      }
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  public void run() {
    int ckeyCode = INVALID_KEY_CODE;
    synchronized (this) {
      if (this.keyCode != INVALID_KEY_CODE) {
        ckeyCode = this.keyCode;
      }
    }
    if (ckeyCode != INVALID_KEY_CODE) {
      procKeyPressed(ckeyCode);
      synchronized (this) {
        this.keyCode = INVALID_KEY_CODE;
      }
    }
    int cx = -1;
    int cy = -1;
    synchronized (this) {
      if ((this.pointerPress[0] != -1) && (this.pointerPress[1] != -1)) {
        cx = this.pointerPress[0];
        cy = this.pointerPress[1];
      }
    }
    if ((cx != -1) && (cy != -1)) {
      procPointerPressed(cx, cy);
      synchronized (this) {
        this.pointerPress[0] = -1;
        this.pointerPress[1] = -1;
      }
    }
  }

  public void setMessage(final String message) {
    synchronized (this) {
      this.message = message;
      this.messageEnd = 0;
    }
  }

  public void setMessage(final String message, final int delay) {
    synchronized (this) {
      this.message = message;
      this.messageEnd = System.currentTimeMillis() + delay * 1000;
    }
  }

  public void updatePossibleMoves() {
    BoardGameMove[] cpossibleMoves = (BoardGameMove[])BoardGameScreen.rgame.possibleMoves(BoardGameScreen.table, BoardGameScreen.actPlayer);
    synchronized (this) {
      possibleMoves = cpossibleMoves;
    }
  }

  public void updateSkillInfo() {
    if (!BoardGameScreen.twoplayer) {
      infoLines[BoardGameScreen.table.nbrPlayers] = Application.messages[AppConstants.MSG_LEVELPREFIX + BoardGameApp.gsLevel[BoardGameApp.PD_CURR]] + BoardGameApp.gsDepth[BoardGameApp.PD_CURR];
    }
    else {
      infoLines[BoardGameScreen.table.nbrPlayers] = null;
    }
  }

  protected BoardGameMove computerTurn(final TwoPlayerGame tpg, final BoardGameMove prevMove) {
    BoardGameMove move = (BoardGameMove)gMiniMax.precalculatedBestMove(prevMove);
    if (move == null) {
      setMessage(Application.messages[AppConstants.MSG_THINKING]);
      Thread.currentThread().yield();
      gMiniMax.cancel(false);
      move = (BoardGameMove)gMiniMax.minimax(BoardGameScreen.getActSkill(), BoardGameScreen.table, BoardGameScreen.actPlayer, tpg, true, 0, true, true, null);
    }
    setMessage(null);
    tpg.resetEvalNum();
    return move;
  }

  public static byte getActPlayer() {
    return BoardGameScreen.actPlayer;
  }

  public static int getActSkill() {
    int actSkill = BoardGameApp.gsDepth[BoardGameApp.PD_CURR];
    if (BoardGameScreen.turnNum > 50) {
      actSkill++;
    }
    if (BoardGameScreen.turnNum > 55) {
      actSkill++;
    }
    return actSkill;
  }

  public void nextTurn(final int row, final int col) {
    if (mtt != null) {
      mtt.cancel();
      while (mtt.ended == false) {
        synchronized (this) {
          try {
            wait(50);
          }
          catch (final Exception e) {
            //
            Debug.ignore(e);
          }
        }
      }
    }
    if (gameEnded) { return; }
    final BoardGameMove move = table.getBoardGameMove(row, col);
    if (!processMove(move, false)) { return; }
    updatePossibleMoves();
    while (!gameEnded && !isHuman[BoardGameScreen.actPlayer]) {
      if (BoardGameApp.precalculate) {
        mtt = new MinimaxTimerTask();
      }
      BoardGameMove computerMove = computerTurn(BoardGameScreen.rgame, move);
      if (computerMove == null) {
        computerMove = (BoardGameMove)(BoardGameScreen.table).getEmptyMove();
        computerMove.row = ((BoardGameTable)BoardGameScreen.table).nbrRow;
        computerMove.col = ((BoardGameTable)BoardGameScreen.table).nbrCol;
      }
      else {
        selx = computerMove.col;
        sely = computerMove.row;
      }
      processMove(computerMove, BoardGameApp.precalculate);
      updatePossibleMoves();
      gMiniMax.clearPrecalculatedMoves();
      break;
    }
  }

  /**
   * Process the move. Change the player to the other player
   *
   * @param move
   * @param startForeThinking
   * @author Irv Bunton
   */
  abstract protected boolean processMove(final BoardGameMove move, final boolean startForeThinking);

  abstract public void procEndGame(byte player);

  public void procEndGame() {
    procEndGame(BoardGameScreen.actPlayer);
  }

  public void procEndGame(final int firstNum, final int secondNum, final byte player) {
    try {
      final int result = BoardGameScreen.rgame.getGameResult(player);
      String endMessage;
      final boolean firstWin = ((result == TwoPlayerGame.LOSS) && (player == 1)) || ((result == TwoPlayerGame.WIN) && (player == 0));
      final int winner;
      if (firstWin) {
        winner = (BoardGameApp.gsFirst == 0) ? 1 : 0;
      }
      else {
        winner = (BoardGameApp.gsFirst == 1) ? 1 : 0;
      }
      if (!BoardGameScreen.twoplayer && ((firstWin && (BoardGameApp.gsFirst != 0)) || (!firstWin && (BoardGameApp.gsFirst == 0)))) {
        endMessage = Application.messages[AppConstants.MSG_WONCOMPUTER];
      }
      else if (result == TwoPlayerGame.DRAW) {
        endMessage = Application.messages[AppConstants.MSG_DRAW];
      }
      else {
        if (BoardGameScreen.twoplayer) {
          endMessage = BoardGameApp.playerNames[winner] + Application.messages[AppConstants.MSG_PLAYERWON];
        }
        else {
          endMessage = Application.messages[AppConstants.MSG_HUMANWON];
        }
      }
      endMessage += BoardGameScreen.NL + BoardGameApp.playerNames[0] + BoardGameScreen.SEP + firstNum + BoardGameScreen.NL + BoardGameApp.playerNames[1] + BoardGameScreen.SEP + secondNum;
      setMessage(endMessage);
    }
    catch (Throwable e) {
      Debug.ignore(e);
    }
  }

  public int saveGameParameters(final byte[] b, final int offset) {
    int index = offset;
    // isHuman
    b[index] = 0;
    if (isHuman[0]) {
      b[index] |= 1;
    }
    if (isHuman[1]) {
      b[index] |= 2;
    }
    index++;
    // actPlayer
    b[index++] = BoardGameScreen.actPlayer;
    b[index++] = (byte)BoardGameScreen.table.nbrRow;
    // row
    b[index++] = (byte)BoardGameScreen.table.nbrCol;
    // col
    b[index++] = (byte)BoardGameApp.gsLevel[BoardGameApp.PD_CURR];
    b[index++] = (byte)BoardGameApp.gsDepth[BoardGameApp.PD_CURR];
    return index - offset;
  }

  /**
   * Saves data into byte[]
   */
  public byte[] saveRecordStore()
      throws Exception {
    try {
      if (BoardGameScreen.table == null) { return null; }
      final byte[] result = new byte[SCREEN_STORE_BYTES];
      int offset = 0;
      result[offset++] = STORE_VERS;
      result[offset++] = (byte)selx;
      result[offset++] = (byte)sely;
      result[offset++] = (byte)(gameEnded ? 1 : 0);
      // turnNum
      result[offset++] = (byte)((BoardGameScreen.turnNum & 0xFF00) >> 16);
      result[offset++] = (byte)(BoardGameScreen.turnNum & 0x00FF);
      if (result.length != offset) {
        Exception e = new Exception("saveRecordStore Size of result does not match result.length != offset " + result.length + "!=" + offset);
        Debug.ignore(e);
        throw e;
      }
      byte[] byteArray = BoardGameScreen.table.toByteArray();
      byte[] nresult = new byte[byteArray.length + result.length];
      System.arraycopy(result, 0, nresult, 0, result.length);
      System.arraycopy(byteArray, 0, nresult, result.length, byteArray.length);
      return nresult;
    }
    catch (Throwable e) {
      Debug.ignore(e);
      return new byte[0];
    }
  }

  /**
   * Loads data from byte[]
   */
  public int loadRecordStore(final byte[] b) {
    try {
      int offset = 0;
      int vers = b[offset++];
      if (vers != STORE_VERS) { return 0; }
      selx = b[offset++];
      sely = b[offset++];
      gameEnded = (b[offset++] == 1) ? true : false;
      BoardGameScreen.turnNum = (b[offset++] << 16) + b[offset++];
      if (SCREEN_STORE_BYTES != offset) {
        Exception e = new Exception("Size of result does not match " + SCREEN_STORE_BYTES + "!=" + offset);
        Debug.propagate(e);
        throw e;
      }
      synchronized (this) {
        BoardGameScreen.table = BoardGameScreen.table.getBoardGameTable(b, offset);
      }
      return offset;
    }
    catch (Throwable e) {
      Debug.ignore(e);
      return 0;
    }
  }

  public byte[] getSavedGameRecord() {
    DataInputStream dis = null;
    try {
      RecordStore gstore = BaseApp.getRecordStore(BoardGameApp.storeName, true, true);
      if (gstore == null) { return new byte[0]; }
      dis = BaseApp.readRecord(gstore, 1);
      if (dis == null) { return new byte[0]; }
      byte[] brec = new byte[Application.getRecordSize(gstore, 1)];
      int len = dis.read(brec, 0, brec.length);
      BaseApp.closeRecordStores();
      if (len == 0) { return new byte[0]; }
      if (brec[0] != STORE_VERS) { return new byte[0]; }
      if (len < brec.length) {
        byte[] nbrec = new byte[len];
        System.arraycopy(brec, 0, nbrec, 0, len);
        return nbrec;
      }
      else {
        return brec;
      }
    }
    catch (Throwable e) {
      Debug.ignore(e);
      return null;
    }
  }

  /* Undo last move.  If playing against AI, need to do undo AI move first. */
  public boolean undoTable() {
    synchronized (this) {
      BoardGameScreen.actPlayer = (byte)(1 - BoardGameScreen.actPlayer);
      if (!isHuman[BoardGameScreen.actPlayer]) {
        if (rgame.undoTable(BoardGameScreen.actPlayer) == null) { return false; }
        BoardGameScreen.actPlayer = (byte)(1 - BoardGameScreen.actPlayer);
      }
      if (rgame.undoTable(BoardGameScreen.actPlayer) == null) { return false; }
      synchronized (this) {
        BoardGameScreen.table = (BoardGameTable)BoardGameScreen.rgame.getTable();
      }
      updatePossibleMoves();
      return true;
    }
  }

  /* check last move.  If playing against AI, need to do undo AI move first. */
  public boolean checkLastTable() {
    synchronized (this) {
      byte prevPlayer = (byte)(1 - BoardGameScreen.actPlayer);
      byte ix = 0;
      if (!isHuman[prevPlayer]) {
        if (!rgame.checkLast(prevPlayer, ix)) { return false; }
        prevPlayer = (byte)(1 - prevPlayer);
        ix++;
      }
      if (!rgame.checkLast(prevPlayer, ix)) { return false; }
      return true;
    }
  }

  public boolean redoTable() {
    synchronized (this) {
      if ((rgame.redoTable(BoardGameScreen.actPlayer)) == null) { return false; }
      BoardGameScreen.actPlayer = (byte)(1 - BoardGameScreen.actPlayer);
      if (!isHuman[BoardGameScreen.actPlayer]) {
        if (rgame.redoTable(BoardGameScreen.actPlayer) == null) { return false; }
        BoardGameScreen.actPlayer = (byte)(1 - BoardGameScreen.actPlayer);
      }
      synchronized (this) {
        BoardGameScreen.table = (BoardGameTable)BoardGameScreen.rgame.getTable();
      }
      updatePossibleMoves();
      return true;
    }
  }

  /* check last redo move.  If playing against AI, need to do undo AI move first. */
  public boolean checkLastRedoTable() {
    synchronized (this) {
      byte ix = 0;
      if (!rgame.checkLastRedo(BoardGameScreen.actPlayer, ix)) { return false; }
      byte nextPlayer = (byte)(1 - BoardGameScreen.actPlayer);
      ix++;
      if (!isHuman[nextPlayer]) {
        if (!rgame.checkLastRedo(nextPlayer, ix)) { return false; }
      }
      return true;
    }
  }

}
