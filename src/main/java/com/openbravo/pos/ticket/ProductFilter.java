//    uniCenta oPOS  - Touch Friendly Point Of Sale
//    Copyright (c) 2009-2018 uniCenta
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
package com.openbravo.pos.ticket;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.ListQBFModelNumber;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.inventory.ProductsPanel;
import com.openbravo.pos.reports.ReportEditorCreator;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author JG uniCenta
 */
public class ProductFilter extends javax.swing.JPanel implements ReportEditorCreator {

    private SentenceList m_sentcat;
    private ComboBoxValModel m_CategoryModel;
    private DataLogicSales m_dlSales;
    public final int CATEGORY_ITEM_WIDTH = 142;
//    private SentenceList m_sentsup;
//    private ComboBoxValModel m_SupplierModel;
    /**
     * Creates new form JQBFProduct
     */
    public ProductFilter() {

        initComponents();
    }

    /**
     *
     * @param app
     */
    @Override
    public void init(AppView app) {

        m_dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");

        m_sentcat = m_dlSales.getCategoriesList();
        m_CategoryModel = new ComboBoxValModel();

        m_jCboName.setModel(ListQBFModelNumber.getMandatoryString());
        m_jCboPriceBuy.setModel(ListQBFModelNumber.getMandatoryNumber());
        m_jCboPriceSell.setModel(ListQBFModelNumber.getMandatoryNumber());

        jPanel1.setVisible(false);
        jPanel2.setVisible(true);
    }

    private boolean m_isProductsPanel;
    
    public void init(AppView app, boolean isProductsPanel) {
        init(app);
        
        jPanel1.setVisible(true);
        jPanel2.setVisible(false);
        
        m_isProductsPanel = isProductsPanel;
    }

    public void setSelectIndex(int i) {
        m_jlstCategories.setSelectedIndex(0);
    }
    
