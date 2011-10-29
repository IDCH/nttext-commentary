// START WRAPPER: The YUI.add wrapper is added by the build system, when you use 
// YUI Builder to build your component from the raw source in this file

YUI.add("nttcc", function(Y) {
	
	/* Any frequently used shortcuts, strings and constants */
	var Lang = Y.Lang;
	
	
//=====================================================================================
// PREVIEW DIALOG CONTROL
//=====================================================================================
	/**
	 * Displays a dialog to inform users of the preview nature of this site. This dialog 
	 * will be displayed the first time a user visits the site to help ensure that they
	 * are aware that this is a 'preview' version of the commentary. After it is displayed
	 * a cookie will be set to ensure that the user isn't troubled with it again. If auto-
	 * displayed, it will be automatically hidden after a short delay. 
	 */
	
	/* PreviewDialog class constructor */
	function PreviewDialog(config) {
	    PreviewDialog.superclass.constructor.apply(this, arguments);
	}
	
	/* Required NAME static field. */
	PreviewDialog.NAME = "previewDialog";
	
	/*
	 * The attribute configuration for the widget. This defines the core user facing state of the widget
	 */
	PreviewDialog.ATTRS = {
		text : {
			value : 
				'<p>' +
		        '  <strong>During our preview phase</strong>, the Textual Commentary is limited to ' +
		        '  searches in the book of 1 Peter and Philippians. We are constantly adding more ' +
		        '  commentary material to our database, so stay tuned. <br> ' + 
		        ' '  +
		        '  <a href="#" title="Sign up for email updates">Notify me of updates &#8250;</a> '+
		        '</p>' 
				
		},
		
		display : {
			value : false
		}
	};
	
	
	PreviewDialog.HTML_PARSER = {

	     contentNode: "div.notification-content",
	     
	     closeNode: "a.close",

	     text : function(srcNode) {
	    	 var cNode = srcNode.one("div.notification-content");
	    	 return (cNode) ? cNode.get("innerHTML") : "";
	     }

	};
	
	/* PreviewDialog extends the base Widget class */
	Y.extend(PreviewDialog, Y.Widget, {
	
	    initializer: function(config) {
//	    	alert(config);
	    	// not doing anything interesting yet
	    },
	
	    destructor : function() {
	    	// free up any listeners
	    },
	
	    renderUI : function() {
//	    	this.get("closeNode");
	        // TODO need to add code to sign up for announcements
	    	
	    	// set the text of the preview box (if not set)
	    	// set 
	    },
	
	    bindUI : function() {
	    	var src = this.get("srcNode");
	    	
	    	// add a link to the 'a' tag
	    	var closeButton = src.one("a.close");
	    	closeButton.after("click", Y.bind(this.hide, this));
	    	
	    	var previewButton = Y.one("#preview_btn");
	    	previewButton.after("click", Y.bind(this.toggle, this));
	    	
	    	this.after('visibleChange', Y.bind(this._afterVisbileChange, this));
	    	
	    	
	    },
	
	    syncUI : function() {
	    },
	    
	   
	    toggle : function() {
	    	if (this.get("visible")) {
    			this.hide();
    		} else {
    			this.show();
    		}
	    },
	    /* Listeners, UI update methods */
        
	    
        _afterVisbileChange : function(e) {
        	// TODO add animation support if loaded.
        	var srcNode = this.get("srcNode");
        	if (this.get("visible"))
        		srcNode.setStyle("display", "block");
        	else
        		srcNode.setStyle("display", "none");
        }
	    
	});
	
	
//=====================================================================================
// NAVIGATION CONTROL
//=====================================================================================

	
// nttcc - New Testament Textual Criticism Commentary - FOR NOW
Y.namespace("nttext.comm").PreviewDialog = PreviewDialog;

}, "3.2.0", {requires:["widget", "substitute"]});
// END WRAPPER