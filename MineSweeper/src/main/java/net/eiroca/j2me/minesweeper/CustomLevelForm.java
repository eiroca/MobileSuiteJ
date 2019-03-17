/**
 * Copyright (C) 2006-2019 eIrOcA (eNrIcO Croce & sImOnA Burzio) - GPL >= 3.0
 *
 * Portion Copyright (C) M. Jumari
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/
 */
package net.eiroca.j2me.minesweeper;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import net.eiroca.j2me.app.Application;

/**
 * The Class CustomLevelForm.
 */
public class CustomLevelForm extends Form {

  /** The t height. */
  private final TextField tHeight;

  /** The t width. */
  private final TextField tWidth;

  /** The t bomb. */
  private final TextField tBomb;

  /**
   * Instantiates a new custom level form.
   */
  public CustomLevelForm() {
    super(Application.messages[MineSweeperMIDlet.MSG_CUSTOMLEVEL]);
    tHeight = new TextField(Application.messages[MineSweeperMIDlet.MSG_CL_HEIGTH], "", 2, TextField.NUMERIC);
    tWidth = new TextField(Application.messages[MineSweeperMIDlet.MSG_CL_WIDTH], "", 2, TextField.NUMERIC);
    tBomb = new TextField(Application.messages[MineSweeperMIDlet.MSG_CL_BOMBS], "", 2, TextField.NUMERIC);
    append(tHeight);
    append(tWidth);
    append(tBomb);
  }

  /**
   * Sets the inputs.
   */
  public void setInputs() {
    tHeight.setString(Integer.toString(MineSweeperMIDlet.height));
    tWidth.setString(Integer.toString(MineSweeperMIDlet.width));
    tBomb.setString(Integer.toString(MineSweeperMIDlet.bomb));
  }

  /**
   * Gets the inputs.
   *
   * @return the inputs
   */
  public void getInputs() {
    MineSweeperMIDlet.height = Integer.parseInt(tHeight.getString());
    MineSweeperMIDlet.width = Integer.parseInt(tWidth.getString());
    MineSweeperMIDlet.bomb = Integer.parseInt(tBomb.getString());
  }

}