    /**
     *
     * @throws BasicException
     */
    @Override
    public void activate() throws BasicException {

        List catlist = m_sentcat.list();
        catlist.add(0, null);
        m_CategoryModel = new ComboBoxValModel(catlist);
        m_jCategory.setModel(m_CategoryModel);
        
        
        if(m_isProductsPanel) {
            java.util.List<CategoryInfo> categories = null;
            try {
                categories = m_dlSales.getRootCategories("");
            } catch (BasicException ex) {
                Logger.getLogger(ProductsPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            m_jlstCategories.setModel(new CategoriesListModel(categories)); // aCatList        
            int itemCount = categories.size();

            m_jlstCategories.setPreferredSize(new Dimension(CATEGORY_ITEM_WIDTH * itemCount , 0));            
        }
    }

    public List<CategoryInfo> getSelectedCategories() {
        if( ! m_jlstCategories.isEnabled())
            return new ArrayList<>();
        
        List<CategoryInfo> lst = m_jlstCategories.getSelectedValuesList();
        return lst;
    }
        
    public void setCategoryActionListener(javax.swing.event.ListSelectionListener listener) {
        this.m_jlstCategories.addListSelectionListener(listener);
    }
    
    private class CategoriesListModel extends AbstractListModel {

        private final java.util.List m_aCategories;

        public CategoriesListModel(java.util.List aCategories) {
            m_aCategories = aCategories;
        }

        @Override
        public int getSize() {
            return m_aCategories.size();
        }

        @Override
        public Object getElementAt(int i) {
            return m_aCategories.get(i);
        }
    }

    private class CategoryRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            CategoryInfo cat = (CategoryInfo) value;

            int width = cat.getName().length() * 10 + 20;
            Dimension size = new Dimension(width, 50);
            setBorder(new EmptyBorder(10, 10, 0, 10));
            setVerticalAlignment(JLabel.CENTER);
            this.setPreferredSize(size);

            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
            g.setColor(new Color(230, 230, 230));
            Dimension size = this.getSize();
            g.fillRect(20, size.height - 2, size.width - 40, 2);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public SerializerWrite getSerializerWrite() {
        return new SerializerWriteBasic(
                new Datas[]{
                    Datas.OBJECT, Datas.STRING,
                    Datas.OBJECT, Datas.DOUBLE,
                    Datas.OBJECT, Datas.DOUBLE,
                    Datas.OBJECT, Datas.STRING,
                    Datas.OBJECT, Datas.STRING});
    }

    /**
     *
     * @return
     */
    @Override
    public Component getComponent() {
        return this;
    }

    /**
     *
     * @return @throws BasicException
     */
    @Override
    public Object createValue() throws BasicException {

        if (m_jBarcode.getText() == null || m_jBarcode.getText().equals("")) {

            return new Object[]{
                m_jCboName.getSelectedItem(), m_jName.getText(),
                m_jCboPriceBuy.getSelectedItem(), Formats.CURRENCY.parseValue(m_jPriceBuy.getText()),
                m_jCboPriceSell.getSelectedItem(), Formats.CURRENCY.parseValue(m_jPriceSell.getText()),
                m_CategoryModel.getSelectedKey() == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_EQUALS, m_CategoryModel.getSelectedKey(),
                //                m_SupplierModel.getSelectedKey() == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_EQUALS, m_SupplierModel.getSelectedKey(),                                
                QBFCompareEnum.COMP_NONE, null
            };
        } else {
            return new Object[]{
                QBFCompareEnum.COMP_NONE, null,
                QBFCompareEnum.COMP_NONE, null,
                QBFCompareEnum.COMP_NONE, null,
                QBFCompareEnum.COMP_NONE, null,
                QBFCompareEnum.COMP_EQUALS, m_jBarcode.getText()
            };
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jImageButton1 = new com.openbravo.beans.JImageButton();
        jImageButton2 = new com.openbravo.beans.JImageButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jlstCategories = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        m_jCategory = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        m_jBarcode = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        m_jCboName = new javax.swing.JComboBox();
        m_jName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        m_jCboPriceBuy = new javax.swing.JComboBox();
        m_jPriceBuy = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        m_jCboPriceSell = new javax.swing.JComboBox();
        m_jPriceSell = new javax.swing.JTextField();
        jBtnReset = new javax.swing.JButton();

        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(976, 50));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(976, 50));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jImageButton1.setFocusable(false);
        jImageButton1.setIconPrefix("single-left-48");
        jImageButton1.setPreferredSize(new java.awt.Dimension(50, 50));
        jImageButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jImageButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jImageButton1, java.awt.BorderLayout.LINE_START);

        jImageButton2.setFocusable(false);
        jImageButton2.setIconPrefix("single-right-48");
        jImageButton2.setPreferredSize(new java.awt.Dimension(50, 50));
        jImageButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jImageButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jImageButton2, java.awt.BorderLayout.LINE_END);

