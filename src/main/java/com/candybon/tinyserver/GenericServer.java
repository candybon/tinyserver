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
package com.candybon.tinyserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A very simple, multi-threaded HTTP server.
 * 
 * @author Xiaowei Chen
 */
public class GenericServer {

    public static final String DEFAULT_HOME_DIR = System.getProperty("user.dir")
            + File.separator + "target" + File.separator + "classes"
            + File.separator + "var" + File.separator + "public_html";
    public static final int DEFAULT_CONN_TIMEOUT = 5000;
    public static final int DEFAULT_WORKERS_COUNT = 25;
    private static final Logger log = Logger.getLogger(GenericServer.class.getName());
    /* our server'clientSocket configuration information is stored in these properties */
    protected static Properties props = new Properties();

    /* Where worker workerPool stand idle */
    private List workerPool = new ArrayList();

    /* the web server'clientSocket virtual root */
    private File root;
    
    /* webserver port*/
    private int port;
    
    /* timeout on client connections */
    private int timeout = DEFAULT_CONN_TIMEOUT;

    /* max # worker workerPool */
    private int poolSize = DEFAULT_WORKERS_COUNT;

    public GenericServer() {
        root = new File(DEFAULT_HOME_DIR);
    }

    public GenericServer(File propFile) {
        try {
            loadProps(propFile);
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Error loading Property file {0}", ex);
        }
    }

    public void start() throws Exception {
        /* start worker workerPool */
        for (int i = 0; i < poolSize; ++i) {
            HttpHandler worker = new HttpHandler(this);
            (new Thread(worker, "worker #" + i)).start();
            workerPool.add(worker);
        }

        ServerSocket serverSocket = new ServerSocket(0);
        port = serverSocket.getLocalPort();
        log.log(Level.FINEST, "[Server] : Started on port {0}", port);
        System.out.println("[Server] : Started on port :" + port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            HttpHandler workerInPool = null;
            synchronized (workerPool) {
                if (workerPool.isEmpty()) {
                    HttpHandler worker = new HttpHandler(this);
                    worker.setSocket(clientSocket);
                    (new Thread(worker, "additional worker")).start();
                } else {
                    workerInPool = (HttpHandler) workerPool.get(0);
                    workerPool.remove(0);
                    workerInPool.setSocket(clientSocket);
                }
            }
        }
    }

    /* load customized properties from File */
    private void loadProps(File config) throws IOException {
        if (config != null && config.exists()) {
            InputStream is = new BufferedInputStream(new FileInputStream(config));
            props.load(is);
            is.close();
            String r = props.getProperty("root");
            if (r != null) {
                root = new File(r);
                if (!root.exists()) {
                    throw new Error(root + " doesn't exist as server root");
                }
            }
            r = props.getProperty("timeout");
            if (r != null) {
                timeout = Integer.parseInt(r);
            }
            r = props.getProperty("poolsize");
            if (r != null) {
                poolSize = Integer.parseInt(r);
            }
            r = props.getProperty("log");
            if (r != null) {
                log.log(Level.FINEST, "opening log file: {0}", r);
            }
        }

        /* if no properties were specified, choose defaults */
        if (root == null) {
            root = new File(System.getProperty(DEFAULT_HOME_DIR));
        }

        log.log(Level.FINEST, "root={0}", root);
        log.log(Level.FINEST, "timeout={0}", timeout);
        log.log(Level.FINEST, "poolsize={0}", poolSize);
    }

    public List getWorkerPool() {
        return workerPool;
    }

    public File getRoot() {
        return root;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getPoolSize() {
        return poolSize;
    }
    
    public int getPort() {
        return port;
    }
}
