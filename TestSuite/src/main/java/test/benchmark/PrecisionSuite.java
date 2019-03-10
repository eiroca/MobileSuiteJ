/**
 * Copyright (C) 2006-2019 eIrOcA (eNrIcO Croce & sImOnA Burzio) - GPL >= 3.0
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
package test.benchmark;

/**
 * The Class PrecisionSuite.
 */
public class PrecisionSuite extends SuiteAbstract {

  /** The Constant PREFIX. */
  public static final String PREFIX = "B.P.";

  /** The Constant CATEGORY. */
  public static final String CATEGORY = "Precision";

  /**
   * Instantiates a new precision suite.
   */
  public PrecisionSuite() {
    super(PrecisionSuite.CATEGORY, PrecisionSuite.PREFIX);
    benchmark = new BenchmarkAbstract[1];
    benchmark[0] = new Precision(this);
  }

}
