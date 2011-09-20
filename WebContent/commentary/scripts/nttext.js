/* File: nttext.js
 * Author: Neal Audenaert <neal@idch.org>
 * Created: 15 September 2011
 * 
 * Description: 
 *   Main bootstrapping script for the CNTTS commentary site.
 */

// TEMPORARY configuration information. This will ultimately be migrated into a real build
//			 system for JavaScript components.
var NTTEXT_config = {
	modules : {
		nttcc   : { fullpath : '/nttext/scripts/pg-controls.js', requires : ['widget', 'substitute']}
	}
};

YUI(NTTEXT_config).use('nttcc', 'tabview', function(Y) {
	
	
	Y.log("loaded");
	var cssName = Y.ClassNameManager.getClassName(Y.nttext.comm.PreviewDialog.NAME, "value");
	Y.log(cssName);
	
	var previewDialog = new Y.nttext.comm.PreviewDialog(
			{ srcNode: "#preview_ctrl .preview-notification",
		      visible : false });
	previewDialog.render();
	
	previewDialog.after('render', function() {
		
		previewDialog.show();
		
		Y.later(2000, previewDialog, Y.bind(previewDialog.hide, previewDialog));
		
	});
	
// TODO preview widget
    
    // ENABLE HEADER BUTTONS
    Y.delegate('click', function(e) {
        var buttonID = e.currentTarget.get('id');
        var preview = Y.Node.one('div.preview-notification');
        
        if (buttonID === 'preview_btn') {
        	Y.Node.one("div.blue-notification").setStyle('display', 'block');
            e.preventDefault();
        } else if (buttonID == 'close-bn') {
        	Y.Node.one("div.blue-notification").setStyle('display', 'none');
        	e.preventDefault();
        } else if (buttonID == 'signin') {
        	// TODO this needs to actually log in the user
        	Y.Node.one("#user-account").removeClass("not-signed-in");
        	Y.Node.one("#user-account").addClass("signed-in");
        } else if (buttonID == 'signout') {
            Y.Node.one("#user-account").removeClass("signed-in");
        	Y.Node.one("#user-account").addClass("not-signed-in");
        } else if (buttonID == 'user-link') {
        	alert("Ack! Please Implement Me");
        }
        
        // TODO Signup for updates
    }, document, '#header .button');
    
    var template = 
    	'<div id="search-notice"><strong>Not found.</strong> During our preview ' + 
    	'phase only Philippians and 1 Peter are available. More books will be ' +
    	'added in the future.</div>';
    
    // TODO help widget
//     var help = Y.Node.one("#help-container");
//     help.hide();
    
//     Y.delegate('click', function(e) {
//     	alert('expand-topic');
//     	e.stop();
//     }, document, '.help-module .expand-topic');
    
  

	
});
