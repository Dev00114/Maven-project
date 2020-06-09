//    uniCenta oPOS  - Touch Friendly Point Of Sale
//    Copyright (c) 2009-2018 uniCenta & previous Openbravo POS works
//    https://unicenta.com
//
//    This file is part of uniCenta oPOS
//
//    uniCenta oPOS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//   uniCenta oPOS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with uniCenta oPOS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.beans;

import java.awt.ComponentOrientation;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author JG uniCenta
 */
public class JNumberKeys extends javax.swing.JPanel {

    private Vector m_Listeners = new Vector();
    
    private boolean minusenabled = true;
    private boolean equalsenabled = true;
    
    /** Creates new form JNumberKeys */
    public JNumberKeys() {
        initComponents ();
        
        m_jKey0.addActionListener(new MyKeyNumberListener('0'));
        m_jKey1.addActionListener(new MyKeyNumberListener('1'));
        m_jKey2.addActionListener(new MyKeyNumberListener('2'));
        m_jKey3.addActionListener(new MyKeyNumberListener('3'));
        m_jKey4.addActionListener(new MyKeyNumberListener('4'));
        m_jKey5.addActionListener(new MyKeyNumberListener('5'));
        m_jKey6.addActionListener(new MyKeyNumberListener('6'));
        m_jKey7.addActionListener(new MyKeyNumberListener('7'));
        m_jKey8.addActionListener(new MyKeyNumberListener('8'));
        m_jKey9.addActionListener(new MyKeyNumberListener('9'));
        m_jKeyDot.addActionListener(new MyKeyNumberListener('.'));
        m_jMultiply.addActionListener(new MyKeyNumberListener('*'));
        m_jCE.addActionListener(new MyKeyNumberListener('\u007f'));
        m_jPlus.addActionListener(new MyKeyNumberListener('+'));        
        m_jMinus.addActionListener(new MyKeyNumberListener('-'));        
        m_jEquals.addActionListener(new MyKeyNumberListener('='));
    }

