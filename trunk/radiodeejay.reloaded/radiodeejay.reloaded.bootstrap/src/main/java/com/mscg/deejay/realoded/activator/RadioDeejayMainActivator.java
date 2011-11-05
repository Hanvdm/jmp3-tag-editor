package com.mscg.deejay.realoded.activator;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class RadioDeejayMainActivator implements BundleActivator {

    private static final String BUNDLES_FOLDER = "bundle";
    private static final String FILE_PREFIX = "file:";

    private String getBundleFileLocation(Bundle bundle) {
        try {
            String bundleFileName = bundle.getLocation();
            bundleFileName = bundleFileName.substring(bundleFileName.lastIndexOf(FILE_PREFIX) + FILE_PREFIX.length());
            bundleFileName = bundleFileName.substring(0, bundleFileName.lastIndexOf('/'));
            return bundleFileName;
        } catch(Exception e) {
            return bundle.getLocation();
        }
    }

    private void loadAllInstalledBundles(BundleContext context) throws Exception {
        Bundle mainBundle = context.getBundle();
        String mainBundleFileName = getBundleFileLocation(mainBundle);

        File mainBundleFile = new File(mainBundleFileName);
        File bundlesHolderFolder = mainBundleFile.getParentFile();
        File bundlesFolder = new File(bundlesHolderFolder, BUNDLES_FOLDER);

        if(bundlesFolder.exists() && bundlesFolder.isDirectory()) {
            File bundleFiles[] = bundlesFolder.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().endsWith(".jar");
                }
            });

            List<Bundle> newBundles = new LinkedList<Bundle>();
            for(File bundleFile : bundleFiles) {
                String relativePath = bundlesHolderFolder.getName() + "/" + BUNDLES_FOLDER + "/" + bundleFile.getName();
                newBundles.add(context.installBundle("file:" + relativePath + "/"));
            }

            int oldSize;
            do {
                oldSize = newBundles.size();
                for(Iterator<Bundle> it = newBundles.iterator(); it.hasNext();) {
                    try {
                        Bundle newBundle = it.next();
                        if(newBundle.getHeaders().get("Fragment-Host") == null) { // bundle is not a fragment
                            if(newBundle.getState() != Bundle.ACTIVE)
                                newBundle.start();
                        }
                        it.remove();
                    } catch(Exception e){}
                }
            } while(oldSize != 0 && newBundles.size() != 0 && oldSize != newBundles.size());
            if(newBundles.size() != 0)
                throw new Exception("Cannot start the following bundles: " + newBundles);
        }
    }

    @Override
    public void start(BundleContext context) throws Exception {
        loadAllInstalledBundles(context);
    }


    @Override
    public void stop(BundleContext context) throws Exception {

    }

}
