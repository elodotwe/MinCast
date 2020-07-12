package com.jacobarau.mincast.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.io.IOException;
import java.net.URL;

//@RunWith(RobolectricTestRunner.class)
public class DownloaderTest {
    // Commented out to keep me from hitting this URL every time I run tests. The downloader seems to work at the moment.
//    @Test
    public void testDownload() throws IOException {
        Downloader downloader = new Downloader();
        downloader.downloadFile(new URL("http://feedproxy.google.com/~r/CatholicInASmallTown/~5/Vf35fWSH1t0/CST_463.mp3"), new File("/home/jacob/cst463.mp3"), new Downloader.ProgressListener() {
            @Override
            public void onProgress(long position, Long total) {
                System.out.println("onProgress() called with: position = [" + position + "], total = [" + total + "]");
            }
        });
    }
}
