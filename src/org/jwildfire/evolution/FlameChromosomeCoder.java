/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jwildfire.evolution;

import java.util.List;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.BooleanGene;
import org.jgap.impl.CompositeGene;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;
import org.jgap.impl.GaussianRandomGenerator;
import org.jgap.impl.IntegerGene;
import org.jwildfire.create.tina.base.Constants;
import org.jwildfire.create.tina.base.Flame;
import org.jwildfire.create.tina.base.XForm;
import org.jwildfire.create.tina.palette.RGBColor;
import org.jwildfire.create.tina.palette.RGBPalette;
import org.jwildfire.create.tina.render.dof.DOFBlurShapeType;
import org.jwildfire.create.tina.variation.LineFunc;
import org.jwildfire.create.tina.variation.Variation;
import org.jwildfire.create.tina.variation.VariationFunc;
import org.jwildfire.create.tina.variation.VariationFuncList;

/**
 *
 * @author psyriccio
 */
public class FlameChromosomeCoder {

  private Configuration conf;
  private final List<String> variations = VariationFuncList.getNameList();
  
  public FlameChromosomeCoder() {
    conf = new DefaultConfiguration();
  }

  public FlameChromosomeCoder(Configuration conf) {
    this.conf = conf;
  }
  
  public CompositeGene constructVariationFuncSampleGene() throws InvalidConfigurationException {
    CompositeGene gene = new CompositeGene(conf);
    gene.addGene(new IntegerGene(conf, 0, variations.size()-1)); // index of func
    for(int k = 0; k <= 19; k++) { 
      gene.addGene(new DoubleGene(conf)); // params
    }
    return gene;
  }
  
  public void fillVariationFuncGene(CompositeGene gene, VariationFunc val) {
    gene.geneAt(0).setAllele(variations.indexOf(val.getName()));
    for(int k = 1; k <= 20; k++) {
      try {
        gene.geneAt(k).setAllele(val.getParameterValues()[k-1]);
      } catch (Exception ex) {
        gene.geneAt(k).setAllele(0.0d);
      }
    }
  }
  
  public VariationFunc createVariationFunc(CompositeGene gene) {
    VariationFunc vfunc = VariationFuncList.getVariationFuncInstance(variations.get((int) gene.geneAt(0).getAllele()));
    if( vfunc != null) {
      for(int k = 1; k <= 20; k++) {
        try {
          vfunc.setParameter(vfunc.getParameterNames()[k-1], (double) gene.geneAt(k).getAllele());
        } catch (Exception ex) {
          // none
        }
      }
      return vfunc;
    }
    return null;
  }
  
  public CompositeGene constructVariationSampleGene() throws InvalidConfigurationException {
    CompositeGene gene = new CompositeGene(conf);
    gene.addGene(new IntegerGene(conf)); // priority
    gene.addGene(new DoubleGene(conf)); // amount
    gene.addGene(constructVariationFuncSampleGene()); // func
    return gene;
  }

  public void fillVariationGene(CompositeGene gene, Variation val) {
    if(val != null) {
      gene.geneAt(0).setAllele(val.getPriority());
      gene.geneAt(1).setAllele(val.getAmount());
      fillVariationFuncGene((CompositeGene) gene.geneAt(2), val.getFunc());
    } else {
      gene.geneAt(0).setAllele(0);
      gene.geneAt(1).setAllele(0.0d);
      fillVariationFuncGene((CompositeGene) gene.geneAt(2), new LineFunc());
    }
  }
  
  public Variation createVariation(CompositeGene gene) {
    Variation var = null;
    int prio = (int) gene.geneAt(0).getAllele();
    double amo = (double) gene.geneAt(1).getAllele();
    if(prio != 0 || amo != 0.0) {
      var = new Variation();
      var.setPriority(prio);
      var.setAmount(amo);
      var.setFunc(createVariationFunc((CompositeGene) gene.geneAt(2)));
    }
    return var;
  }
  
  public CompositeGene constructModWeightSampleGene() throws InvalidConfigurationException {
    CompositeGene gene = new CompositeGene(conf);
    for(int k = 0; k <= Constants.MAX_MOD_WEIGHT_COUNT-1; k++) {
      gene.addGene(new DoubleGene(conf)); //modWeight
    }
    return gene;
  }
  
