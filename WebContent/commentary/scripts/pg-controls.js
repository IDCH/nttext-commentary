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
		test : { value : "Hello World" },
		
		display : {
			value : false
		},
		
		attrA : {
	        value: "A"                     // The default value for attrA, used if the user does not set a value during construction.
	
	        /*
	        , valueFn: "_defAttrAVal"      // Can be used as a substitute for "value", when you need access to "this" to set the default value.
	         
	        , setter: "_setAttrA"          // Used to normalize attrA's value while during set. Refers to a prototype method, to make customization easier
	        , getter: "_getAttrA"          // Used to normalize attrA's value while during get. Refers to a prototype method, to make customization easier
	        , validator: "_validateAttrA"  // Used to validate attrA's value before updating it. Refers to a prototype method, to make customization easier
	
	        , readOnly: true               // Cannot be set by the end user. Can be set by the component developer at any time, using _set
	        , writeOnce: true              // Can only be set once by the end user (usually during construction). Can be set by the component developer at any time, using _set
	        
	        , lazyAdd: false               // Add (configure) the attribute during initialization. 
	        
	                                       // You only need to set lazyAdd to false if your attribute is
	                                       // setting some other state in your setter which needs to be set during initialization 
	                                       // (not generally recommended - the setter should be used for normalization. 
	                                       // You should use listeners to update alternate state). 
	
	        , broadcast: 1                 // Whether the attribute change event should be broadcast or not.
	        */
	    }
	};
	
	/* PreviewDialog extends the base Widget class */
	Y.extend(PreviewDialog, Y.Widget, {
	
	    initializer: function(config) {
	    	// not doing anything interesting yet
	    },
	
	    destructor : function() {
	    	// free up any listeners
	    },
	
	    renderUI : function() {
	        // TODO need to add code to sign up for announcements 
	    },
	
	    bindUI : function() {
	    	this.after('visibleChange', Y.bind(this._afterVisbileChange, this));
	    },
	
	    syncUI : function() {
	    },
	    
	  
	    // Beyond this point is the PreviewDialog specific application and rendering logic
	
	    // for each attribute, attrA, define the following methods as needed
	    
	    //---------------------------
	    // HELPER METHODS FOR: attrA 
        //---------------------------
	    
	    /* Attribute state supporting methods (see attribute config above) */
	    _defAttrAVal : function() {
	    	// default value
        },

        _setAttrA : function(attrVal, attrName) {
            // return attrVal.toUpperCase();
        },

        _getAttrA : function(attrVal, attrName) {
            // return attrVal.toUpperCase();
        },

        _validateAttrA : function(attrVal, attrName) {
            // return Lang.isString(attrVal);
        },

        /* Listeners, UI update methods */

        _afterAttrAChange : function(e) {
            /* Listens for changes in state, and asks for a UI update (controller). */

            // this._uiSetAttrA(e.newVal);
        },

        _uiSetAttrA : function(val) {
            /* Update the state of attrA in the UI (view) */

            // this._mynode.set("innerHTML", val);
        },
	    
	    
	   
	    /* Listeners, UI update methods */
        
        _afterVisbileChange : function(e) {
        	var srcNode = this.get("srcNode");
        	if (this.get("visible"))
        		srcNode.setStyle("display", "block");
        	else
        		srcNode.setStyle("display", "none");
        }
	    
	});
// nttcc - New Testament Textual Criticism Commentary - FOR NOW
Y.namespace("nttext.comm").PreviewDialog = PreviewDialog;

}, "3.2.0", {requires:["widget", "substitute"]});
// END WRAPPER