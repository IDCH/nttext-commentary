/**
 * 
 */
package org.idch.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.idch.util.PersistenceUtil;

/**
 * http://download.oracle.com/docs/cd/E13222_01/wls/docs70/webapp/app_events.html
 * @author Neal Audenaert
 */
public class ContextListener implements ServletContextListener 
{
     // implement the required context init method
    public void contextInitialized(ServletContextEvent sce) {
    }

     // implement the required context destroy method
    public void contextDestroyed(ServletContextEvent sce) {
    	PersistenceUtil.shutdown();
    }
}