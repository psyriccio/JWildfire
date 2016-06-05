/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jwildfire.evolution.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import org.jwildfire.base.Prefs;
import org.jwildfire.create.tina.base.Flame;
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
import org.jwildfire.image.SimpleImage;
import org.jwildfire.swing.ErrorHandler;
import org.jwildfire.swing.ImagePanel;

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
  
  private void init() {
    flamePanel.setPreferredSize(new Dimension(320, 240));
    flamePanel.setSize(320, 240);
    flamePanel.setMaximumSize(new Dimension(320, 240));
    flamePanel.setAlignmentX(LEFT_ALIGNMENT);
    flamePanel.setAlignmentY(TOP_ALIGNMENT);
    this.add(flamePanel);
    this.add(testButton);
    testButton.setText("TEST");
    testButton.setActionCommand("test");
    testButton.addActionListener(this);
    testButton.setMaximumSize(new Dimension(50, 20));
    testButton.setAlignmentX(LEFT_ALIGNMENT);
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
    testButton.setEnabled(true);
  }
  
  public EvolutionPanel() {
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    testButton = new JButton();
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
