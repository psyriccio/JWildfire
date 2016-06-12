/*
 * Copyright (C) 2016 John Gleezowood (psyriccio@gmail.com).
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.jwildfire.evolution;

import org.jgap.Configuration;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.CompositeGene;
import org.jwildfire.create.tina.palette.RGBPalette;

/**
 *
 * @author psyriccio
 */
public class RGBPaletteGeneCompositor extends GeneCompositor<RGBPalette> {

  private final RGBColorGeneCompositor rgbColorGC;
  
  public RGBPaletteGeneCompositor(Configuration configuration) {
    super(RGBPalette.class, configuration);
    rgbColorGC = new RGBColorGeneCompositor(configuration);
  }

  @Override
  public CompositeGene compose(RGBPalette obj) throws InvalidConfigurationException {
    
    GeneBuilder gb = buildGene("RGBPalette");
    for(int k = 0; k <= 255; k++) {
      gb.addGene("color" + Integer.toString(k), GeneType.COMPOSITE)
              .val(rgbColorGC.compose(obj.getRawColor(k)));
    }
    
    gb
            .addGene("modRed",        GeneType.INT, -255, 255).val(obj.getModRed())
            .addGene("modGreen",      GeneType.INT, -255, 255).val(obj.getModGreen())
            .addGene("modBlue",       GeneType.INT, -255, 255).val(obj.getModBlue())
            .addGene("modShift",      GeneType.INT, -255, 255).val(obj.getModShift())
            .addGene("modHue",        GeneType.INT, -255, 255).val(obj.getModHue())
            .addGene("modContrast",   GeneType.INT, -255, 255).val(obj.getModContrast())
            .addGene("modGamma",      GeneType.INT, -255, 255).val(obj.getModGamma())
            .addGene("modBrightness", GeneType.INT, -255, 255).val(obj.getModBrightness())
            .addGene("modSaturation", GeneType.INT, -255, 255).val(obj.getModSaturation())
            .addGene("modSwapRGB",    GeneType.INT, -255, 255).val(obj.getModSwapRGB())
            .addGene("modFrequency",  GeneType.INT, 1, 16)    .val(obj.getModFrequency())
            .addGene("modBlur",       GeneType.INT, 0, 128)   .val(obj.getModBlur());
  
    return gb.build();
  
  }

  @Override
  public void decompose(CompositeGene gene, RGBPalette obj) throws InvalidConfigurationException {
    
  
  }
  
}
