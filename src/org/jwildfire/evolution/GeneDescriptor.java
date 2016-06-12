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

import java.util.Optional;
import org.jgap.Gene;

/**
 *
 * @author psyriccio
 */
public class GeneDescriptor {

  public static Optional<GeneDescriptor> ofGene(Gene gene) {
    Object appData = gene.getApplicationData();
    if(appData != null && appData instanceof GeneDescriptor) {
      return Optional.ofNullable((GeneDescriptor) appData);
    } else {
      return Optional.empty();
    }
  }
  
  private final GeneCompositor.GeneType type;
  private final String name;

  public GeneDescriptor(GeneCompositor.GeneType type, String name) {
    this.type = type;
    this.name = name;
  }

  public GeneCompositor.GeneType getType() {
    return type;
  }

  public String getName() {
    return name;
  }
  
  @Override
  public String toString() {
    return name + "@" + Integer.toHexString(this.hashCode()) + ":(" + type.toString() + ")";
  }
  
}
