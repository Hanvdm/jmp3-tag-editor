function RadioDeejayReloadedDownloadHelper() {

}

RadioDeejayReloadedDownloadHelper.prototype = {
    // Helper function to wait for appropriate MIME-type headers and
    // then prompt the user with a file picker
    saveHelper : function(linkURL, linkText, dialogTitle, bypassCache, doc) {
        // canonical def in nsURILoader.h
        const NS_ERROR_SAVE_LINK_AS_TIMEOUT = 0x805d0020;

        // an object to proxy the data through to
        // nsIExternalHelperAppService.doContent, which will wait for the
        // appropriate MIME-type headers and then prompt the user with a
        // file picker
        function saveAsListener() {
        }
        saveAsListener.prototype = {
            extListener : null,

            onStartRequest : function saveLinkAs_onStartRequest(aRequest,
                    aContext) {

                // if the timer fired, the error status will have been caused by
                // that,
                // and we'll be restarting in onStopRequest, so no reason to
                // notify
                // the user
                if (aRequest.status == NS_ERROR_SAVE_LINK_AS_TIMEOUT)
                    return;

                timer.cancel();

                // some other error occured; notify the user...
                if (!Components.isSuccessCode(aRequest.status)) {
                    try {
                        const
                        sbs = Cc["@mozilla.org/intl/stringbundle;1"]
                                .getService(Ci.nsIStringBundleService);
                        const
                        bundle = sbs
                                .createBundle("chrome://mozapps/locale/downloads/downloads.properties");

                        const
                        title = bundle
                                .GetStringFromName("downloadErrorAlertTitle");
                        const
                        msg = bundle.GetStringFromName("downloadErrorGeneric");

                        const
                        promptSvc = Cc["@mozilla.org/embedcomp/prompt-service;1"]
                                .getService(Ci.nsIPromptService);
                        promptSvc.alert(doc.defaultView, title, msg);
                    } catch (ex) {
                    }
                    return;
                }

                var extHelperAppSvc = Cc["@mozilla.org/uriloader/external-helper-app-service;1"]
                        .getService(Ci.nsIExternalHelperAppService);
                var channel = aRequest.QueryInterface(Ci.nsIChannel);
                this.extListener = extHelperAppSvc.doContent(
                        channel.contentType, aRequest, doc.defaultView, true);
                this.extListener.onStartRequest(aRequest, aContext);
            },

            onStopRequest : function saveLinkAs_onStopRequest(aRequest,
                    aContext, aStatusCode) {
                if (aStatusCode == NS_ERROR_SAVE_LINK_AS_TIMEOUT) {
                    // do it the old fashioned way, which will pick the best
                    // filename
                    // it can without waiting.
                    saveURL(linkURL, linkText, dialogTitle, bypassCache, false,
                            doc.documentURIObject);
                }
                if (this.extListener)
                    this.extListener.onStopRequest(aRequest, aContext,
                            aStatusCode);
            },

            onDataAvailable : function saveLinkAs_onDataAvailable(aRequest,
                    aContext, aInputStream, aOffset, aCount) {
                this.extListener.onDataAvailable(aRequest, aContext,
                        aInputStream, aOffset, aCount);
            }
        }

        function callbacks() {
        }
        callbacks.prototype = {
            getInterface : function sLA_callbacks_getInterface(aIID) {
                if (aIID.equals(Ci.nsIAuthPrompt)
                        || aIID.equals(Ci.nsIAuthPrompt2)) {
                    // If the channel demands authentication prompt, we must
                    // cancel it
                    // because the save-as-timer would expire and cancel the
                    // channel
                    // before we get credentials from user. Both authentication
                    // dialog
                    // and save as dialog would appear on the screen as we fall
                    // back to
                    // the old fashioned way after the timeout.
                    timer.cancel();
                    channel.cancel(NS_ERROR_SAVE_LINK_AS_TIMEOUT);
                }
                throw Cr.NS_ERROR_NO_INTERFACE;
            }
        }

        // if it we don't have the headers after a short time, the user
        // won't have received any feedback from their click. that's bad. so
        // we give up waiting for the filename.
        function timerCallback() {
        }
        timerCallback.prototype = {
            notify : function sLA_timer_notify(aTimer) {
                channel.cancel(NS_ERROR_SAVE_LINK_AS_TIMEOUT);
                return;
            }
        }

        // set up a channel to do the saving
        var ioService = Cc["@mozilla.org/network/io-service;1"]
                .getService(Ci.nsIIOService);
        var channel = ioService.newChannelFromURI(makeURI(linkURL));
        channel.notificationCallbacks = new callbacks();

        var flags = Ci.nsIChannel.LOAD_CALL_CONTENT_SNIFFERS;

        if (bypassCache)
            flags |= Ci.nsIRequest.LOAD_BYPASS_CACHE;

        if (channel instanceof Ci.nsICachingChannel)
            flags |= Ci.nsICachingChannel.LOAD_BYPASS_LOCAL_CACHE_IF_BUSY;

        channel.loadFlags |= flags;

        if (channel instanceof Ci.nsIHttpChannel) {
            channel.referrer = doc.documentURIObject;
            if (channel instanceof Ci.nsIHttpChannelInternal)
                channel.forceAllowThirdPartyCookie = true;
        }

        // fallback to the old way if we don't see the headers quickly
        var timeToWait = gPrefService
                .getIntPref("browser.download.saveLinkAsFilenameTimeout");
        var timer = Cc["@mozilla.org/timer;1"].createInstance(Ci.nsITimer);
        timer.initWithCallback(new timerCallback(), timeToWait,
                timer.TYPE_ONE_SHOT);

        // kick off the channel with our proxy object as the listener
        channel.asyncOpen(new saveAsListener(), null);
    }
}