  public void fillModWeightGene(CompositeGene gene, double[] modWeights) {
    for(int k = 0; k <= Constants.MAX_MOD_WEIGHT_COUNT-1; k++) {
      gene.geneAt(k).setAllele(modWeights[k]);
    }
  }
  
  public void setModWights(CompositeGene gene, double[] modWights) {
    for(int k = 0; k <= Constants.MAX_MOD_WEIGHT_COUNT-1; k++) {
      modWights[k] = (double) gene.geneAt(k).getAllele();
    }
  }
  
  public CompositeGene constructRGBColorSampleGene() throws InvalidConfigurationException {
    CompositeGene gene = new CompositeGene(conf);
    gene.addGene(new IntegerGene(conf)); // red
    gene.addGene(new IntegerGene(conf)); // green
    gene.addGene(new IntegerGene(conf)); // blue
    return gene;
  }
  
  public void fillRGBColorGene(CompositeGene gene, RGBColor val) {
    gene.geneAt(0).setAllele(val.getRed());
    gene.geneAt(1).setAllele(val.getGreen());
    gene.geneAt(2).setAllele(val.getBlue());
  }
  
  public RGBColor createRGBColor(CompositeGene gene) {
    return new RGBColor(
      (int) gene.geneAt(0).getAllele(), 
      (int) gene.geneAt(1).getAllele(), 
      (int) gene.geneAt(2).getAllele()
    );
  }
  
  public CompositeGene constructRGBPaletteSampleGene() throws InvalidConfigurationException {
    CompositeGene gene = new CompositeGene(conf);
    gene.addGene(new IntegerGene(conf)); // modRed
    gene.addGene(new IntegerGene(conf)); // modGreen
    gene.addGene(new IntegerGene(conf)); // modBlue
    gene.addGene(new IntegerGene(conf)); // modShift
    gene.addGene(new IntegerGene(conf)); // modHue
    gene.addGene(new IntegerGene(conf)); // modContrast
    gene.addGene(new IntegerGene(conf)); // modGamma
    gene.addGene(new IntegerGene(conf)); // modBrightness
    gene.addGene(new IntegerGene(conf)); // modSaturation
    gene.addGene(new IntegerGene(conf)); // modSwapRGB
    gene.addGene(new IntegerGene(conf)); // modFrequency
    gene.addGene(new IntegerGene(conf)); // modBlur
    for(int k = 0; k <= 255; k++) {
      gene.addGene(new BooleanGene(conf)); // on/off
      gene.addGene(constructRGBColorSampleGene()); // rawColors
    }
    return gene;
  }
  
  public void fillRGBPaletteGene(CompositeGene gene, RGBPalette val) {
    gene.geneAt(0).setAllele(val.getModRed());
    gene.geneAt(1).setAllele(val.getModGreen());
    gene.geneAt(2).setAllele(val.getModBlue());
    gene.geneAt(3).setAllele(val.getModShift());
    gene.geneAt(4).setAllele(val.getModHue());
    gene.geneAt(5).setAllele(val.getModContrast());
    gene.geneAt(6).setAllele(val.getModGamma());
    gene.geneAt(7).setAllele(val.getModBrightness());
    gene.geneAt(8).setAllele(val.getModSaturation());
    gene.geneAt(9).setAllele(val.getModSwapRGB());
    gene.geneAt(10).setAllele(val.getModFrequency());
    gene.geneAt(11).setAllele(val.getModBlur());
    for(int k = 0; k <= 255; k++) {
      RGBColor col = null;
      try {
        col = val.getRawColor(k);
      } catch (Exception ex) {
        col = null;
      }
      gene.geneAt(12+(k*2)).setAllele(col != null);
      fillRGBColorGene((CompositeGene) gene.geneAt(12+(k*2)+1), col != null ? col : new RGBColor());
    }
  }
  
