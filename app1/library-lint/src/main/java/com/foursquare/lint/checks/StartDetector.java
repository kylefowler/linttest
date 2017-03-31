package com.foursquare.lint.checks;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.TextFormat;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;

import java.util.Arrays;
import java.util.List;

public class StartDetector extends Detector implements Detector.JavaPsiScanner {
    public static final Issue ISSUE = Issue.create(
            "FoursquareStart",
            "Missing a call to `Foursquare#start()`",
            "You must call `Foursquare#start()` at least once during your app lifecycle " +
                    "outside of your Application class. Usually it is put in MainActivity.java if your user" +
                    " is logged in (also if you have no login), or at the end of your login flow",
            Category.CORRECTNESS,
            10,
            Severity.FATAL,
            new Implementation(StartDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    private boolean foundCall;

    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("start");
    }

    @Override
    public void visitMethod(JavaContext context, JavaElementVisitor visitor,
                            PsiMethodCallExpression call, PsiMethod method) {
        PsiReferenceExpression methodExpression = call.getMethodExpression();
        String fullyQualifiedMethodName = methodExpression.getQualifiedName();
        if (fullyQualifiedMethodName.equals("com.foursquare.Foursquare.start")) {
            foundCall = true;
        }
    }

    @Override
    public void beforeCheckProject(Context context) {
        System.out.println("Starting the start lint");
        foundCall = false;
    }

    @Override
    public void afterCheckProject(@NonNull Context context) {
        if (!foundCall) {
            context.report(ISSUE, Location.create(context.getProject().getDir()), ISSUE.getExplanation(TextFormat.TEXT));
        }
    }
}