    /**
     *
     * @param value
     */
    public void setNumbersOnly(boolean value) {
        m_jEquals.setVisible(value);
        m_jMinus.setVisible(value);
        m_jPlus.setVisible(value);
        m_jMultiply.setVisible(value);
    }
    
    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        
        m_jKey0.setEnabled(b);
        m_jKey1.setEnabled(b);
        m_jKey2.setEnabled(b);
        m_jKey3.setEnabled(b);
        m_jKey4.setEnabled(b);
        m_jKey5.setEnabled(b);
        m_jKey6.setEnabled(b);
        m_jKey7.setEnabled(b);
        m_jKey8.setEnabled(b);
        m_jKey9.setEnabled(b);
        m_jKeyDot.setEnabled(b);
        m_jMultiply.setEnabled(b);
        m_jCE.setEnabled(b);
        m_jPlus.setEnabled(b);       
        m_jMinus.setEnabled(minusenabled && b);
        m_jEquals.setEnabled(equalsenabled && b);   
    }
    
    @Override
    public void setComponentOrientation(ComponentOrientation o) {
        // Nothing to change
    }
    
    /**
     *
     * @param b
     */
    public void setMinusEnabled(boolean b) {
        minusenabled = b;
        m_jMinus.setEnabled(minusenabled && isEnabled());
    }
    
    /**
     *
     * @return
     */
    public boolean isMinusEnabled() {
        return minusenabled;
    }
    
    /**
     *
     * @param b
     */
    public void setEqualsEnabled(boolean b) {
        equalsenabled = b;
        m_jEquals.setEnabled(equalsenabled && isEnabled());
    }
    
    /**
     *
     * @return
     */
    public boolean isEqualsEnabled() {
        return equalsenabled;
    }
    
    /**
     *
     * @param enabled
     */
    public void dotIs00(boolean enabled) {
        if (enabled) {
            m_jKeyDot.setIcon(new javax.swing.ImageIcon(getClass()
                    .getResource("/com/openbravo/images/btn00.png")));
        }
    }
    
    /**
     *
     * @return
     */
    public boolean isNumbersOnly() {
        return m_jEquals.isVisible();
    }
    
    /**
     *
     * @param listener
     */
    public void addJNumberEventListener(JNumberEventListener listener) {
        m_Listeners.add(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeJNumberEventListener(JNumberEventListener listener) {
        m_Listeners.remove(listener);
    }
    
    private class MyKeyNumberListener implements java.awt.event.ActionListener {
        
        private final char m_cCad;
        
        public MyKeyNumberListener(char cCad){
            m_cCad = cCad;
        }
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
           
            JNumberEvent oEv = new JNumberEvent(JNumberKeys.this, m_cCad);            
            JNumberEventListener oListener;
            
            for (Enumeration e = m_Listeners.elements(); e.hasMoreElements();) {
                oListener = (JNumberEventListener) e.nextElement();
                oListener.keyPerformed(oEv);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        m_jCE = new com.openbravo.beans.JCalcButton();
        m_jMultiply = new com.openbravo.beans.JCalcButton();
        m_jMinus = new com.openbravo.beans.JCalcButton();
        m_jPlus = new com.openbravo.beans.JCalcButton();
        m_jKey9 = new com.openbravo.beans.JCalcButton();
        m_jKey8 = new com.openbravo.beans.JCalcButton();
        m_jKey7 = new com.openbravo.beans.JCalcButton();
        m_jKey4 = new com.openbravo.beans.JCalcButton();
        m_jKey5 = new com.openbravo.beans.JCalcButton();
        m_jKey6 = new com.openbravo.beans.JCalcButton();
        m_jKey3 = new com.openbravo.beans.JCalcButton();
        m_jKey2 = new com.openbravo.beans.JCalcButton();
        m_jKey1 = new com.openbravo.beans.JCalcButton();
        m_jKey0 = new com.openbravo.beans.JCalcButton();
        m_jKeyDot = new com.openbravo.beans.JCalcButton();
        m_jEquals = new com.openbravo.beans.JCalcButton();

        setBackground(new java.awt.Color(30, 30, 30));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setMinimumSize(new java.awt.Dimension(193, 200));
        setPreferredSize(new java.awt.Dimension(193, 200));
        setLayout(new java.awt.GridBagLayout());

        m_jCE.setFocusPainted(false);
        m_jCE.setFocusable(false);
        m_jCE.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/nce_a.png"))); // NOI18N
        m_jCE.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/nce_h.png"))); // NOI18N
        m_jCE.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/nce_n.png"))); // NOI18N
        m_jCE.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jCE.setMaximumSize(new java.awt.Dimension(66, 36));
        m_jCE.setMinimumSize(new java.awt.Dimension(66, 36));
        m_jCE.setPreferredSize(new java.awt.Dimension(66, 36));
        m_jCE.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(m_jCE, gridBagConstraints);

        m_jMultiply.setFocusPainted(false);
        m_jMultiply.setFocusable(false);
        m_jMultiply.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/nmul_a.png"))); // NOI18N
        m_jMultiply.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/nmul_h.png"))); // NOI18N
        m_jMultiply.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/nmul_n.png"))); // NOI18N
        m_jMultiply.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jMultiply.setMaximumSize(new java.awt.Dimension(42, 36));
        m_jMultiply.setMinimumSize(new java.awt.Dimension(42, 36));
        m_jMultiply.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jMultiply.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(m_jMultiply, gridBagConstraints);

        m_jMinus.setFocusPainted(false);
        m_jMinus.setFocusable(false);
        m_jMinus.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n-_a.png"))); // NOI18N
        m_jMinus.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n-_h.png"))); // NOI18N
        m_jMinus.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n-_n.png"))); // NOI18N
        m_jMinus.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jMinus.setMaximumSize(new java.awt.Dimension(42, 36));
        m_jMinus.setMinimumSize(new java.awt.Dimension(42, 36));
        m_jMinus.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jMinus.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        add(m_jMinus, gridBagConstraints);

        m_jPlus.setFocusPainted(false);
        m_jPlus.setFocusable(false);
        m_jPlus.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n+_a.png"))); // NOI18N
        m_jPlus.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n+_h.png"))); // NOI18N
        m_jPlus.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n+_n.png"))); // NOI18N
        m_jPlus.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jPlus.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jPlus.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(m_jPlus, gridBagConstraints);

        m_jKey9.setFocusPainted(false);
        m_jKey9.setFocusable(false);
        m_jKey9.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n9_a.png"))); // NOI18N
        m_jKey9.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n9_h.png"))); // NOI18N
        m_jKey9.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n9_n.png"))); // NOI18N
        m_jKey9.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey9.setMaximumSize(new java.awt.Dimension(42, 36));
        m_jKey9.setMinimumSize(new java.awt.Dimension(42, 36));
        m_jKey9.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jKey9.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey9, gridBagConstraints);

        m_jKey8.setFocusPainted(false);
        m_jKey8.setFocusable(false);
        m_jKey8.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n8_a.png"))); // NOI18N
        m_jKey8.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n8_h.png"))); // NOI18N
        m_jKey8.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n8_n.png"))); // NOI18N
        m_jKey8.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey8.setMaximumSize(new java.awt.Dimension(42, 36));
        m_jKey8.setMinimumSize(new java.awt.Dimension(42, 36));
        m_jKey8.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jKey8.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey8, gridBagConstraints);

        m_jKey7.setFocusPainted(false);
        m_jKey7.setFocusable(false);
        m_jKey7.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n7_a.png"))); // NOI18N
        m_jKey7.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n7_h.png"))); // NOI18N
        m_jKey7.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n7_n.png"))); // NOI18N
        m_jKey7.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey7.setMaximumSize(new java.awt.Dimension(42, 36));
        m_jKey7.setMinimumSize(new java.awt.Dimension(42, 36));
        m_jKey7.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jKey7.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey7, gridBagConstraints);

        m_jKey4.setFocusPainted(false);
        m_jKey4.setFocusable(false);
        m_jKey4.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n4_a.png"))); // NOI18N
        m_jKey4.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n4_h.png"))); // NOI18N
        m_jKey4.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n4_n.png"))); // NOI18N
        m_jKey4.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey4.setMaximumSize(new java.awt.Dimension(42, 36));
        m_jKey4.setMinimumSize(new java.awt.Dimension(42, 36));
        m_jKey4.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jKey4.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey4, gridBagConstraints);

        m_jKey5.setFocusPainted(false);
        m_jKey5.setFocusable(false);
        m_jKey5.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n5_a.png"))); // NOI18N
        m_jKey5.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n5_h.png"))); // NOI18N
        m_jKey5.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n5_n.png"))); // NOI18N
        m_jKey5.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey5.setMaximumSize(new java.awt.Dimension(42, 36));
        m_jKey5.setMinimumSize(new java.awt.Dimension(42, 36));
        m_jKey5.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jKey5.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey5, gridBagConstraints);

        m_jKey6.setFocusPainted(false);
        m_jKey6.setFocusable(false);
        m_jKey6.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n6_a.png"))); // NOI18N
        m_jKey6.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n6_h.png"))); // NOI18N
        m_jKey6.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n6_n.png"))); // NOI18N
        m_jKey6.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey6.setMaximumSize(new java.awt.Dimension(42, 36));
        m_jKey6.setMinimumSize(new java.awt.Dimension(42, 36));
        m_jKey6.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jKey6.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey6, gridBagConstraints);

        m_jKey3.setFocusPainted(false);
        m_jKey3.setFocusable(false);
        m_jKey3.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n3_a.png"))); // NOI18N
        m_jKey3.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n3_h.png"))); // NOI18N
        m_jKey3.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n3_n.png"))); // NOI18N
        m_jKey3.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey3.setMaximumSize(new java.awt.Dimension(42, 36));
        m_jKey3.setMinimumSize(new java.awt.Dimension(42, 36));
        m_jKey3.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jKey3.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey3, gridBagConstraints);

        m_jKey2.setFocusPainted(false);
        m_jKey2.setFocusable(false);
        m_jKey2.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n2_a.png"))); // NOI18N
        m_jKey2.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n2_h.png"))); // NOI18N
        m_jKey2.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n2_n.png"))); // NOI18N
        m_jKey2.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey2.setMaximumSize(new java.awt.Dimension(42, 36));
        m_jKey2.setMinimumSize(new java.awt.Dimension(42, 36));
        m_jKey2.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jKey2.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey2, gridBagConstraints);

        m_jKey1.setFocusPainted(false);
        m_jKey1.setFocusable(false);
        m_jKey1.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n1_a.png"))); // NOI18N
        m_jKey1.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n1_h.png"))); // NOI18N
        m_jKey1.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n1_n.png"))); // NOI18N
        m_jKey1.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey1.setMaximumSize(new java.awt.Dimension(42, 36));
        m_jKey1.setMinimumSize(new java.awt.Dimension(42, 36));
        m_jKey1.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jKey1.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey1, gridBagConstraints);

        m_jKey0.setFocusPainted(false);
        m_jKey0.setFocusable(false);
        m_jKey0.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n0_a.png"))); // NOI18N
        m_jKey0.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n0_h.png"))); // NOI18N
        m_jKey0.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n0_n.png"))); // NOI18N
        m_jKey0.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey0.setMaximumSize(new java.awt.Dimension(42, 36));
        m_jKey0.setMinimumSize(new java.awt.Dimension(42, 36));
        m_jKey0.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jKey0.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(m_jKey0, gridBagConstraints);

        m_jKeyDot.setFocusPainted(false);
        m_jKeyDot.setFocusable(false);
        m_jKeyDot.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/ndot_a.png"))); // NOI18N
        m_jKeyDot.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/ndot_h.png"))); // NOI18N
        m_jKeyDot.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/ndot_n.png"))); // NOI18N
        m_jKeyDot.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKeyDot.setMaximumSize(new java.awt.Dimension(42, 36));
        m_jKeyDot.setMinimumSize(new java.awt.Dimension(42, 36));
        m_jKeyDot.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jKeyDot.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(m_jKeyDot, gridBagConstraints);

        m_jEquals.setFocusPainted(false);
        m_jEquals.setFocusable(false);
        m_jEquals.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n=_a.png"))); // NOI18N
        m_jEquals.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n=_h.png"))); // NOI18N
        m_jEquals.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n=_n.png"))); // NOI18N
        m_jEquals.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jEquals.setPreferredSize(new java.awt.Dimension(42, 36));
        m_jEquals.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(m_jEquals, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.openbravo.beans.JCalcButton m_jCE;
    private com.openbravo.beans.JCalcButton m_jEquals;
    private com.openbravo.beans.JCalcButton m_jKey0;
    private com.openbravo.beans.JCalcButton m_jKey1;
    private com.openbravo.beans.JCalcButton m_jKey2;
    private com.openbravo.beans.JCalcButton m_jKey3;
    private com.openbravo.beans.JCalcButton m_jKey4;
    private com.openbravo.beans.JCalcButton m_jKey5;
    private com.openbravo.beans.JCalcButton m_jKey6;
    private com.openbravo.beans.JCalcButton m_jKey7;
    private com.openbravo.beans.JCalcButton m_jKey8;
    private com.openbravo.beans.JCalcButton m_jKey9;
    private com.openbravo.beans.JCalcButton m_jKeyDot;
    private com.openbravo.beans.JCalcButton m_jMinus;
    private com.openbravo.beans.JCalcButton m_jMultiply;
    private com.openbravo.beans.JCalcButton m_jPlus;
    // End of variables declaration//GEN-END:variables

}
