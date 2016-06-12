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

import java.util.HashMap;
import java.util.Optional;
import org.jgap.Configuration;
import org.jgap.InvalidConfigurationException;
import org.jgap.Gene;
import org.jgap.impl.BooleanGene;
import org.jgap.impl.CompositeGene;
import org.jgap.impl.DoubleGene;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.StringGene;
import org.jgap.impl.FixedBinaryGene;

/**
 *
 * @author psyriccio
 * @param <T>
 */
public abstract class GeneCompositor<T> {

  public static Optional<GeneDescriptor> getGeneDescriptor(Gene gene) {
    return GeneDescriptor.ofGene(gene);
  }
  
  public class GeneParser {
    
    private final Gene gene;
    private final HashMap<String, Object> items; 
    private final Object value;
            
    public GeneParser(Gene gene) {
      this.gene = gene;
      if(gene instanceof CompositeGene) {
        CompositeGene cGene = (CompositeGene) gene;
        value = gene;
        items = new HashMap<>();
        cGene.getGenes().stream().forEach((subGene) -> {
          String name = getGeneDescriptor(subGene).orElse(new GeneDescriptor(GeneType.INT, "")).getName();
          items.put(name, subGene);
        });
      } else {
        value = gene.getAllele();
        items = null;
      }
    }
    
    public Object val() {
      return value;
    }
    
    public int intVal() {
      return (int) value;
    }
    
    public double doubleVal() {
      return (double) value;
    }
    
    public String stringVal() {
      return (String) value;
    }
    
    public boolean boolVal() {
      return (boolean) value;
    }
    
    public GeneType type() {
      return getGeneDescriptor(gene).orElse(new GeneDescriptor(null, "")).getType();
    }
    
    public GeneParser get(String name) {
      String names[];
      if(name.contains("/")) {
        names = name.split("/");
      } else {
        return new GeneParser((Gene) items.get(name));
      }
      
      GeneParser ret = null;
      for(String nameVal : names) {
        if(ret == null) {
          ret = new GeneParser((Gene) items.get(name));
        } else {
          ret = ret.get(nameVal);
        }
        return ret;
      }
      
      return null;
      
    }
    
  }
  
  public class GeneBuilder {

    private final CompositeGene gene;

    public GeneBuilder(String name) throws InvalidConfigurationException {
      gene = new CompositeGene(getConfiguration());
      gene.setApplicationData(new GeneDescriptor(GeneType.COMPOSITE, name));
    }

    public GeneBuilder(String name, CompositeGene gene) throws InvalidConfigurationException {
      this.gene = gene;
      gene.setApplicationData(new GeneDescriptor(GeneType.COMPOSITE, name));
    }
    
    public CompositeGene build() {
      return gene;
    }
    
    public GeneBuilder addGene(String name, GeneType type, Object min, Object max) throws InvalidConfigurationException {
      Gene newGene = null;
      switch (type) {
        case COMPOSITE:
          newGene = new CompositeGene(getConfiguration());
          break;
        case BOOLEAN:
          newGene = new BooleanGene(getConfiguration());
          break;
        case DOUBLE:
          if(min != null || max != null) {
            newGene = new DoubleGene(getConfiguration(), (double) min, (double) max);
          } else {
            newGene = new DoubleGene(getConfiguration());
          }
          break;
        case INT:
          if(min != null || max != null) {
            newGene = new IntegerGene(getConfiguration(), (int) min, (int) max);
          } else {
            newGene = new IntegerGene(getConfiguration());
          }
          break;
        case STRING:
          if(min != null || max != null) {
            newGene = new StringGene(getConfiguration(), (int) min, (int) max);
          } else {
            newGene = new StringGene(getConfiguration());
          }
          break;
        case BINARY:
          int length = min != null ? (int) min : (int) max;
          newGene = new FixedBinaryGene(configuration, length);
          break;
        default:
      }
      gene.setApplicationData(new GeneDescriptor(type, name));
      gene.addGene(newGene);
      return this;
    }

    public GeneBuilder addGene(String name, GeneType type) throws InvalidConfigurationException {
      return this.addGene(name, type, null, null);
    }

    public GeneBuilder addGene(String name, GeneType type, int min, int max) throws InvalidConfigurationException {
      return this.addGene(name, type, (Object) min, (Object) max);
    }

    public GeneBuilder addGene(String name, GeneType type, double min, double max) throws InvalidConfigurationException {
      return this.addGene(name, type, (Object) min, (Object) max);
    }
   
    protected Optional<Gene> getGene(String name) {
      for(Gene subgene : gene.getGenes()) {
       if(getGeneDescriptor(subgene).orElse(new GeneDescriptor(null, "")).getName().equals(name)) {
         return Optional.of(subgene);
       }
      }
      return Optional.empty();
    }
    
    public GeneBuilder setAllele(String name, Object value) {
      getGene(name).ifPresent((Gene thisGene) -> thisGene.setAllele(value));
      return this;
    }
    
    public GeneBuilder val(Object value) {
      gene.getGenes().get(
              gene.getGenes().size()-1
      ).setAllele(value);
      return this;
    }
    
  }

  public enum GeneType {
    COMPOSITE, BOOLEAN, INT, DOUBLE, STRING, BINARY
  }

  private final Configuration configuration;
  private final Class<T> clazz;

  public GeneCompositor(Class<T> clazz, Configuration configuration) {
    this.clazz = clazz;
    this.configuration = configuration;
  }

  protected Configuration getConfiguration() {
    return configuration;
  }

  protected GeneBuilder buildGene(String name) throws InvalidConfigurationException {
    return new GeneBuilder(name);
  }
  
  protected GeneBuilder wrapGene(String name, CompositeGene gene) throws InvalidConfigurationException {
    return new GeneBuilder(name, gene);
  }
  
  protected GeneParser parseGene(CompositeGene gene) {
    return new GeneParser(gene);
  }
  
  public abstract CompositeGene compose(T obj) throws InvalidConfigurationException;

  public abstract void decompose(CompositeGene gene, T obj) throws InvalidConfigurationException;

  public T construct(CompositeGene gene) throws InvalidConfigurationException, InstantiationException, IllegalAccessException {
    T obj = clazz.newInstance();
    decompose(gene, obj);
    return obj;
  }

}
