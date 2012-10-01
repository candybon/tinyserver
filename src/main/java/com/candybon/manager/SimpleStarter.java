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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Xiaowei Chen
 */
public class SimpleStarter {

    private static final Logger log = Logger.getLogger(SimpleStarter.class.getName());

    public static void main(String[] args) throws IOException {
        try {
            new GenericServer().start();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Could not start webserver{0}", ex.getMessage());
            System.exit(0);
        }
    }
}
