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
public class CustomizedStarter {
    private String ROOT_DIR = System.getProperty("user.dir")
            + File.separator + "target"
            + File.separator + "classes"
            + File.separator + "var";
    private String CONF_PATH = ROOT_DIR + File.separator + "server.prop";
    private static final Logger log = Logger.getLogger(SimpleStarter.class.getName());
    
    public static void main(String[] args) throws IOException {
        new CustomizedStarter().defaultStart();
    }

    public void defaultStart() {
        Thread webserver_thread = new Thread(new MyWebServerThread());
        webserver_thread.start();
    }

    private class MyWebServerThread implements Runnable {

        public void run() {
            try {
                File config = new File(CONF_PATH);
                new GenericServer(config).start();
            } catch (final Exception ex) {
                log.log(Level.SEVERE, "Could not start webserver{0}", ex.getMessage());
                System.exit(0);
            }
        }
    }
}
