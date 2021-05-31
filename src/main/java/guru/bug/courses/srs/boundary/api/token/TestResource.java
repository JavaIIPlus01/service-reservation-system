package guru.bug.courses.srs.boundary.api.token;


import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/test")
public class TestResource {
    private static final Logger LOG = LoggerFactory.getLogger(TestResource.class);

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Test getTest(@PathParam("userId") UUID userId,
                        @QueryParam("filter") String filter,
                        @Range(min = 10, max = 100) @QueryParam("max-count") int maxCount,
                        @HeaderParam("App-Version") String appVersion,
                        @HeaderParam("Accept-Language") String lang) {
        LOG.info("Requested test id {}; filter {}; maxCount {}", userId, filter, maxCount);
        LOG.info("Language: {}", lang);
        LOG.info("App ver: {}", appVersion);

        var result = new Test();
        result.userId = userId;
        result.filter = filter;
        result.maxCount = maxCount;
        result.appVersion = appVersion;
        result.lang = lang;

        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void setTest(@Valid @NotNull Test testObj) {
        LOG.info("received test object {}", testObj);
    }

    public static class Test {
        UUID userId;
        String filter;
        @Range(min = 10, max = 100)
        int maxCount;
        String appVersion;
        String lang;

        public UUID getUserId() {
            return userId;
        }

        public void setUserId(UUID userId) {
            this.userId = userId;
        }

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public int getMaxCount() {
            return maxCount;
        }

        public void setMaxCount(int maxCount) {
            this.maxCount = maxCount;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        @Override
        public String toString() {
            return "Test{" +
                   "userId=" + userId +
                   ", filter='" + filter + '\'' +
                   ", maxCount=" + maxCount +
                   ", appVersion='" + appVersion + '\'' +
                   ", lang='" + lang + '\'' +
                   '}';
        }
    }
}
