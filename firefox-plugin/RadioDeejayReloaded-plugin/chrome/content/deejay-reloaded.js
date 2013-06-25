function RadioDeejayReloadedDownload() {
    this.menuItem = null;
    this.popupMenuItem = null;
    this.popupMenuSeparator = null;
}

RadioDeejayReloadedDownload.urlPatter = new RegExp("http://.*\\.?deejay\.it/audio/(\\d{4}\\d{2}\\d{2})/\\d+?.+");
RadioDeejayReloadedDownload.downloadPatter = new RegExp(".*file=(http://.*).*");
RadioDeejayReloadedDownload.downloadHelper = new RadioDeejayReloadedDownloadHelper();
//RadioDeejayReloadedDownload.clipboard = Components.classes["@mozilla.org/widget/clipboardhelper;1"].
//                                                   getService(Components.interfaces.nsIClipboardHelper);

RadioDeejayReloadedDownload.prototype = {
    menuItem: null,
    popupMenuItem: null,
    popupMenuSeparator: null,

    init: function() {
        var me = this;

        this.menuItem = document.getElementById("deejay-reloaded-download-menu");
        this.popupMenuItem = document.getElementById("deejay-reloaded-download-menu-popup");
        this.popupMenuSeparator = document.getElementById("deejay-reloaded-download-menu-popup-separator");
        gBrowser.addEventListener("load", function(event) {
            me.onPageLoad(event);
        }, true);
        gBrowser.tabContainer.addEventListener("TabSelect", function(event) {
            me.onTabSelection(event);
        }, false);
        this.checkTabLocation(gBrowser.getBrowserForTab(gBrowser.selectedTab).currentURI.spec);
    },

    onPageLoad: function(event) {
        var win = event.originalTarget.defaultView;  
        if (!win || win.frameElement) {  
            return;
        }
        this.checkTabLocation(win.location.toString());
    },

    onTabSelection: function(event) {
        if(event && event.originalTarget) {
            var browser = gBrowser.getBrowserForTab(event.originalTarget);
            this.checkTabLocation(browser.currentURI.spec);
        }
    },

    checkTabLocation: function(pageUrl) {
        if(!pageUrl)
            this.setMenuEnabledStatus(false)
        else {
            var match = RadioDeejayReloadedDownload.urlPatter.exec(pageUrl);
            if(match != null)
                this.setMenuEnabledStatus(true);
            else
                this.setMenuEnabledStatus(false);
        }
    },
    
    setMenuEnabledStatus: function(enabled) {
        this.menuItem.disabled = !enabled;
        this.popupMenuItem.disabled = !enabled;
        this.popupMenuItem.hidden = !enabled;
        this.popupMenuSeparator.hidden = !enabled;
    },

    startDownload: function() {
        if(!this.menuItem.disabled) {
            var xPathScript = content.document.evaluate("//object[@id='flashadvplayer']/param[@name='flashvars']/@value",
            		                                    content.document, null, XPathResult.ANY_TYPE, null);
            var script = xPathScript.iterateNext().textContent;
            var match = RadioDeejayReloadedDownload.downloadPatter.exec(script);
            if(match != null && match.length >= 2) {
            	var url = match[1];
            	
            	if("undefined" == typeof(DTA) || "undefined" == typeof(DTA.saveSingleLink)) {
            		// DownThem All! is not installed, so use default download
	            	RadioDeejayReloadedDownload.downloadHelper.saveHelper(url, "", null,
	            			                                              true, content.document);
            	}
            	else {
            		// DownThem All! is installed, so use it to download the file
//            		RadioDeejayReloadedDownload.clipboard.copyString(url);
            		DTA.saveSingleLink(window, false, url);
            	}
            }
        }
    }
}

var radioDeejayDownloader = new RadioDeejayReloadedDownload();

window.addEventListener("load", function(){radioDeejayDownloader.init()}, false);