package com.example.codekamon;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;

public class MockFireStore {
    private HashMap<String,HashMap<String, HashMap<String, Object>>> collections;
    private FirebaseFirestore mockFireStore = Mockito.mock(FirebaseFirestore.class);

    public MockFireStore(HashMap<String,HashMap<String, HashMap<String, Object>>> collections){
        this.collections = collections;

        for (String collection : collections.keySet()){
            //Mock firestore.collection(collectionString) is not empty
            CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
            Mockito.when(mockFireStore.collection(collection)).thenReturn(mockCollectionReference);

            //Mock collectionReference.getId()
            Mockito.when(mockCollectionReference.getId()).thenReturn(collection.toString());

            //Mock firestore.collection(collectionString).document(documentString) is empty
            DocumentReference mockDocumentReferenceDefault = Mockito.mock(DocumentReference.class);
            Mockito.when(mockCollectionReference.document(Mockito.anyString())).thenReturn(mockDocumentReferenceDefault);
            Task mockTaskDefault = Mockito.mock(Task.class);
            Mockito.when(mockDocumentReferenceDefault.get()).thenReturn(mockTaskDefault);
            DocumentSnapshot mockSnapShotNotFound = Mockito.mock(DocumentSnapshot.class);
            Mockito.when(mockSnapShotNotFound.exists()).thenReturn(false);
            Mockito.when(mockTaskDefault.getResult()).thenReturn(mockSnapShotNotFound);
            Mockito.when(mockTaskDefault.addOnSuccessListener(Mockito.any(OnSuccessListener.class))).thenAnswer(new Answer<Task>() {
                @Override
                public Task answer(InvocationOnMock invocation) throws Throwable {
                    ((OnSuccessListener) invocation.getArgument(0)).onSuccess(mockSnapShotNotFound);
                    return mockTaskDefault;
                }
            });
            Mockito.when(mockTaskDefault.addOnFailureListener(Mockito.any(OnFailureListener.class))).thenAnswer(new Answer<Task>() {
                @Override
                public Task answer(InvocationOnMock invocation) throws Throwable {
                    Exception e = Mockito.mock(Exception.class);
                    Mockito.when(e.getMessage()).thenReturn("Player not found");
                    ((OnFailureListener) invocation.getArgument(0)).onFailure(e);
                    return mockTaskDefault;
                }
            });

            //Mock firestore.collection(collectionString).document(documentString) not empty
            for (String document : collections.get(collection).keySet()){
                DocumentReference mockDocumentReference = Mockito.mock(DocumentReference.class);
                Mockito.when(mockCollectionReference.document(document)).thenReturn(mockDocumentReference);
                Task<DocumentSnapshot> task = Mockito.mock(Task.class);
                Mockito.when(mockDocumentReference.get()).thenReturn(task);
                DocumentSnapshot mockSnapShot = Mockito.mock(DocumentSnapshot.class);
                Mockito.when(mockSnapShot.exists()).thenReturn(collections.get(collection).containsKey(document));
                Mockito.when(task.getResult()).thenReturn(mockSnapShot);
                for (String field : collections.get(collection).get(document).keySet()){
                    Mockito.when(mockSnapShot.get(field)).thenReturn(collections.get(collection).get(document).get(field));
                }
                Mockito.when(task.addOnSuccessListener(Mockito.any(OnSuccessListener.class))).thenAnswer(new Answer<Task>() {
                    @Override
                    public Task answer(InvocationOnMock invocation) throws Throwable {
                        ((OnSuccessListener) invocation.getArgument(0)).onSuccess(mockSnapShot);
                        return task;
                    }
                });
                Mockito.when(task.addOnFailureListener(Mockito.any(OnFailureListener.class))).thenAnswer(new Answer<Task>() {
                    @Override
                    public Task answer(InvocationOnMock invocation) throws Throwable {
                        Exception e = Mockito.mock(Exception.class);
                        if (mockSnapShot.exists()){
                            Mockito.when(e.getMessage()).thenReturn("Player found");
                        }
                        else {
                            Mockito.when(e.getMessage()).thenReturn("Player not found");
                        }
                        ((OnFailureListener) invocation.getArgument(0)).onFailure(e);
                        return task;
                    }
                });
            }
        }

    }

    public FirebaseFirestore getInstance(){
        return mockFireStore;
    }
}
