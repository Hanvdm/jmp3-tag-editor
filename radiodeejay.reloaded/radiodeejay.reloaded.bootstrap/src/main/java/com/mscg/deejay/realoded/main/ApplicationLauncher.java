package com.mscg.deejay.realoded.main;

import org.eclipse.core.runtime.adaptor.EclipseStarter;

public class ApplicationLauncher {

    public static void main(String args[]) throws Exception {
        // prevent bug with log4j class loader
        System.setProperty("log4j.ignoreTCL", "");

        // add the parameter -clean (if not present)
        boolean found = false;
        for(String arg : args) {
            if("-clean".equals(arg)) {
                found = true;
                break;
            }
        }
        if(!found) {
            String tmp[] = new String[args.length + 1];
            tmp[0] = "-clean";
            System.arraycopy(args, 0, tmp, 1, args.length);
            args = tmp;
        }

        EclipseStarter.main(args);
    }

}
