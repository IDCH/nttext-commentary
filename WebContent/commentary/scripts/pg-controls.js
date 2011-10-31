// START WRAPPER: The YUI.add wrapper is added by the build system, when you use 
// YUI Builder to build your component from the raw source in this file

YUI.add("nttcc", function(Y) {
	
	/* Any frequently used shortcuts, strings and constants */
	var Lang = Y.Lang,
		VerseRange = Y.idch.ref.VerseRange,
		VerseRef = Y.idch.ref.VerseRef,
		ReferenceParser = Y.idch.ref.ReferenceParser;
	
	
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
	    	closeButton.after("click", Y.bind(this._onCloseClick, this));
	    	
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
        
	    _onCloseClick : function(e) {
	    	e.halt();
	    	this.hide();
	    	return false;
	    },
	    
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
	// At the moment this is overkill, but it is likely to be some
	
	/* SearchControl class constructor */
	function SearchControl (config) {
		SearchControl.superclass.constructor.apply(this, arguments);
	}
	
	/* Required NAME static field. */
	SearchControl.NAME = "searchControl";
	
	/*
	 * The attribute configuration for the widget. This defines the core user facing state of the widget
	 */
	SearchControl.ATTRS = {
		parser : { value : ReferenceParser.NT }
		
	};
	
	/* SearchControl extends the base Widget class */
	Y.extend(SearchControl, Y.Widget, {
	
		_isMessageVisible : false,
	
	    initializer: function(config) {
	    	// not doing anything interesting yet
	    },
	
	    destructor : function() {
	    	// free up any listeners
	    },
	
	    renderUI : function() { /* we'll assume this uses on-page markup for now */  },
	
	    bindUI : function() {
	    	var src = this.get("srcNode"), 
	    		input, searchForm, searchLink;
	    	
	    	input = src.one("input");
	    	input.on('change', Y.bind(this._onInputChange, this));
	    	
	    	searchForm = src.one("form");
	    	searchForm.on('submit', Y.bind(this._onSubmit, this));
	    	
	    	searchLink = src.one("a.search-btn");
	    	searchLink.on('click', Y.bind(this._onSearchButtonClick, this));
	    },
	
	    syncUI : function() {  },
	    
	    /** 
	     * Submits the form and navigates to the selected entry, if the entry is valid. 
	     * Otherwise, takes no action.
	     */
	    submit : function(vs) {
	    	vs = vs || this.getValidReference();
	    	
	    	if (vs && vs.toOsisString) {
	    		if (!vs.verse) vs.setVerse(1);
	    		if (!vs.chapter) vs.setChapter(1);
	    		
	    		var path = document.location.pathname;
	    		var ix = path.lastIndexOf("entry"); 
	    		if (ix > 0) {
	    			path = path.substring(0, ix) + "entry/" + vs.toOsisString();
	    			document.location = path;
	    		}
	    	} else {
	    		this.showMessageBox();
	    	}
	    },
	    
	    getParsedReference : function() {
	    	var input = this.get("srcNode").one("input"),
	    		parser = this.get("parser"),
	    		order = parser.bookOrder,
		    	ref = parser.parse(input.get("value"));
	    	
	    	return (ref.indexOf("-") >= 0) 
    	    		? new VerseRange(order, ref) 
    				: new VerseRef(order, ref);
	    },
	   
	    /** 
	     * Returns <tt>true</tt> if this is a valid reference to a book in the 
	     */
	    getValidReference : function() {
	    	var vs = this.getParsedReference(),
    	    	bk = (vs instanceof VerseRange) ? vs.start.book : vs.book;
    	
	    	if (bk && ((bk.osisId == "1Pet") || (bk.osisId == "Phil"))) {
	    		return vs;
	    	} else {
	    		return false;
	    	}
	    },
	    
	    showMessageBox : function() {
	    	var src = this.get("srcNode"), 
	    		messageBox = src.one("div.search-notice");
	    	if (!this._isMessageVisible) {
		    	var anim = new Y.Anim({
		    	    node: messageBox,
		    	    from : { opacity : 0 },
		    	    to: { opacity : 1 },
		    	    easing: Y.Easing.easeOut,
		    	    duration: 0.5
		    	});
		    	
		    	messageBox.setStyle("visibility", "visible");
		    	anim.run();
		    	this._isMessageVisible = true;
		    	Y.later(5000, this, this.hideMessageBox);
	    	}
	    },
	    
	    hideMessageBox : function() {
	    	var src = this.get("srcNode"), 
	    		messageBox = src.one("div.search-notice");
	    	
	    	if (this._isMessageVisible) {
		    	var anim = new Y.Anim({
		    	    node: messageBox,
		    	    from : { opacity : 1 },
		    	    to: { opacity : 0 },
		    	    easing: Y.Easing.easeOut,
		    	    duration: 1.0
		    	});
		    	
		    	anim.on('end', function() {
			    	messageBox.setStyle("visibility", "hidden");
		    	});
		    	anim.run();
		    	this._isMessageVisible = false;
	    	}
	    },
	    
	    
	    /* Listeners, UI update methods */
        _onInputChange  : function(e) {
        	var vs = this.getParsedReference(),
        	    bk = (vs instanceof VerseRange) ? vs.start.book : vs.book;
        	
        	if (!bk) {
        		// TODO show drop down
        		// alert("Not a book");
        		this.showMessageBox();
        	} else if ((bk.osisId == "1Pet") || (bk.osisId == "Phil")) {
        		this.hideMessageBox();
//        		alert("OK: " + document.location);
        	} else {
        		this.showMessageBox();
        		// TODO show drop down
        		// alert("Incomplete Book: " + vs.format());
        	}
        	
        },
        
        _onSearchButtonClick : function(e) {
        	e.halt();
        	this.submit();
        	return false;
        },
        
        _onSubmit : function(e) {
        	e.halt();
        	this.submit();
        	return false;
        },
	    
        _afterVisbileChange : function(e) {
        	e.halt();
        	return false;
        }
	    
	});
	
// nttcc - New Testament Textual Criticism Commentary - FOR NOW
Y.namespace("nttext.comm").PreviewDialog = PreviewDialog;
Y.namespace("nttext.comm").SearchControl = SearchControl;

}, "3.2.0", {requires:["widget", "substitute", "anim", "idch-ref"]});
// END WRAPPER