  public RGBPalette createRGBPallete(CompositeGene gene) {
    RGBPalette pal = new RGBPalette();
    pal.setModRed((int) gene.geneAt(0).getAllele());
    pal.setModGreen((int) gene.geneAt(1).getAllele());
    pal.setModBlue((int) gene.geneAt(2).getAllele());
    pal.setModShift((int) gene.geneAt(3).getAllele());
    pal.setModHue((int) gene.geneAt(4).getAllele());
    pal.setModContrast((int) gene.geneAt(5).getAllele());
    pal.setModGamma((int) gene.geneAt(6).getAllele());
    pal.setModBrightness((int) gene.geneAt(7).getAllele());
    pal.setModSaturation((int) gene.geneAt(8).getAllele());
    pal.setModSwapRGB((int) gene.geneAt(9).getAllele());
    pal.setModFrequency((int) gene.geneAt(10).getAllele());
    pal.setModBlur((int) gene.geneAt(11).getAllele());
    for(int k = 0; k <= 255; k++) {
      if((boolean) gene.geneAt(12+(k*2)).getAllele()) {
        RGBColor col = createRGBColor((CompositeGene) gene.geneAt(12+(k*2)+1));
        pal.addColor(col.getRed(), col.getGreen(), col.getBlue());
      }
    }
 
    return pal;    
  }
  
  public CompositeGene constructXFormSampleGene() throws InvalidConfigurationException {
    CompositeGene gene = new CompositeGene(conf);

    gene.addGene(new DoubleGene(conf)); // weight
    gene.addGene(new DoubleGene(conf)); // color
    gene.addGene(new DoubleGene(conf)); // colorSymmetry
    gene.addGene(new DoubleGene(conf)); // modGamma
    gene.addGene(new DoubleGene(conf)); // modGammaSpeed
    gene.addGene(new DoubleGene(conf)); // modContrast
    gene.addGene(new DoubleGene(conf)); // modContrastSpeed
    gene.addGene(new DoubleGene(conf)); // modSaturation
    gene.addGene(new DoubleGene(conf)); // modSaturationSpeed
    
    gene.addGene(new DoubleGene(conf)); // xyCoeff00
    gene.addGene(new DoubleGene(conf)); // xyCoeff01
    gene.addGene(new DoubleGene(conf)); // xyCoeff10
    gene.addGene(new DoubleGene(conf)); // xyCoeff11
    gene.addGene(new DoubleGene(conf)); // xyCoeff20
    gene.addGene(new DoubleGene(conf)); // xyCoeff21
    gene.addGene(new DoubleGene(conf)); // xyPostCoeff00
    gene.addGene(new DoubleGene(conf)); // xyPostCoeff01
    gene.addGene(new DoubleGene(conf)); // xyPostCoeff10
    gene.addGene(new DoubleGene(conf)); // xyPostCoeff11
    gene.addGene(new DoubleGene(conf)); // xyPostCoeff20
    gene.addGene(new DoubleGene(conf)); // xyPostCoeff21
    
    gene.addGene(new BooleanGene(conf)); // hasXYPostCoeffs
    gene.addGene(new BooleanGene(conf)); // hasXYCoeffs

    gene.addGene(new DoubleGene(conf)); // yzCoeff00
    gene.addGene(new DoubleGene(conf)); // yzCoeff01
    gene.addGene(new DoubleGene(conf)); // yzCoeff10
    gene.addGene(new DoubleGene(conf)); // yzCoeff11
    gene.addGene(new DoubleGene(conf)); // yzCoeff20
    gene.addGene(new DoubleGene(conf)); // yzCoeff21
    gene.addGene(new DoubleGene(conf)); // yzPostCoeff00
    gene.addGene(new DoubleGene(conf)); // yzPostCoeff01
    gene.addGene(new DoubleGene(conf)); // yzPostCoeff10
    gene.addGene(new DoubleGene(conf)); // yzPostCoeff11
    gene.addGene(new DoubleGene(conf)); // yzPostCoeff20
    gene.addGene(new DoubleGene(conf)); // yzPostCoeff21
    
    gene.addGene(new BooleanGene(conf)); // hasYZPostCoeffs
    gene.addGene(new BooleanGene(conf)); // hasYZCoeffs

    gene.addGene(new DoubleGene(conf)); // zxCoeff00
    gene.addGene(new DoubleGene(conf)); // zxCoeff01
    gene.addGene(new DoubleGene(conf)); // zxCoeff10
    gene.addGene(new DoubleGene(conf)); // zxCoeff11
    gene.addGene(new DoubleGene(conf)); // zxCoeff20
    gene.addGene(new DoubleGene(conf)); // zxCoeff21
    gene.addGene(new DoubleGene(conf)); // zxPostCoeff00
    gene.addGene(new DoubleGene(conf)); // zxPostCoeff01
    gene.addGene(new DoubleGene(conf)); // zxPostCoeff10
    gene.addGene(new DoubleGene(conf)); // zxPostCoeff11
    gene.addGene(new DoubleGene(conf)); // zxPostCoeff20
    gene.addGene(new DoubleGene(conf)); // zxPostCoeff21
    
    gene.addGene(new BooleanGene(conf)); // hasZXPostCoeffs
    gene.addGene(new BooleanGene(conf)); // hasZXCoeffs
    
    for(int k = 0; k <= 9; k++) {
      gene.addGene(new BooleanGene(conf)); // on/off
      gene.addGene(constructVariationSampleGene()); // variations
    }

    gene.addGene(constructModWeightSampleGene()); // modWeight
    
    return gene;
  
  }

