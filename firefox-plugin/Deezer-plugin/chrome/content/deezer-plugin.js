function DeezerPluginManager() {
	this.pollingInterval = null;
	this.playercontrol = null;
	this.tools = null;
	this.toolsLabels = null;
	this.playButton = null;
	this.pauseButton = null;
	this.prevButton = null;
	this.noSong = null;
	this.currentSongTitle = null;
	this.currentSongAuthor = null;
}

DeezerPluginManager.urlPatter = new RegExp("http[s]?://.*\\.?deezer\.com.*");

DeezerPluginManager.prototype = {
    
    pollingInterval: null,
    playercontrol: null,
    tools: null,
    toolsLabels: null,
    playButton: null,
    pauseButton: null,
    nextButton: null,
    prevButton: null,
    noSong: null,
    currentSongTitle: null,
    currentSongAuthor: null,
		
    init: function() {
        var me = this;

        gBrowser.addEventListener("load", function(event) {
            me.onPageLoad(event);
        }, true);
        
        this.initReferences(null);
    },
    
    initReferences: function(event) {
    	this.destroy();
    	
    	this.tools = document.getElementById("deezer-plugin-toolbar-tools");
    	this.toolsLabels = document.getElementById("deezer-plugin-toolbar-labels");
    	
    	if(this.tools != null || this.toolsLabels != null) {
	    	var me = this;
	    	
	    	if(this.tools != null) {
		        this.playButton = document.getElementById("deezer-plugin-toolbar-play-button");
		        this.pauseButton = document.getElementById("deezer-plugin-toolbar-pause-button");
		        this.nextButton = document.getElementById("deezer-plugin-toolbar-next-button");
		        this.prevButton = document.getElementById("deezer-plugin-toolbar-prev-button");
	    	}
	    	if(this.toolsLabels != null) {
	    		this.noSong = document.getElementById("deezer-plugin-toolbar-nosong");
		        this.currentSongTitle = document.getElementById("deezer-plugin-toolbar-song-title");
		        this.currentSongAuthor = document.getElementById("deezer-plugin-toolbar-song-author");
	    	}
	    	
	    	this.disableButtons();
	    	pollingInterval = setInterval(function(){me.pollTabs();}, 1500);
        }
    },
    
    destroy: function() {
    	if(this.pollingInterval != null) {
	    	clearInterval(this.pollingInterval);
	    	this.pollingInterval = null;
    	}
    },

    onPageLoad: function(event) {
    	
    },
    
    pollTabs: function() {
    	var windowMediator = Components.classes["@mozilla.org/appshell/window-mediator;1"].
    	                         getService(Components.interfaces.nsIWindowMediator);
    	var winEnum = windowMediator.getEnumerator(null);
    	
    	var tabFound = false;
    	while(winEnum.hasMoreElements() && !tabFound) {
    		var win = winEnum.getNext();
    		var winBrowser = win.gBrowser;
    		if(!winBrowser)
    			winBrowser = gBrowser;
    		for(var i = 0, l = winBrowser.tabContainer.itemCount; i < l && !tabFound; i++) {
    			var actualTab = winBrowser.tabContainer.getItemAtIndex(i);
        		var browser = winBrowser.getBrowserForTab(actualTab);
        		var match = DeezerPluginManager.urlPatter.exec(browser.currentURI.spec);
        		if(match != null) {
        			if(this.playercontrol == null) {
        				this.initPlayerControl(actualTab);
        	    		this.enableButtons();
        			}
        			this.updateFromPage(browser);
        			
        			tabFound = true;
        		}
        	}
    	}
    	
    	if(!tabFound) {
    		this.disableButtons();
    		this.playercontrol = null;
    	}
    },
    
    initPlayerControl: function(actualTab) {
    	var browser = gBrowser.getBrowserForTab(actualTab);
    	var win = browser.contentWindow;
    	
    	this.playercontrol = win.wrappedJSObject.playercontrol;
    },
    
    enableButtons: function() {
    	if(this.tools != null) {
	    	this.playButton.disabled = false;
	    	this.pauseButton.disabled = false;
	    	this.nextButton.disabled = false;
	    	this.prevButton.disabled = false;
    	}
    },
    
    disableButtons: function() {
    	if(this.tools != null) {
	    	this.playButton.disabled = true;
	    	this.pauseButton.disabled = true;
	    	this.switchPlay(true);
	    	this.nextButton.disabled = true;
	    	this.prevButton.disabled = true;
    	}
    	if(this.toolsLabels != null) {
	    	this.currentSongTitle.hidden = true;
	    	this.currentSongAuthor.hidden = true;
	    	this.noSong.hidden = false;
    	}
    },
    
    switchPlay: function(pause) {
    	this.pauseButton.hidden = pause;
    	this.playButton.hidden = !pause;
    },
    
    updateFromPage: function(browser) {
    	var document = browser.contentDocument;
    	
    	if(this.tools != null) {
	    	var playBox = document.getElementById("h_play");
	    	var playLink = playBox.getElementsByClassName("h_icn_play");
	    	playLink = playLink[0];
	    	this.switchPlay(playLink.style.display != "none");
    	}
    	
    	if(this.toolsLabels != null) {
	    	var currentTrack = document.getElementById("current-track");
	    	var currentArtist = document.getElementById("current-artist");
	    	this.currentSongTitle.value = (currentTrack.innerHTML + " ").replace("&amp;", "&");
	    	this.currentSongTitle.hidden = false;
	    	this.currentSongAuthor.value = ("- " + currentArtist.innerHTML).replace("&amp;", "&");
	    	this.currentSongAuthor.hidden = false;
	    	this.noSong.hidden = true;
    	}
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