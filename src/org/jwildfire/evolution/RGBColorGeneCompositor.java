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
import org.jwildfire.create.tina.palette.RGBColor;

/**
 *
 * @author psyriccio
 */
public class RGBColorGeneCompositor extends GeneCompositor<RGBColor> {

  public RGBColorGeneCompositor(Configuration configuration) {
    super(RGBColor.class, configuration);
  }

  @Override
  public CompositeGene compose(RGBColor obj) throws InvalidConfigurationException {
    return buildGene("COLOR")
            .addGene("RED", GeneType.INT, 0, 255)
            .addGene("GREEN", GeneType.INT, 0, 255)
            .addGene("BLUE", GeneType.INT, 0, 255)
            .build();
  }

  @Override
  public void decompose(CompositeGene gene, RGBColor obj) throws InvalidConfigurationException {
    GeneParser parser = parseGene(gene);
    obj.setRed(parser.get("RED").intVal());
    obj.setGreen(parser.get("GREEN").intVal());
    obj.setBlue(parser.get("BLUE").intVal());
  }


}
