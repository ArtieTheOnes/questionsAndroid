package hk.ust.cse.hunkim.questionroom.question;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hk.ust.cse.hunkim.questionroom.BR;

/**
 * Created by hunkim on 7/16/15.
 */
public class Question extends BaseObservable {

    /**
     * Must be synced with firebase JSON structure
     * Each must have getters
     */
    @SerializedName("_id")
    private String key;

    private String roomName;
    private String username;
    @SerializedName("anonymous")
    private boolean isAnonymous;
    private String image;
    private String wholeMsg;
    private String head;
    private String headLastChar;
    private String desc;
    private String linkedDesc;
    private boolean completed;
    private long timestamp;
    private String tags;
    public int echo;

    @SerializedName("hate")
    public int dislikes;

    private int order;

    @SerializedName("reply")
    private List<Reply> replies;


    /**
     * Set question from a String message
     * @param message string message
     */
    public Question(String message, String roomName, String username, boolean isAnonymous) {
        this.wholeMsg = message;
        this.echo = 0;
        this.dislikes = 0;
        this.head = getFirstSentence(message).trim();
        this.desc = "";
        this.roomName = roomName;
        if (this.head.length() < message.length()) {
            this.desc = message.substring(this.head.length());
        }

        // get the last char
        if (head.length() == 0) {headLastChar = "";}
         else this.headLastChar = head.substring(head.length() - 1);
        this.timestamp = new Date().getTime();
        this.replies = new ArrayList<Reply>();
        this.username = username;
        this.isAnonymous = isAnonymous;
    }

    public Question(String message) {
        this.wholeMsg = message;
        this.echo = 0;
        this.dislikes = 0;
        this.head = getFirstSentence(message).trim();
        this.desc = "";
        if (this.head.length() < message.length()) {
            this.desc = message.substring(this.head.length());
        }

        // get the last char
        this.headLastChar = head.substring(head.length() - 1);

        this.timestamp = new Date().getTime();
        this.replies = new ArrayList<Reply>();
        this.replies.add(new Reply(""));
        this.username = "Anonymous";
        this.isAnonymous = false;
    }

    /**
     * Get first sentence from a message
     * @param message
     * @return
     */
    public static String getFirstSentence(String message) {
        String[] tokens = {". ", "? ", "! "};

        int index = -1;

        for (String token : tokens) {
            int i = message.indexOf(token);
            if (i == -1) {
                continue;
            }

            if (index == -1) {
                index = i;
            } else {
                index = Math.min(i, index);
            }
        }

        if (index == -1) {
            return message;
        }

        return message.substring(0, index+1);
    }

    /* -------------------- Getters ------------------- */
    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    @Bindable
    public String getMsgString() {
        String msgString = "";
        if (isNewQuestion()) {
            msgString += "<font color=red>NEW </font>";
        }
        String newHead = getHead();
        String newDesc = getDesc();
        newHead = newHead.replace("<", "&lt;");
        newHead = newHead.replace(">", "&gt;");
        newDesc = newDesc.replace("<", "&lt;");
        newDesc = newDesc.replace(">", "&gt;");
        msgString += "<B>" + newHead + "</B>" + newDesc;
        return msgString;
    }

    @Bindable
    public int getEcho() {
        return echo;
    }

    @Bindable
    public int getDislikes() { return dislikes; }

    public String getWholeMsg() {
        return wholeMsg;
    }

    public String getHeadLastChar() {
        return headLastChar;
    }

    public String getLinkedDesc() {
        return linkedDesc;
    }

    public boolean isCompleted() {
        return completed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTags() {
        return tags;
    }

    public int getOrder() {
        return order;
    }

    public int getNumOfReplies() {
        if(replies == null) {
            return 0;
        }
        return  replies.size();
    }
    public List<Reply> getReplies() { return replies; }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setEcho(int echo) {
        this.echo = echo;
        notifyPropertyChanged(BR.echo);
    }

    public void setImage(String picture){
        image=picture;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
        notifyPropertyChanged(BR.dislikes);
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public boolean isNewQuestion() {
        return (this.timestamp > new Date().getTime() - 180000);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Question)) {
            return false;
        }
        Question other = (Question)o;
        return key.equals(other.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    public String getImage(){
        return image;
    }

    public String getUsername () {return username;}

    public boolean isIncognito() { return isAnonymous;}

    public String getRoomName() {return roomName;}
}
