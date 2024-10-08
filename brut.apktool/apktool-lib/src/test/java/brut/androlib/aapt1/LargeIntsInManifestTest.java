/*
 *  Copyright (C) 2010 Ryszard Wiśniewski <brut.alll@gmail.com>
 *  Copyright (C) 2010 Connor Tumbleson <connor.tumbleson@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package brut.androlib.aapt1;

import brut.androlib.*;
import brut.directory.ExtFile;
import brut.common.BrutException;
import brut.util.OS;
import java.io.File;
import java.io.IOException;

import org.junit.*;

public class LargeIntsInManifestTest extends BaseTest {

    @BeforeClass
    public static void beforeClass() throws Exception {
        TestUtils.cleanFrameworkFile();
        sTmpDir = new ExtFile(OS.createTempDirectory());
        TestUtils.copyResourceDir(LargeIntsInManifestTest.class, "aapt1/issue767/", sTmpDir);
    }

    @AfterClass
    public static void afterClass() throws BrutException {
        OS.rmdir(sTmpDir);
    }

    @Test
    public void checkIfLargeIntsAreHandledTest() throws BrutException, IOException {
        String apk = "issue767.apk";

        // decode issue767.apk
        ApkDecoder apkDecoder = new ApkDecoder(new ExtFile(sTmpDir + File.separator + apk));
        sTestOrigDir = new ExtFile(sTmpDir + File.separator + apk + ".out");

        File outDir = new File(sTmpDir + File.separator + apk + ".out");
        apkDecoder.decode(outDir);

        // build issue767
        Config config = Config.getDefaultConfig();
        config.aaptVersion = 1;
        ExtFile testApk = new ExtFile(sTmpDir, apk + ".out");
        new ApkBuilder(testApk, config).build(null);
        String newApk = apk + ".out" + File.separator + "dist" + File.separator + apk;

        // decode issue767 again
        apkDecoder = new ApkDecoder(new ExtFile(sTmpDir + File.separator + newApk));
        sTestNewDir = new ExtFile(sTmpDir + File.separator + apk + ".out.two");

        outDir = new File(sTmpDir + File.separator + apk + ".out.two");
        apkDecoder.decode(outDir);

        compareXmlFiles("AndroidManifest.xml");
    }
}
