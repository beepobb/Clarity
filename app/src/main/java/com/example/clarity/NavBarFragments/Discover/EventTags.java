package com.example.clarity.NavBarFragments.Discover;

import androidx.annotation.NonNull;

// for tag button creation
public enum EventTags {
    ALL, CAREER, CAMPUS_LIFE, FIFTH_ROW, COMPETITION, WORKSHOP;

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case ALL:
                return "All";
            case CAREER:
                return "Career";
            case WORKSHOP:
                return "Workshop";
            case FIFTH_ROW:
                return "Fifth Row";
            case CAMPUS_LIFE:
                return "Campus Life";
            case COMPETITION:
                return "Competition";
            default:
                return "ERROR";
        }
    }
}
