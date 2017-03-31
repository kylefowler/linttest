package com.foursquare.lint.checks;

import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.List;

public final class IssueRegistry extends com.android.tools.lint.client.api.IssueRegistry {
    @Override public List<Issue> getIssues() {
        return Arrays.asList(StartDetector.ISSUE);
    }
}
