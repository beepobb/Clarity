package com.example.clarity;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

import com.example.clarity.NavBarFragments.Discover.EventTags;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Ideally, we would use a ViewModel class
public class MyDataRepository {
    // Singleton class for managing data (Post objects)
    private static MyDataRepository instance;
    private MutableLiveData<List<Post>> savedEventsLiveData = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Post>> favouriteEventsLiveData = new MutableLiveData<>(new ArrayList<>());
    /*
    By initialising the value inside the MutableLiveData in the constructor, observer callbacks
    will be executed the moment they attach, even without setValue/postValue
     */

    // Singleton class
    private MyDataRepository() {
        // Initialize values for use in Discover fragment
        for (EventTags e : EventTags.values()) {
            tagsEventMapping.put(e, new ArrayList<>());
        }
    }
    public static MyDataRepository getInstance() {
        if (instance == null) {
            instance = new MyDataRepository();
        }
        return instance;
    }


    // CALENDAR METHODS: for posts/events saved to calendar //
    public MutableLiveData<List<Post>> getSavedEventsLiveData() {
        // Primarily used to attach listeners to the MutableLiveData
        return savedEventsLiveData;
    }

    public List<Post> getSavedEvents() {
        // For fetching the list of saved events (Posts)
        return savedEventsLiveData.getValue();
    }

    public void loadSavedEventsOnWorkerThread(List<Post> events) {
        // Should be use in database (RestRepo) onComplete callbacks which are executed on worker thread
        savedEventsLiveData.postValue(events); // postValue can only be executed on worker thread
    }

    public void addSavedEventOnMainThread(Post post) {
        List<Post> savedEvents = savedEventsLiveData.getValue();
        assert savedEvents != null;
        savedEvents.add(post);
        savedEventsLiveData.setValue(savedEvents); // setValue can only be executed on main UI thread
        // Observers of savedEventsLiveData will be triggered
    }

    public void removeSavedEventOnMainThread(Post post) {
        List<Post> savedEvents = savedEventsLiveData.getValue();
        assert savedEvents != null;
        savedEvents.remove(post);
        savedEventsLiveData.setValue(savedEvents); // setValue can only be executed on main UI thread
        // Observers of savedEventsLiveData will be triggered
    }

    public void resetSavedEventsOnMainThread() {
        savedEventsLiveData.setValue(new ArrayList<>()); // empty list
        // Observers of savedEventsLiveData will be triggered
    }

    // FAVOURITE METHODS: for posts/events liked by logged-in user
    public MutableLiveData<List<Post>> getFavouriteEventsLiveData() {
        // Primarily used to attach listeners to the MutableLiveData
        return favouriteEventsLiveData;
    }

    public List<Post> getFavouriteEvents() {
        // For fetching list of liked posts by User
        return favouriteEventsLiveData.getValue();
    }

    public void loadFavouriteEventsOnWorkerThread(List<Post> events) {
        // Should be use in database (RestRepo) onComplete callbacks which are executed on worker thread
        favouriteEventsLiveData.postValue(events); // postValue can only be executed on worker thread
    }

    public void addFavouriteEventOnMainThread(Post post) {
        List<Post> favouriteEvents = favouriteEventsLiveData.getValue();
        assert favouriteEvents != null;
        favouriteEvents.add(post);
        favouriteEventsLiveData.setValue(favouriteEvents); // setValue can only be executed on main UI thread
        // Observers of favouriteEventsLiveData will be triggered
    }

    public void removeFavouriteEventOnMainThread(Post post) {
        List<Post> favouriteEvents = favouriteEventsLiveData.getValue();
        assert favouriteEvents != null;
        favouriteEvents.remove(post);
        favouriteEventsLiveData.setValue(favouriteEvents); // setValue can only be executed on main UI thread
        // Observers of savedEventsLiveData will be triggered
    }

    public boolean postInFavourites(Post post) {
        List<Post> favouriteEvents = favouriteEventsLiveData.getValue();
        assert favouriteEvents != null;
        return favouriteEvents.contains(post);
    }




    // DISCOVER METHODS //
    private MutableLiveData<List<Post>> allEventsLiveData = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<HashMap<Integer, Bitmap>> eventImageMappingLiveData = new MutableLiveData<>(new HashMap<>());
    private HashMap<EventTags, ArrayList<Integer>> tagsEventMapping = new HashMap<>();

    /*
    fetch all posts --> save to allEvents --> observer 1) fetch all tags --> load all tags
    observer 2) fetch all images --> load all images
     */

    public void loadEventImageMappingLiveData(HashMap<Integer, Bitmap> mapping) {
        eventImageMappingLiveData.postValue(mapping);
    }
    public HashMap<EventTags, ArrayList<Integer>> getTagsEventMapping() {
        return tagsEventMapping;
    }

    public MutableLiveData<List<Post>> getAllEventsLiveData() {
        return allEventsLiveData;
    }

    public MutableLiveData<HashMap<Integer, Bitmap>> getEventImageMappingLiveData() {
        return eventImageMappingLiveData;
    }

    public void loadAllEventsOnWorkerThread(ArrayList<Post> eventsList) {
        allEventsLiveData.postValue(eventsList);
        // Observers of allEventsLiveData will be triggered
    }

    public void createTagEventMapping(ArrayList<Tag> tagObjects) {
        // Populate: tagEventNapping
        for (Tag tag : tagObjects) {
            Integer post_id = tag.getPost_id();
            String tag_category = tag.getTag_category();

            if (tag_category.equals(EventTags.FIFTH_ROW.name())) {
                tagsEventMapping.get(EventTags.FIFTH_ROW).add(post_id);
            } else if (tag_category.equals(EventTags.CAREER.name())) {
                tagsEventMapping.get(EventTags.CAREER).add(post_id);
            } else if (tag_category.equals(EventTags.WORKSHOP.name())) {
                tagsEventMapping.get(EventTags.WORKSHOP).add(post_id);
            } else if (tag_category.equals(EventTags.CAMPUS_LIFE.name())) {
                tagsEventMapping.get(EventTags.CAMPUS_LIFE).add(post_id);
            } else if (tag_category.equals(EventTags.COMPETITION.name())) {
                tagsEventMapping.get(EventTags.COMPETITION).add(post_id);
            }
        }
    }
}
