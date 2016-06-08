/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jwildfire.evolution;

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
  
  public abstract CompositeGene compose(T obj) throws InvalidConfigurationException;

  public abstract void decompose(CompositeGene gene, T obj) throws InvalidConfigurationException;

  public T construct(CompositeGene gene) throws InvalidConfigurationException, InstantiationException, IllegalAccessException {
    T obj = clazz.newInstance();
    decompose(gene, obj);
    return obj;
  }

}
