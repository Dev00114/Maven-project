/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.beans;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JToggleButton;

/**
 *
 * @author Dullard
 */
public class JImageToggleButton extends JToggleButton {

    Icon iconNormal = null;
    Icon iconHover = null;
    Icon iconActive = null;

    String icon_prefix;
    String icon_image_path;

    int borderW;
    int arcW;

    boolean isToggled;

    public JImageToggleButton() {
        this("");
    }

    public JImageToggleButton(String str) {
        super(str);
        
        borderW = 1;
        arcW = 0;
        isToggled = false;

        
        setForeground(new Color(0xD9D9D9));
        setOpaque(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        
        addMouseListener(adapter);
        icon_image_path = "/com/openbravo/images/buttons";
    }

    private final MouseAdapter adapter = new MouseAdapter() {

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
            if (iconNormal != null && ! isSelected()) {
                setIcon(iconNormal);
            }
            setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
            if (iconHover != null && ! isSelected()) {
                setIcon(iconHover);
            }
            setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e); //To change body of generated methods, choose Tools | Templates.
            
            if (iconNormal != null) {
                if(isSelected())
                    setIcon(iconActive);
                else
                    setIcon(iconNormal);
            }
            setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            isToggled = false;
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e); //To change body of generated methods, choose Tools | Templates.
            
            if (iconActive != null && ! isSelected()) {
                setIcon(iconActive);
            }
            setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            isToggled = true;
            repaint();
        }
    };

    public void setIconPrefix(String prefix) {
        this.icon_prefix = prefix;
        if (prefix.equals("")) {
            return;
        }

        this.setIconNormal(new javax.swing.ImageIcon(getClass().getResource(icon_image_path + "/" + prefix + "-n.png")));

        this.setIconHover(new javax.swing.ImageIcon(getClass().getResource(icon_image_path + "/" + prefix + "-h.png")));

        this.setIconActive(new javax.swing.ImageIcon(getClass().getResource(icon_image_path + "/" + prefix + "-a.png")));
    }

    public String getIconPrefix() {
        return this.icon_prefix;
    }

    public void setIconPath(String path) {
        this.icon_image_path = path;
    }

    public String getIconPath() {
        return this.icon_image_path;
    }

    public void setIconNormal(Icon icon) {
        iconNormal = icon;
        setIcon(iconNormal);
        setDisabledIcon(iconNormal);
    }

    public Icon getIconNormal() {
        return iconNormal;
    }

    public void setIconHover(Icon icon) {
        iconHover = icon;
    }

    public Icon getIconHover() {
        return iconHover;
    }

    public void setIconActive(Icon icon) {
        iconActive = icon;
    }

    public Icon getIconActive() {
        return iconActive;
    }
}
