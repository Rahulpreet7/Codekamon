package com.example.codekamon;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;

public class PlayersDBTest {
    @Test
    public void testConstructor(){
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = mock(CollectionReference.class);

        when(mockFirestore.collection("Players")).thenReturn(mockCollectionReference);
        PlayersDB playersDB = new PlayersDB(mockFirestore);
        assertTrue(playersDB.getDb().equals(mockFirestore));
        assertTrue(playersDB.getCollectionReference().equals(mockCollectionReference));
    }

    @Test
    public void testGetExistingPlayer(){
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = mock(CollectionReference.class);
        DocumentReference mockDocumentReference = mock(DocumentReference.class);
        Task<DocumentSnapshot> mockTask = mock(Task.class);
        DocumentSnapshot mockSnapshot = mock(DocumentSnapshot.class);
        Object snapShot = mock(Object.class);

        when(mockFirestore.collection("Players")).thenReturn(mockCollectionReference);
        when(mockCollectionReference.document(anyString())).thenReturn(mockDocumentReference);
        when(mockDocumentReference.get()).thenReturn(mockTask);
        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.get(anyString())).thenReturn(snapShot);
        when(snapShot.toString()).thenReturn("23");
        when(mockSnapshot.get("ScannedCodes")).thenReturn(mock(HashMap.class));
        when(mockTask.addOnSuccessListener(any(OnSuccessListener.class))).thenAnswer(new Answer<Task>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                ((OnSuccessListener) invocation.getArgument(0)).onSuccess(mockSnapshot);
                return mockTask;
            }
        });

        PlayersDB playersDB = new PlayersDB(mockFirestore);
        playersDB.getPlayer("dummyName", new OnCompleteListener<Player>() {
            @Override
            public void onComplete(Player item, boolean success) {
                assertTrue(success);
                assertTrue(item.getUserName().equals("23"));
            }
        });
    }


    @Test
    public void testGetNonExistentPlayer(){
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = mock(CollectionReference.class);
        DocumentReference mockDocumentReference = mock(DocumentReference.class);
        Task<DocumentSnapshot> mockTask = mock(Task.class);
        DocumentSnapshot mockSnapshot = mock(DocumentSnapshot.class);
        Object snapShot = mock(Object.class);

        when(mockFirestore.collection("Players")).thenReturn(mockCollectionReference);
        when(mockCollectionReference.document(anyString())).thenReturn(mockDocumentReference);
        when(mockDocumentReference.get()).thenReturn(mockTask);
        when(mockSnapshot.exists()).thenReturn(false);
        when(mockTask.addOnSuccessListener(any(OnSuccessListener.class))).thenAnswer(new Answer<Task>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                ((OnSuccessListener) invocation.getArgument(0)).onSuccess(mockSnapshot);
                return mockTask;
            }
        });

        PlayersDB playersDB = new PlayersDB(mockFirestore);
        playersDB.getPlayer("dummyNameNotExist", new OnCompleteListener<Player>() {
            @Override
            public void onComplete(Player item, boolean success) {
                assertFalse(success);
                assertNull(item);
            }
        });
    }

    /**
     * Tests adding non existent and existing player.
     */
    @Test
    public void testAddPlayer(){

        Player player = new Player();
        player.setUserName("dummyName");
        player.setAndroidId("asd");

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = mock(CollectionReference.class);
        DocumentReference mockDocumentReference = mock(DocumentReference.class);
        Task<Void> mockTask = mock(Task.class);
        DocumentSnapshot mockSnapshot = mock(DocumentSnapshot.class);
        Object snapShot = mock(Object.class);

        when(mockFirestore.collection("Players")).thenReturn(mockCollectionReference);
        when(mockCollectionReference.document(anyString())).thenReturn(mockDocumentReference);
        when(mockDocumentReference.set(any(HashMap.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any(OnSuccessListener.class))).thenAnswer(new Answer<Task>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                return mockTask;
            }
        });

        PlayersDB playersDB = new PlayersDB(mockFirestore);
        playersDB.addPlayer(player, new OnCompleteListener<Player>() {
            @Override
            public void onComplete(Player item, boolean success) {
                assertTrue(success);
                assertTrue(item.getUserName().equals(player.getUserName()));
            }
        });
    }
}
