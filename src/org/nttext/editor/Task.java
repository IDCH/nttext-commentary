/**
 * 
 */
package org.nttext.editor;

import java.util.Date;

/**
 * Represents a task that can be assigned to a user, such as reviewing an entry instance.
 * @author Neal Audenaert
 */
public interface Task {
    
    public Date getDueDate();
    
    public void setDueDate(Date date);
    
    public String getTitle();
    
    public String setTitle();
    
    public String getDescription();
    
    public String setDescription();
    
    // TODO change to User and/or account
    public String getAssignee();

    public void setAssignee(String editor);
}
