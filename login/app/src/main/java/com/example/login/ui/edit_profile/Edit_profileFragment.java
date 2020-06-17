package com.example.login.ui.edit_profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.login.R;

public class Edit_profileFragment extends Fragment {

    private Edit_profileViewModel editprofileViewModel;
    Button btn_qr;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        editprofileViewModel = ViewModelProviders.of(this).get(Edit_profileViewModel.class);

        View root = inflater.inflate(R.layout.acerca_de, container, false);



        return root;
    }
}