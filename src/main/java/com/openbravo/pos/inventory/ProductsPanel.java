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

package com.openbravo.pos.inventory;

import com.openbravo.basic.BasicException;
import com.openbravo.beans.VirtualKeyboard;
import com.openbravo.data.user.EditorListener;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.JRootFrame;
import com.openbravo.pos.panels.JPanelTable2;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.ProductFilter;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author JG uniCenta
 *
 */
public class ProductsPanel extends JPanelTable2 implements EditorListener {

    private ProductsEditor jeditor;
    private ProductFilter jproductfilter;
    
    private DataLogicSales m_dlSales = null;
    
    private VirtualKeyboard vk;
    
    public ProductsPanel() {
        
    }
    
    /**
     *
     */
    @Override
    protected void init() {   
        m_dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");
        
        

        
        jproductfilter = new ProductFilter();
        jproductfilter.init(app, true);
        
        jproductfilter.setCategoryActionListener(categoryActionListener);
        row = m_dlSales.getProductsRow();

        lpr =  new ListProviderCreator(m_dlSales.getProductCatQBF(), jproductfilter);
        
        spr = new SaveProvider(
            m_dlSales.getProductCatUpdate(),
            m_dlSales.getProductCatInsert(),
            m_dlSales.getProductCatDelete());
        
        jeditor = new ProductsEditor(app, dirty);
        
        vk = new VirtualKeyboard();
        vk.show(JRootFrame.getInstance(), getKeyboardPanel());
    }
    
    public javax.swing.event.ListSelectionListener categoryActionListener = new javax.swing.event.ListSelectionListener() {
        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            try {
                List<CategoryInfo> sel_cat = jproductfilter.getSelectedCategories();
                
                List products = null;
                if(sel_cat.get(0) != null && ! sel_cat.get(0).equals(""))
                    products = m_dlSales.getProductByCat(sel_cat.get(0).getID());
                
                reloadList(products);
                
                jeditor.setSubEnabled(true);
                if(sel_cat.get(0).getID().equals("000"))
                    jeditor.setSubEnabled(false);
                
            } catch (BasicException ex) {
                Logger.getLogger(ProductsPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    /**
     *
     * @return value
     */
    @Override
    public EditorRecord getEditor() {
        return jeditor;
    }
    
    /**
     *
     * @return value
     */
    @Override
    public Component getFilter() {
        return jproductfilter.getComponent();
    }

    /**
     *
     * @return btnScanPal
     */
    @Override
    public Component getToolbarExtras() {
        
        JButton btnScanPal = new JButton();
        btnScanPal.setText("ScanPal");
        btnScanPal.setVisible(app.getDeviceScanner() != null);
        btnScanPal.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScanPalActionPerformed(evt);
            }
        });      
        
        return btnScanPal;
    }
    
    private void btnScanPalActionPerformed(java.awt.event.ActionEvent evt) {                                           
  
        JDlgUploadProducts.showMessage(this, app.getDeviceScanner(), bd);
    }
    
    /**
     *
     * @return value
     */
    @Override
    public String getTitle() {
        return AppLocal.getIntString("Menu.Products");
    }

    /**
     *
     * @throws BasicException
     */
    @Override
    public void activate() throws BasicException {
        jeditor.activate(); 
        jproductfilter.activate();
        super.activate();
        
        getKeyboardPanel().setVisible(true);
        getKeyboardPanel().setPreferredSize(new Dimension(500, 200));
        
        this.validate();
        this.repaint();
        
        jproductfilter.setSelectIndex(0);
    }

    @Override
    public boolean deactivate() {
        getKeyboardPanel().setVisible(false);
        getKeyboardPanel().setPreferredSize(new Dimension(0, 0));
        this.validate();
        this.repaint();
        return super.deactivate(); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     *
     * @param value
     */
    @Override
    public void updateValue(Object value) {
    }    
}