package com.example.login.ui.set_passwd;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Set_passwdViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Set_passwdViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}