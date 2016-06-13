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

import java.util.ArrayList;
import java.util.List;
import org.jgap.impl.CompositeGene;

/**
 *
 * @author psyriccio
 */
public class GeneSelector {

  private final List<GeneDescriptor> context;
  private final List<GeneDescriptor> content;

  public GeneSelector(GeneDescriptor context) {
    this.context = new ArrayList<>();
    this.context.add(context);
    content = new ArrayList<>();
  }

  public GeneSelector(List<GeneDescriptor> context) {
    this.context = new ArrayList<>(context);
    content = new ArrayList<>();
  }
  
  public GeneSelector(GeneDescriptor context, List<GeneDescriptor> content) {
    this.context = new ArrayList<>();
    this.context.add(context);
    this.content = new ArrayList<>(content);
  }
  
  public GeneSelector(List<GeneDescriptor> context, List<GeneDescriptor> content) {
    this.context = new ArrayList<>(context);
    this.content = new ArrayList<>(content);
  }

  public GeneSelector(List<GeneDescriptor> context, List<GeneDescriptor> content, Object dirtyHack) {
    this.context = new ArrayList<>(context);
    this.content = new ArrayList<>(content);
  }
  
  public GeneSelector select(String query) {
    
      if(query.isEmpty() || context == null)
        return new GeneSelector(null, content, null);
      
      final String[] path = query.split("/", 2);
      
      context.stream()
              .filter((ctx) -> ctx.getType() == GeneCompositor.GeneType.COMPOSITE)
              .filter((ctx) -> !ctx.isHidden() && !ctx.isNull())
              .map((ctx) -> (CompositeGene) ctx.thisGene() )
              .forEach((ctx) -> {
                ctx.getGenes().stream()
                        .map((i) -> GeneDescriptor.ofGene(i).get())
                        .filter((i) -> !i.isHidden() && !i.isNull())
                        .filter((i) -> path[0].equals("*") 
                                || i.getName().equals(path[0]))
                        .forEach((i) -> content.add(i));
              });
      
      return 
              path.length == 1 ? 
                new GeneSelector(null, content, null) 
                  : (new GeneSelector(content)).select(path[1]);

  }

  public List<GeneDescriptor> done() {
    return content;
  }
  
}
