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
package org.netbeans.modules.exceptions;

import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import org.netbeans.lib.uihandler.BugTrackingAccessor;
import org.netbeans.modules.uihandler.Installer;
import org.openide.awt.HtmlBrowser;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays Exception reporter results.
 */
public final class ReporterResultTopComponent extends TopComponent implements HyperlinkListener {

    private static ReporterResultTopComponent instance;
    private static final RequestProcessor RP = new RequestProcessor("ReporterResultTopComponentLoader", 3);
    private static final Logger LOG = Logger.getLogger(ReporterResultTopComponent.class.getName());
    private static boolean showUpload = false;
    /** path to the icon used by the component and its open action */
    private static final String ICON_PATH = "org/netbeans/modules/exceptions/reporter.png";
    private static final String PREFERRED_ID = "ReporterResultTopComponent";

    private ReporterResultTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ReporterResultTopComponent.class, "CTL_ReporterResultTopComponent"));
        setToolTipText(NbBundle.getMessage(ReporterResultTopComponent.class, "HINT_ReporterResultTopComponent"));
        dataDisplayer.addHyperlinkListener(this);
        dataDisplayer.setContentType("text/html");
        dataDisplayer.putClientProperty(javax.swing.JTextPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        Image img = ImageUtilities.loadImage(ICON_PATH, true);
        setIcon(img);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        dataDisplayer = new javax.swing.JEditorPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        dataDisplayer.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        dataDisplayer.setEditable(false);
        jScrollPane1.setViewportView(dataDisplayer);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(320, 35));

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(ReporterResultTopComponent.class, "ReporterResultTopComponent.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addContainerGap(173, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        RP.post(new URLDisplayer(true));
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane dataDisplayer;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized ReporterResultTopComponent getDefault() {
        if (instance == null) {
            instance = new ReporterResultTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ReporterResultTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ReporterResultTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(ReporterResultTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");   //NOI18N
            return getDefault();
        }
        if (win instanceof ReporterResultTopComponent) {
            return (ReporterResultTopComponent) win;
        }
        Logger.getLogger(ReporterResultTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID + //NOI18N
                "' ID. That is a potential source of errors and unexpected behavior.");         //NOI18N
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        if (showUpload) {
            return;
        }
        RP.post(new URLDisplayer(false));
    }

    @Override
    public void componentClosed() {
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    public static void showUploadDone(final URL url){
        if (EventQueue.isDispatchThread()) {
            try {
                findInstance().showUploadDoneImpl(url);
            } catch (IOException ex) {
                handleIOException(url, ex);
            }
        } else {
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    try {
                        findInstance().showUploadDoneImpl(url);
                    } catch (IOException ex) {
                        handleIOException(url, ex);
                    }
                }
            });
        }
    }

    private void showUploadDoneImpl(final URL url) throws IOException {
        assert (EventQueue.isDispatchThread());
        showUpload = true;
        open();
        showUpload = false;
        loadPage(url, true);
    }

    private class URLDisplayer implements Runnable {
        private String urlStr = null;
        private final boolean show;

        public URLDisplayer(boolean show) {
            this.show = show;
        }

        @Override
        public void run() {
            if (EventQueue.isDispatchThread()){
                try {
                    loadPage(new URL(urlStr), show);
                } catch (MalformedURLException ex) {
                    handleIOException(urlStr, ex);
                }
            }else{
                String userName = new ExceptionsSettings().getUserName();
                if (userName != null && !"".equals(userName)) {             //NOI18N
                    urlStr = NbBundle.getMessage(ReporterResultTopComponent.class, "userNameURL") + userName;
                } else {
                    String userId = Installer.findIdentity();
                    if (userId != null) {
                        urlStr = NbBundle.getMessage(ReporterResultTopComponent.class, "userIdURL") + userId;
                    }
                }
                if (urlStr == null) {
                    return; // XXX prompt to log in?
                }
                EventQueue.invokeLater(this);
            }
        }
    }

    private void loadPage(URL url, boolean show) {
        assert (EventQueue.isDispatchThread());
        dataDisplayer.setText(getLoadingPage(url));
        RP.post(new PageUploader(url, show));
    }

    private class PageUploader implements Runnable{

        private URL localData = null;
        private final URL url;
        private final boolean show;

        private PageUploader(URL url, boolean show) {
            this.url = url;
            this.show = show;
        }

        public void run() {
            try {
                if (EventQueue.isDispatchThread()) {
                    if (show) {
                        ReporterResultTopComponent topComponent =
                                ReporterResultTopComponent.this;
                        topComponent.requestVisible();
                        topComponent.requestActive();
                    }
                    dataDisplayer.setPage(localData);
                } else {
                    LOG.log(Level.FINE, "Loading: {0}", url);        //NOI18N
                    localData = uploadURL(url);
                    EventQueue.invokeLater(this);
                }
            } catch (IOException ex) {
                handleIOException(url, ex);
            }
        }
        
    }

    private static URL uploadURL(URL url) throws IOException {
        assert(!EventQueue.isDispatchThread());
        File tmpFile = File.createTempFile("loading", ".html");        //NOI18N
        tmpFile.deleteOnExit();
        FileOutputStream fw = new FileOutputStream(tmpFile);
        try{
            URLConnection conn = url.openConnection();
            conn.setReadTimeout(200000);
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setRequestProperty("User-Agent", "NetBeans");      //NOI18N
            InputStream is = conn.getInputStream();
            if (is == null) {
                throw new IOException("Null input stream from "+conn);
            }
            try{
                while(true) {
                    int ch = is.read();
                    if (ch == -1) {
                        break;
                    }
                    fw.write(ch);
                }
            }finally{
                is.close();
            }
        }finally{
            fw.close();
        }
        return Utilities.toURI(tmpFile).toURL();
    }

    private static void handleIOException(URL url, IOException ex) {
        handleIOException(url.toString(), ex);
    }

    private static void handleIOException(final String url, IOException ex) {
        LOG.log(Level.INFO, "URL Loading failed", ex);        //NOI18N
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                instance.dataDisplayer.setText(NbBundle.getMessage(ReporterResultTopComponent.class,"no_data_found", url));
            }
        });
    }

    private static String getLoadingPage(URL url) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title></title></head><body>");
        sb.append(NbBundle.getMessage(ReporterResultTopComponent.class, "LoadingMessage"));
        sb.append("<a href=\"").append(url.toString()).append("\">").append(url).append("</a>");
        sb.append("</body></html>");
        return sb.toString();
    }

    @Override
    public void hyperlinkUpdate(final HyperlinkEvent e) {
        if (!HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
            return;
        }
        BugTrackingAccessor accessor = Lookup.getDefault().lookup(BugTrackingAccessor.class);
        if (accessor != null){
            AttributeSet ats = e.getSourceElement().getAttributes();
            Object attribute = ats.getAttribute(HTML.getTag("a"));
            if (attribute instanceof SimpleAttributeSet) {
                SimpleAttributeSet attributeSet = (SimpleAttributeSet) attribute;
                Object bugId = attributeSet.getAttribute(HTML.getAttributeKey("id"));
                if (bugId != null){
                    try{
                        Integer.parseInt(bugId.toString());
                        LOG.log(Level.FINE, "Open issue {0}", bugId);
                        accessor.openIssue(bugId.toString());
                        return;
                    }catch(NumberFormatException nfe){
                        LOG.log(Level.INFO, "Invalid id attribute", nfe);
                    }
                }
            }
        } else {
            LOG.log(Level.INFO, "Bugzilla Accessor not found");
        }
        RP.post(new Runnable(){

            @Override
            public void run() {
                HtmlBrowser.URLDisplayer.getDefault().showURL(e.getURL());
            }

        });
    }

    static final class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return ReporterResultTopComponent.getDefault();
        }
    }
}
