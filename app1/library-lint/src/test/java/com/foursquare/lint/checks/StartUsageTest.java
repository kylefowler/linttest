package com.foursquare.lint.checks;

import com.android.annotations.NonNull;
import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import org.intellij.lang.annotations.Language;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class StartUsageTest extends LintDetectorTest {

    private final TestFile pilgrimSdkStub = java(""
            + "package com.foursquare;\n"
            + "import android.content.Context;\n"
            + "public class Foursquare {\n"
            + "  public static void start() {  }\n"
            + "}");

    public void testStartIsSuccessfullyCalled() throws Exception {
        // String test = System.getenv("ANDROID_HOME");
        @Language("JAVA") String source = ""
                + "package foo;\n"
                + "import android.app.Activity;\n"
                + "import android.os.Bundle;\n"
                + "import com.foursquare.Foursquare;\n"
                + "public class Example extends Activity {\n"
                + "  public void onCreate(Bundle bundle) {\n"
                + "    super.onCreate(bundle); \n"
                + "    Foursquare.start(); \n"
                + "  }\n"
                + "}";
        lint().files(
                java(source),
                pilgrimSdkStub
        )
                .client(new com.android.tools.lint.checks.infrastructure.TestLintClient() {
                    @Override
                    public File findResource(@NonNull String relativePath) {
                        return null;
                    }

                })
                .run()
                .expectClean();
    }

    public void testStartIsNotCalled() throws Exception {
        // String test = System.getenv("ANDROID_HOME");
        @Language("JAVA") String source = ""
                + "package foo;\n"
                + "import android.app.Activity;\n"
                + "import android.os.Bundle;\n"
                + "import com.foursquare.Foursquare;\n"
                + "public class Example extends Activity {\n"
                + "  public void onCreate(Bundle bundle) {\n"
                + "    super.onCreate(bundle); \n"
                + "  }\n"
                + "}";
        lint().files(
                java(source),
                pilgrimSdkStub
        )
                .client(new com.android.tools.lint.checks.infrastructure.TestLintClient() {
                    @Override
                    public File findResource(@NonNull String relativePath) {
                        return null;
                    }

                })
                .run()
                .expect("project0: Error: You must call Foursquare#start() at least once during your app lifecycle outside of your Application class. Usually it is put in MainActivity.java if your user is logged in (also if you have no login), or at the end of your login flow [FoursquareStart]\n"
                        + "1 errors, 0 warnings\n"
                );
    }

    @Override protected Detector getDetector() {
        return new StartDetector();
    }

    @Override protected List<Issue> getIssues() {
        return Arrays.asList(StartDetector.ISSUE);
    }

    @Override protected boolean allowCompilationErrors() {
        return true;
    }
}
