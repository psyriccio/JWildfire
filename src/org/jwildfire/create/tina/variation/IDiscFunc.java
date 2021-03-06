/*
  JWildfire - an image and animation processor written in Java 
  Copyright (C) 1995-2011 Andreas Maschke

  This is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser 
  General Public License as published by the Free Software Foundation; either version 2.1 of the 
  License, or (at your option) any later version.
 
  This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License along with this software; 
  if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
  02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jwildfire.create.tina.variation;

import static org.jwildfire.base.mathlib.MathLib.M_1_PI;
import static org.jwildfire.base.mathlib.MathLib.M_PI;
import static org.jwildfire.base.mathlib.MathLib.atan2;
import static org.jwildfire.base.mathlib.MathLib.sinAndCos;
import static org.jwildfire.base.mathlib.MathLib.sqr;
import static org.jwildfire.base.mathlib.MathLib.sqrt;
import odk.lang.DoubleWrapper;

import org.jwildfire.create.tina.base.Layer;
import org.jwildfire.create.tina.base.XForm;
import org.jwildfire.create.tina.base.XYZPoint;

public class IDiscFunc extends SimpleVariationFunc {
  private static final long serialVersionUID = 1L;

  private DoubleWrapper sina = new DoubleWrapper();
  private DoubleWrapper cosa = new DoubleWrapper();

  @Override
  public void transform(FlameTransformationContext pContext, XForm pXForm, XYZPoint pAffineTP, XYZPoint pVarTP, double pAmount) {
    // idisc my Michael Faber, http://michaelfaber.deviantart.com/art/The-Lost-Variations-258913970 */
    double a = M_PI / (sqrt(sqr(pAffineTP.x) + sqr(pAffineTP.y)) + 1.0);
    double r = atan2(pAffineTP.y, pAffineTP.x) * _v;

    sinAndCos(a, sina, cosa);

    pVarTP.x += r * cosa.value;
    pVarTP.y += r * sina.value;
    if (pContext.isPreserveZCoordinate()) {
  pVarTP.z += pAmount * pAffineTP.z;
}

  }

  @Override
  public String getName() {
    return "idisc";
  }

  private double _v;

  @Override
  public void init(FlameTransformationContext pContext, Layer pLayer, XForm pXForm, double pAmount) {
    _v = pAmount * M_1_PI;
  }

}
