function DeezerPluginManager() {
	this.pollingInterval = null;
	this.playercontrol = null;
	this.tools = null;
	this.playButton = null;
	this.pauseButton = null;
	this.prevButton = null;
	this.noSong = null;
	this.currentSong = null;
}

DeezerPluginManager.urlPatter = new RegExp("http[s]?://.*\\.?deezer\.com.*");

DeezerPluginManager.prototype = {
    
    pollingInterval: null,
    playercontrol: null,
    tools: null,
    playButton: null,
    pauseButton: null,
    nextButton: null,
    prevButton: null,
    noSong: null,
    currentSong: null,
		
    init: function() {
        var me = this;

        gBrowser.addEventListener("load", function(event) {
            me.onPageLoad(event);
        }, true);
        
        this.initReferences(null);
    },
    
    initReferences: function(event) {
    	if(this.tools == null) {
	    	var me = this;
	    	this.tools = document.getElementById("deezer-plugin-toolbar-tools");
	    	if(this.tools != null) {
		        this.playButton = document.getElementById("deezer-plugin-toolbar-play-button");
		        this.pauseButton = document.getElementById("deezer-plugin-toolbar-pause-button");
		        this.nextButton = document.getElementById("deezer-plugin-toolbar-next-button");
		        this.prevButton = document.getElementById("deezer-plugin-toolbar-prev-button");
		        this.noSong = document.getElementById("deezer-plugin-toolbar-nosong");
		        this.currentSong = document.getElementById("deezer-plugin-toolbar-song");
		        this.disableButtons();
		        pollingInterval = setInterval(function(){me.pollTabs();}, 1500);
	    	}
        }
    },
    
    destroy: function() {
    	clearInterval(this.pollingInterval);
    },

    onPageLoad: function(event) {
    	
    },
    
    pollTabs: function() {
    	var windowMediator = Components.classes["@mozilla.org/appshell/window-mediator;1"].
    	                         getService(Components.interfaces.nsIWindowMediator);
    	var winEnum = windowMediator.getEnumerator(null);
    	
    	var exitLoop = false;
    	while(winEnum.hasMoreElements() && !exitLoop) {
    		var win = winEnum.getNext();
    		for(var i = 0, l = win.gBrowser.tabContainer.itemCount; i < l && !exitLoop; i++) {
        		var browser = win.gBrowser.getBrowserForTab(win.gBrowser.tabContainer.getItemAtIndex(i));
        		var match = DeezerPluginManager.urlPatter.exec(browser.currentURI.spec);
        		if(match != null) {
        			if(this.playercontrol == null) {
        	    		this.enableButtons();
        	    		this.initPlayerControl();
        			}
        			this.updateFromPage(browser);
        			
        			exitLoop = true;
        		}
        		else {
        			this.disableButtons();
        			this.playercontrol = null;
        		}
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
    	this.currentSong.hidden = true;
    	this.noSong.hidden = false;
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
    	
    	var currentTrack = document.getElementById("current-track");
    	var currentArtist = document.getElementById("current-artist");
    	var song = currentTrack.innerHTML + " - " + currentArtist.innerHTML;
    	this.currentSong.value = song;
    	this.currentSong.hidden = false;
    	this.noSong.hidden = true;
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
window.addEventListener("aftercustomization", function(event){deezerPluginManager.initReferences(event);}, false); 