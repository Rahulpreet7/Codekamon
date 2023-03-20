package com.example.codekamon;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controller class for ease of manipulation of the QRCodes collection in the database.
 */
public class QRCodesDB {

    /**
     * Holds the firebase instance.
     */
    private FirebaseFirestore db;

    /**
     * Holds the QRcodes collection reference.
     */
    private CollectionReference collectionReference;

    /**
     * Constructor for QRCodesDB.
     *
     * @param firestore The instance of the database.
     */
    public QRCodesDB(FirebaseFirestore firestore){
        this.db = firestore;
        this.collectionReference = db.collection("QRCodes");
    }

    /**
     * Gets the qrcode.
     *
     * @param name The name of the qrcode to get
     * @param listener The listener to call when the task is done.
     */
    public void getQRCode(String name, OnCompleteListener<QRCode> listener){
        collectionReference
                .document(name)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()){
                        this.getQRCode(snapshot, listener);
                    }
                    else {
                        listener.onComplete(null, false);
                    }
                })
                .addOnFailureListener(e -> {
                    listener.onComplete(null, false);
                });
    }

    /**
     * Gets the qr code.
     *
     * @param snapshot The snapshot of the qr code.
     * @param listener The listener to call when the task is done.
     */
    public void getQRCode(DocumentSnapshot snapshot, OnCompleteListener<QRCode> listener){
        HashMap<String, Object> document = (HashMap<String, Object>) snapshot.get("QRCode content: ");
        QRCode code = new QRCode((String) document.get("content"));
        code.setLocation((Double) document.get("latitude"), (Double) document.get("longitude"));
        code.setName((String) document.get("name"));
        code.setPhotoAsBytes((String) document.get("photoAsBytes"));
        code.setScore(Math.toIntExact((Long) document.get("score")));
        code.setComments((ArrayList<HashMap<String, String>>) document.get("comments"));
        listener.onComplete(code, true);
    }

    /**
     * Updates the qr code in the database.
     *
     * @param code The qr code with updated values.
     * @param listener The listener to call when the task is done.
     */
    public void updateQRCode(QRCode code, OnCompleteListener<QRCode> listener){
        collectionReference
                .document(code.getName())
                .update("QRCode content: ", code)
                .addOnSuccessListener(unused -> {
                    listener.onComplete(code, true);
                })
                .addOnFailureListener(e -> {
                    listener.onComplete(null, false);
                });
    }

    /**
     * Adds the qr code to the database.
     *
     * @param code The qr code to add to the database.
     * @param listener The listener to call when the task is done.
     */
    public void addQRCode(QRCode code, OnCompleteListener<QRCode> listener){
        HashMap<String, QRCode> map = new HashMap<>();
        map.put("QRCode content: ", code);
        collectionReference
                .document(code.getName())
                .set(map)
                .addOnSuccessListener(unused -> {
                    listener.onComplete(code, true);
                })
                .addOnFailureListener(e -> {
                    listener.onComplete(null, false);
                });
    }

    /**
     * Deletes the qr code in the database.
     *
     * @param name The name of the qr code to be deleted.
     * @param listener The listener to call when the task is done.
     */
    public void deleteQRCode(String name, OnCompleteListener<QRCode> listener){
        collectionReference
                .document(name)
                .delete()
                .addOnSuccessListener(unused -> {
                    listener.onComplete(null, true);
                })
                .addOnFailureListener(e -> {
                    listener.onComplete(null, false);
                });
    }

    /**
     * Gets the instance of the database passed to this class.
     *
     * @return The instance to the database passed to this class.
     */
    public FirebaseFirestore getFirebaseFirestore(){
        return db;
    }

    /**
     * Get the collection of QRCodes
     * @return
     */
    public CollectionReference getCollectionReference(){
        return this.collectionReference;
    }
}
