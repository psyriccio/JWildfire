/*
  JWildfire - an image and animation processor written in Java 
  Copyright (C) 1995-2014 Andreas Maschke

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
package org.jwildfire.create.tina.integration.chaotica;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jwildfire.create.tina.variation.AugerFunc;
import org.jwildfire.create.tina.variation.Blade3DFunc;
import org.jwildfire.create.tina.variation.Blob3DFunc;
import org.jwildfire.create.tina.variation.BubbleWFFunc;
import org.jwildfire.create.tina.variation.Butterfly3DFunc;
import org.jwildfire.create.tina.variation.ColorMapWFFunc;
import org.jwildfire.create.tina.variation.ConicFunc;
import org.jwildfire.create.tina.variation.CropFunc;
import org.jwildfire.create.tina.variation.CustomWFFunc;
import org.jwildfire.create.tina.variation.CylinderApoFunc;
import org.jwildfire.create.tina.variation.DCBubbleFunc;
import org.jwildfire.create.tina.variation.DCCrackleWFFunc;
import org.jwildfire.create.tina.variation.DCHexesWFFunc;
import org.jwildfire.create.tina.variation.DCZTranslFunc;
import org.jwildfire.create.tina.variation.Disc2Func;
import org.jwildfire.create.tina.variation.DisplacementMapWFFunc;
import org.jwildfire.create.tina.variation.EllipticFunc;
import org.jwildfire.create.tina.variation.EpispiralFunc;
import org.jwildfire.create.tina.variation.ExtrudeFunc;
import org.jwildfire.create.tina.variation.Fan2Func;
import org.jwildfire.create.tina.variation.FlattenFunc;
import org.jwildfire.create.tina.variation.FractFormulaJuliaWFFunc;
import org.jwildfire.create.tina.variation.FractFormulaMandWFFunc;
import org.jwildfire.create.tina.variation.InflateZ_1Func;
import org.jwildfire.create.tina.variation.InflateZ_2Func;
import org.jwildfire.create.tina.variation.InflateZ_3Func;
import org.jwildfire.create.tina.variation.InflateZ_4Func;
import org.jwildfire.create.tina.variation.InflateZ_5Func;
import org.jwildfire.create.tina.variation.InflateZ_6Func;
import org.jwildfire.create.tina.variation.LogApoFunc;
import org.jwildfire.create.tina.variation.LogFunc;
import org.jwildfire.create.tina.variation.MobiusFunc;
import org.jwildfire.create.tina.variation.NgonFunc;
import org.jwildfire.create.tina.variation.PerspectiveFunc;
import org.jwildfire.create.tina.variation.Pie3DFunc;
import org.jwildfire.create.tina.variation.PieFunc;
import org.jwildfire.create.tina.variation.PostBumpMapWFFunc;
import org.jwildfire.create.tina.variation.PostColorMapWFFunc;
import org.jwildfire.create.tina.variation.PostCustomWFFunc;
import org.jwildfire.create.tina.variation.PostDCZTranslFunc;
import org.jwildfire.create.tina.variation.PostDepthFunc;
import org.jwildfire.create.tina.variation.PostDisplacementMapWFFunc;
import org.jwildfire.create.tina.variation.PostZTranslateWFFunc;
import org.jwildfire.create.tina.variation.PreCustomWFFunc;
import org.jwildfire.create.tina.variation.PreDCZTranslFunc;
import org.jwildfire.create.tina.variation.PreSubFlameWFFunc;
import org.jwildfire.create.tina.variation.PreWave3DWFFunc;
import org.jwildfire.create.tina.variation.PreZScaleFunc;
import org.jwildfire.create.tina.variation.PreZTranslateFunc;
import org.jwildfire.create.tina.variation.SVGWFFunc;
import org.jwildfire.create.tina.variation.SeparationFunc;
import org.jwildfire.create.tina.variation.Sinusoidal3DFunc;
import org.jwildfire.create.tina.variation.Spherical3DFunc;
import org.jwildfire.create.tina.variation.Splits3DFunc;
import org.jwildfire.create.tina.variation.Square3DFunc;
import org.jwildfire.create.tina.variation.SubFlameWFFunc;
import org.jwildfire.create.tina.variation.SwirlFunc;
import org.jwildfire.create.tina.variation.Tangent3DFunc;
import org.jwildfire.create.tina.variation.TangentFunc;
import org.jwildfire.create.tina.variation.TextWFFunc;
import org.jwildfire.create.tina.variation.WedgeFunc;
import org.jwildfire.create.tina.variation.ZBlurFunc;
import org.jwildfire.create.tina.variation.ZConeFunc;
import org.jwildfire.create.tina.variation.ZScaleFunc;
import org.jwildfire.create.tina.variation.ZTranslateFunc;
import org.jwildfire.create.tina.variation.iflames.IFlamesFunc;

public class ChaoticaPluginTranslators {

  private static Map<String, ChaoticaPluginTranslator> translators = new HashMap<String, ChaoticaPluginTranslator>();

  static {
    EmptyChaoticaPluginTranslator emptyTranslator = new EmptyChaoticaPluginTranslator();
    translators.put(new AugerFunc().getName(), new ChaoticaPluginTranslator("auger_wf", name("freq"), name("weight"), name("sym"), name("scale")));
    translators.put(new Blade3DFunc().getName(), new ChaoticaPluginTranslator("blade"));
    translators.put(new Blob3DFunc().getName(), new ChaoticaPluginTranslator("blob"));
    translators.put(new BubbleWFFunc().getName(), new ChaoticaPluginTranslator("bubble"));
    translators.put(new Butterfly3DFunc().getName(), new ChaoticaPluginTranslator("butterfly"));
    translators.put(new ColorMapWFFunc().getName(), emptyTranslator);
    translators.put(new ConicFunc().getName(), new ChaoticaPluginTranslator("conic_wf", name("eccentricity"), name("holes")));
    translators.put(new CropFunc().getName(), new ChaoticaPluginTranslator("crop_wf", name("left"), name("right"), name("top"), name("bottom"),
        name("scatter_area"), name("zero")));
    translators.put(new CustomWFFunc().getName(), emptyTranslator);
    translators.put(new CylinderApoFunc().getName(), new ChaoticaPluginTranslator("cylinder"));
    translators.put(new DCBubbleFunc().getName(), emptyTranslator);
    translators.put(new DCCrackleWFFunc().getName(), new ChaoticaPluginTranslator("crackle", name("cellsize"), name("power"), name("distort"), name("scale"), name("z")));
    translators.put(new DCHexesWFFunc().getName(), new ChaoticaPluginTranslator("hexes", name("cellsize"), name("power"), name("rotate"), name("scale")));
    translators.put(new DCZTranslFunc().getName(), new ChaoticaPluginTranslator("linear3D"));
    translators.put(new Disc2Func().getName(), new ChaoticaPluginTranslator("disc2_wf", name("rot"), name("twist")));
    translators.put(new DisplacementMapWFFunc().getName(), emptyTranslator);
    translators.put(new EllipticFunc().getName(), new ChaoticaPluginTranslator("elliptic_wf"));
    translators.put(new EpispiralFunc().getName(), new ChaoticaPluginTranslator("Epispiral", name("n"), name("thickness"), name("holes")));
    translators.put(new ExtrudeFunc().getName(), emptyTranslator);
    translators.put(new Fan2Func().getName(), new ChaoticaPluginTranslator("fan2_wf", name("x"), name("y")));
    translators.put(new FlattenFunc().getName(), emptyTranslator);
    translators.put(new FractFormulaMandWFFunc().getName(), new ChaoticaPluginTranslator("fract_mandelbrot_wf", name("x"),
        name("max_iter"), name("xmin"), name("xmax"), name("ymin"), name("ymax"), name("buddhabrot_mode"),
        name("buddhabrot_min_iter"), name("direct_color"), name("scalez"), name("clip_iter_min"),
        name("clip_iter_max"), name("max_clip_iter"), name("scale"), name("offsetx"), name("offsety"),
        name("offsetz"), name("z_fill"), name("z_logscale"),
        name("power")));
    translators.put(new FractFormulaJuliaWFFunc().getName(), new ChaoticaPluginTranslator("fract_julia_wf",
        name("max_iter"), name("xmin"), name("xmax"), name("ymin"), name("ymax"), name("buddhabrot_mode"),
        name("buddhabrot_min_iter"), name("direct_color"), name("scalez"), name("clip_iter_min"),
        name("clip_iter_max"), name("max_clip_iter"), name("scale"), name("offsetx"), name("offsety"),
        name("offsetz"), name("z_fill"), name("z_logscale"),
        name("power"), name("xseed"), name("yseed")));
    translators.put(new IFlamesFunc().getName(), emptyTranslator);
    translators.put(new InflateZ_1Func().getName(), emptyTranslator);
    translators.put(new InflateZ_2Func().getName(), emptyTranslator);
    translators.put(new InflateZ_3Func().getName(), emptyTranslator);
    translators.put(new InflateZ_4Func().getName(), emptyTranslator);
    translators.put(new InflateZ_5Func().getName(), emptyTranslator);
    translators.put(new InflateZ_6Func().getName(), emptyTranslator);
    translators.put(new LogApoFunc().getName(), new ChaoticaPluginTranslator("log", name("base")));
    translators.put(new LogFunc().getName(), new ChaoticaPluginTranslator("log", fixedValue("base", 2.71828182845905)));
    translators.put(new MobiusFunc().getName(), new ChaoticaPluginTranslator("mobius_wf", name("re_a"), name("re_b"), name("re_c"), name("re_d"), name("im_a"), name("im_b"), name("im_c"), name("im_d")));
    translators.put(new NgonFunc().getName(), new ChaoticaPluginTranslator("ngon_wf", name("circle"), name("corners"), name("power"), name("sides")));
    translators.put(new PerspectiveFunc().getName(), new ChaoticaPluginTranslator("perspective_wf", name("angle"), name("dist")));
    translators.put(new PieFunc().getName(), new ChaoticaPluginTranslator("pie_wf", name("slices"), name("rotation"), name("thickness")));
    translators.put(new Pie3DFunc().getName(), new ChaoticaPluginTranslator("pie_wf", name("slices"), name("rotation"), name("thickness")));
    translators.put(new PostBumpMapWFFunc().getName(), emptyTranslator);
    translators.put(new PostColorMapWFFunc().getName(), emptyTranslator);
    translators.put(new PostCustomWFFunc().getName(), emptyTranslator);
    translators.put(new PostDepthFunc().getName(), new ChaoticaPluginTranslator("linear3D"));
    translators.put(new PostDCZTranslFunc().getName(), new ChaoticaPluginTranslator("linear3D"));
    translators.put(new PostDisplacementMapWFFunc().getName(), emptyTranslator);
    translators.put(new PostZTranslateWFFunc().getName(), emptyTranslator);
    translators.put(new PreCustomWFFunc().getName(), emptyTranslator);
    translators.put(new PreDCZTranslFunc().getName(), new ChaoticaPluginTranslator("linear3D"));
    translators.put(new PreSubFlameWFFunc().getName(), new ChaoticaPluginTranslator("pre_blur"));
    translators.put(new PreWave3DWFFunc().getName(), emptyTranslator);
    translators.put(new PreZScaleFunc().getName(), emptyTranslator);
    translators.put(new PreZTranslateFunc().getName(), emptyTranslator);
    translators.put(new SeparationFunc().getName(), new ChaoticaPluginTranslator("separation_wf", name("x"), name("xinside"), name("y"), name("yinside")));
    translators.put(new Sinusoidal3DFunc().getName(), new ChaoticaPluginTranslator("sinusoidal"));
    translators.put(new Spherical3DFunc().getName(), new ChaoticaPluginTranslator("spherical"));
    translators.put(new Splits3DFunc().getName(), new ChaoticaPluginTranslator("splits", name("x"), name("y")));
    translators.put(new Square3DFunc().getName(), new ChaoticaPluginTranslator("square"));
    translators.put(new SubFlameWFFunc().getName(), new ChaoticaPluginTranslator("blur"));
    translators.put(new SVGWFFunc().getName(), new ChaoticaPluginTranslator("square"));
    translators.put(new SwirlFunc().getName(), new ChaoticaPluginTranslator("swirl_wf"));
    translators.put(new TangentFunc().getName(), new ChaoticaPluginTranslator("tangent_wf"));
    translators.put(new Tangent3DFunc().getName(), new ChaoticaPluginTranslator("tangent_wf"));
    translators.put(new TextWFFunc().getName(), new ChaoticaPluginTranslator("square"));
    translators.put(new ZBlurFunc().getName(), emptyTranslator);
    translators.put(new ZConeFunc().getName(), emptyTranslator);
    translators.put(new ZScaleFunc().getName(), emptyTranslator);
    translators.put(new ZTranslateFunc().getName(), emptyTranslator);
    translators.put(new WedgeFunc().getName(), new ChaoticaPluginTranslator("wedge_wf", name("angle"), name("hole"), name("count"), name("swirl")));
  }

  private static NamePair name(String pFromTo) {
    return new NamePair(pFromTo, pFromTo);
  }

  private static NamePair namePair(String pFrom, String pTo) {
    return new NamePair(pFrom, pTo);
  }

  private static FixedValue fixedValue(String pName, Double pValue) {
    return new FixedValue(pName, pValue);
  }

  public String translateVarName(String varName) {
    ChaoticaPluginTranslator translator = translators.get(varName);
    return translator != null ? translator.getTranslatedName() : varName;
  }

  public String translatePropertyName(String varName, String propertyName) {
    ChaoticaPluginTranslator translator = translators.get(varName);
    if (translator != null) {
      return translator.translatePropertyName(propertyName);
    }
    else {
      return varName + "_" + propertyName;
    }
  }

  public List<String> getFixedValueNames(String pVarName) {
    ChaoticaPluginTranslator translator = translators.get(pVarName);
    if (translator != null) {
      return translator.getFixedValueNames();
    }
    else {
      return Collections.emptyList();
    }
  }

  public Double getFixedValue(String pVarName, String pName) {
    return translators.get(pVarName).getFixedValue(pName);
  }

}
