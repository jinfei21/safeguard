package com.ctrip.safeguard.intf;
public class HealthCounts {
        private final long totalCount;
        private final long errorCount;
        private final int errorPercentage;

        HealthCounts(long total, long error, int errorPercentage) {
            this.totalCount = total;
            this.errorCount = error;
            this.errorPercentage = errorPercentage;
        }

        public long getTotalRequests() {
            return totalCount;
        }

        public long getErrorCount() {
            return errorCount;
        }

        public int getErrorPercentage() {
            return errorPercentage;
        }
    }
