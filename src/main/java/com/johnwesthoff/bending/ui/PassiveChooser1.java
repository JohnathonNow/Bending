/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.Session;
import com.johnwesthoff.bending.spells.Spell;

/**
 *
 * @author John
 */
public class PassiveChooser1 extends javax.swing.JPanel {
    private static final long serialVersionUID = 472334502759693391L;
    /**
     * Creates new form SpellChooser
     */
    ArrayList<ImageIcon> images = Spell.passiveimages;
    ArrayList<String> names = Spell.passivenames;
    ArrayList<String> tips = Spell.passivetips;

    public PassiveChooser1(SpellList1 e) {
        initComponents();
        // jList1.addListSelectionListener(e);
        // jList1.setListData(Spell.spellnames.toArray());j
        jButton1.addActionListener(e);
        jButton1.setActionCommand("Choose2");
        jList1.setListData(Spell.passivenames.toArray());
        jList1.setCellRenderer(new PassiveChooser1.ComboBoxRenderer());
        jList1.setBackground(Color.gray);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // super.paintComponent(g);

        g.drawImage(Session.getInstance().clientui.bimage, 0, 0, getWidth(), getHeight(), null);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<Object>();
        jButton1 = new javax.swing.JButton();

        jList1.setModel(new javax.swing.AbstractListModel<Object>() {
            private static final long serialVersionUID = 1L;
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8",
                    "Item 9", "Item 10" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setRequestFocusEnabled(false);
        jScrollPane1.setViewportView(jList1);

        jButton1.setText("Choose");
        jButton1.addActionListener(event -> {
            //TODO: no behavior defined
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup().addGap(153, 153, 153).addComponent(jButton1)
                                        .addGap(0, 168, Short.MAX_VALUE)))
                        .addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 222,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18).addComponent(jButton1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        // pack();
    }// </editor-fold>

    public javax.swing.JList<Object> getList() {
        return jList1;
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JList<Object> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration

    class ComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {
        private static final long serialVersionUID = -8010091627549067273L;

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        /*
         * This method finds the image and text corresponding to the selected value and
         * returns the label, set up to display the text and image.
         */
        @Override
        public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            // Set the icon and text. If icon was null, say so.
            ImageIcon icon;
            String pet;
            Spell.passives.get(index).unlock();
            if (Spell.passives.get(index).locked) {
                icon = Spell.lockedImage;
                pet = "UNLOCKED AT " + Spell.passives.get(index).unlockXP + " XP";
                if (isSelected)
                    jList1.setSelectedIndex(0);
            } else {
                pet = names.get(index);
                icon = images.get(index);
            }

            setIcon(icon);
            if (icon != null) {
                setText(pet);
                setFont(list.getFont());
                this.setToolTipText(tips.get(index));
            } else {
                setText("ERROR");
            }

            return this;
        }
    }
}
