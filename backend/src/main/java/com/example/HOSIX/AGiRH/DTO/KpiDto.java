package com.example.HOSIX.AGiRH.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpiDto {

    private String status;
    private LocalDate generatedAt;
    private TotalResidents totalResidents;
    private StatutDistribution statutDistribution;
    private ServiceDistribution serviceDistribution;
    private InactiveRate inactiveRate;
    private DeactivationCandidates candidatesForDeactivation;
    private SourceDistribution sourceDistribution;
    private MonthlyCreation monthlyCreation;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TotalResidents {
        private Long count;
        private String label;
        private String icon;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatutDistribution {
        private List<StatutData> data;
        private String title;
        private String type;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class StatutData {
            private String status;
            private Long count;
            private Long percentage;
            private String color;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceDistribution {
        private List<ServiceData> data;
        private String title;
        private String type;
        private Long totalWithService;
        private Long totalWithoutService;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ServiceData {
            private String service;
            private Long count;
            private Long percentage;
            private String color;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InactiveRate {
        private Double rate;
        private Long inactiveCount;
        private Long totalCount;
        private String label;
        private String unit;
        private String color;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeactivationCandidates {
        private List<CandidateData> candidates;
        private Long totalCandidates;
        private String title;
        private String criteria;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class CandidateData {
            private String matricule;
            private String fullName;
            private String service;
            private LocalDate lastModified;
            private Long daysSinceModification;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceDistribution {
        private List<SourceData> data;
        private String title;
        private String type;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SourceData {
            private String source;
            private Long count;
            private String color;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyCreation {
        private List<MonthData> data;
        private String title;
        private String type;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class MonthData {
            private String month;
            private Long count;
            private String label;
        }
    }
}