  public void fillXFormGene(CompositeGene gene, XForm val) {
    
    if(val != null) {
      gene.geneAt(0).setAllele(val.getWeight());
      gene.geneAt(1).setAllele(val.getColor());
      gene.geneAt(2).setAllele(val.getColorSymmetry());
      gene.geneAt(3).setAllele(val.getModGamma());
      gene.geneAt(4).setAllele(val.getModGammaSpeed());
      gene.geneAt(5).setAllele(val.getModContrast());
      gene.geneAt(6).setAllele(val.getModContrastSpeed());
      gene.geneAt(7).setAllele(val.getModSaturation());
      gene.geneAt(8).setAllele(val.getModSaturationSpeed());
  
      gene.geneAt(9).setAllele(val.getXYCoeff00());
      gene.geneAt(10).setAllele(val.getXYCoeff01());
      gene.geneAt(11).setAllele(val.getXYCoeff10());
      gene.geneAt(12).setAllele(val.getXYCoeff11());
      gene.geneAt(13).setAllele(val.getXYCoeff20());
      gene.geneAt(14).setAllele(val.getXYCoeff21());
      gene.geneAt(15).setAllele(val.getXYPostCoeff00());
      gene.geneAt(16).setAllele(val.getXYPostCoeff01());
      gene.geneAt(17).setAllele(val.getXYPostCoeff10());
      gene.geneAt(18).setAllele(val.getXYPostCoeff11());
      gene.geneAt(19).setAllele(val.getXYPostCoeff20());
      gene.geneAt(20).setAllele(val.getXYPostCoeff21());
      gene.geneAt(21).setAllele(val.isHasXYPostCoeffs());
      gene.geneAt(22).setAllele(val.isHasXYCoeffs());

      gene.geneAt(23).setAllele(val.getYZCoeff00());
      gene.geneAt(24).setAllele(val.getYZCoeff01());
      gene.geneAt(25).setAllele(val.getYZCoeff10());
      gene.geneAt(26).setAllele(val.getYZCoeff11());
      gene.geneAt(27).setAllele(val.getYZCoeff20());
      gene.geneAt(28).setAllele(val.getYZCoeff21());
      gene.geneAt(29).setAllele(val.getYZPostCoeff00());
      gene.geneAt(30).setAllele(val.getYZPostCoeff01());
      gene.geneAt(31).setAllele(val.getYZPostCoeff10());
      gene.geneAt(32).setAllele(val.getYZPostCoeff11());
      gene.geneAt(33).setAllele(val.getYZPostCoeff20());
      gene.geneAt(34).setAllele(val.getYZPostCoeff21());
      gene.geneAt(35).setAllele(val.isHasYZPostCoeffs());
      gene.geneAt(36).setAllele(val.isHasYZCoeffs());

      gene.geneAt(37).setAllele(val.getZXCoeff00());
      gene.geneAt(38).setAllele(val.getZXCoeff01());
      gene.geneAt(39).setAllele(val.getZXCoeff10());
      gene.geneAt(40).setAllele(val.getZXCoeff11());
      gene.geneAt(41).setAllele(val.getZXCoeff20());
      gene.geneAt(42).setAllele(val.getZXCoeff21());
      gene.geneAt(43).setAllele(val.getZXPostCoeff00());
      gene.geneAt(44).setAllele(val.getZXPostCoeff01());
      gene.geneAt(45).setAllele(val.getZXPostCoeff10());
      gene.geneAt(46).setAllele(val.getZXPostCoeff11());
      gene.geneAt(47).setAllele(val.getZXPostCoeff20());
      gene.geneAt(48).setAllele(val.getZXPostCoeff21());
      gene.geneAt(49).setAllele(val.isHasZXPostCoeffs());
      gene.geneAt(50).setAllele(val.isHasZXCoeffs());
    
      for(int k = 0; k <= 9; k++) {
        Variation var = null;
        try {
          var = val.getVariation(k);
        } catch (Exception ex) {
          var = null;
        }
        gene.geneAt(51+(k*2)).setAllele(var != null);
        fillVariationGene((CompositeGene) gene.geneAt(51+(k*2)+1), var);
      }

      fillModWeightGene((CompositeGene) gene.geneAt(51+(9*2)+2), val.getModifiedWeights());
    
    } else {
      gene.setToRandomValue(new GaussianRandomGenerator());
    }
    
  }

