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
package org.jwildfire.evolution.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jgap.Chromosome;
import org.jgap.Gene;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.CompositeGene;
import org.jwildfire.base.Prefs;
import org.jwildfire.create.tina.base.Flame;
import org.jwildfire.create.tina.io.FlameWriter;
import org.jwildfire.create.tina.palette.RGBPalette;
import org.jwildfire.create.tina.randomflame.AllRandomFlameGenerator;
import org.jwildfire.create.tina.randomflame.RandomFlameGeneratorSampler;
import org.jwildfire.create.tina.randomgradient.RandomGradientGeneratorList;
import org.jwildfire.create.tina.randomsymmetry.RandomSymmetryGeneratorList;
import org.jwildfire.create.tina.render.ProgressUpdater;
import org.jwildfire.create.tina.swing.FlameHolder;
import org.jwildfire.create.tina.swing.FlameMessageHelper;
import org.jwildfire.create.tina.swing.FlamePanelProvider;
import org.jwildfire.create.tina.swing.FlamePreviewHelper;
import org.jwildfire.create.tina.swing.RandomBatchQuality;
import org.jwildfire.create.tina.swing.TinaController;
import org.jwildfire.create.tina.swing.flamepanel.FlamePanel;
import org.jwildfire.create.tina.swing.flamepanel.FlamePanelConfig;
import org.jwildfire.evolution.FlameChromosomeCoder;
import org.jwildfire.image.SimpleImage;
import org.jwildfire.swing.ErrorHandler;

/**
 *
 * @author psyriccio
 */
public class EvolutionPanel extends JPanel implements ErrorHandler, ProgressUpdater, FlameHolder, ActionListener, FlamePanelProvider, FlameMessageHelper {

  private final Prefs prefs = Prefs.getPrefs();

  private Flame currentFlame;
  
  private FlamePanel flamePanel = new FlamePanel(prefs, new SimpleImage(320, 240), 0, 0, 320, this, null);
  private final FlamePreviewHelper flamePrevHelper = 
          new FlamePreviewHelper(
                  this, 
                  flamePanel, 
                  null, null, null, this, this, null, null, this, this, null
          );
  
  private final JButton testButton;
  private final JButton mutButton;
  private final JButton addFromEditorButton;
  private final JButton crossButton;
  
