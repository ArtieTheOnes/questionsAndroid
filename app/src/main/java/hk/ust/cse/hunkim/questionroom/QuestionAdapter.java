package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.Html;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import hk.ust.cse.hunkim.questionroom.databinding.QuestionBinding;
import hk.ust.cse.hunkim.questionroom.db.DBUtil;
import hk.ust.cse.hunkim.questionroom.question.Question;
import hk.ust.cse.hunkim.questionroom.question.Reply;

/**
 * Created by Teman on 11/9/2015.
 */
public class QuestionAdapter extends ArrayAdapter<Question> {
    private LayoutInflater mInflater;
    private MainActivity mParentActivity;
    private Map<String, Question> mKeyQuestionMap;

    private static Comparator<Question> questionComparator = new Comparator<Question>() {
        @Override
        public int compare(Question question, Question other) {
            // Push new on top

            if (question.isNewQuestion() != other.isNewQuestion()) {
                return question.isNewQuestion() ? 1 : -1; // this is the winner
            }
            if (question.echo != other.echo){
                return question.echo > other.echo? 1 : -1;
            }
            if (question.dislikes != other.dislikes){
                return question.dislikes < other.dislikes? 1 : -1;
            }
            if (question.getTimestamp() != other.getTimestamp()){
                return question.getTimestamp() > other.getTimestamp() ? 1 : -1;
            }
            return 0;
            /*
            if (this.echo == other.echo) {
                if (other.timestamp == this.timestamp) {
                    return 0;
                }
                return other.timestamp > this.timestamp ? -1 : 1;
            }
            return (this.echo - other.echo);*/
        }
    };

    public QuestionAdapter(Context context, List<Question> questions) {
        super(context, 0, questions);
        mKeyQuestionMap = new ArrayMap<>();
        for(Question q: questions) {
            mKeyQuestionMap.put(q.getKey(), q);
        }
        setNotifyOnChange(true);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mParentActivity = (MainActivity)context;
    }

    private static class ViewHolder {
        public ImageButton echoButton;
        public ImageButton dislikeButton;
        public ImageButton replyButton;
        public TextView hashView;
        public TextView description;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionBinding binding;
        ViewHolder holder;
        if(convertView == null) {
            binding = DataBindingUtil.inflate(mInflater, R.layout.question, parent, false);
            convertView = binding.getRoot();
            holder = new ViewHolder();
            holder.echoButton = (ImageButton) convertView.findViewById(R.id.questionEchoButton);
            holder.dislikeButton = (ImageButton) convertView.findViewById(R.id.questionDislikeButton);
            holder.replyButton = (ImageButton) convertView.findViewById(R.id.questionReplyButton);
            holder.hashView = (TextView) convertView.findViewById(R.id.head_desc);
            holder.description = (TextView) convertView.findViewById(R.id.questionUsername);
            convertView.setTag(holder);
        }
        else {
            binding = DataBindingUtil.getBinding(convertView);
            convertView = binding.getRoot();
            holder = (ViewHolder) convertView.getTag();
        }
        final Question question = getItem(position);
        binding.setQuestion(question);

        // Display question
        String msgString = question.getMsgString();
        ((TextView) convertView.findViewById(R.id.head_desc)).setText(Html.fromHtml(msgString));


        //Display description
        TextView description = holder.description;
        if (question.isIncognito())
        {
            description.setText("Anonymous");
        }
        else
        {
            description.setText(question.getUsername());

        }

        // Like button
        //ImageButton echoButton = (ImageButton) convertView.findViewById(R.id.questionEchoButton);
        ImageButton echoButton = holder.echoButton;
        echoButton.setTag(question.getKey()); // Set tag for button
        echoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //question.setEcho(question.getEcho() + 1);
                        mParentActivity.emitLikeQuestion(question.getKey());
                    }
                }
        );

        // Dislike button
        //ImageButton dislikeButton = (ImageButton) convertView.findViewById(R.id.questionDislikeButton);
        ImageButton dislikeButton = holder.dislikeButton;
        dislikeButton.setTag(question.getKey()); // Set tag for button
        dislikeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //question.setDislikes(question.getDislikes() + 1);
                        mParentActivity.emitDislikeQuestion(question.getKey());
                    }
                }
        );

        // reply button
        //ImageButton replyButton = (ImageButton) convertView.findViewById(R.id.questionReplyButton);
        ImageButton replyButton = holder.replyButton;
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enterReply(question.getKey());
                mParentActivity.enterReply(question.getKey());
            }
        });


        //TextView hashView = (TextView) convertView.findViewById(R.id.head_desc);
        TextView hashView = holder.hashView;
        //SpannableString ss = new SpannableString(hashView.getText().toString());
        Pattern tagPattern = Pattern.compile("[#]+[A-Za-z0-9-_]+\\b");
        //Matcher tagMatcher = tagPattern.matcher(ss);
        String newActivityURL = "tag";
        Linkify.addLinks(hashView, tagPattern, newActivityURL);

        return convertView;
    }

    public void setQuestionList(List<Question> questions) {
        clear();
        addAll(questions);
        mKeyQuestionMap = new ArrayMap<>();
        for(Question q: questions) {
            mKeyQuestionMap.put(q.getKey(), q);
        }
        notifyDataSetChanged();
    }

    // add question to the end of question list
    public void addQuestion(Question question) {
        mKeyQuestionMap.put(question.getKey(), question);
        add(question);
    }

    public void insertQuestion(Question question, int position) {
        mKeyQuestionMap.put(question.getKey(), question);
        insert(question, position);
    }

    public void removeQuestion(String questionKey) {
        Question question = mKeyQuestionMap.get(questionKey);
        remove(question);
        mKeyQuestionMap.remove(questionKey);
    }

    public void likeQuestion(String questionKey, int numOfLikes, int order) {
        Question question = mKeyQuestionMap.get(questionKey);
        if(question != null) {
            question.setEcho(numOfLikes);
            question.setOrder(order);
            sortQuestionList();
        }
    }

    public void dislikeQuestion(String questionKey, int numOfDislikes, int order) {
        Question question = mKeyQuestionMap.get(questionKey);
        if(question != null) {
            question.setDislikes(numOfDislikes);
            question.setOrder(order);
            sortQuestionList();
        }
    }

    public void sortQuestionList() {
        sort(questionComparator);
        notifyDataSetChanged();
    }
}
