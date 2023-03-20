package com.example.codekamon;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashMap;

public class QRCodesDBTest {
    @Test
    public void testConstructor(){
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = mock(CollectionReference.class);
        when(mockFirestore.collection("QRCodes")).thenReturn(mockCollectionReference);
        QRCodesDB codesDB = new QRCodesDB(mockFirestore);
        assertTrue(codesDB.getDb().equals(mockFirestore));
        assertTrue(codesDB.getCollectionReference().equals(mockCollectionReference));
    }

    @Test
    public void testGetExistingQRCode(){
        HashMap<String,Object> map = new HashMap<>();

        map.put("content", "dummyContent");
        map.put("comments", new ArrayList<>());
        map.put("name", "dummyName");
        map.put("latitude", 21.0);
        map.put("longitude", 10.0);
        map.put("score", (long) 23);
        map.put("photoAsBytes", "dummyPhoto");


        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = mock(CollectionReference.class);
        DocumentReference mockDocumentReference = mock(DocumentReference.class);
        Task<DocumentSnapshot> mockTask = mock(Task.class);
        DocumentSnapshot snapshot = mock(DocumentSnapshot.class);
        when(mockFirestore.collection("QRCodes")).thenReturn(mockCollectionReference);
        when(mockCollectionReference.document(map.get("name").toString())).thenReturn(mockDocumentReference);
        when(mockDocumentReference.get()).thenReturn(mockTask);
        when(snapshot.get("QRCode content: ")).thenReturn(map);
        when(snapshot.exists()).thenReturn(true);
        when(mockTask.addOnSuccessListener(any(OnSuccessListener.class))).thenAnswer(new Answer<Task>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                ((OnSuccessListener) invocation.getArgument(0)).onSuccess(snapshot);
                return mockTask;
            }
        });
        QRCodesDB codesDB = new QRCodesDB(mockFirestore);
        codesDB.getQRCode(map.get("name").toString(), new OnCompleteListener<QRCode>() {
            @Override
            public void onComplete(QRCode item, boolean success) {
                assertTrue(success);
                assertTrue(item.getName().equals(map.get("name")));
            }
        });
    }

    @Test
    public void testGetNonExistentQRCode(){
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = mock(CollectionReference.class);
        DocumentReference mockDocumentReference = mock(DocumentReference.class);
        Task<DocumentSnapshot> mockTask = mock(Task.class);
        DocumentSnapshot snapshot = mock(DocumentSnapshot.class);

        when(mockFirestore.collection("QRCodes")).thenReturn(mockCollectionReference);
        when(mockCollectionReference.document(anyString())).thenReturn(mockDocumentReference);
        when(mockDocumentReference.get()).thenReturn(mockTask);
        when(snapshot.exists()).thenReturn(false);
        when(mockTask.addOnSuccessListener(any(OnSuccessListener.class))).thenAnswer(new Answer<Task>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                ((OnSuccessListener) invocation.getArgument(0)).onSuccess(snapshot);
                return mockTask;
            }
        });

        QRCodesDB codesDB = new QRCodesDB(mockFirestore);
        codesDB.getQRCode("dummyName", new OnCompleteListener<QRCode>() {
            @Override
            public void onComplete(QRCode item, boolean success) {
                assertFalse(success);
                assertNull(item);
            }
        });
    }

    @Test
    public void testGetQRCodeFailed(){
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = mock(CollectionReference.class);
        DocumentReference mockDocumentReference = mock(DocumentReference.class);
        Task<DocumentSnapshot> mockTask = mock(Task.class);
        DocumentSnapshot snapshot = mock(DocumentSnapshot.class);

        when(mockFirestore.collection("QRCodes")).thenReturn(mockCollectionReference);
        when(mockCollectionReference.document(anyString())).thenReturn(mockDocumentReference);
        when(mockDocumentReference.get()).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any(OnSuccessListener.class))).thenAnswer(new Answer<Task>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                return mockTask;
            }
        });
        when(mockTask.addOnFailureListener(any(OnFailureListener.class))).thenAnswer(new Answer<Task>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                return mockTask;
            }
        });

        QRCodesDB codesDB = new QRCodesDB(mockFirestore);
        codesDB.getQRCode("dummyName", new OnCompleteListener<QRCode>() {
            @Override
            public void onComplete(QRCode item, boolean success) {
                assertFalse(success);
                assertNull(item);
            }
        });
    }

    @Test
    public void testAddQRCode(){
        QRCode code = new QRCode("dummyContent");
        code.setName("dummyName");
        code.setLocation(21, 34);
        code.setPhotoAsBytes("dummyPhoto");
        code.setScore(23);

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = mock(CollectionReference.class);
        DocumentReference mockDocumentReference = mock(DocumentReference.class);
        Task<Void> mockTask = mock(Task.class);
        DocumentSnapshot snapshot = mock(DocumentSnapshot.class);

        when(mockFirestore.collection("QRCodes")).thenReturn(mockCollectionReference);
        when(mockCollectionReference.document(code.getName())).thenReturn(mockDocumentReference);
        when(mockDocumentReference.set(any(HashMap.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any(OnSuccessListener.class))).thenAnswer(new Answer<Task>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                return mockTask;
            }
        });

        QRCodesDB codesDB = new QRCodesDB(mockFirestore);
        codesDB.addQRCode(code, new OnCompleteListener<QRCode>() {
            @Override
            public void onComplete(QRCode item, boolean success) {
                assertTrue(item.getName().equals(code.getName()));
                assertTrue(success);
            }
        });
    }

    @Test
    public void testUpdateExistingQRCode(){
        QRCode code = new QRCode("dummyContent");
        code.setName("dummyName");
        code.setLocation(21, 34);
        code.setPhotoAsBytes("dummyPhoto");
        code.setScore(23);

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = mock(CollectionReference.class);
        DocumentReference mockDocumentReference = mock(DocumentReference.class);
        Task<Void> mockTask = mock(Task.class);
        DocumentSnapshot snapshot = mock(DocumentSnapshot.class);

        when(mockFirestore.collection("QRCodes")).thenReturn(mockCollectionReference);
        when(mockCollectionReference.document(code.getName())).thenReturn(mockDocumentReference);
        when(mockDocumentReference.update(anyString(), any(QRCode.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any(OnSuccessListener.class))).thenAnswer(new Answer<Task>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                return mockTask;
            }
        });

        QRCodesDB codesDB = new QRCodesDB(mockFirestore);
        codesDB.updateQRCode(code, new OnCompleteListener<QRCode>() {
            @Override
            public void onComplete(QRCode item, boolean success) {
                assertTrue(success);
                assertTrue(item.equals(code));
            }
        });
    }

    /**
     * Test the update of an code that doesn't exist.
     */
    @Test
    public void testUpdateNonExistentQRCode(){
        QRCode code = new QRCode("dummyContent");
        code.setName("dummyName");
        code.setLocation(21, 34);
        code.setPhotoAsBytes("dummyPhoto");
        code.setScore(23);

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = mock(CollectionReference.class);
        DocumentReference mockDocumentReference = mock(DocumentReference.class);
        Task<Void> mockTask = mock(Task.class);
        DocumentSnapshot snapshot = mock(DocumentSnapshot.class);

        when(mockFirestore.collection("QRCodes")).thenReturn(mockCollectionReference);
        when(mockCollectionReference.document(anyString())).thenReturn(mockDocumentReference);
        when(mockDocumentReference.update(anyString(), any(QRCode.class))).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any(OnSuccessListener.class))).thenAnswer(new Answer<Task>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                return mockTask;
            }
        });
        when(mockTask.addOnFailureListener(any(OnFailureListener.class))).thenAnswer(new Answer<Task>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                return mockTask;
            }
        });

        QRCodesDB codesDB = new QRCodesDB(mockFirestore);
        codesDB.updateQRCode(code, new OnCompleteListener<QRCode>() {
            @Override
            public void onComplete(QRCode item, boolean success) {
                assertFalse(success);
                assertNull(item);
            }
        });
    }

    /**
     * Test deleting non existent and code that exists.
     */
    @Test
    public void testDeleteQRCode(){
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = mock(CollectionReference.class);
        DocumentReference mockDocumentReference = mock(DocumentReference.class);
        Task<Void> mockTask = mock(Task.class);
        DocumentSnapshot snapshot = mock(DocumentSnapshot.class);

        when(mockFirestore.collection("QRCodes")).thenReturn(mockCollectionReference);
        when(mockCollectionReference.document(anyString())).thenReturn(mockDocumentReference);
        when(mockDocumentReference.delete()).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any(OnSuccessListener.class))).thenAnswer(new Answer<Task>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                return mockTask;
            }
        });

        QRCodesDB codesDB = new QRCodesDB(mockFirestore);
        codesDB.deleteQRCode("dummyCode", new OnCompleteListener<QRCode>() {
            @Override
            public void onComplete(QRCode item, boolean success) {
                assertTrue(success);
                assertNull(item);
            }
        });
    }

}
