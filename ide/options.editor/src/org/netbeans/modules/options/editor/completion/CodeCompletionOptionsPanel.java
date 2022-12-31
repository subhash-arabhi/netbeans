/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.netbeans.modules.options.editor.completion;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;
import org.netbeans.api.options.OptionsDisplayer;
import org.netbeans.modules.editor.settings.storage.api.EditorSettings;
import org.netbeans.modules.options.editor.spi.PreferencesCustomizer;
import org.netbeans.modules.options.util.LanguagesComparator;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.WeakListeners;

/**
 * @author Dusan Balek
 */
@OptionsPanelController.Keywords(keywords = {"#KW_CodeCompletion"}, location = OptionsDisplayer.EDITOR, tabTitle="#CTL_CodeCompletion_DisplayName")
public class CodeCompletionOptionsPanel extends JPanel implements PropertyChangeListener {
    
    private CodeCompletionOptionsSelector selector;
    private PropertyChangeListener weakListener;
    private Object lastSelectedItem = null;
    
    /** 
     * Creates new form CodeCompletionOptionsPanel.
     */
    public CodeCompletionOptionsPanel () {
        initComponents ();

        // Languages combobox renderer
        cbLanguage.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof String) {
                    value = ((String)value).length() > 0
                            ? EditorSettings.getDefault().getLanguageName((String)value)
                            : org.openide.util.NbBundle.getMessage(CodeCompletionOptionsPanel.class, "LBL_AllLanguages"); //NOI18N
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

    }
    
    public void setSelector(CodeCompletionOptionsSelector selector) {
        if (this.selector != null) {
            this.selector.removePropertyChangeListener(weakListener);
        }

        this.selector = selector;

        if (this.selector != null) {
            this.weakListener = WeakListeners.propertyChange(this, this.selector);
            this.selector.addPropertyChangeListener(weakListener);

            // Languages combobox model
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            ArrayList<String> mimeTypes = new ArrayList<String>();
            mimeTypes.addAll(selector.getMimeTypes());
            Collections.sort(mimeTypes, LanguagesComparator.INSTANCE);

            for (String mimeType : mimeTypes) {
                model.addElement(mimeType);
            }
            cbLanguage.setModel(model);
            if (lastSelectedItem != null) {
                cbLanguage.setSelectedItem(lastSelectedItem);
            } else {
                cbLanguage.setSelectedIndex(0);
            }
        } else {
            if (cbLanguage.getSelectedItem() != null) {
                lastSelectedItem = cbLanguage.getSelectedItem();
            }
            cbLanguage.setModel(new DefaultComboBoxModel());
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        panel.setVisible(false);
        panel.removeAll();
        PreferencesCustomizer c = selector.getSelectedCustomizer();
        if (c != null)
            panel.add(c.getComponent(), BorderLayout.CENTER);
        panel.setVisible(true);
        cbLanguage.setSelectedItem(evt.getNewValue());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lLanguage = new javax.swing.JLabel();
        cbLanguage = new javax.swing.JComboBox();
        panel = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lLanguage.setLabelFor(cbLanguage);
        org.openide.awt.Mnemonics.setLocalizedText(lLanguage, org.openide.util.NbBundle.getMessage(CodeCompletionOptionsPanel.class, "CTL_Language")); // NOI18N

        cbLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                languageChanged(evt);
            }
        });

        panel.setOpaque(false);
        panel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lLanguage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lLanguage)
                    .addComponent(cbLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void languageChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_languageChanged
        selector.setSelectedMimeType((String)cbLanguage.getSelectedItem());
    }//GEN-LAST:event_languageChanged
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbLanguage;
    private javax.swing.JLabel lLanguage;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables

}
