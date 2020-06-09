package com.openbravo.beans;


import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 *
 * @author Dullard
 */
public class JCalcButton extends JButton {

    Icon iconNormal = null;
    Icon iconHover = null;
    Icon iconActive = null;

    int borderW;
    int arcW;
    
    boolean isToggled;
    
    public JCalcButton() {
        this("");
    }

    public JCalcButton(String str) {
        super(str);

        borderW = 1;
        arcW = 0;
        isToggled = false;
        
        setContentAreaFilled(false);
        this.setForeground(new Color(0xD9D9D9));
            
        CustomBorder border = new CustomBorder(new Color(0x53575C), new Color(0x0F1014), new Color(0x53575C), new Color(0x0F1014), borderW, arcW);
        setBorder(border);
            
        this.addMouseListener(adapter);
    }

    private final MouseAdapter adapter = new MouseAdapter() {

        @Override
        public void mouseExited(MouseEvent e) {
            if (iconNormal != null && !isToggled) {
                setIcon(iconNormal);
            }
            super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (iconHover != null && ! isToggled) {
                setIcon(iconHover);
            }
            super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (iconNormal != null) {
                setIcon(iconNormal);
            }
            isToggled = false;

            CustomBorder border = new CustomBorder(new Color(0x53575C), new Color(0x0F1014), new Color(0x53575C), new Color(0x0F1014), borderW, arcW);
            setBorder(border);
        
            repaint();
            super.mouseReleased(e); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (iconActive != null) {
                setIcon(iconActive);
            }
            if(isEnabled()){
                Border border = BorderFactory.createLineBorder(new Color(0x0F1014), borderW);
                setBorder(border);
                isToggled = true;
            }

            super.mousePressed(e); //To change body of generated methods, choose Tools | Templates.
        }
    };

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
    
    @Override
    protected void paintComponent(Graphics g) {
        Color color1 = new Color(0x44494F);
        Color color2 = new Color(0x2F3237);
        Color toggle = new Color(0x2A2D31);
        
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setPaint(new GradientPaint(
                new Point(0, 0),
                color1,
                new Point(0, getHeight()),
                color2));
        
        if(isToggled)
            g2.setColor(toggle);
        
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcW, arcW);
        g2.dispose();
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
    }

    private class CustomBorder implements Border {

        Color top;
        Color left;
        Color bottom;
        Color right;
        int width = 0;
        int arc = 0;

        public CustomBorder(Color top, Color bottom, Color left, Color right, int width, int arc) {
            this.top = top;
            this.bottom = bottom;
            this.left = left;
            this.right = right;
            this.width = width;
            this.arc = arc;
        }
        
        @Override
        public void paintBorder(Component c,
                Graphics g,
                int x, int y,
                int width, int height) {

            Insets insets = getBorderInsets(c);
            if (top != null) {
                g.setColor(top);
            }

            g.fill3DRect(arc,
                    0,
                    width - insets.right - arc,
                    insets.top,
                    true);

            if (bottom != null) {
                g.setColor(bottom);
            }
            g.fill3DRect(insets.left - arc, height - insets.bottom,
                    width - insets.left - arc, insets.bottom, true);
            
            if (left != null) {
                g.setColor(left);
            }
            g.fill3DRect(0, insets.top - arc, insets.left ,
                    height - insets.top - arc, true);

            if (right != null) {
                g.setColor(right);
            }            
            g.fill3DRect(width - insets.right,  arc, insets.right,
                    height - insets.bottom - arc, true);            
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(width, width, width, width);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }
    }
}