  public XForm createXForm(CompositeGene gene) {
    
    XForm frm = new XForm();
    
    frm.setWeight((double) gene.geneAt(0).getAllele());
    frm.setColor((double) gene.geneAt(1).getAllele());
    frm.setColorSymmetry((double) gene.geneAt(2).getAllele());
    frm.setModGamma((double) gene.geneAt(3).getAllele());
    frm.setModGammaSpeed((double) gene.geneAt(4).getAllele());
    frm.setModContrast((double) gene.geneAt(5).getAllele());
    frm.setModContrastSpeed((double) gene.geneAt(6).getAllele());
    frm.setModSaturation((double) gene.geneAt(7).getAllele());
    frm.setModSaturationSpeed((double) gene.geneAt(8).getAllele());

    frm.setXYCoeff00((double) gene.geneAt(9).getAllele());
    frm.setXYCoeff01((double) gene.geneAt(10).getAllele());
    frm.setXYCoeff10((double) gene.geneAt(11).getAllele());
    frm.setXYCoeff11((double) gene.geneAt(12).getAllele());
    frm.setXYCoeff20((double) gene.geneAt(13).getAllele());
    frm.setXYCoeff21((double) gene.geneAt(14).getAllele());
    frm.setXYPostCoeff00((double) gene.geneAt(15).getAllele());
    frm.setXYPostCoeff01((double) gene.geneAt(16).getAllele());
    frm.setXYPostCoeff10((double) gene.geneAt(17).getAllele());
    frm.setXYPostCoeff11((double) gene.geneAt(18).getAllele());
    frm.setXYPostCoeff20((double) gene.geneAt(19).getAllele());
    frm.setXYPostCoeff21((double) gene.geneAt(20).getAllele());
    //21
    //22

    frm.setYZCoeff00((double) gene.geneAt(23).getAllele());
    frm.setYZCoeff01((double) gene.geneAt(24).getAllele());
    frm.setYZCoeff10((double) gene.geneAt(25).getAllele());
    frm.setYZCoeff11((double) gene.geneAt(26).getAllele());
    frm.setYZCoeff20((double) gene.geneAt(27).getAllele());
    frm.setYZCoeff21((double) gene.geneAt(28).getAllele());
    frm.setYZPostCoeff00((double) gene.geneAt(29).getAllele());
    frm.setYZPostCoeff01((double) gene.geneAt(30).getAllele());
    frm.setYZPostCoeff10((double) gene.geneAt(31).getAllele());
    frm.setYZPostCoeff11((double) gene.geneAt(32).getAllele());
    frm.setYZPostCoeff20((double) gene.geneAt(33).getAllele());
    frm.setYZPostCoeff21((double) gene.geneAt(34).getAllele());
    //35
    //36

    frm.setZXCoeff00((double) gene.geneAt(37).getAllele());
    frm.setZXCoeff01((double) gene.geneAt(38).getAllele());
    frm.setZXCoeff10((double) gene.geneAt(39).getAllele());
    frm.setZXCoeff11((double) gene.geneAt(40).getAllele());
    frm.setZXCoeff20((double) gene.geneAt(41).getAllele());
    frm.setZXCoeff21((double) gene.geneAt(42).getAllele());
    frm.setZXPostCoeff00((double) gene.geneAt(43).getAllele());
    frm.setZXPostCoeff01((double) gene.geneAt(44).getAllele());
    frm.setZXPostCoeff10((double) gene.geneAt(45).getAllele());
    frm.setZXPostCoeff11((double) gene.geneAt(46).getAllele());
    frm.setZXPostCoeff20((double) gene.geneAt(47).getAllele());
    frm.setZXPostCoeff21((double) gene.geneAt(48).getAllele());
    //49
    //50

    for(int k = 0; k <= 9; k++) {
      if((boolean) gene.geneAt(51+(k*2)).getAllele()) {
        Variation var = createVariation((CompositeGene) gene.geneAt(51+(k*2)+1));
        if(var != null) {
          frm.addVariation(var);
        }
      }
    }

    setModWights((CompositeGene) gene.geneAt(51+(9*2)+2), frm.getModifiedWeights());

    return frm;
    
  }
  
