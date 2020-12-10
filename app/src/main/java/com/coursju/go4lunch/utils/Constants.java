package com.coursju.go4lunch.utils;

import com.coursju.go4lunch.modele.Restaurant;
import com.coursju.go4lunch.modele.Workmate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

 public  class  Constants {

    public static FirebaseUser CURRENT_USER = FirebaseAuth.getInstance().getCurrentUser();
    public static Workmate CURRENT_WORKMATE = new Workmate();
    public static Restaurant DETAILS_RESTAURANT = null;

 }
