/*
 * Copyright (c) XIAOWEI CHEN, 2010.
 * All Rights Reserved. Reproduction in whole or in part is prohibited
 * without the written consent of the copyright owner.
 * 
 * XIAOWEI CHEN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. XIAOWEI CHEN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * 
 * All rights reserved.
 */
package com.candybon.manager;

import com.candybon.tinyserver.GenericServer;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Note: set property correctly according to your environment
 * @author Xiaowei Chen
 */
public class ClusterStarter {
    private String ROOT_DIR = System.getProperty("user.dir")
            + File.separator + "target"
            + File.separator + "classes"
            + File.separator + "var";
    private String CONF_PATH = ROOT_DIR + File.separator + "server.prop";
    private static final Logger log = Logger.getLogger(SimpleStarter.class.getName());
    
    public static void main(String[] args) throws IOException {
        new ClusterStarter().defaultStart();
    }

    public void defaultStart() {
        
        MyWebServerThread server1 = new MyWebServerThread();
        MyWebServerThread server2 = new MyWebServerThread();
        
        Thread webserver_thread1 = new Thread(server1);
        Thread webserver_thread2 = new Thread(server2);
        
        webserver_thread1.start();
        webserver_thread2.start();
    }
    
    private class MyWebServerThread implements Runnable {
        private GenericServer server;
        public void run() {
            try {
                File config = new File(CONF_PATH);
                server = new GenericServer(config);
                server.start();
            } catch (final Exception ex) {
                log.log(Level.SEVERE, "Could not start webserver{0}", ex.getMessage());
                System.exit(0);
            }
        }
        
        public int getPort() {
            if(server != null) {
                return server.getPort();
            } else {
                return -1;
            }
        }
    }
}
