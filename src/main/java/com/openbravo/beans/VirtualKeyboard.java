/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.beans;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

/**
 * A simple virtual keyboard in the Brazilian ABNT2 layout.
 *
 * In order to use this class you must:
 *
 * 1. Create a new instance providing the size of the virtual keyboard; <br>
 * 2. Provide a text component that will be used to store the the keys typed
 * (this is performed with a separate call to setCurrentTextComponent; <br>
 * 3. Call the show method in order to show the virtual keyboard in a given
 * JFrame.
 *
 * @author Wilson de Carvalho
 */
public class VirtualKeyboard implements FocusListener {

    /**
     * Private class for storing key specification.
     */
    private class Key {

        public final int keyCode;
        public final String value;
        public final String shiftValue;

        public Key(int keyCode, String value, String shiftValue, int width_ratio) {
            this.keyCode = keyCode;
            this.value = value;
            this.shiftValue = shiftValue;
        }

        public Key(int keyCode, String value) {
            this(keyCode, value, value, 1);
        }

        public Key(int keyCode, String value, String shiftValue) {
            this(keyCode, value, shiftValue, 1);
        }

        public boolean hasShiftValue() {
            return !this.value.equals(this.shiftValue);
        }

        public boolean isLetter() {
            return value.length() == 1
                    && Character.isLetter(value.toCharArray()[0]);
        }
    }
    
    private Dimension fullSize;
    private Dimension smallSize;
    private JPanel p;
    // Special keys definition
    private final Key TAB_KEY = new Key(KeyEvent.VK_TAB, "Tab");
    private final Key CAPS_LOCK_KEY = new Key(KeyEvent.VK_CAPS_LOCK, "Caps Lock");
    private final Key SHIFT_KEY = new Key(KeyEvent.VK_SHIFT, "⇪");
    private final Key ACUTE_KEY = new Key(KeyEvent.VK_DEAD_ACUTE, "´", "`", 1);
    private final Key GRAVE_KEY = new Key(KeyEvent.VK_DEAD_GRAVE, "`");
    private final Key TILDE_CIRCUMFLEX_KEY = new Key(KeyEvent.VK_DEAD_TILDE, "~", "^", 1);
    private final Key CIRCUMFLEX_KEY = new Key(KeyEvent.VK_DEAD_TILDE, "^");

    private JPanel keyPanel = new JPanel();
    private JPanel numPanel = new JPanel();
    
    // First key row
    private Key[] row1 = new Key[]{
        new Key(KeyEvent.VK_A, "a", "T"), new Key(KeyEvent.VK_Z, "z", "Z"),
        new Key(KeyEvent.VK_E, "e", "E"), new Key(KeyEvent.VK_R, "r", "R"),
        new Key(KeyEvent.VK_T, "t", "T"), new Key(KeyEvent.VK_Y, "y", "Y"),
        new Key(KeyEvent.VK_U, "u", "U"), new Key(KeyEvent.VK_I, "i", "I"),
        new Key(KeyEvent.VK_O, "o", "O"), new Key(KeyEvent.VK_P, "p", "P"),};

    // First key row
    private Key[] row2 = new Key[]{
        new Key(KeyEvent.VK_Q, "q", "Q"), new Key(KeyEvent.VK_S, "s", "S"),
        new Key(KeyEvent.VK_D, "d", "D"), new Key(KeyEvent.VK_F, "f", "F"),
        new Key(KeyEvent.VK_G, "g", "G"), new Key(KeyEvent.VK_H, "h", "H"),
        new Key(KeyEvent.VK_J, "j", "J"), new Key(KeyEvent.VK_K, "k", "K"),
        new Key(KeyEvent.VK_I, "l", "L"), new Key(KeyEvent.VK_M, "m", "M"),};

    // First key row
    private Key[] row3 = new Key[]{
        new Key(KeyEvent.VK_W, "w", "W"), new Key(KeyEvent.VK_X, "x", "X"),
        new Key(KeyEvent.VK_C, "c", "C"), new Key(KeyEvent.VK_V, "v", "V"),
        new Key(KeyEvent.VK_B, "b", "B"), new Key(KeyEvent.VK_N, "n", "N"),
        new Key(KeyEvent.VK_COMMA, ","), new Key(KeyEvent.VK_EURO_SIGN, "€"),
        new Key(KeyEvent.VK_BACK_SPACE, "←", "←", 2)
    };

    private Key[] row4 = new Key[]{
        SHIFT_KEY, new Key(KeyEvent.VK_AT, "@"),
        new Key(KeyEvent.VK_DIVIDE, "/"), new Key(KeyEvent.VK_SPACE, " ", " ", 4),
        new Key(KeyEvent.VK_UNDERSCORE, "_"),
        new Key(KeyEvent.VK_LEFT, "◁"),
        new Key(KeyEvent.VK_RIGHT, "▷"),};

