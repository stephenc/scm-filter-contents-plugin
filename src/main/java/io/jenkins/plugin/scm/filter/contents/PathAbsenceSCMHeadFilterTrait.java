package io.jenkins.plugin.scm.filter.contents;

import hudson.Extension;
import hudson.util.FormValidation;
import jenkins.scm.api.SCMHead;
import jenkins.scm.api.SCMSourceCriteria;
import jenkins.scm.api.trait.SCMSourceContext;
import jenkins.scm.api.trait.SCMSourceTrait;
import jenkins.scm.api.trait.SCMSourceTraitDescriptor;
import jenkins.scm.impl.trait.Selection;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

/**
 * Filters only those {@link SCMHead} instances that do not have a specified path.
 */
public class PathAbsenceSCMHeadFilterTrait extends SCMSourceTrait {
    private final String path;

    @DataBoundConstructor
    public PathAbsenceSCMHeadFilterTrait(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    protected void decorateContext(SCMSourceContext<?, ?> context) {
        context.withCriteria((SCMSourceCriteria) (probe, listener) -> !probe.stat(path).exists());
    }

    /**
     * Our descriptor.
     */
    @Symbol("pathAbsenceFilter")
    @Extension
    @Selection
    public static class DescriptorImpl extends SCMSourceTraitDescriptor {

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayName() {
            return Messages.PathAbsenceSCMHeadFilterTrait_DisplayName();
        }

        /**
         * Form validation for the regular expression.
         *
         * @param value the regular expression.
         * @return the validation results.
         */
        @Restricted(NoExternalUse.class) // stapler
        public FormValidation doCheckPath(@QueryParameter String value) {
            if (StringUtils.isBlank(value)) {
                return FormValidation.error(Messages.pathMissing());
            }
            return FormValidation.ok();
        }
    }
}
