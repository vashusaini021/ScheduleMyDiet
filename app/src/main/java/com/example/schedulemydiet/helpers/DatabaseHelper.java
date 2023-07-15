package com.example.schedulemydiet.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.schedulemydiet.models.FavouriteModel;
import com.example.schedulemydiet.models.MyUserData;
import com.example.schedulemydiet.models.food.Hit;
import com.example.schedulemydiet.models.food.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DatabaseHelper {

    //Firebase
    public FirebaseStorage storage;
    public StorageReference storageReference;
    public FirebaseFirestore db;
    public FirebaseAuth mAuth;
    public FirebaseUser firebaseUser;
    private static DatabaseHelper single_instance = null;

    SharedPreferences pref;
    public static Context applicationContext;

    public  MyUserData user;
    public FavouriteModel favouriteRecipes;

    private DatabaseHelper() {
        initialiseFirebase();
    }

    public void initialiseFirebase() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }

    public static synchronized DatabaseHelper getInstance() {
        if (single_instance == null)
            single_instance = new DatabaseHelper();

        return single_instance;
    }

    //Set Data
    public SharedPreferences getPref() {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public void saveUserData(MyUserData usrData, TaskCompletion<MyUserData> completion) {

        db.collection("user")
                .add(usrData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("save user data success Userid-->>" + usrData.getUserId());

                        usrData.setDocumentId(documentReference.getId());
                        updateUserData(usrData, new TaskCompletion<MyUserData>() {
                            @Override
                            public void taskCompletion(boolean isSuccess, MyUserData data) {
                                fetchUser(new TaskCompletion<MyUserData>()  {
                                    @Override
                                    public void taskCompletion(boolean isSuccess, MyUserData data) {
                                        completion.taskCompletion(true, user);
                                    }
                                });
                            }
                        });
                        Log.w("Database: ", "User Data saved-> successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Database: ", "Error adding user", e);
                        completion.taskCompletion(false, null);
                    }
                });
    }

    public void updateUserData(MyUserData usrData, TaskCompletion<MyUserData> completion) {

        System.out.println("updateUserData Userid-->>" + usrData.getUserId());

        db.collection("user").document(usrData.getDocumentId())
                .update("profileURL", usrData.getProfileURL(), "documentId", usrData.getDocumentId(),
                        "firstName",usrData.getFirstName(),
                        "lastName", usrData.getLastName(),
                        "city", usrData.getCity(),
                        "phone", usrData.getPhone(),
                        "address", usrData.getAddress())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("User Data saved-> successful");
                        completion.taskCompletion(true, user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completion.taskCompletion(false, null);
                        Log.w("Firebase: ", "Error adding user", e);
                    }
                });
    }


    public void fetchUser(TaskCompletion<MyUserData> completion) {

        try {
            String userID= getPref().getString("userId", "");
            DatabaseHelper.getInstance().db.collection("user")
                    .whereEqualTo("userId", userID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Toast.makeText(applicationContext, "User Fetched.", Toast.LENGTH_SHORT).show();
                                    user = document.toObject(MyUserData.class);
                                }
                            } else {
                                Toast.makeText(applicationContext, "No User Fetched.", Toast.LENGTH_SHORT).show();
                                Log.d("DATABASE", "Error getting documents: ", task.getException());
                            }

                            if (completion != null)
                                completion.taskCompletion(task.isSuccessful(), user);
                        }
                    });

        } catch (Exception e)
        {
            Log.d("Vasu DBhelper-->>", "fetchUser method -->>"+ e.getLocalizedMessage());
        }
    }

    public void saveFavouriteRecipe(Hit recipe, TaskCompletion completion) {

        if(favouriteRecipes == null)
            favouriteRecipes = new FavouriteModel();

        favouriteRecipes.favs.add(recipe);

        if(favouriteRecipes.getDocumentId() != null) {
            updateFavouriteRecipe(favouriteRecipes, new TaskCompletion<FavouriteModel>() {
                @Override
                public void taskCompletion(boolean isSuccess, FavouriteModel data) {
                    completion.taskCompletion(true, data);
                }
            });
        }
        else {

            db.collection("favouriteRecipes")
                    .add(favouriteRecipes)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            favouriteRecipes.setDocumentId(documentReference.getId());
                            Log.i("Datbase: ", "Favourite Recipe Data saved-> successful");
                            updateFavouriteRecipe(favouriteRecipes, new TaskCompletion<FavouriteModel>() {
                                @Override
                                public void taskCompletion(boolean isSuccess, FavouriteModel data) {
                                    completion.taskCompletion(true, data);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Datbase: ", "Error save Favourite Recipe user", e);
                            completion.taskCompletion(false, null);
                        }
                    });
        }
    }

    public void updateFavouriteRecipe(FavouriteModel favs, TaskCompletion<FavouriteModel> completion) {

        if(favs == null)
            favs = favouriteRecipes;

        db.collection("favouriteRecipes").document(favs.getDocumentId())
                .update("userId", favs.userId, "favs", favs.favs, "documentId", favs.getDocumentId())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("Datbase: ", "Favourite Recipe Data updated-> successful");
                        completion.taskCompletion(true, favouriteRecipes);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completion.taskCompletion(false, null);
                        Log.e("Datbase: ", "Error update Favourite Recipe user", e);
                    }
                });
    }


    public void getFavouriteRecipes(TaskCompletion<FavouriteModel> completion) {

            DatabaseHelper.getInstance().db.collection("favouriteRecipes")
                    .whereEqualTo("userId", user.getUserId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    favouriteRecipes = document.toObject(FavouriteModel.class);
                                }
                            } else {
                                Log.d("DATABASE", "Error getting documents: ", task.getException());
                            }
                            if (completion != null)
                                completion.taskCompletion(task.isSuccessful(), favouriteRecipes);
                        }
                    });

    }

}
