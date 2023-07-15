package com.example.schedulemydiet.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.schedulemydiet.auth.LoginActivity;
import com.example.schedulemydiet.databinding.FragmentNotificationsBinding;
import com.example.schedulemydiet.helpers.DatabaseHelper;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        binding.textNotifications.setText("Notifiactions");
        binding.idBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper.getInstance().mAuth.signOut();
                SharedPreferences.Editor editor = DatabaseHelper.getInstance().getPref().edit();
                editor.clear();
                editor.apply();
                getActivity().finish();
                Intent newInt = new Intent(getContext(), LoginActivity.class);
                startActivity(newInt);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}