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

package com.openbravo.editor;

import com.openbravo.pos.customers.JCustomerFinder;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;
import javax.swing.event.EventListenerList;

/**
 *
 * @author JG uniCenta
 */
public class JEditorKeys extends javax.swing.JPanel implements EditorKeys {
    
    private final static char[] SET_DOUBLE = {'\u007f', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-'};
    private final static char[] SET_DOUBLE_POSITIVE = {'\u007f', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};
    private final static char[] SET_INTEGER = {'\u007f', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-'};
    private final static char[] SET_INTEGER_POSITIVE = {'\u007f', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    
    /**
     *
     */
    protected EventListenerList listeners = new EventListenerList();
    
    private EditorComponent editorcurrent ;
    
    private char[] keysavailable;    
    private boolean m_bMinus;
    private boolean m_bKeyDot;
    private JCustomerFinder customerFinder;
       
    /*
    HS KeyShortcuts
     */

    /**
     *
     * @return
     */
    
    public JCustomerFinder getCustomerFinder() {
        return customerFinder;
    }

    /**
     *
     * @param customerFinder
     */
    public void setCustomerFinder(JCustomerFinder customerFinder) {
        this.customerFinder = customerFinder;
    }
       
    
    /** Creates new form JEditorKeys */
    public JEditorKeys() {
        initComponents();
        
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
        m_jCE.addActionListener(new MyKeyNumberListener('\u007f'));
        m_jMinus.addActionListener(new MyKeyNumberListener('-'));     
        
        editorcurrent = null;
        setMode(MODE_STRING);
        doEnabled(false);
    }
    
    @Override
    public void setComponentOrientation(ComponentOrientation o) {
        // Nothing to change
    }
    
    /**
     *
     * @param l
     */
    public void addActionListener(ActionListener l) {
        listeners.add(ActionListener.class, l);
    }

    /**
     *
     * @param l
     */
    public void removeActionListener(ActionListener l) {
        listeners.remove(ActionListener.class, l);
    }
    
    /**
     *
     */
    protected void fireActionPerformed() {
        EventListener[] l = listeners.getListeners(ActionListener.class);
        ActionEvent e = null;
        for (int i = 0; i < l.length; i++) {
            if (e == null) {
                e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null);
            }
            ((ActionListener) l[i]).actionPerformed(e);	       
        }
    }   
    
    private void doEnabled(boolean b) {
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
        m_jKeyDot.setEnabled(b && m_bKeyDot);
        m_jCE.setEnabled(b);
        m_jMinus.setEnabled(b && m_bMinus);
    }
    
    /**
     *
     * @param iMode
     */
    public void setMode(int iMode) {
        switch (iMode) {
            case MODE_DOUBLE:
                m_bMinus = true;
                m_bKeyDot = true;
                keysavailable = SET_DOUBLE;
                break;
            case MODE_DOUBLE_POSITIVE:
                m_bMinus = false;
                m_bKeyDot = true;
                keysavailable = SET_DOUBLE_POSITIVE;
                break;
            case MODE_INTEGER:
                m_bMinus = true;
                m_bKeyDot = false;
                keysavailable = SET_INTEGER;
                break;
            case MODE_INTEGER_POSITIVE:
                m_bMinus = false;
                m_bKeyDot = false;
                keysavailable = SET_INTEGER_POSITIVE;
                break;
            case MODE_STRING:
            default:
                m_bMinus = true;
                m_bKeyDot = true;
                keysavailable = null;
                break;                                
        }
    }
    
    private class MyKeyNumberListener implements java.awt.event.ActionListener {
        
        private char m_cCad;
        
        public MyKeyNumberListener(char cCad){
            m_cCad = cCad;
        }
        public void actionPerformed(java.awt.event.ActionEvent evt) {
                     
            // como contenedor de editores
            if (editorcurrent != null) {
                editorcurrent.transChar(m_cCad);
            }
        }
    }

    /**
     *
     * @param e
     * @param iMode
     */
    public void setActive(EditorComponent e, int iMode) {
       
        if (editorcurrent != null) {
            editorcurrent.deactivate();
        }        
        editorcurrent = e;  // e != null    
        setMode(iMode);
        doEnabled(true);
        
        // keyboard listener activation
        m_txtKeys.setText(null);       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                m_txtKeys.requestFocus();
            }
        });          
    }
    
    /**
     *
     * @param e
     */
    public void setInactive(EditorComponent e) {
        
        if (e == editorcurrent && editorcurrent != null) { // e != null
            editorcurrent.deactivate();
            editorcurrent = null;
            setMode(MODE_STRING);
            doEnabled(false);
        }        
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        m_jCE = new com.openbravo.beans.JCalcButton();
        m_jMinus = new com.openbravo.beans.JCalcButton();
        m_jKey1 = new com.openbravo.beans.JCalcButton();
        m_jKey2 = new com.openbravo.beans.JCalcButton();
        m_jKey3 = new com.openbravo.beans.JCalcButton();
        m_jKey4 = new com.openbravo.beans.JCalcButton();
        m_jKey5 = new com.openbravo.beans.JCalcButton();
        m_jKey6 = new com.openbravo.beans.JCalcButton();
        m_jKey7 = new com.openbravo.beans.JCalcButton();
        m_jKey8 = new com.openbravo.beans.JCalcButton();
        m_jKey9 = new com.openbravo.beans.JCalcButton();
        m_jKey0 = new com.openbravo.beans.JCalcButton();
        m_jKeyDot = new com.openbravo.beans.JCalcButton();
        m_txtKeys = new javax.swing.JTextField();

        setBackground(new java.awt.Color(30, 30, 30));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setPreferredSize(new java.awt.Dimension(300, 300));
        setLayout(new java.awt.GridBagLayout());

        m_jCE.setFocusPainted(false);
        m_jCE.setFocusable(false);
        m_jCE.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/nce_a.png"))); // NOI18N
        m_jCE.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/nce_h.png"))); // NOI18N
        m_jCE.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/nce_n.png"))); // NOI18N
        m_jCE.setMargin(new java.awt.Insets(8, 16, 8, 16));
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

        m_jMinus.setFocusPainted(false);
        m_jMinus.setFocusable(false);
        m_jMinus.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n-_a.png"))); // NOI18N
        m_jMinus.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n-_h.png"))); // NOI18N
        m_jMinus.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n-_n.png"))); // NOI18N
        m_jMinus.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jMinus.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(m_jMinus, gridBagConstraints);

        m_jKey1.setFocusPainted(false);
        m_jKey1.setFocusable(false);
        m_jKey1.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n1_a.png"))); // NOI18N
        m_jKey1.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n1_h.png"))); // NOI18N
        m_jKey1.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n1_n.png"))); // NOI18N
        m_jKey1.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey1.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey1, gridBagConstraints);

        m_jKey2.setFocusPainted(false);
        m_jKey2.setFocusable(false);
        m_jKey2.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n2_a.png"))); // NOI18N
        m_jKey2.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n2_h.png"))); // NOI18N
        m_jKey2.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n2_n.png"))); // NOI18N
        m_jKey2.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey2.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey2, gridBagConstraints);

        m_jKey3.setFocusPainted(false);
        m_jKey3.setFocusable(false);
        m_jKey3.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n3_a.png"))); // NOI18N
        m_jKey3.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n3_h.png"))); // NOI18N
        m_jKey3.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n3_n.png"))); // NOI18N
        m_jKey3.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey3.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey3, gridBagConstraints);

        m_jKey4.setFocusPainted(false);
        m_jKey4.setFocusable(false);
        m_jKey4.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n4_a.png"))); // NOI18N
        m_jKey4.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n4_h.png"))); // NOI18N
        m_jKey4.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n4_n.png"))); // NOI18N
        m_jKey4.setMargin(new java.awt.Insets(8, 16, 8, 16));
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
        m_jKey6.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey6, gridBagConstraints);

        m_jKey7.setFocusPainted(false);
        m_jKey7.setFocusable(false);
        m_jKey7.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n7_a.png"))); // NOI18N
        m_jKey7.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n7_h.png"))); // NOI18N
        m_jKey7.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n7_n.png"))); // NOI18N
        m_jKey7.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey7.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey7, gridBagConstraints);

        m_jKey8.setFocusPainted(false);
        m_jKey8.setFocusable(false);
        m_jKey8.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n8_a.png"))); // NOI18N
        m_jKey8.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n8_h.png"))); // NOI18N
        m_jKey8.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n8_n.png"))); // NOI18N
        m_jKey8.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey8.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey8, gridBagConstraints);

        m_jKey9.setFocusPainted(false);
        m_jKey9.setFocusable(false);
        m_jKey9.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n9_a.png"))); // NOI18N
        m_jKey9.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n9_h.png"))); // NOI18N
        m_jKey9.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n9_n.png"))); // NOI18N
        m_jKey9.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jKey9.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_jKey9, gridBagConstraints);

        m_jKey0.setFocusPainted(false);
        m_jKey0.setFocusable(false);
        m_jKey0.setIconActive(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n0_a.png"))); // NOI18N
        m_jKey0.setIconHover(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n0_h.png"))); // NOI18N
        m_jKey0.setIconNormal(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/calculator/n0_n.png"))); // NOI18N
        m_jKey0.setMargin(new java.awt.Insets(8, 16, 8, 16));
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
        m_jKeyDot.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(m_jKeyDot, gridBagConstraints);

        m_txtKeys.setPreferredSize(new java.awt.Dimension(0, 0));
        m_txtKeys.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m_txtKeysFocusLost(evt);
            }
        });
        m_txtKeys.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                m_txtKeysKeyTyped(evt);
            }
        });
        add(m_txtKeys, new java.awt.GridBagConstraints());
    }// </editor-fold>//GEN-END:initComponents

    private void m_txtKeysKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_txtKeysKeyTyped

        // como contenedor de editores solo
        if (editorcurrent != null) {
            m_txtKeys.setText("0");
            
            // solo lo lanzamos si esta dentro del set de teclas
            char c = evt.getKeyChar();
            if (c == '\n') {
                fireActionPerformed();
            } else {
                if (keysavailable == null) {
                    // todo disponible
                    editorcurrent.typeChar(c);
                } else {
                    for (int i = 0; i < keysavailable.length; i++) {
                        if (c == keysavailable[i]) {
                            // todo disponible
                            editorcurrent.typeChar(c);
                        }
                    }
                }
            }
        }
        
    }//GEN-LAST:event_m_txtKeysKeyTyped

    private void m_txtKeysFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_txtKeysFocusLost

        setInactive(editorcurrent); 

    }//GEN-LAST:event_m_txtKeysFocusLost
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    com.openbravo.beans.JCalcButton m_jCE;
    com.openbravo.beans.JCalcButton m_jKey0;
    com.openbravo.beans.JCalcButton m_jKey1;
    com.openbravo.beans.JCalcButton m_jKey2;
    com.openbravo.beans.JCalcButton m_jKey3;
    com.openbravo.beans.JCalcButton m_jKey4;
    com.openbravo.beans.JCalcButton m_jKey5;
    com.openbravo.beans.JCalcButton m_jKey6;
    com.openbravo.beans.JCalcButton m_jKey7;
    com.openbravo.beans.JCalcButton m_jKey8;
    com.openbravo.beans.JCalcButton m_jKey9;
    com.openbravo.beans.JCalcButton m_jKeyDot;
    com.openbravo.beans.JCalcButton m_jMinus;
    javax.swing.JTextField m_txtKeys;
    // End of variables declaration//GEN-END:variables
    
}
