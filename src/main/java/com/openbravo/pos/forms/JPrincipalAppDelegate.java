/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.forms;

import javax.swing.Icon;

/**
 *
 * @author Dullard
 */
public class JPrincipalAppDelegate {
    private static JPrincipalAppDelegate instance = null;
    private JPrincipalApp app;
    private Icon appOpenIcon;
    private Icon appCloseIcon;
    
    public static JPrincipalAppDelegate getInstance()
    {
        if(instance == null)
            instance = new JPrincipalAppDelegate();
        return instance;
    }
    
    public void setApp(JPrincipalApp principalApp)
    {
        this.app = principalApp;
    }
    
    public JPrincipalApp getApp()
    {
        return app;
    }
    
    public void setOpenIcon(Icon openIcon)
    {
        this.appOpenIcon = openIcon;
    }
    
    public Icon getOpenIcon()
    {
        return this.appOpenIcon;
    }
    
    public void setCloseIcon(Icon closeIcon)
    {
        this.appCloseIcon = closeIcon;
    }
    
    public Icon getCloseIcon()
    {
        return this.appCloseIcon;
    }
}