    // First key row
    private Key[] numRow1 = new Key[]{
        new Key(KeyEvent.VK_7, "7"),
        new Key(KeyEvent.VK_8, "8"),
        new Key(KeyEvent.VK_9, "9")};

    private Key[] numRow2 = new Key[]{
        new Key(KeyEvent.VK_4, "4"),
        new Key(KeyEvent.VK_5, "5"),
        new Key(KeyEvent.VK_6, "6"),};

    private Key[] numRow3 = new Key[]{
        new Key(KeyEvent.VK_1, "1"),
        new Key(KeyEvent.VK_2, "2"),
        new Key(KeyEvent.VK_3, "3")};

    private Key[] numRow4 = new Key[]{
        new Key(KeyEvent.VK_MINUS, "-"),
        new Key(KeyEvent.VK_0, "0"),
        new Key(KeyEvent.VK_PERIOD, ".")
    };
    private Key[] hiddenRow = new Key[]{
        new Key(KeyEvent.VK_ESCAPE, "Hide"),
    };

    private final Map<Key, JKeyboardButton> buttons;
    private Component currentComponent;
    private JTextComponent lastFocusedTextComponent;
    private JFrame frame;
    private boolean isCapsLockPressed = false;
    private boolean isShiftPressed = false;
    private Color defaultColor;
    private Key accentuationBuffer;

    public VirtualKeyboard() {
        this.buttons = new HashMap();
    }

    /**
     * Initializes the virtual keyboard and shows in the informed JFrame.
     *
     * @param frame JFrame that will be used to show the virtual keyboard.
     * @param keyboardPanel The panel where this keyboard will be held.
     */
    public void show(JFrame frame, JPanel keyboardPanel) {
        this.frame = frame;
        currentComponent = frame.getFocusOwner();
        if (currentComponent == null) {
            currentComponent = frame.getFocusTraversalPolicy().getFirstComponent(frame);
        }
//        keyboardPanel.setLayout(new GridLayout(5, 1));
        BorderLayout layout = new BorderLayout();
        layout.setHgap(50);
        keyboardPanel.setLayout(layout);
        this.p = keyboardPanel;
        keyPanel.setOpaque(false);
        keyPanel.setLayout(new GridLayout(4, 1));
        
        numPanel.setOpaque(false);
        numPanel.setLayout(new GridLayout(4, 1));
        numPanel.setSize(new Dimension(200, keyboardPanel.getSize().height));

        keyPanel.add(initRow(row1, keyPanel.getSize(), false));
        keyPanel.add(initRow(row2, keyPanel.getSize(), false));
        keyPanel.add(initRow(row3, keyPanel.getSize(), false));
        keyPanel.add(initRow(row4, keyPanel.getSize(), false));

        numPanel.add(initRow(numRow1, numPanel.getSize(), false));
        numPanel.add(initRow(numRow2, numPanel.getSize(), false));
        numPanel.add(initRow(numRow3, numPanel.getSize(), false));
        numPanel.add(initRow(numRow4, numPanel.getSize(), false));
        
        
        JPanel hiddenPanel = new JPanel();
        hiddenPanel.setOpaque(false);
        hiddenPanel.setLayout(new GridLayout(1, 1));
        hiddenPanel.setSize(new Dimension(hiddenPanel.getSize().width, 80));
        hiddenPanel.add(initRow(hiddenRow, hiddenPanel.getSize(), true));

        keyboardPanel.add(keyPanel, BorderLayout.CENTER);
        keyboardPanel.add(numPanel, BorderLayout.EAST);
        keyboardPanel.add(hiddenPanel, BorderLayout.SOUTH);
        
        fullSize = new Dimension(keyboardPanel.getSize().width, 200);
        smallSize = new Dimension(fullSize.width, 20);
    }

    private JPanel initRow(Key[] keys, Dimension dimensions, boolean isEscape) {
        JPanel p = new JPanel(new GridLayout(1, keys.length));
        p.setOpaque(false);
        int buttonWidth = dimensions.width / keys.length;
        int buttonHeight = dimensions.height / 4; // number of rows
        for (int i = 0; i < keys.length; ++i) {
            Key key = keys[i];
            JKeyboardButton button;
            if (buttons.containsKey(key)) {
                button = buttons.get(key);
            } else {
                button = new JKeyboardButton(key.value);
                button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                button.addFocusListener(this);
                buttons.put(key, button);
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Key button_key = null;
                        for (Key key : buttons.keySet()) {
                            if (buttons.get(key).equals(e.getSource())) {
                                button_key = key;
                            }
                        }
                        if (button_key != null) {
                            actionListener(button_key);
                        }
                    }
                });
                
