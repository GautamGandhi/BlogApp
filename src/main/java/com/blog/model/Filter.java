    package com.blog.model;

    import java.util.List;

    public class Filter {
        private List<Tag> tagFilterList;
        private List<Post> authorFilterList;
        private String startDate;
        private String endDate;

        public Filter(List<Tag> tagFilterList, List<Post> authorFilterList, List<Post> dateTimeFilterList) {
            this.tagFilterList = tagFilterList;
            this.authorFilterList = authorFilterList;
        }

        public Filter() {
        }

        public Filter(List<Tag> tags) {
            this.tagFilterList = tags;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public List<Post> getAuthorFilterList() {
            return authorFilterList;
        }

        public void setAuthorFilterList(List<Post> authorFilterList) {
            this.authorFilterList = authorFilterList;
        }

        public List<Tag> getTagFilterList() {
            return tagFilterList;
        }

        public void setTagFilterList(List<Tag> tagFilterList) {
            this.tagFilterList = tagFilterList;
        }

        @Override
        public String toString() {
            return "Filter{" +
                    "tagFilterList=" + tagFilterList +
                    ", authorFilterList=" + authorFilterList +
                    '}';
        }
    }