        jScrollPane1.getHorizontalScrollBar().setPreferredSize(new Dimension(0,0));
        jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(100000001, 30));

        m_jlstCategories.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        m_jlstCategories.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", ".Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        m_jlstCategories.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        m_jlstCategories.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        CategoryRenderer renderer = new CategoryRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setVerticalAlignment(SwingConstants.CENTER);
        m_jlstCategories.setCellRenderer(renderer);
        m_jlstCategories.setPreferredSize(new java.awt.Dimension(99999999, 40));
        m_jlstCategories.setVisibleRowCount(1);
        jScrollPane1.setViewportView(m_jlstCategories);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(976, 220));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(110, 30));

        m_jCategory.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m_jCategory.setPreferredSize(new java.awt.Dimension(250, 30));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText(AppLocal.getIntString("label.prodbarcode")); // NOI18N
        jLabel5.setPreferredSize(new java.awt.Dimension(110, 30));

        m_jBarcode.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m_jBarcode.setPreferredSize(new java.awt.Dimension(250, 30));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText(AppLocal.getIntString("label.prodname")); // NOI18N
        jLabel2.setPreferredSize(new java.awt.Dimension(110, 30));

        m_jCboName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m_jCboName.setPreferredSize(new java.awt.Dimension(250, 30));

        m_jName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m_jName.setPreferredSize(new java.awt.Dimension(330, 30));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText(AppLocal.getIntString("label.prodpricebuy")); // NOI18N
        jLabel4.setPreferredSize(new java.awt.Dimension(110, 30));

        m_jCboPriceBuy.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m_jCboPriceBuy.setPreferredSize(new java.awt.Dimension(150, 30));

        m_jPriceBuy.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m_jPriceBuy.setPreferredSize(new java.awt.Dimension(60, 30));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText(AppLocal.getIntString("label.prodpricesell")); // NOI18N
        jLabel3.setPreferredSize(new java.awt.Dimension(110, 30));

        m_jCboPriceSell.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m_jCboPriceSell.setPreferredSize(new java.awt.Dimension(150, 30));

        m_jPriceSell.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m_jPriceSell.setPreferredSize(new java.awt.Dimension(60, 30));

        jBtnReset.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jBtnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/reload.png"))); // NOI18N
        jBtnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1001, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(m_jCboName, 0, 541, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(m_jCboPriceBuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(m_jPriceBuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(m_jCboPriceSell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(m_jPriceSell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(m_jName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(m_jCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(35, 35, 35)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(m_jBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(27, 27, 27)
                            .addComponent(jBtnReset)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(m_jCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(m_jBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jBtnReset))
                    .addGap(52, 52, 52)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(m_jCboPriceBuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(m_jPriceBuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(m_jCboPriceSell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(m_jPriceSell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(m_jCboName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(m_jName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(22, Short.MAX_VALUE)))
        );

        add(jPanel2, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnResetActionPerformed
        m_jBarcode.setText(null);
        m_jCategory.setSelectedIndex(0);
        m_jCboName.setSelectedIndex(0);
        m_jCboPriceBuy.setSelectedIndex(0);
        m_jCboPriceSell.setSelectedIndex(0);
        m_jName.setText(null);
        m_jPriceBuy.setText(null);
        m_jPriceSell.setText(null);
    }//GEN-LAST:event_jBtnResetActionPerformed

    private void jImageButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jImageButton1ActionPerformed
        int scrollWidth = jScrollPane1.getSize().width;
        int contentWidth = m_jlstCategories.getSize().width;
        int scrollPos = jScrollPane1.getHorizontalScrollBar().getValue();

        if (scrollWidth < contentWidth) {
            int nextPos = scrollPos - 800;
            if (nextPos < 0) {
                nextPos = 0;
            }
            setCategoriesScrollPos(nextPos);
        }
    }//GEN-LAST:event_jImageButton1ActionPerformed

    private void jImageButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jImageButton2ActionPerformed
        int scrollWidth = jScrollPane1.getSize().width;
        int contentWidth = m_jlstCategories.getSize().width;
        int scrollPos = jScrollPane1.getHorizontalScrollBar().getValue();

        if (scrollWidth < contentWidth) {
            int nextPos = scrollPos + 800;
            if (nextPos + scrollWidth >= contentWidth) {
                nextPos = contentWidth - scrollWidth + 150;
            }
            setCategoriesScrollPos(nextPos);
        }
    }//GEN-LAST:event_jImageButton2ActionPerformed

    private void setCategoriesScrollPos(int value) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jScrollPane1.getHorizontalScrollBar().setValue(value);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnReset;
    private com.openbravo.beans.JImageButton jImageButton1;
    private com.openbravo.beans.JImageButton jImageButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField m_jBarcode;
    private javax.swing.JComboBox m_jCategory;
    private javax.swing.JComboBox m_jCboName;
    private javax.swing.JComboBox m_jCboPriceBuy;
    private javax.swing.JComboBox m_jCboPriceSell;
    private javax.swing.JTextField m_jName;
    private javax.swing.JTextField m_jPriceBuy;
    private javax.swing.JTextField m_jPriceSell;
    private javax.swing.JList m_jlstCategories;
    // End of variables declaration//GEN-END:variables

}
