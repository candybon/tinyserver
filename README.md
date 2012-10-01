tinyserver
==========

A generic, salable, multi-threaded HTTP server. 
	Written in Java.
	Build in Maven.
	Allow concurrent connections, and easy to scale to multicore architecture / distributed environment.
	Easy to modify and integrate to your application.
	
	
How to use:
1) Example 1: Start with Single Server.

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
	
	Class GenericServer is our tiny server, in the code above it will choose an available port in your system to startup.
	You can place your resource file under ./tinyserver/src/main/resources/var/public_html/
	
2) Example 2: Start with your own server configuration

	try {
		File config = new File(CONF_PATH);
		new GenericServer(config).start();
	} catch (final Exception ex) {
		log.log(Level.SEVERE, "Could not start webserver{0}", ex.getMessage());
		System.exit(0);
	}
	
	The server will use the property specified in your CONF_PATH to startup. Example property file is available at ./tinyserver/src/main/resources/var/server.prop
	
3) Example 3: Start multiple servers (useful in cloud environment)
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
	
	You can create as many server as you want, and modify the code to fit your requirements.
	