  private void init() {
    flamePanel.setPreferredSize(new Dimension(320, 240));
    flamePanel.setSize(320, 240);
    flamePanel.setMaximumSize(new Dimension(320, 240));
    flamePanel.setAlignmentX(LEFT_ALIGNMENT);
    flamePanel.setAlignmentY(TOP_ALIGNMENT);
    this.add(flamePanel);
    this.add(testButton);
    this.add(mutButton);
    this.add(crossButton);
    this.add(addFromEditorButton);
    testButton.setText("TEST");
    testButton.setActionCommand("test");
    testButton.addActionListener(this);
    testButton.setMaximumSize(new Dimension(100, 20));
    testButton.setAlignmentX(LEFT_ALIGNMENT);
    mutButton.setText("Mutate");
    mutButton.setActionCommand("mutate");
    mutButton.addActionListener(this);
    mutButton.setMaximumSize(new Dimension(100, 20));
    mutButton.setAlignmentX(LEFT_ALIGNMENT);
    crossButton.setText("Crossover");
    crossButton.setActionCommand("crossover");
    crossButton.addActionListener(this);
    crossButton.setMaximumSize(new Dimension(100, 20));
    crossButton.setAlignmentX(LEFT_ALIGNMENT);
    addFromEditorButton.setText("From editor");
    addFromEditorButton.setActionCommand("addFromEditor");
    addFromEditorButton.addActionListener(this);
    addFromEditorButton.setMaximumSize(new Dimension(100, 20));
    addFromEditorButton.setAlignmentX(LEFT_ALIGNMENT);
    FlameChromosomeCoder coder = new FlameChromosomeCoder();
    try {
      Chromosome flameSampleChromosome = coder.constructSampleChromosome();
      for(Gene gene : flameSampleChromosome.getGenes()) {
        System.out.println(gene.toString());
      }
    } catch (Exception ex) {
      Logger.getLogger(EvolutionPanel.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  private void doTest() {
    testButton.setEnabled(false);
    AllRandomFlameGenerator randFlameGen = new AllRandomFlameGenerator();
    int palettePoints = 3 + (int) (Math.random() * 42.0);
    boolean fadePaletteColors = Math.random() > 0.09;
    RandomFlameGeneratorSampler randFlameGenSampler = 
            new RandomFlameGeneratorSampler(
                    320, 240, 
                    prefs, 
                    randFlameGen, 
                    RandomSymmetryGeneratorList.NONE, 
                    RandomGradientGeneratorList.DEFAULT, 
                    palettePoints, 
                    fadePaletteColors, 
                    RandomBatchQuality.LOW
            );
    currentFlame = randFlameGenSampler.createSample().getFlame();
    //flamePrevHelper.refreshFlameImage(false, true, 1, true);
    flamePanel.setPreferredSize(new Dimension(320, 240));
    flamePanel.getConfig().setProgressivePreview(true);
    flamePanel.setImage(
      flamePrevHelper.renderFlameImage(true, false, 1), 0, 0, 320, 240
    );
    flamePanel.repaint();
    TinaController.mainTinaController.importFlame(currentFlame, true);
    FlameChromosomeCoder coder = new FlameChromosomeCoder();
    try {
      Chromosome chr = coder.createChromosome(currentFlame);
      Flame decoded = coder.createFlame(chr);
      TinaController.mainTinaController.importFlame(decoded, true);
      try {
        FlameWriter writer = new FlameWriter();
        writer.writeFlame(currentFlame, "main.flame");
        writer.writeFlame(decoded, "decoded.flame");
      } catch (Exception ex) {
        Logger.getLogger(EvolutionPanel.class.getName()).log(Level.SEVERE, null, ex);
      }
    } catch (InvalidConfigurationException ex) {
        Logger.getLogger(EvolutionPanel.class.getName()).log(Level.SEVERE, null, ex);
    }
    testButton.setEnabled(true);
  }
  
  private void mutateIt(Gene gene) {
    if(gene instanceof CompositeGene) {
      for(Gene child : ((CompositeGene) gene).getGenes()) {
        mutateIt(child);
      }
    } else {
      for(int k = 0; k <= gene.size()-1; k++) {
        if(Math.random() > 0.7d) {
          gene.applyMutation(k, 0.05d);
        }
      }
    }
  }
  
  private void doMutate() {

    mutButton.setEnabled(false);
    
    FlameChromosomeCoder coder = new FlameChromosomeCoder();
    try {
      Chromosome mutchr = coder.createChromosome(currentFlame);
      for(Gene gene : mutchr.getGenes()) {
        if(Math.random() > 0.7d) {
          mutateIt(gene);
        }
      }
      Flame mutated = coder.createFlame(mutchr);
      try {
        FlameWriter writer = new FlameWriter();
        writer.writeFlame(mutated, "mutated.flame");
      } catch (Exception ex) {
        Logger.getLogger(EvolutionPanel.class.getName()).log(Level.SEVERE, null, ex);
      }
      TinaController.mainTinaController.importFlame(mutated, true);
      currentFlame = mutated;
      flamePanel.setImage(
        flamePrevHelper.renderFlameImage(true, false, 1), 0, 0, 320, 240
      );
      flamePanel.repaint();
    } catch (InvalidConfigurationException ex) {
      Logger.getLogger(EvolutionPanel.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    mutButton.setEnabled(true);

  }
  
  private void doCrossover() {
    crossButton.setEnabled(false);
    crossButton.setEnabled(true);
  }
  
  private void doAddFromEditor() {
    addFromEditorButton.setEnabled(false);
    currentFlame = TinaController.mainTinaController.getCurrFlame();
    flamePanel.setImage(
      flamePrevHelper.renderFlameImage(true, false, 1), 0, 0, 320, 240
    );
    flamePanel.repaint();
    addFromEditorButton.setEnabled(true);
  }
  
  public EvolutionPanel() {
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    testButton = new JButton();
    mutButton = new JButton();
    crossButton = new JButton();
    addFromEditorButton = new JButton();
    init();
  }

  @Override
  public void handleError(Throwable pThrowable) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void initProgress(int pMaxSteps) {
    //
  }

  @Override
  public void updateProgress(int pStep) {
    //
  }

  @Override
  public Flame getFlame() {
    return currentFlame;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getActionCommand().equals("test")) {
      testButton.setEnabled(false);
      new Thread(new Runnable() {
        @Override
        public void run() {
          doTest();
        }
      }).start();
    }
    if(e.getActionCommand().equals("mutate")) {
      mutButton.setEnabled(false);
      new Thread(new Runnable() {
        @Override
        public void run() {
          doMutate();
        }
      }).start();
    }
    if(e.getActionCommand().equals("addFromEditor")) {
      addFromEditorButton.setEnabled(false);
      new Thread(new Runnable() {
        @Override
        public void run() {
          doAddFromEditor();
        }
      }).start();
    }
    if(e.getActionCommand().equals("crossover")) {
      crossButton.setEnabled(false);
      new Thread(new Runnable() {
        @Override
        public void run() {
          doCrossover();
        }
      }).start();
    }
  }

  @Override
  public FlamePanel getFlamePanel() {
    return flamePanel;
  }

  @Override
  public FlamePanelConfig getFlamePanelConfig() {
    return flamePanel.getConfig();
  }

  @Override
  public void showStatusMessage(String pStatus) {
    System.out.println(pStatus);
  }

  @Override
  public void showStatusMessage(Flame pFlame, String pStatus) {
    System.out.println(pFlame.toString() + " - " + pStatus);
  }

  @Override
  public void showStatusMessage(RGBPalette pGradient, String pStatus) {
    System.out.println(pGradient.toString() + " - " + pStatus);
  }
  
}
