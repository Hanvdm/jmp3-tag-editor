function RadioDeejayReloadedDownload() {
    this.menuItem = null;
}

RadioDeejayReloadedDownload.prototype.init = function() {
    var me = this;

    this.menuItem = gBrowser.ownerDocument.getElementById("deejay-reloaded-download-menu");
    gBrowser.addEventListener("load", function(event) {
        me.onPageLoad(event);
    }, true);
    gBrowser.tabContainer.addEventListener("TabSelect", function(event) {
        me.onTabSelection(event);
    }, false);
}

RadioDeejayReloadedDownload.prototype.onPageLoad = function(event) {
    var win = event.originalTarget.defaultView;  
    if (!win || win.frameElement) {  
        return;
    }
    this.checkTabLocation(win.location.host, win.location.pathname);
}

RadioDeejayReloadedDownload.prototype.onTabSelection = function(event) {
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
}

RadioDeejayReloadedDownload.prototype.checkTabLocation = function(host, path) {
    if(!host || !path)
        this.menuItem.disabled = true;
    else {
        if(host.lastIndexOf("deejay.it") >= 0 && path.indexOf("/dj/radio/reloaded") >= 0)
            this.menuItem.disabled = false;
        else
            this.menuItem.disabled = true;
    }
}

RadioDeejayReloadedDownload.prototype.startDownload = function() {
    if(!this.menuItem.disabled) {
        alert("Starting download");
    }
}

var radioDeejayDownloader = new RadioDeejayReloadedDownload();

window.addEventListener("load", function(){radioDeejayDownloader.init()}, false);