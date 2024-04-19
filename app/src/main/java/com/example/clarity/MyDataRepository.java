package com.example.clarity;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

import com.example.clarity.NavBarFragments.Discover.EventTags;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyDataRepository {
    /**
     * Singleton class for storing and managing data fetched from database
     * Activities/Fragments can attach listeners (observers) to the MutableLiveData
     * attributes of this class instance to be informed of changes in data and
     * update their respective UI accordingly
     */

    private static MyDataRepository instance;
    /*
    By initialising the value inside the MutableLiveData in the constructor, observer callbacks
    will be executed the moment they attach, even without setValue/postValue
     */


    private MyDataRepository() {
        for (EventTags e : EventTags.values()) {
            getTagsEventMapping().put(e, new ArrayList<>());
        }
    }

    /**
     * Singleton class constructor
     * @return MyDataRepository instance
     */
    public static MyDataRepository getInstance() {
        if (instance == null) {
            instance = new MyDataRepository();
        }
        return instance;
    }


    // CALENDAR METHODS: for posts/events saved to calendar //
    private MutableLiveData<List<Post>> savedEventsLiveData = new MutableLiveData<>(new ArrayList<>());
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


    // FAVOURITE METHODS: for posts/events liked by logged-in user //
    private MutableLiveData<List<Post>> favouriteEventsLiveData = new MutableLiveData<>(new ArrayList<>());
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
        // Returns true if post is in user's favourites
        List<Post> favouriteEvents = favouriteEventsLiveData.getValue();
        assert favouriteEvents != null;
        return favouriteEvents.contains(post);
    }



    // DISCOVER METHODS: all uploaded posts //
    private MutableLiveData<HashMap<Integer, Post>> allEventsHashMapLiveData = new MutableLiveData<>(new HashMap<>());
    private MutableLiveData<HashMap<Integer, Bitmap>> eventImageMappingLiveData = new MutableLiveData<>(new HashMap<>());
    private MutableLiveData<HashMap<EventTags, ArrayList<Integer>>> tagsEventMappingLiveData = new MutableLiveData<>(new HashMap<>());

    public void loadAllEventsOnWorkerThread(ArrayList<Post> eventsList) {
        // Takes list of Post objects and save it in a hash map (the key is the post id)
        HashMap<Integer, Post> hashMap = new HashMap<>();
        for (Post post: eventsList) {
            hashMap.put(post.getId(), post);
        }
        allEventsHashMapLiveData.postValue(hashMap);

        // Observers of allEventsLiveData will be triggered
    }
    public void loadEventImageMappingOnWorkerThread(HashMap<Integer, Bitmap> mapping) {
        eventImageMappingLiveData.postValue(mapping);
        // Observers of eventImageMapping will be triggered
    }

    public void createTagEventMappingOnWorkerThread(ArrayList<Tag> tagObjects) {
        // Initialize hash map
        HashMap<EventTags, ArrayList<Integer>> tagsEventMapping = new HashMap<>();
        for (EventTags e : EventTags.values()) {
            tagsEventMapping.put(e, new ArrayList<>());
        }

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

        tagsEventMappingLiveData.postValue(tagsEventMapping);
        // Observers of tagsEventMappingLiveData will be triggered
    }

    public MutableLiveData<HashMap<Integer, Post>> getAllEventsHashMapLiveData() {
        return allEventsHashMapLiveData;
    }
    public HashMap<Integer, Post> getAllEventsHashMap() { return allEventsHashMapLiveData.getValue(); }
    public List<Post> getAllEvents() {
        return new ArrayList<>(getAllEventsHashMap().values());
    }

    public MutableLiveData<HashMap<Integer, Bitmap>> getEventImageMappingLiveData() { return eventImageMappingLiveData; }
    public HashMap<Integer, Bitmap> getEventImageMapping() { return eventImageMappingLiveData.getValue(); }

    public MutableLiveData<HashMap<EventTags, ArrayList<Integer>>> getTagsEventMappingLiveData() { return tagsEventMappingLiveData; }
    public HashMap<EventTags, ArrayList<Integer>> getTagsEventMapping() { return tagsEventMappingLiveData.getValue(); }


}
