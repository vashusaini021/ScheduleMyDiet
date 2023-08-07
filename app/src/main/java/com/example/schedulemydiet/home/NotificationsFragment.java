package com.example.schedulemydiet.home;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.schedulemydiet.R;
import com.example.schedulemydiet.auth.LoginActivity;
import com.example.schedulemydiet.databinding.FragmentNotificationsBinding;
import com.example.schedulemydiet.helpers.DatabaseHelper;
import com.example.schedulemydiet.helpers.TaskCompletion;
import com.example.schedulemydiet.models.MyUserData;
import com.example.schedulemydiet.network.Loader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    Uri imageURI;
    String imageSelected = "";

    MyUserData userData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        userData = DatabaseHelper.getInstance().user;
        binding.idEdiTextFnameEditProfile.setText(userData.getFirstName());
        binding.idEdiTextLnameEditProfile.setText(userData.getLastName());
        binding.idEditprofilePhoneNumberEditText.setText(userData.getPhone());
        binding.idEditProfileAddressEditText.setText(userData.getAddress());
        binding.idEditProfileCityEditText.setText(userData.getCity());

        binding.idSubmitButtonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userData.setPhone(binding.idEditprofilePhoneNumberEditText.getText().toString().trim());
                userData.setLastName(binding.idEdiTextLnameEditProfile.getText().toString().trim());
                userData.setFirstName(binding.idEdiTextFnameEditProfile.getText().toString().trim());
                userData.setAddress(binding.idEditProfileAddressEditText.getText().toString().trim());
                userData.setCity(binding.idEditProfileCityEditText.getText().toString().trim());

                uploadImage();
            }
        });

        binding.idEditProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        if (!(userData.getProfileURL().equals(""))) {
            Picasso.get().load(userData.getProfileURL()).into(binding.idEditProfileImageView);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void selectImage() {

        //Setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //Set title
        builder.setTitle("Notice");

        //Set message
        builder.setMessage("Choose an action");

        //Add the buttons
        builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                openGallery();
            }
        });
        builder.setNegativeButton("Cancel", null);

        //Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void openGallery() {


        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);

        } else {
            // Permission has already been granted

            //Create an Intent with action as ACTION_PICK
            Intent intent = new Intent(Intent.ACTION_PICK);
            // Sets the type as image/*. This ensures only components of type image are selected
            intent.setType("image/*");
            //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            // Launching the Intent
            startActivityForResult(intent, 1);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 1:
                    try {
                        //data.getData returns the content URI for the selected Image
                        imageURI = data.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageURI);
                        final Bitmap selectedImageInBM = BitmapFactory.decodeStream(imageStream);
                        //image_view.setImageBitmap(selectedImage);
                        binding.idEditProfileImageView.setImageBitmap(selectedImageInBM);

                    } catch (FileNotFoundException e) {
                        Log.d("Photo exception-->>" ,e.toString());
                    }
                    break;
            }
    }


    private void uploadImage() {

        if(imageURI != null) {
//            InputStream  inputStream = getAssets()
            Loader.show(getContext());
            //Upload image to Storage and get the url
            final StorageReference ref = DatabaseHelper.getInstance().storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("DATABASE: ", "Uploaded image URL: "+ uri.toString());
                            imageSelected = uri.toString();
                            userData.setProfileURL(imageSelected);
                            //Create new user and use image URL
                            updateUserProfile();
                        }
                    });
                }
            });
        } else {
            updateUserProfile();
        }
    }

    private void updateUserProfile() {

        String name = userData.getFirstName() + " " +userData.getFirstName();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(imageSelected))
                .build();

        DatabaseHelper.getInstance().firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Loader.dismiss();
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = DatabaseHelper.getInstance().getPref().edit();
                            editor.putString("imageURL",imageSelected); // Storing string
                            editor.apply(); // commit changes

                            DatabaseHelper.getInstance().updateUserData(userData, new TaskCompletion() {
                                @Override
                                public void taskCompletion(boolean isSuccess, Object data) {

                                    DatabaseHelper.getInstance().user = userData;
                                    Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                    Loader.dismiss();
                                }

                            });
                        }
                    }
                });
    }
}