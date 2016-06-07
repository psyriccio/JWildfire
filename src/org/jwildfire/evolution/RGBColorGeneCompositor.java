/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

  public RGBColorGeneCompositor() {
    super(RGBColor.class);
  }

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
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


}