  public CompositeGene constructCameraSampleChromosome() throws InvalidConfigurationException {

    CompositeGene gene = new CompositeGene(conf);
    gene.addGene(new DoubleGene(conf)); // camPitch;
    gene.addGene(new DoubleGene(conf)); // camYaw
    gene.addGene(new DoubleGene(conf)); // camPerspective
    gene.addGene(new DoubleGene(conf)); // camRoll
    gene.addGene(new DoubleGene(conf)); // camZoom
    gene.addGene(new DoubleGene(conf)); // camPosX
    gene.addGene(new DoubleGene(conf)); // camPosY
    gene.addGene(new DoubleGene(conf)); // camPosZ
    gene.addGene(new DoubleGene(conf)); // camZ
    gene.addGene(new DoubleGene(conf)); // focusX
    gene.addGene(new DoubleGene(conf)); // focusY
    gene.addGene(new DoubleGene(conf)); // focusZ
    gene.addGene(new DoubleGene(conf)); // dimishZ
    gene.addGene(new DoubleGene(conf)); // camDOF
    gene.addGene(new IntegerGene(conf, 0, 11)); // camDOFShape;
    gene.addGene(new DoubleGene(conf)); // camDOFScale
    gene.addGene(new DoubleGene(conf)); // camDOFAngle
    gene.addGene(new DoubleGene(conf)); // camDOFFade
    gene.addGene(new DoubleGene(conf)); // camDOFParam1
    gene.addGene(new DoubleGene(conf)); // camDOFParam2
    gene.addGene(new DoubleGene(conf)); // camDOFParam3
    gene.addGene(new DoubleGene(conf)); // camDOFParam4
    gene.addGene(new DoubleGene(conf)); // camDOFParam5
    gene.addGene(new DoubleGene(conf)); // camDOFParam6
    gene.addGene(new DoubleGene(conf)); // camDOFExponent
    gene.addGene(new DoubleGene(conf)); // camDOFArea
    gene.addGene(new BooleanGene(conf)); // newCamDOF
    gene.addGene(new DoubleGene(conf)); // camZoom
    gene.addGene(new DoubleGene(conf)); // PixelsPerUnit
    gene.addGene(new IntegerGene(conf)); // width
    gene.addGene(new IntegerGene(conf)); // height
    gene.addGene(new DoubleGene(conf)); // camPosX
    gene.addGene(new DoubleGene(conf)); // camPosY
    gene.addGene(new DoubleGene(conf)); // camPosZ
    
    return gene;

  }
  
