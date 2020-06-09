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

package com.openbravo.pos.catalog;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.util.ThumbNailBuilder;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author adrianromero
 */
public class JCatalog extends JPanel implements ListSelectionListener, CatalogSelector {
    
    /**
     *
     */
    protected EventListenerList listeners = new EventListenerList();
    private DataLogicSales m_dlSales;   
    private TaxesLogic taxeslogic;
    
    private boolean pricevisible;
    private boolean taxesincluded;
    
    // Set of Products panels
    private final Map<String, ProductInfoExt> m_productsset = new HashMap<>();
    
    // Set of Categoriespanels
     private final Set<String> m_categoriesset = new HashSet<>();
        
    private ThumbNailBuilder tnbbutton;
    private ThumbNailBuilder tnbcat;
    private ThumbNailBuilder tnbsubcat;
    
    private CategoryInfo showingcategory = null;
        
    /** Creates new form JCatalog
     * @param dlSales */
    public JCatalog(DataLogicSales dlSales) {
        this(dlSales, false, false, 90, 60);
    }

    /**
     *
     * @param dlSales
     * @param pricevisible
     * @param taxesincluded
     * @param width
     * @param height
     */
    public JCatalog(DataLogicSales dlSales, boolean pricevisible,
            boolean taxesincluded, int width, int height) {
        
        m_dlSales = dlSales;
        this.pricevisible = pricevisible;
        this.taxesincluded = taxesincluded;
        
        initComponents();
        m_jListCategories.addListSelectionListener(this);
                
        tnbcat = new ThumbNailBuilder(48, 48, "com/openbravo/images/category.png");  
        tnbsubcat = new ThumbNailBuilder(width, height, "com/openbravo/images/subcategory.png"); 
        tnbbutton = new ThumbNailBuilder(width, height, "com/openbravo/images/null.png");        

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
     * @param id
     */
    @Override
    public void showCatalogPanel(String id) {
           
        if (id == null) {
            showRootCategoriesPanel();
        } else {       
            showProductPanel(id);
        }
    }
    
    public Component getCatComponent() {
        return m_jCategories;
    }

    public Component getProductComponent() {
        return m_jProducts;
    }

    public boolean setControls(String position) {
        if (position.equals("south")) {
            m_jRootCategories.add(jPanel2, BorderLayout.SOUTH);
            m_jSubCategories.add(jPanel1, BorderLayout.SOUTH);
            ((GridLayout) jPanel3.getLayout()).setRows(1);
            ((GridLayout) jPanel5.getLayout()).setRows(1);
            return true;
        }
        return false;
    }    
    
    
    /**
     *
     * @throws BasicException
     */
    @Override
    public void loadCatalog() throws BasicException {
        
        // delete all categories panel
        m_jProducts.removeAll();
        
        m_productsset.clear();        
        m_categoriesset.clear();
        
        showingcategory = null;
                
        // Load the taxes logic
        taxeslogic = new TaxesLogic(m_dlSales.getTaxList().list());

        // Load all categories.
        java.util.List<CategoryInfo> categories = m_dlSales.getRootCategories("000"); 
        SmallCategoryRenderer renderer = new SmallCategoryRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setVerticalAlignment(SwingConstants.TOP);
        // Select the first category
        m_jListCategories.setCellRenderer(renderer);
        m_jListCategories.setModel(new CategoriesListModel(categories)); // aCatList
        
        if (m_jListCategories.getModel().getSize() == 0) {
            m_jscrollcat.setVisible(false);
            jPanel2.setVisible(false);
        } else {
            m_jscrollcat.setVisible(true);
            jPanel2.setVisible(true);
            m_jListCategories.setSelectedIndex(0);
        }
            
        showRootCategoriesPanel();
    }
    
    /**
     *
     * @param value
     */
    @Override
    public void setComponentEnabled(boolean value) {
        
        m_jListCategories.setEnabled(value);
        m_jscrollcat.setEnabled(value);
        m_lblIndicator.setEnabled(value);
        m_btnBack1.setEnabled(value);
        m_jProducts.setEnabled(value); 

        synchronized (m_jProducts.getTreeLock()) {
            int compCount = m_jProducts.getComponentCount();
            for (int i = 0 ; i < compCount ; i++) {
                m_jProducts.getComponent(i).setEnabled(value);
            }
        }
     
        this.setEnabled(value);
    }
    
    /**
     *
     * @param l
     */
    @Override
    public void addActionListener(ActionListener l) {
        listeners.add(ActionListener.class, l);
    }

    /**
     *
     * @param l
     */
    @Override
    public void removeActionListener(ActionListener l) {
        listeners.remove(ActionListener.class, l);
    }

    @Override
    public void valueChanged(ListSelectionEvent evt) {
        
        if (!evt.getValueIsAdjusting()) {
            int i = m_jListCategories.getSelectedIndex();
            if (i >= 0) {
                // Lo hago visible...
                Rectangle oRect = m_jListCategories.getCellBounds(i, i);
                m_jListCategories.scrollRectToVisible(oRect);       
            }
        }
    }

    /**
     *
     * @param prod
     */
    protected void fireSelectedProduct(ProductInfoExt prod) {
        EventListener[] l = listeners.getListeners(ActionListener.class);
        ActionEvent e = null;
        for (EventListener l1 : l) {
            if (e == null) {
                e = new ActionEvent(prod, ActionEvent.ACTION_PERFORMED, prod.getID());
            }
            ((ActionListener) l1).actionPerformed(e);	       
        }
    }   
    
    private void selectCategoryPanel(String catid) {
        try {
            if (!m_categoriesset.contains(catid)) {
                
                jcurrTab = new JCatalogTab();     
                jcurrTab.applyComponentOrientation(getComponentOrientation());
                m_jProducts.add(jcurrTab, catid);
                m_categoriesset.add(catid);
               
                java.util.List<CategoryInfo> categories = m_dlSales.getSubcategories(catid);
                for (CategoryInfo cat : categories) {
                   
                    if (cat.getCatShowName()) {
                        jcurrTab.addButton(new ImageIcon(tnbsubcat.getThumbNailText
                        (cat.getImage(), cat.getName())), 
                            new SelectedCategory(cat),cat.getTextTip());
                    }else{
                        jcurrTab.addButton(new ImageIcon(
                        tnbsubcat.getThumbNailText(cat.getImage(), "")), 
                            new SelectedCategory(cat),cat.getTextTip());
                    }
                }

                java.util.List<ProductInfoExt> prods = m_dlSales.getProductConstant();
                for (ProductInfoExt prod : prods) {
                    jcurrTab.addButton(
                    new ImageIcon(tnbbutton.getThumbNailText(prod.getImage(), 
                    getProductLabel(prod))), 
                    new SelectedAction(prod), 
                    prod.getTextTip());
                }
                
                java.util.List<ProductInfoExt> products = m_dlSales.getProductCatalog(catid);

                for (ProductInfoExt prod : products) {
                    jcurrTab.addButton(
                    new ImageIcon(tnbbutton.getThumbNailText(prod.getImage(), 
                    getProductLabel(prod))), 
                    new SelectedAction(prod),prod.getTextTip());
                }
            }
            
            CardLayout cl = (CardLayout)(m_jProducts.getLayout());
            cl.show(m_jProducts, catid);  
        } catch (BasicException e) {
            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_WARNING, 
                AppLocal.getIntString("message.notactive"), e));            
        }
    }
    
    private String getProductLabel(ProductInfoExt product) {

        if (pricevisible) {
            if (taxesincluded) {
                TaxInfo tax = taxeslogic.getTaxInfo(product.getTaxCategoryID());
                if(!"".equals(product.getDisplay())){
                    return "<html><center>" + product.getDisplay() + "<br>" + product.printPriceSellTax(tax);                
                } else {
                    return "<html><center>" + product.getName() + "<br>" + product.printPriceSellTax(tax);                                    
                }
            } else {
                if(!"".equals(product.getDisplay())){
                    return "<html><center>" + product.getDisplay() + "<br>" + product.printPriceSell();                
                } else {
                    return "<html><center>" + product.getName() + "<br>" + product.printPriceSell();                
                }                
            }
        } else {

            if (!"".equals(product.getDisplay())) {
                return product.getDisplay();                
            } else {
                return product.getName();
            }
        }
    }
    
    private void selectIndicatorPanel(Icon icon, String label, String texttip) {
        
        m_lblIndicator.setText(label);
        m_lblIndicator.setIcon(icon);
        
        // Show subcategories panel
        CardLayout cl = (CardLayout)(m_jCategories.getLayout());
        cl.show(m_jCategories, "subcategories");
    }
    
    private void selectIndicatorCategories() {
        // Show root categories panel
        CardLayout cl = (CardLayout)(m_jCategories.getLayout());
        cl.show(m_jCategories, "rootcategories");
    }
    
    private void showRootCategoriesPanel() {
        
        selectIndicatorCategories();
        // Show selected root category
        CategoryInfo cat = (CategoryInfo) m_jListCategories.getSelectedValue();
        
        if (cat != null) {
            selectCategoryPanel(cat.getID());
        }
        showingcategory = null;
    }
    
    private void showSubcategoryPanel(CategoryInfo category) {
// Modified JDL 13.04.13
// this is the new panel that displays when a sub catergory is selected mouse does not work here        
        selectIndicatorPanel(new ImageIcon(tnbsubcat.getThumbNail(
            category.getImage())),category.getName(), category.getTextTip());
        selectCategoryPanel(category.getID());
        showingcategory = category;
    }
   
    private void showProductPanel(String id) {

        ProductInfoExt product = m_productsset.get(id);
        CategoryInfo cat = (CategoryInfo) m_jListCategories.getSelectedValue();

        if (product == null) {
            if (m_productsset.containsKey(id)) {
                // It is an empty panel
                if (showingcategory == null) {
                    showRootCategoriesPanel();                         
                } else {
                    showSubcategoryPanel(showingcategory);
                }
            } else {
                try {
                    // Create  products panel
                    java.util.List<ProductInfoExt> products = m_dlSales.getProductComments(id);
                    
                    if (products.isEmpty()) {// && cat_subproducts.isEmpty()) {                    
                        m_productsset.put(id, null);

                        if (showingcategory == null) {
                            showRootCategoriesPanel();                         
                        } else {
                            showSubcategoryPanel(showingcategory);
                        }
                    } else {
//                        java.util.List<ProductInfoExt> subproducts = null;
//                        subproducts = products;

                        product = m_dlSales.getProductInfo(id);
                        m_productsset.put(id, product);

                        JCatalogTab jcurrTab = new JCatalogTab();      
                        jcurrTab.applyComponentOrientation(getComponentOrientation());
                        m_jProducts.add(jcurrTab, "PRODUCT." + id);                        

                        // Add products
                        for (ProductInfoExt prod : products) {
                            jcurrTab.addButton(new ImageIcon(tnbbutton.getThumbNailText(prod.getImage(), 
                                getProductLabel(prod))), new SelectedAction(prod),prod.getTextTip());                            
                        }                       
                        selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(product.getImage())),
                            product.getDisplay(), product.getTextTip());                        
                        
                        CardLayout cl = (CardLayout)(m_jProducts.getLayout());
                        cl.show(m_jProducts, "PRODUCT." + id); 
                    }
                } catch (BasicException eb) {
                    m_productsset.put(id, null);
                    if (showingcategory == null) {
                        showRootCategoriesPanel();                         
                    } else {
                        showSubcategoryPanel(showingcategory);
                    }
                }
            }
        } else {
            selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(
                product.getImage())), product.getName(), product.getTextTip());            
            
            CardLayout cl = (CardLayout)(m_jProducts.getLayout());
            cl.show(m_jProducts, "PRODUCT." + id); 
        }
    }
    
    private class SelectedAction implements ActionListener {
        private final ProductInfoExt prod;
        public SelectedAction(ProductInfoExt prod) {
            this.prod = prod;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            fireSelectedProduct(prod);
        }
    }
    
    private class SelectedCategory implements ActionListener {
        private final CategoryInfo category;
        public SelectedCategory(CategoryInfo category) {
            this.category = category;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            showSubcategoryPanel(category);
        }
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
    
    private class SmallCategoryRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, 
          int index, boolean isSelected, boolean cellHasFocus) {

            super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
            CategoryInfo cat = (CategoryInfo) value;
            this.setText("<html><div align='center'>"+cat.getName() + "<div></html>");

            int height = (cat.getName().length() / 20 + 1) * 25 + 20;
            Dimension size = new Dimension(0, height);
            setBorder(new EmptyBorder(10,10,0,10));
            setVerticalAlignment(JLabel.TOP);
            this.setPreferredSize(size);
            
            if(isSelected)
                setForeground(new Color(100, 0, 0));
            else
                setForeground(new Color(230,230,230));
            
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
            g.setColor(new Color(230,230,230));
            Dimension size = this.getSize();
            g.fillRect(20, size.height - 2, size.width - 40, 2);
        }
    }            
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jCategories = new javax.swing.JPanel();
        m_jRootCategories = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        m_jscrollcat = new javax.swing.JScrollPane();
        m_jListCategories = new javax.swing.JList() {
            @Override
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(93, 163, 187);
                Color color2 = new Color(0,111,148);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                super.paintComponent(g);
            }
        };
        jPanel7 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        btnCategoriesUp = new com.openbravo.beans.JImageButton();
        jPanel13 = new javax.swing.JPanel();
        btnCategoriesDown = new com.openbravo.beans.JImageButton();
        m_jSubCategories = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        m_lblIndicator = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        m_btnBack1 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        m_jProducts = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        btnProductScrollUp = new com.openbravo.beans.JImageButton();
        jPanel11 = new javax.swing.JPanel();
        btnProductScrollDown = new com.openbravo.beans.JImageButton();

        setOpaque(false);
        setLayout(new java.awt.BorderLayout());

        m_jCategories.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        m_jCategories.setMaximumSize(new java.awt.Dimension(275, 600));
        m_jCategories.setOpaque(false);
        m_jCategories.setPreferredSize(new java.awt.Dimension(200, 0));
        m_jCategories.setLayout(new java.awt.CardLayout());

        m_jRootCategories.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        m_jRootCategories.setMinimumSize(new java.awt.Dimension(200, 100));
        m_jRootCategories.setOpaque(false);
        m_jRootCategories.setPreferredSize(new java.awt.Dimension(150, 194));
        m_jRootCategories.setLayout(new java.awt.BorderLayout());

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.GridLayout(0, 1, 0, 5));
        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        m_jRootCategories.add(jPanel2, java.awt.BorderLayout.LINE_END);

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.darkGray, java.awt.Color.darkGray));
        jPanel6.setPreferredSize(new java.awt.Dimension(150, 194));
        jPanel6.setLayout(new java.awt.BorderLayout());

        m_jscrollcat.getViewport().setOpaque(false);
        m_jscrollcat.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        m_jscrollcat.setBorder(null);
        m_jscrollcat.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        m_jscrollcat.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        m_jscrollcat.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        m_jscrollcat.setOpaque(false);
        m_jscrollcat.setPreferredSize(new java.awt.Dimension(150, 130));

        m_jListCategories.setBackground(new java.awt.Color(0,0,0,0));
        m_jListCategories.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        m_jListCategories.setForeground(new java.awt.Color(230, 230, 230));
        m_jListCategories.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        m_jListCategories.setFocusable(false);
        m_jListCategories.setOpaque(false);
        m_jListCategories.setSelectionBackground(new java.awt.Color(0, 0, 0, 0));
        m_jListCategories.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                m_jListCategoriesValueChanged(evt);
            }
        });
        m_jscrollcat.setViewportView(m_jListCategories);

        jPanel6.add(m_jscrollcat, java.awt.BorderLayout.CENTER);

        jPanel7.setBackground(new java.awt.Color(0, 111, 148));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

        jPanel12.setOpaque(false);

        btnCategoriesUp.setToolTipText("Previous Category");
        btnCategoriesUp.setFocusable(false);
        btnCategoriesUp.setIconPrefix("categories_up");
        btnCategoriesUp.setPreferredSize(new java.awt.Dimension(50, 50));
        btnCategoriesUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCategoriesUpActionPerformed(evt);
            }
        });
        jPanel12.add(btnCategoriesUp);

        jPanel7.add(jPanel12);

        jPanel13.setOpaque(false);

        btnCategoriesDown.setToolTipText("Next Category");
        btnCategoriesDown.setFocusable(false);
        btnCategoriesDown.setIconPrefix("categories_down");
        btnCategoriesDown.setPreferredSize(new java.awt.Dimension(50, 50));
        btnCategoriesDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCategoriesDownActionPerformed(evt);
            }
        });
        jPanel13.add(btnCategoriesDown);

        jPanel7.add(jPanel13);

        jPanel6.add(jPanel7, java.awt.BorderLayout.SOUTH);

        m_jRootCategories.add(jPanel6, java.awt.BorderLayout.CENTER);

        m_jCategories.add(m_jRootCategories, "rootcategories");

        m_jSubCategories.setOpaque(false);
        m_jSubCategories.setLayout(new java.awt.BorderLayout());

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.BorderLayout());

        m_lblIndicator.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        m_lblIndicator.setForeground(new java.awt.Color(0, 204, 204));
        m_lblIndicator.setText("jLabel1");
        m_lblIndicator.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel4.add(m_lblIndicator, java.awt.BorderLayout.NORTH);

        m_jSubCategories.add(jPanel4, java.awt.BorderLayout.WEST);

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
        jPanel5.setOpaque(false);
        jPanel5.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        m_btnBack1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/2uparrow.png"))); // NOI18N
        m_btnBack1.setFocusPainted(false);
        m_btnBack1.setFocusable(false);
        m_btnBack1.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_btnBack1.setPreferredSize(new java.awt.Dimension(60, 45));
        m_btnBack1.setRequestFocusEnabled(false);
        m_btnBack1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_btnBack1ActionPerformed(evt);
            }
        });
        jPanel5.add(m_btnBack1);

        jPanel1.add(jPanel5, java.awt.BorderLayout.NORTH);

        m_jSubCategories.add(jPanel1, java.awt.BorderLayout.EAST);

        m_jCategories.add(m_jSubCategories, "subcategories");

        add(m_jCategories, java.awt.BorderLayout.LINE_START);

        jPanel8.setBackground(new java.awt.Color(90, 90, 90));
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray, java.awt.Color.darkGray, java.awt.Color.gray, java.awt.Color.gray));
        jPanel8.setLayout(new java.awt.BorderLayout());

        m_jProducts.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m_jProducts.setOpaque(false);
        m_jProducts.setLayout(new java.awt.CardLayout());
        jPanel8.add(m_jProducts, java.awt.BorderLayout.CENTER);

        jPanel9.setOpaque(false);
        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        jPanel10.setOpaque(false);

        btnProductScrollUp.setToolTipText("Scroll Up");
        btnProductScrollUp.setFocusable(false);
        btnProductScrollUp.setIconPrefix("single-up-48");
        btnProductScrollUp.setPreferredSize(new java.awt.Dimension(50, 50));
        btnProductScrollUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductScrollUpActionPerformed(evt);
            }
        });
        jPanel10.add(btnProductScrollUp);

        jPanel9.add(jPanel10);

        jPanel11.setOpaque(false);

        btnProductScrollDown.setToolTipText("Scroll Down");
        btnProductScrollDown.setFocusable(false);
        btnProductScrollDown.setIconPrefix("single-down-48");
        btnProductScrollDown.setPreferredSize(new java.awt.Dimension(50, 50));
        btnProductScrollDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductScrollDownActionPerformed(evt);
            }
        });
        jPanel11.add(btnProductScrollDown);

        jPanel9.add(jPanel11);

        jPanel8.add(jPanel9, java.awt.BorderLayout.SOUTH);

        add(jPanel8, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jListCategoriesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_m_jListCategoriesValueChanged

        if (!evt.getValueIsAdjusting()) {
            CategoryInfo cat = (CategoryInfo) m_jListCategories.getSelectedValue();
            if (cat != null) {
                selectCategoryPanel(cat.getID());
            }
        }
        
    }//GEN-LAST:event_m_jListCategoriesValueChanged

    private void m_btnBack1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_btnBack1ActionPerformed

        showRootCategoriesPanel();

    }//GEN-LAST:event_m_btnBack1ActionPerformed

    private void btnProductScrollDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductScrollDownActionPerformed

        int scrollHeight = jcurrTab.getScrollHeight();
        int contentHeight = jcurrTab.getScrollContent();
        int scrollPos = jcurrTab.getScrollValue();
        
        if(scrollHeight < contentHeight 
                && scrollPos < contentHeight - scrollHeight + 10){
            int nextPos = scrollPos + 500;
            if(nextPos + scrollHeight > contentHeight)
                nextPos = contentHeight - scrollHeight;
            jcurrTab.setScrollValue(nextPos);
        }
    }//GEN-LAST:event_btnProductScrollDownActionPerformed

    private void btnProductScrollUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductScrollUpActionPerformed

        int scrollHeight = jcurrTab.getScrollHeight();
        int contentHeight = jcurrTab.getScrollContent();
        int scrollPos = jcurrTab.getScrollValue();
        
        if(scrollHeight < contentHeight 
                && scrollPos > 0) {
            int nextPos = scrollPos - 500;
            if(nextPos < 0)
                nextPos = 0;
            jcurrTab.setScrollValue(nextPos);
        }
    }//GEN-LAST:event_btnProductScrollUpActionPerformed

    private void btnCategoriesUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCategoriesUpActionPerformed
        int scrollHeight = m_jscrollcat.getSize().height;
        int contentHeight = m_jListCategories.getSize().height;
        int scrollPos = m_jscrollcat.getVerticalScrollBar().getValue();
        
        if(scrollHeight < contentHeight) {
            int nextPos = scrollPos - 500;
            if(nextPos < 0)
                nextPos = 0;
            setCategoriesScrollPos(nextPos);
        }
