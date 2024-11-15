package com.example.learnhub.model;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {

        private static final String PREF_NAME = "user_session";
        private static final String KEY_USER_TYPE = "user_type";
        private static final String KEY_CLASS_CODE = "class_code";
        private SharedPreferences sharedPreferences;

        public UserSession(Context context) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }

        // Save user type
        public void saveUserType(String userType) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_USER_TYPE, userType);
            editor.apply();
        }
        public  void saveClassCode(String classcode){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_CLASS_CODE, classcode);
            editor.apply();
        }
    public String getClassCode() {
        return sharedPreferences.getString(KEY_CLASS_CODE, "default"); // default if not set
    }

        // Get user type
        public String getUserType() {
            return sharedPreferences.getString(KEY_USER_TYPE, "default"); // default if not set
        }
    }