  public void fillCameraGene(CompositeGene gene, Flame val) {
    
    gene.geneAt(0).setAllele(val.getCamPitch());
    gene.geneAt(1).setAllele(val.getCamYaw());
    gene.geneAt(2).setAllele(val.getCamPerspective());
    gene.geneAt(3).setAllele(val.getCamRoll());
    gene.geneAt(4).setAllele(val.getCamZoom());
    gene.geneAt(5).setAllele(val.getCamPosX());
    gene.geneAt(6).setAllele(val.getCamPosY());
    gene.geneAt(7).setAllele(val.getCamPosZ());
    gene.geneAt(8).setAllele(val.getCamZ());
    gene.geneAt(9).setAllele(val.getFocusX());
    gene.geneAt(10).setAllele(val.getFocusY());
    gene.geneAt(11).setAllele(val.getFocusZ());
    gene.geneAt(12).setAllele(val.getDimishZ());
    gene.geneAt(13).setAllele(val.getCamDOF());
    gene.geneAt(14).setAllele(val.getCamDOFShape().ordinal());
    gene.geneAt(15).setAllele(val.getCamDOFScale());
    gene.geneAt(16).setAllele(val.getCamDOFAngle());
    gene.geneAt(17).setAllele(val.getCamDOFFade());
    gene.geneAt(18).setAllele(val.getCamDOFParam1());
    gene.geneAt(19).setAllele(val.getCamDOFParam2());
    gene.geneAt(20).setAllele(val.getCamDOFParam3());
    gene.geneAt(21).setAllele(val.getCamDOFParam4());
    gene.geneAt(22).setAllele(val.getCamDOFParam5());
    gene.geneAt(23).setAllele(val.getCamDOFParam6());
    gene.geneAt(24).setAllele(val.getCamDOFExponent());
    gene.geneAt(25).setAllele(val.getCamDOFArea());
    gene.geneAt(26).setAllele(val.isNewCamDOF());
    gene.geneAt(27).setAllele(val.getCamZoom());
    gene.geneAt(28).setAllele(val.getPixelsPerUnit());
    gene.geneAt(29).setAllele(val.getWidth());
    gene.geneAt(30).setAllele(val.getHeight());
    gene.geneAt(31).setAllele(val.getCamPosX());
    gene.geneAt(32).setAllele(val.getCamPosY());
    gene.geneAt(33).setAllele(val.getCamPosZ());

  }

  public void setCamera(CompositeGene gene, Flame fl) {

    fl.setCamPitch((double) gene.geneAt(0).getAllele());
    fl.setCamYaw((double) gene.geneAt(1).getAllele());
    fl.setCamPerspective((double) gene.geneAt(2).getAllele());
    fl.setCamRoll((double) gene.geneAt(3).getAllele());
    fl.setCamZoom((double) gene.geneAt(4).getAllele());
    fl.setCamZoom((double) gene.geneAt(5).getAllele());
    fl.setCamPosY((double) gene.geneAt(6).getAllele());
    fl.setCamPosZ((double) gene.geneAt(7).getAllele());
    fl.setCamZ((double) gene.geneAt(8).getAllele());
    fl.setFocusX((double) gene.geneAt(9).getAllele());
    fl.setFocusY((double) gene.geneAt(10).getAllele());
    fl.setFocusZ((double) gene.geneAt(11).getAllele());
    fl.setDimishZ((double) gene.geneAt(12).getAllele());
    fl.setCamDOF((double) gene.geneAt(13).getAllele());
    fl.setCamDOFShape(DOFBlurShapeType.values()[(int) gene.geneAt(14).getAllele()]);
    fl.setCamDOFScale((double) gene.geneAt(15).getAllele());
    fl.setCamDOFAngle((double) gene.geneAt(16).getAllele());
    fl.setCamDOFFade((double) gene.geneAt(17).getAllele());
    fl.setCamDOFParam1((double) gene.geneAt(18).getAllele());
    fl.setCamDOFParam2((double) gene.geneAt(19).getAllele());
    fl.setCamDOFParam3((double) gene.geneAt(20).getAllele());
    fl.setCamDOFParam4((double) gene.geneAt(21).getAllele());
    fl.setCamDOFParam5((double) gene.geneAt(22).getAllele());
    fl.setCamDOFParam6((double) gene.geneAt(23).getAllele());
    fl.setCamDOFExponent((double) gene.geneAt(24).getAllele());
    fl.setCamDOFArea((double) gene.geneAt(25).getAllele());
    fl.setNewCamDOF((boolean) gene.geneAt(26).getAllele());
    fl.setCamZoom((double) gene.geneAt(27).getAllele());
    fl.setPixelsPerUnit((double) gene.geneAt(28).getAllele());
    fl.setWidth((int) gene.geneAt(29).getAllele());
    fl.setHeight((int) gene.geneAt(30).getAllele());
    fl.setCamPosX((double) gene.geneAt(31).getAllele());
    fl.setCamPosY((double) gene.geneAt(32).getAllele());
    fl.setCamPosZ((double) gene.geneAt(33).getAllele());
    
  }
  
