function DeezerPluginManager() {
	this.pollingInterval = null;
	this.playercontrol = null;
	this.playButton = null;
	this.pauseButton = null;
	this.prevButton = null;
}

DeezerPluginManager.urlPatter = new RegExp("http[s]?://.*\\.?deezer\.com.*");

DeezerPluginManager.prototype = {
    
    pollingInterval: null,
    playercontrol: null,
    playButton: null,
    pauseButton: null,
    nextButton: null,
    prevButton: null,
		
    init: function() {
        var me = this;

        this.playButton = document.getElementById("deezer-plugin-toolbar-play-button");
        this.pauseButton = document.getElementById("deezer-plugin-toolbar-pause-button");
        this.nextButton = document.getElementById("deezer-plugin-toolbar-next-button");
        this.prevButton = document.getElementById("deezer-plugin-toolbar-prev-button");
        
        gBrowser.addEventListener("load", function(event) {
            me.onPageLoad(event);
        }, true);

        this.disableButtons();
        pollingInterval = setInterval(function(){me.pollTabs();}, 1500);
    },
    
    destroy: function() {
    	clearInterval(this.pollingInterval);
    },

    onPageLoad: function(event) {
    	
    },
    
    pollTabs: function() {
    	for(var i = 0, l = gBrowser.tabContainer.itemCount; i < l; i++) {
    		var browser = gBrowser.getBrowserForTab(gBrowser.tabContainer.getItemAtIndex(i));
    		var match = DeezerPluginManager.urlPatter.exec(browser.currentURI.spec);
    		if(match != null) {
    			if(this.playercontrol == null) {
    	    		this.enableButtons();
    	    		this.initPlayerControl();
    			}
    			this.updateFromPage(browser);
    			
    			break;
    		}
    		else {
    			this.disableButtons();
    			this.playercontrol = null;
    		}
    	}
    },
    
    initPlayerControl: function() {
    	var browser = gBrowser.getBrowserForTab(gBrowser.selectedTab);
    	var win = browser.contentWindow;
    	
    	this.playercontrol = win.wrappedJSObject.playercontrol;
    },
    
    enableButtons: function() {
    	this.playButton.disabled = false;
    	this.pauseButton.disabled = false;
    	this.nextButton.disabled = false;
    	this.prevButton.disabled = false;
    },
    
    disableButtons: function() {
    	this.playButton.disabled = true;
    	this.pauseButton.disabled = true;
    	this.switchPlay(true);
    	this.nextButton.disabled = true;
    	this.prevButton.disabled = true;
    },
    
    switchPlay: function(pause) {
    	this.pauseButton.hidden = pause;
    	this.playButton.hidden = !pause;
    },
    
    updateFromPage: function(browser) {
    	var document = browser.contentDocument;
    	var playBox = document.getElementById("h_play");
    	var playLink = playBox.getElementsByClassName("h_icn_play");
    	playLink = playLink[0];
    	this.switchPlay(playLink.style.display != "none");
    },
    
    play: function() {
    	if(this.playercontrol != null) {
    		this.playercontrol.doAction('play');
    		this.switchPlay(false);
    	}
    },
    
    pause: function() {
    	if(this.playercontrol != null) {
    		this.playercontrol.doAction('pause');
    		this.switchPlay(true);
    	}
    },
    
    next: function() {
    	if(this.playercontrol != null)
    		this.playercontrol.doAction('next');
    },
    
    previous: function() {
    	if(this.playercontrol != null)
    		this.playercontrol.doAction('prev');
    }
    
}

var deezerPluginManager = new DeezerPluginManager();

window.addEventListener("load", function(){deezerPluginManager.init()}, false);
window.addEventListener("unload", function(){deezerPluginManager.destroy()}, false);