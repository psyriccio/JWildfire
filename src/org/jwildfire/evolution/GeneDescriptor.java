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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
  
  private final Gene thisGene;
  private final GeneCompositor.GeneType type;
  private final String name;
  private final List<GeneCompositor.GeneFlag> flags;
  private final List<GeneCompositor.GeneFlag> distributedFlags;
  private final GeneCompositor compositor;
  private final GeneDescriptor parent;
  
  public GeneDescriptor(Gene thisGene, GeneCompositor compositor, GeneDescriptor parent, GeneCompositor.GeneType type, String name) {
    this.type = type;
    this.name = name;
    this.flags = new ArrayList<>();
    this.compositor = compositor;
    this.parent = parent;
    this.distributedFlags = new ArrayList<>();
    this.thisGene = thisGene;
  }

  public GeneDescriptor(Gene thisGene, GeneCompositor compositor, GeneCompositor.GeneType type, String name) {
    this.type = type;
    this.name = name;
    this.flags = new ArrayList<>();
    this.compositor = compositor;
    this.parent = null;
    this.distributedFlags = new ArrayList<>();
    this.thisGene = thisGene;
  }

  public GeneDescriptor(Gene thisGene, GeneCompositor compositor, GeneDescriptor parent, GeneCompositor.GeneType type, String name, Collection<GeneCompositor.GeneFlag> flags) {
    this.type = type;
    this.name = name;
    this.flags = new ArrayList<>(flags);
    this.compositor = compositor;
    this.parent = parent;
    this.distributedFlags = new ArrayList<>();
    this.thisGene = thisGene;
  }

  public GeneDescriptor(Gene thisGene, GeneCompositor compositor, GeneDescriptor parent, GeneCompositor.GeneType type, String name, Collection<GeneCompositor.GeneFlag> flags, Collection<GeneCompositor.GeneFlag> distributedFlags) {
    this.type = type;
    this.name = name;
    this.flags = new ArrayList<>(flags);
    this.compositor = compositor;
    this.parent = parent;
    this.distributedFlags = new ArrayList<>(distributedFlags);
    this.thisGene = thisGene;
  }

  
  public GeneDescriptor(Gene thisGene, GeneCompositor compositor, GeneCompositor.GeneType type, String name, Collection<GeneCompositor.GeneFlag> flags) {
    this.type = type;
    this.name = name;
    this.flags = new ArrayList<>(flags);
    this.compositor = compositor;
    this.parent = null;
    this.distributedFlags = new ArrayList<>();
    this.thisGene = thisGene;
  }

  public GeneDescriptor(Gene thisGene, GeneCompositor compositor, GeneCompositor.GeneType type, String name, Collection<GeneCompositor.GeneFlag> flags, Collection<GeneCompositor.GeneFlag> distributedFlags) {
    this.type = type;
    this.name = name;
    this.flags = new ArrayList<>(flags);
    this.compositor = compositor;
    this.parent = null;
    this.distributedFlags = new ArrayList<>(distributedFlags);
    this.thisGene = thisGene;
  }
  
  public GeneCompositor.GeneType getType() {
    return type;
  }

  public String getName() {
    return name;
  }
  
  public boolean isFlagged(GeneCompositor.GeneFlag flag) {
    return flags.contains(flag);
  }
  
  public boolean  isDisabled() {
    return isFlagged(GeneCompositor.GeneFlag.DISABLED);
  }
  
  public boolean isImmutable() {
    return isFlagged(GeneCompositor.GeneFlag.IMMUTABLE);
  }
  
  public boolean isNull() {
    return isFlagged(GeneCompositor.GeneFlag.NULL);
  }
  
  public boolean isCorrupted() {
    return isFlagged(GeneCompositor.GeneFlag.CORRUPTED);
  }
  
  public boolean isHidden() {
    return isFlagged(GeneCompositor.GeneFlag.HIDDEN);
  }
  
  public void setFlag(GeneCompositor.GeneFlag flag) {
    if(!flags.contains(flag)) {
      flags.add(flag);
    }
  }
  
  public void unsetFlag(GeneCompositor.GeneFlag flag) {
    if(flags.contains(flag)) {
      flags.remove(flag);
    }
  }
  
  public void setDisabled() {
    setFlag(GeneCompositor.GeneFlag.DISABLED);
  }
  
  public void unsetDisabled() {
    unsetFlag(GeneCompositor.GeneFlag.DISABLED);
  }
  
  public void setImmutable() {
    setFlag(GeneCompositor.GeneFlag.IMMUTABLE);
  }
  
  public void unsetImmutable() {
    unsetFlag(GeneCompositor.GeneFlag.IMMUTABLE);
  }
  
  public void setNull() {
    setFlag(GeneCompositor.GeneFlag.NULL);
  }
  
  public void unsetNull() {
    unsetFlag(GeneCompositor.GeneFlag.NULL);
  }
  
  public void setCorrupted() {
    setFlag(GeneCompositor.GeneFlag.CORRUPTED);
  }
  
  public void unsetCorrupted() {
    unsetFlag(GeneCompositor.GeneFlag.CORRUPTED);
  }
  
  public void setHidden() {
    setFlag(GeneCompositor.GeneFlag.HIDDEN);
  }
  
  public void unsetHidden() {
    unsetFlag(GeneCompositor.GeneFlag.HIDDEN);
  }
  
  public Collection<GeneCompositor.GeneFlag> getFlags() {
    return flags;
  }
  
  public void putFlagValue(GeneCompositor.GeneFlag flag, boolean value) {
    if(value) {
      if(!flags.contains(flag)) flags.add(flag);
    } else {
      if(flags.contains(flag)) flags.remove(flag);
    }
  }

  public Collection<GeneCompositor.GeneFlag> getDistributedFlags() {
    return distributedFlags;
  }
  
  public GeneDescriptor getParent() {
    return parent;
  }
  
  public Gene thisGene() {
    return thisGene;
  }
  
  public GeneSelector select(String query) {
    return new GeneSelector(this).select(query);
  }
  
  @Override
  public String toString() {
    return name + "@" + Integer.toHexString(this.hashCode()) + ":(" + type.toString() + ")";
  }
  
}
