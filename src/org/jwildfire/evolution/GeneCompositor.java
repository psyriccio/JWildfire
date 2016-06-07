/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jwildfire.evolution;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.BooleanGene;
import org.jgap.impl.CompositeGene;
import org.jgap.impl.DoubleGene;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.StringGene;

/**
 *
 * @author psyriccio
 * @param <T>
 */
public abstract class GeneCompositor<T> {

  public class GeneBuilder {

    private final CompositeGene gene;

    public GeneBuilder(String name) throws InvalidConfigurationException {
      gene = new CompositeGene(getConfiguration());
      gene.setApplicationData(new GeneDescriptor(GeneType.COMPOSITE, name));
    }
    
    public CompositeGene build() {
      return gene;
    }
    
    public GeneBuilder addGene(String name, GeneType type) throws InvalidConfigurationException {
      Gene newGene = null;
      switch (type) {
        case COMPOSITE:
          newGene = new CompositeGene(getConfiguration());
          break;
        case BOOLEAN:
          newGene = new BooleanGene(getConfiguration());
          break;
        case DOUBLE:
          newGene = new DoubleGene(getConfiguration());
          break;
        case INT:
          newGene = new IntegerGene(getConfiguration());
          break;
        case STRING:
          newGene = new StringGene(getConfiguration());
          break;
        default:
      }
      gene.setApplicationData(new GeneDescriptor(type, name));
      gene.addGene(newGene);
      return this;
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
          newGene = new DoubleGene(getConfiguration(), (double) min, (double) max);
          break;
        case INT:
          newGene = new IntegerGene(getConfiguration(), (int) min, (int) max);
          break;
        case STRING:
          newGene = new StringGene(getConfiguration(), (int) min, (int) max);
          break;
        default:
      }
      gene.setApplicationData(new GeneDescriptor(type, name));
      gene.addGene(newGene);
      return this;
    }

    public GeneBuilder addGene(String name, GeneType type, int min, int max) throws InvalidConfigurationException {
      return this.addGene(name, type, (Object) min, (Object) max);
    }

    public GeneBuilder addGene(String name, GeneType type, double min, double max) throws InvalidConfigurationException {
      return this.addGene(name, type, (Object) min, (Object) max);
    }
    
  }

  public enum GeneType {
    COMPOSITE, BOOLEAN, INT, DOUBLE, STRING
  }

  private Configuration configuration;
  private Class<T> clazz;

  public GeneCompositor(Class<T> clazz) {
    this.configuration = null;
    this.clazz = clazz;
  }

  public GeneCompositor(Class<T> clazz, Configuration configuration) {
    this.clazz = clazz;
    this.configuration = configuration;
  }

  public void setConficuration(Configuration configuration) {
    this.configuration = configuration;
  }

  protected Configuration getConfiguration() {
    return configuration;
  }

  protected GeneBuilder buildGene(String name) throws InvalidConfigurationException {
    return new GeneBuilder(name);
  }
  
  public abstract CompositeGene compose(T obj) throws InvalidConfigurationException;

  public abstract void decompose(CompositeGene gene, T obj) throws InvalidConfigurationException;

  public T construct(CompositeGene gene) throws InvalidConfigurationException, InstantiationException, IllegalAccessException {
    T obj = clazz.newInstance();
    decompose(gene, obj);
    return obj;
  }

}
