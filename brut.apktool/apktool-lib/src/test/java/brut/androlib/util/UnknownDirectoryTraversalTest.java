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
package brut.androlib.util;

import brut.androlib.BaseTest;
import brut.androlib.TestUtils;
import brut.common.BrutException;
import brut.common.InvalidUnknownFileException;
import brut.common.RootUnknownFileException;
import brut.common.TraversalUnknownFileException;
import brut.directory.ExtFile;
import brut.util.BrutIO;
import brut.util.OS;
import brut.util.OSDetection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UnknownDirectoryTraversalTest extends BaseTest {

    @BeforeClass
    public static void beforeClass() throws Exception {
        sTmpDir = new ExtFile(OS.createTempDirectory());
        TestUtils.copyResourceDir(UnknownDirectoryTraversalTest.class, "util/traversal", sTmpDir);
    }

    @AfterClass
    public static void afterClass() throws BrutException {
        OS.rmdir(sTmpDir);
    }

    @Test
    public void validFileTest() throws BrutException, IOException {
        String validFileName = BrutIO.sanitizePath(sTmpDir, "file");
        assertEquals(validFileName, "file");

        File validFile = new File(sTmpDir, validFileName);
        assertTrue(validFile.isFile());
    }

    @Test(expected = TraversalUnknownFileException.class)
    public void invalidBackwardFileTest() throws BrutException, IOException {
        BrutIO.sanitizePath(sTmpDir, "../file");
    }

    @Test(expected = RootUnknownFileException.class)
    public void invalidRootFileTest() throws BrutException, IOException {
        String rootLocation = OSDetection.isWindows() ? "C:/" : File.separator;
        BrutIO.sanitizePath(sTmpDir, rootLocation + "file");
    }

    @Test(expected = InvalidUnknownFileException.class)
    public void noFilePassedTest() throws BrutException, IOException {
        BrutIO.sanitizePath(sTmpDir, "");
    }

    @Test(expected = TraversalUnknownFileException.class)
    public void invalidBackwardPathOnWindows() throws BrutException, IOException {
        String invalidPath;
        if (! OSDetection.isWindows()) {
            invalidPath = "../../app";
        } else {
            invalidPath = "..\\..\\app.exe";
        }

        BrutIO.sanitizePath(sTmpDir, invalidPath);
    }

    @Test
    public void validDirectoryFileTest() throws BrutException, IOException {
        String validFileName = BrutIO.sanitizePath(sTmpDir, "dir" + File.separator + "file");
        assertEquals("dir" + File.separator + "file", validFileName);
    }
}
