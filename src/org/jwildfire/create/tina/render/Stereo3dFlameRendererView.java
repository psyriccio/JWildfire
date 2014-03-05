package org.jwildfire.create.tina.render;

import static org.jwildfire.base.mathlib.MathLib.M_PI;
import static org.jwildfire.base.mathlib.MathLib.cos;
import static org.jwildfire.base.mathlib.MathLib.sin;

import org.jwildfire.create.tina.base.Stereo3dEye;
import org.jwildfire.create.tina.base.Flame;
import org.jwildfire.create.tina.base.XYZPoint;
import org.jwildfire.create.tina.random.AbstractRandomGenerator;

public class Stereo3dFlameRendererView extends FlameRendererView {
  private double eyeAngle, sina, cosa;
  private double eyeOff;

  public Stereo3dFlameRendererView(Stereo3dEye pEye, Flame pFlame, AbstractRandomGenerator pRandGen, int pBorderWidth, int pMaxBorderWidth, int pImageWidth, int pImageHeight, int pRasterWidth, int pRasterHeight) {
    super(pEye, pFlame, pRandGen, pBorderWidth, pMaxBorderWidth, pImageWidth, pImageHeight, pRasterWidth, pRasterHeight);
  }

  @Override
  protected void init3D() {
    super.init3D();
    doProject3D = true;
    switch (eye) {
      case LEFT:
        eyeAngle = flame.getAnaglyph3dAngle() * M_PI / 180.0;
        eyeOff = -flame.getAnaglyph3dEyeDist();
        break;
      case RIGHT:
        eyeAngle = -flame.getAnaglyph3dAngle() * M_PI / 180.0;
        eyeOff = flame.getAnaglyph3dEyeDist();
    }
    sina = sin(eyeAngle);
    cosa = cos(eyeAngle);
  }

  @Override
  protected void applyCameraMatrix(XYZPoint pPoint) {
    super.applyCameraMatrix(pPoint);
    double ax = cosa * camPoint.x + sina * camPoint.z + eyeOff;
    double az = cosa * camPoint.z - sina * camPoint.x;
    camPoint.x = ax;
    camPoint.z = az;
  }
}
