package com.huliang.oschn.improve.bean;

import com.huliang.oschn.improve.bean.simple.Author;

import java.io.Serializable;

/**
 * 用户信息类
 * <p>
 * Created by huliang on 4/9/17.
 */
public class User extends Author {
    public static final int RELATION_TYPE_BOTH = 0x01;// 双方互为粉丝
    public static final int RELATION_TYPE_ONLY_FANS_HIM = 0x02;// 你单方面关注他
    public static final int RELATION_TYPE_ONLY_FANS_ME = 0x03;// 只有他关注我
    public static final int RELATION_TYPE_NULL = 0x04;// 互不关注

    public static final int GENDER_MALE = 1; // 男
    public static final int GENDER_FEMALE = 2; // 女

    private String desc; // More
    private String suffix; //个性后缀
    private More more;
    private Statistics statistics;
    private String cookie; // 本地缓存多余信息

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public More getMore() {
        return more;
    }

    public void setMore(More more) {
        this.more = more;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public static class More implements Serializable {
        private String joinDate;
        private String city;
        private String expertise;
        private String platform;
        private String company;
        private String position;

        public String getJoinDate() {
            return joinDate;
        }

        public void setJoinDate(String joinDate) {
            this.joinDate = joinDate;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getExpertise() {
            return expertise;
        }

        public void setExpertise(String expertise) {
            this.expertise = expertise;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }
    }

    public static class Statistics implements Serializable {

        private int score;
        private int tweet;
        private int collect;
        private int fans;
        private int follow;
        private int blog;
        private int answer;
        private int discuss;

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getTweet() {
            return tweet;
        }

        public void setTweet(int tweet) {
            this.tweet = tweet;
        }

        public int getCollect() {
            return collect;
        }

        public void setCollect(int collect) {
            this.collect = collect;
        }

        public int getFans() {
            return fans;
        }

        public void setFans(int fans) {
            this.fans = fans;
        }

        public int getFollow() {
            return follow;
        }

        public void setFollow(int follow) {
            this.follow = follow;
        }

        public int getBlog() {
            return blog;
        }

        public void setBlog(int blog) {
            this.blog = blog;
        }

        public int getAnswer() {
            return answer;
        }

        public void setAnswer(int answer) {
            this.answer = answer;
        }

        public int getDiscuss() {
            return discuss;
        }

        public void setDiscuss(int discuss) {
            this.discuss = discuss;
        }
    }
}