//        int cur_index = m_jListCategories.getSelectedIndex();
//        
//        if(cur_index > 0)
//            m_jListCategories.setSelectedIndex(cur_index - 1);
    }//GEN-LAST:event_btnCategoriesUpActionPerformed
    
    private void btnCategoriesDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCategoriesDownActionPerformed
        int scrollHeight = m_jscrollcat.getSize().height;
        int contentHeight = m_jListCategories.getSize().height;
        int scrollPos = m_jscrollcat.getVerticalScrollBar().getValue();
        
        if(scrollHeight < contentHeight){
            int nextPos = scrollPos + 500;
            if(nextPos + scrollHeight > contentHeight)
                nextPos = contentHeight - scrollHeight;
            setCategoriesScrollPos(nextPos);
        }
        
//        int cur_index = m_jListCategories.getSelectedIndex();
//        int all_cnt = m_jListCategories.getModel().getSize();
//        
//        if(cur_index < all_cnt)
//            m_jListCategories.setSelectedIndex(cur_index + 1);
        
    }//GEN-LAST:event_btnCategoriesDownActionPerformed
    
    private void setCategoriesScrollPos(int value) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                m_jscrollcat.getVerticalScrollBar().setValue(value);
            }
        });
    }
    
    private JCatalogTab jcurrTab;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.openbravo.beans.JImageButton btnCategoriesDown;
    private com.openbravo.beans.JImageButton btnCategoriesUp;
    private com.openbravo.beans.JImageButton btnProductScrollDown;
    private com.openbravo.beans.JImageButton btnProductScrollUp;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton m_btnBack1;
    private javax.swing.JPanel m_jCategories;
    private javax.swing.JList m_jListCategories;
    private javax.swing.JPanel m_jProducts;
    private javax.swing.JPanel m_jRootCategories;
    private javax.swing.JPanel m_jSubCategories;
    private javax.swing.JScrollPane m_jscrollcat;
    private javax.swing.JLabel m_lblIndicator;
    // End of variables declaration//GEN-END:variables
    
}
