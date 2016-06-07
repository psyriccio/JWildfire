/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jwildfire.evolution;

/**
 *
 * @author psyriccio
 */
public class GeneDescriptor {

  private final GeneCompositor.GeneType type;
  private final String name;

  public GeneDescriptor(GeneCompositor.GeneType type, String name) {
    this.type = type;
    this.name = name;
  }

  @Override
  public String toString() {
    return name + "@" + Integer.toHexString(this.hashCode()) + ":(" + type.toString() + ")";
  }
  
}
