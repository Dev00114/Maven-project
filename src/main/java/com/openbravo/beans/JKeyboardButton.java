/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.beans;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 *
 * @author Dullard
 */
public class JKeyboardButton extends JButton {
    public JKeyboardButton(){
        this("");
    }
    
    Border normalBorder;
    Border activeBorder;
    Border hoverBorder;

    Color back;
    
    Color normal;
    Color active;
    
    boolean isActive;
    boolean isHover;
    public JKeyboardButton(String txt) {
        super(txt);
        
        setFocusPainted(false);
        setContentAreaFilled(false);

        normalBorder = BorderFactory.createLineBorder(new Color(59, 76, 104), 3);        
        hoverBorder = BorderFactory.createLineBorder(new Color(165, 180, 203), 3);
        
        normal = new Color(81, 97, 123);
        active = new Color(165, 180, 203);
        setBackground(normal);
        
        setForeground(Color.lightGray);
        addMouseListener(listener);
        
        setBorder(normalBorder);
    }
    
    MouseAdapter listener = new MouseAdapter(){

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
            
            isHover = false;
            setForeground(Color.lightGray);
            setBorder(normalBorder);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
            isHover = true;
            setBorder(hoverBorder);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e); //To change body of generated methods, choose Tools | Templates.
            isActive = false;
            setForeground(Color.lightGray);
            setBorder(normalBorder);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e); //To change body of generated methods, choose Tools | Templates.
            isActive = true;
            setForeground(Color.BLACK);
            setBorder(normalBorder);
        }
    };

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        
        Color color = new Color(81, 97, 123);
        if(isActive)
            color = new Color(165, 180, 203);
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        Color txtColor = Color.lightGray;
        if(isActive)
            txtColor= Color.BLACK;
        Rectangle2D strBounds = g.getFontMetrics().getStringBounds(getText(), g);
        
        int left = (getSize().width - (int)strBounds.getWidth())/2;
        int top  = (getSize().height - (int)(strBounds.getHeight())) / 2  - (int)strBounds.getY();
        
        g.setColor(txtColor);
        g.drawString(getText(), left, top);
    } 
    
    @Override
    public void setBackground(Color color)
    {
        back = color;
    }
}