  public Chromosome constructSampleChromosome() throws InvalidConfigurationException {
    Gene[] genes = new Gene[82];
    for(int k = 0; k <= 19; k++) {
      genes[(k*2)] = new BooleanGene(conf); // on/off
      genes[(k*2)+1] = constructXFormSampleGene(); //XForms
    }
    for(int k = 20; k <= 39; k++) {
      genes[(k*2)] = new BooleanGene(conf); // on/off
      genes[(k*2)+1] = constructXFormSampleGene(); //final XForms
    }
    genes[(39*2)+2] = constructRGBPaletteSampleGene();
    genes[(39*2)+3] = constructCameraSampleChromosome();
    Chromosome sample = new Chromosome(conf, genes);
    return sample;
  }

  public Chromosome createChromosome(Flame flame) throws InvalidConfigurationException {
    
    Gene[] genes = new Gene[82];
    for(int k = 0; k <= 19; k++) {
      XForm frm = null;
      try {
        frm = flame.getLayers().get(0).getXForms().get(k);
      } catch (Exception ex) {
        frm = null;
      }
      genes[(k*2)] = new BooleanGene(conf, frm != null);
      genes[(k*2)+1] = constructXFormSampleGene();
      fillXFormGene((CompositeGene) genes[(k*2)+1], frm);
    }
    for(int k = 20; k <= 39; k++) {
      XForm frm = null;
      try {
        frm = flame.getLayers().get(0).getFinalXForms().get(k-20);
      } catch (Exception ex) {
        frm = null;
      }
      genes[(k*2)] = new BooleanGene(conf, frm != null);
      genes[(k*2)+1] = constructXFormSampleGene();
      fillXFormGene((CompositeGene) genes[(k*2)+1], frm);
    }
    genes[(39*2)+2] = constructRGBPaletteSampleGene();
    fillRGBPaletteGene((CompositeGene) genes[(39*2)+2], flame.getLayers().get(0).getPalette());
    genes[(39*2)+3] = constructCameraSampleChromosome();
    fillCameraGene((CompositeGene) genes[(39*2)+3], flame);
    
    return new Chromosome(conf, genes);
    
  }
  
  public Flame createFlame(IChromosome chr) {
    Flame fl = new Flame();
    Gene[] genes = chr.getGenes();

    for(int k = 0; k <= 19; k++) {
      if((boolean) genes[(k*2)].getAllele()) {
        XForm frm = createXForm((CompositeGene) genes[(k*2)+1]);
        fl.getLayers().get(0).getXForms().add(frm);
      }
    }
    for(int k = 20; k <= 39; k++) {
      if((boolean) genes[(k*2)].getAllele()) {
        XForm frm = createXForm((CompositeGene) genes[(k*2)+1]);
        fl.getLayers().get(0).getFinalXForms().add(frm);
      }
    }
    fl.getLayers().get(0).setPalette(createRGBPallete((CompositeGene) genes[(39*2)+2]));
    setCamera((CompositeGene) genes[(39*2)+3], fl);
    
    return fl;
    
  }
  
}
