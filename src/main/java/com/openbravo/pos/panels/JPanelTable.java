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

package com.openbravo.pos.panels;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.*;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.*;
import com.openbravo.pos.customers.CustomerInfoGlobal;
import com.openbravo.pos.forms.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 *
 * @author adrianromero
 */
public abstract class JPanelTable extends JPanel implements JPanelView, BeanFactoryApp {
    
    /**
     *
     */
    protected BrowsableEditableData bd;    

    /**
     *
     */
    protected DirtyManager dirty;    

    /**
     *
     */
    protected AppView app;
    
    /** Creates new form JPanelTableEditor */
    public JPanelTable() {

        initComponents();
    }
    
    /**
     *
     * @param app
     * @throws BeanFactoryException
     */
    @Override
    public void init(AppView app) throws BeanFactoryException {
        
        this.app = app;
        dirty = new DirtyManager();
        bd = null;
        init();
    }

    /**
     *
     * @return
     */
    @Override
    public Object getBean() {
        return this;
    }
    
    /**
     *
     */
    protected void startNavigation() {
        
        if (bd == null) {
            
            // init browsable editable data
            bd = new BrowsableEditableData(getListProvider(), getSaveProvider(), getEditor(), dirty);

            // Add the filter panel
            Component c = getFilter();
            if (c != null) {
                c.applyComponentOrientation(getComponentOrientation());
                add(c, BorderLayout.NORTH);
            }

            // Add the editor
            c = getEditor().getComponent();
            if (c != null) {
                c.applyComponentOrientation(getComponentOrientation());                
//                jScrollPane1.add(c, BorderLayout.CENTER);            
                jScrollPane1.setViewportView(c);
            }

            // el panel este
            ListCellRenderer cr = getListCellRenderer();
            if (cr != null) {
                JListNavigator nl = new JListNavigator(bd);
                nl.applyComponentOrientation(getComponentOrientation());
                if (cr != null) {
                    nl.setCellRenderer(cr);
                }
                container.add(nl, java.awt.BorderLayout.LINE_START);
            }

            // add toolbar extras
            c = getToolbarExtras();
            if (c != null) {
                c.applyComponentOrientation(getComponentOrientation());
                toolbar.add(c);
            }

            // La Toolbar
            c = new JLabelDirty(dirty);
            c.applyComponentOrientation(getComponentOrientation());
            toolbar.add(c);
            c = new JCounter(bd);
            c.applyComponentOrientation(getComponentOrientation());
            toolbar.add(c);
            c = new JNavigator(bd, getVectorer(), getComparatorCreator());
            c.applyComponentOrientation(getComponentOrientation());
            toolbar.add(c);
            c = new JSaver(bd);
            c.applyComponentOrientation(getComponentOrientation());
            toolbar.add(c);
        }
    }
    
    public JPanel getKeyboardPanel()
    {
        return keyboardPanel;
    }
    
    /**
     *
     * @return
     */
    public Component getToolbarExtras() {
        return null;
    }

    /**
     *
     * @return
     */
    public Component getFilter() {    
        return null;
    }
    
    /**
     *
     */
    protected abstract void init();
    
    /**
     *
     * @return
     */
    public abstract EditorRecord getEditor();
    
    /**
     *
     * @return
     */
    public abstract ListProvider getListProvider();
    
    /**
     *
     * @return
     */
    public abstract SaveProvider getSaveProvider();
    
    /**
     *
     * @return
     */
    public Vectorer getVectorer() {
        return null;
    }
    
    /**
     *
     * @return
     */
    public ComparatorCreator getComparatorCreator() {
        return null;
    }
    
    /**
     *
     * @return
     */
    public ListCellRenderer getListCellRenderer() {
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public JComponent getComponent() {
        return this;
    }

    /**
     *
     * @throws BasicException
     */
    @Override
    public void activate() throws BasicException {
        startNavigation();
        bd.actionLoad();
        
        //HS insert new customer 20.03.2014
        if (CustomerInfoGlobal.getInstance()!=null){
            bd.actionInsert();
    }    
    
    }

    public void reloadList(List l) throws BasicException {
        bd.loadList(l);
    }
    
    /**
     *
     * @return
     */
    @Override
    public boolean deactivate() {

        try {
            return bd.actionClosingForm(this);
        } catch (BasicException eD) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.CannotMove"), eD);
            msg.show(this);
            return false;
        }
    }  
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        container = new javax.swing.JPanel();
        toolbar = new javax.swing.JPanel();
        jeditor = new javax.swing.JPanel();
        keyboardPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();

        setBackground(new java.awt.Color(90, 90, 90));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new java.awt.BorderLayout());

        container.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        container.setOpaque(false);
        container.setLayout(new java.awt.BorderLayout());

        toolbar.setOpaque(false);
        container.add(toolbar, java.awt.BorderLayout.NORTH);

        jeditor.setBackground(new java.awt.Color(255, 255, 153));
        jeditor.setOpaque(false);
        jeditor.setLayout(new java.awt.BorderLayout());

        keyboardPanel.setBackground(new java.awt.Color(50, 69, 101));
        keyboardPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        jeditor.add(keyboardPanel, java.awt.BorderLayout.SOUTH);
        jeditor.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        container.add(jeditor, java.awt.BorderLayout.CENTER);

        add(container, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
  
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel container;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jeditor;
    private javax.swing.JPanel keyboardPanel;
    private javax.swing.JPanel toolbar;
    // End of variables declaration//GEN-END:variables
    
}
