function DeezerPluginManager() {

}

DeezerPluginManager.prototype = {
    
    init: function() {
        var me = this;

        gBrowser.addEventListener("load", function(event) {
            me.onPageLoad(event);
        }, true);

    },

    onPageLoad: function(event) {

    },
    
    execute: function() {
    	var browser = gBrowser.getBrowserForTab(gBrowser.selectedTab);
    	var win = browser.contentWindow;
    	
    	var playercontrol = win.wrappedJSObject.playercontrol;
    	playercontrol.doAction('play');
    }
    
}

var deezerPluginManager = new DeezerPluginManager();

window.addEventListener("load", function(){deezerPluginManager.init()}, false);