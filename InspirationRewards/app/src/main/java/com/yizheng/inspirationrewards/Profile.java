package com.yizheng.inspirationrewards;

import java.io.Serializable;
import java.util.ArrayList;

public class Profile implements Serializable, Comparable<Profile> {

    private String username, password, firstname, lastname, department, position, story, photo, location;
    private boolean isAdministrator;
    private Integer pointsToAward, pointsAwarded;
    private ArrayList<Reward> rewards = new ArrayList<>();

    public boolean rewardsIsEmpty(){
        return rewards.isEmpty();
    }

//    public void receivePoints(int p){
//        pointsAwarded += p;
//    }
//
//    public int givePoints(int p){
//        pointsToAward -= p;
//        return p;
//    }


    public boolean hasEnough(int p){
        return (pointsToAward - p) > 0;
    }

    public int totalPointsAwarded(){
        int total = 0;
        for (Reward r : rewards){
            total += r.getValue();
        }
        return total;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAdministrator(boolean administrator) {
        isAdministrator = administrator;
    }



    public Integer getPointsToAward() {
        return pointsToAward;
    }

    public void setPointsToAward(Integer pointsToAward) {
        this.pointsToAward = pointsToAward;
    }

    public Integer getPointsAwarded() {
        return pointsAwarded;
    }

    public void addReward(Reward r){
        rewards.add(r);
    }

    public void setPointsAwarded(Integer pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    ArrayList<Reward> getRewards(){
        return rewards;
    }

    public void setRewards(ArrayList<Reward> rewards) {
        this.rewards = rewards;
    }

    @Override
    public int compareTo(Profile o) {
        return o.totalPointsAwarded() - this.totalPointsAwarded();
    }


//    public void setPointsAwarded(int pointsAwarded) {
//        this.pointsAwarded = pointsAwarded;
//    }
//
//    public void setReward(String username, String name, String date, String notes, int value){
//        this.reward = new Reward(username, name, date, notes, value);
//    }
//
//    public String getRewardUsername(){
//        return reward.getUsername();
//    }
//
//    public String getRewardName(){
//        return reward.getName();
//    }
//
//    public String getDate(){
//        return reward.getDate();
//    }
//
//    public String getNotes(){
//        return reward.getNotes();
//    }
//
//    public int getRewardValue(){
//        return reward.getValue();
//    }

}