                if(isEscape)
                    btnEscape = button;
            }
            p.add(button);
        }
        return p;
    }
    
    JKeyboardButton btnEscape = null;
    
    private void actionListener(Key key) {
        
        if(key.keyCode == KeyEvent.VK_ESCAPE) {
            escapePressed();
            return;
        }
        
        if (currentComponent == null || !(currentComponent instanceof JComponent)) {
            return;
        }
        
        ((JComponent) currentComponent).requestFocus();
        JTextComponent currentTextComponent = getCurrentTextComponent();
        switch (key.keyCode) {
            case KeyEvent.VK_CAPS_LOCK:
                capsLockPressed();
                break;
            case KeyEvent.VK_SHIFT:
                shiftPressed();
                break;
            case KeyEvent.VK_BACK_SPACE:
                if (currentTextComponent == null) {
                    return;
                }
                backspacePressed(currentTextComponent);
                break;
            case KeyEvent.VK_TAB:
                tabPressed();
                break;
            case KeyEvent.VK_LEFT:
                arrowPressed(currentTextComponent, false);
                break;
            case KeyEvent.VK_RIGHT:
                arrowPressed(currentTextComponent, true);
                break;
            default:
                if (currentTextComponent == null) {
                    return;
                }
                otherKeyPressed(key, currentTextComponent);
                break;
        }
    }

    private void escapePressed() {
        boolean visible = keyPanel.isVisible();
        keyPanel.setVisible( ! visible);
        numPanel.setVisible( ! visible);

        p.setPreferredSize( ! visible ? fullSize : smallSize);
        if(btnEscape != null)
            btnEscape.setText( ! visible ? "Hide" : "Show");
    }
    
    private void arrowPressed(JTextComponent component, boolean direction) {
        if (currentComponent instanceof JTextComponent) {
            int caretPosition = component.getCaretPosition();
            if (!component.getText().isEmpty()) {
                
                if(direction)
                    caretPosition += 1;
                else
                    caretPosition -= 1;
                
                if(caretPosition<=0)
                    caretPosition = 0;
                if(caretPosition > component.getText().length())
                    caretPosition = component.getText().length();
                    
                component.setCaretPosition(caretPosition);
            }
        }
    }

    private void capsLockPressed() {
        isCapsLockPressed = !isCapsLockPressed;
        for (Map.Entry<Key, JKeyboardButton> entry : buttons.entrySet()) {
            Key k = entry.getKey();
            JKeyboardButton b = entry.getValue();
            if (k.isLetter() && k.hasShiftValue()) {
                if (isCapsLockPressed) {
                    b.setText(k.shiftValue);
                } else {
                    b.setText(k.value);
                }
            }
        };

        if (isCapsLockPressed) {
            if (defaultColor == null) {
                defaultColor = buttons.get(SHIFT_KEY).getBackground();
            }
            buttons.get(CAPS_LOCK_KEY).setBackground(Color.orange);
        } else {
            buttons.get(CAPS_LOCK_KEY).setBackground(defaultColor);
        }
    }

    private void shiftPressed() {
        isShiftPressed = !isShiftPressed;
        for (Map.Entry<Key, JKeyboardButton> entry : buttons.entrySet()) {
            Key k = entry.getKey();
            JKeyboardButton b = entry.getValue();
            if (k.hasShiftValue()) {
                if (isShiftPressed) {
                    b.setText(k.shiftValue);
                } else {
                    b.setText(k.value);
                }
            }
        };
        if (isShiftPressed) {
            if (defaultColor == null) {
                defaultColor = buttons.get(SHIFT_KEY).getBackground();
            }
            buttons.get(SHIFT_KEY).setBackground(Color.orange);
        } else {
            buttons.get(SHIFT_KEY).setBackground(defaultColor);
        }
    }

    private void backspacePressed(JTextComponent component) {
        if (currentComponent instanceof JTextComponent) {
            int caretPosition = component.getCaretPosition();
            if (!component.getText().isEmpty() && caretPosition > 0) {
                try {
                    component.setText(component.getText(0, caretPosition - 1)
                            + component.getText(caretPosition,
                                    component.getText().length() - caretPosition));
                } catch (BadLocationException ex) {
                }
                component.setCaretPosition(caretPosition - 1);
            }
        }
    }

    private void tabPressed() {
        if (currentComponent != null && currentComponent instanceof JComponent) {
            Component nextComponent = ((JComponent) currentComponent).getNextFocusableComponent();
            if (nextComponent != null) {
                nextComponent.requestFocus();
                this.currentComponent = nextComponent;
            }
        }
    }

    private void otherKeyPressed(Key key, JTextComponent currentTextComponent) {
        if (key.isLetter()) {
            String keyString;
            if (accentuationBuffer == null) {
                keyString = key.value;
            } else {
                switch (key.keyCode) {
                    case KeyEvent.VK_A:
                        keyString = accentuationBuffer
                                == ACUTE_KEY ? "á"
                                        : accentuationBuffer == GRAVE_KEY ? "à"
                                                : accentuationBuffer == TILDE_CIRCUMFLEX_KEY ? "ã"
                                                        : accentuationBuffer == CIRCUMFLEX_KEY ? "â" : key.value;
                        break;
                    case KeyEvent.VK_E:
                        keyString = accentuationBuffer
                                == ACUTE_KEY ? "é"
                                        : accentuationBuffer == GRAVE_KEY ? "è"
                                                : accentuationBuffer == TILDE_CIRCUMFLEX_KEY ? "~e"
                                                        : accentuationBuffer == CIRCUMFLEX_KEY ? "ê" : key.value;
                        break;
                    case KeyEvent.VK_I:
                        keyString = accentuationBuffer
                                == ACUTE_KEY ? "í"
                                        : accentuationBuffer == GRAVE_KEY ? "ì"
                                                : accentuationBuffer == TILDE_CIRCUMFLEX_KEY ? "~i"
                                                        : accentuationBuffer == CIRCUMFLEX_KEY ? "î" : key.value;
                        break;
                    case KeyEvent.VK_O:
                        keyString = accentuationBuffer
                                == ACUTE_KEY ? "ó"
                                        : accentuationBuffer == GRAVE_KEY ? "ò"
                                                : accentuationBuffer == TILDE_CIRCUMFLEX_KEY ? "õ"
                                                        : accentuationBuffer == CIRCUMFLEX_KEY ? "ô" : key.value;
                        break;
                    case KeyEvent.VK_U:
                        keyString = accentuationBuffer
                                == ACUTE_KEY ? "ú"
                                        : accentuationBuffer == GRAVE_KEY ? "ù"
                                                : accentuationBuffer == TILDE_CIRCUMFLEX_KEY ? "~u"
                                                        : accentuationBuffer == CIRCUMFLEX_KEY ? "û" : key.value;
                    default:
                        keyString = key.value;
                        break;
                }
                accentuationBuffer = null;
            }
            if (isCapsLockPressed) {
                keyString = keyString.toUpperCase();
                if (isShiftPressed) {
                    shiftPressed();
                }
            } else if (isShiftPressed) {
                keyString = keyString.toUpperCase();
                shiftPressed();
            }
            addText(currentTextComponent, keyString);
        } else if (key == ACUTE_KEY || key == TILDE_CIRCUMFLEX_KEY) {
            if (key == ACUTE_KEY) {
                if (!isShiftPressed) {
                    accentuationBuffer = key;
                } else {
                    accentuationBuffer = GRAVE_KEY;
                }
            } else if (key == TILDE_CIRCUMFLEX_KEY) {
                if (!isShiftPressed) {
                    accentuationBuffer = key;
                } else {
                    accentuationBuffer = CIRCUMFLEX_KEY;
                }
            }
            if (isShiftPressed) {
                shiftPressed();
            }
        } else {
            String keyString;
            if (isCapsLockPressed) {
                keyString = key.value.toUpperCase();
                if (isShiftPressed) {
                    shiftPressed();
                }
            } else if (isShiftPressed) {
                keyString = key.shiftValue;
                shiftPressed();
            } else {
                keyString = key.value;
            }
            addText(currentTextComponent, keyString);
        }
    }

    private JTextComponent getCurrentTextComponent() {
        if (currentComponent != null && currentComponent instanceof JTextComponent) {
            return (JTextComponent) currentComponent;
        } else {
            return null;
        }
    }

    /**
     * Adds text considering the caret position.
     *
     * @param component Text component.
     * @param text Text that will be added.
     */
    private void addText(JTextComponent component, String text) {
        int caretPosition = component.getCaretPosition();
        try {
            component.setText(component.getText(0, caretPosition)
                    + text + component.getText(caretPosition,
                            component.getText().length() - caretPosition));
            component.setCaretPosition(caretPosition + 1);
        } catch (BadLocationException ex) {
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        Component previousComponent = e.getOppositeComponent();
        if (previousComponent != null && !(previousComponent instanceof JKeyboardButton
                && buttons.values().contains((JKeyboardButton) previousComponent))) {
            this.currentComponent = previousComponent;
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
    }
}
