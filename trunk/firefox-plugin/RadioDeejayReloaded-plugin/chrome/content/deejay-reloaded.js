function RadioDeejayReloadedDownload() {
    this.menuItem = null;
    this.popupMenuItem = null;
}

RadioDeejayReloadedDownload.prototype = {
    menuItem: null,
    popupMenuItem: null,
    
    init: function() {
        var me = this;

        this.menuItem = document.getElementById("deejay-reloaded-download-menu");
        this.popupMenuItem = document.getElementById("deejay-reloaded-download-menu-popup");
        gBrowser.addEventListener("load", function(event) {
            me.onPageLoad(event);
        }, true);
        gBrowser.tabContainer.addEventListener("TabSelect", function(event) {
            me.onTabSelection(event);
        }, false);
    },

    onPageLoad: function(event) {
        var win = event.originalTarget.defaultView;  
        if (!win || win.frameElement) {  
            return;
        }
        this.checkTabLocation(win.location.host, win.location.pathname);
    },

    onTabSelection: function(event) {
        if(event && event.originalTarget) {
            var browser = gBrowser.getBrowserForTab(event.originalTarget);
            var host = null;
            try {
                host = browser.currentURI.host;
            } catch(e){}
            var path = null;
            try {
                path = browser.currentURI.path;
            } catch(e){}
            this.checkTabLocation(host, path);
        }
    },

    checkTabLocation: function(host, path) {
        if(!host || !path) {
            this.menuItem.disabled = true;
            this.popupMenuItem.disabled = true;
        }
        else {
            if(host.lastIndexOf("deejay.it") >= 0 && path.indexOf("/dj/radio/reloaded") >= 0) {
                this.menuItem.disabled = false;
                this.popupMenuItem.disabled = false;
            }
            else {
                this.menuItem.disabled = true;
                this.popupMenuItem.disabled = true;
            }
        }
    },

    startDownload: function() {
        if(!this.menuItem.disabled) {
            alert("Starting download");
        }
    }
}

var radioDeejayDownloader = new RadioDeejayReloadedDownload();

window.addEventListener("load", function(){radioDeejayDownloader.init()}, false);