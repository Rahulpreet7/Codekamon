package com.example.codekamon;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.List;

public class PlayersDBTest {

    /**
     * Tests the PlayersDB constructor.
     */
    @Test
    public void testPlayersDBConstructor(){
        HashMap<String, HashMap<String, HashMap<String, Object> >> collections = generateFakeCollections();
        FirebaseFirestore mockFirestore = new MockFireStore(collections).getInstance();
        PlayersDB playersDB = new PlayersDB(mockFirestore);

        assertTrue(playersDB.getCollectionReference().getId().equals("Players"));
        assertTrue(playersDB.getDb().equals(mockFirestore));
    }

    /**
     * Tests the get player function.
     */
    @Test
    public void testGetPlayer(){

        FirebaseFirestore mockFirestore = new MockFireStore(generateFakeCollections()).getInstance();

        //Tests with player not in the database
        PlayersDB playersDB = new PlayersDB(mockFirestore);
        String mockId = "baby2Id";
        playersDB.getPlayer(mockId, new OnCompleteListener<Player>() {
            @Override
            public void onComplete(Player item, boolean success) {
                assertTrue(success == false);
            }
        });

        //Tests with player in database
        String mockId2 = "babyId";
        playersDB.getPlayer(mockId2, new OnCompleteListener<Player>() {
            @Override
            public void onComplete(Player item, boolean success) {
                assertTrue(success == true);
                assertTrue(item.getUserName() == "baby");
            }
        });

        String mockId3 = "birdId";
        playersDB.getPlayer(mockId3, new OnCompleteListener<Player>() {
            @Override
            public void onComplete(Player item, boolean success) {
                assertTrue(success == true);
                assertTrue(item.getUserName() == "bird");
            }
        });


    }

    /**
     * Generates fake data set.
     *
     * @return A dataset containing fields inside documents, documents inside collections,
     * and collections inside database.
     */

    private HashMap<String, HashMap<String, HashMap<String, Object> >> generateFakeCollections(){
        HashMap<String, Object> field1 = new HashMap<>();
        HashMap<String, HashMap<String, Object> > documents = new HashMap<>();
        HashMap<String, HashMap<String, HashMap<String, Object> >> collections= new HashMap<>();

        field1.put("Username", "baby");
        field1.put("Email", "gamer@baby.com");
        field1.put("Android Id", "babyId");
        field1.put("Highest Score", "21");
        field1.put("Lowest Score", "18");
        field1.put("Number Of Codes Scanned", "4");
        field1.put("Total Score", "76");
        field1.put("Player Ranking", "6");
        field1.put("ScannedCodes", new HashMap<String,String>());

        HashMap<String, Object> field2 = new HashMap<>();

        field2.put("Username", "bird");
        field2.put("Email", "bird@hatemail.com");
        field2.put("Android Id", "birdId");
        field2.put("Highest Score", "45");
        field2.put("Lowest Score", "18");
        field2.put("Number Of Codes Scanned", "6");
        field2.put("Total Score", "65");
        field2.put("Player Ranking", "2");
        field2.put("ScannedCodes", new HashMap<String,String>());

        documents.put("birdId", field2);
        documents.put("babyId", field1);
        collections.put("Players", documents);

        return collections;
    }
}
