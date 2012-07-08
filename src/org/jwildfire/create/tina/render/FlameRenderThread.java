/*
  JWildfire - an image and animation processor written in Java 
  Copyright (C) 1995-2012 Andreas Maschke

  This is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser 
  General Public License as published by the Free Software Foundation; either version 2.1 of the 
  License, or (at your option) any later version.
 
  This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License along with this software; 
  if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
  02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jwildfire.create.tina.render;

import org.jwildfire.create.tina.base.Flame;

public abstract class FlameRenderThread implements Runnable {
  protected final FlameRenderer renderer;
  protected final Flame flame;
  protected final long samples;
  protected volatile long currSample;
  protected SampleTonemapper tonemapper;
  protected boolean forceAbort;
  protected boolean finished;

  public FlameRenderThread(FlameRenderer pRenderer, Flame pFlame, long pSamples) {
    renderer = pRenderer;
    flame = pFlame;
    samples = pSamples;
  }

  protected abstract void initState();

  protected abstract void iterate();

  protected abstract FlameRenderThreadState saveState();

  protected abstract void restoreState(FlameRenderThreadState pState);

  @Override
  public void run() {
    finished = forceAbort = false;
    try {
      try {
        initState();
        iterate();
      }
      catch (Throwable ex) {
        ex.printStackTrace();
        throw new RuntimeException(ex);
      }
    }
    finally {
      finished = true;
    }
  }

  public long getCurrSample() {
    return currSample;
  }

  public boolean isFinished() {
    return finished;
  }

  public SampleTonemapper getTonemapper() {
    return tonemapper;
  }

  protected void setTonemapper(SampleTonemapper tonemapper) {
    this.tonemapper = tonemapper;
  }

  public void cancel() {
    forceAbort = true;
  }

}
