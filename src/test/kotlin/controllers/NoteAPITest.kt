package controllers

import models.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NoteAPITest {

    private var learnKotlin: Note? = null
    private var summerHoliday: Note? = null
    private var codeApp: Note? = null
    private var testApp: Note? = null
    private var swim: Note? = null
    private var populatedNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))
    private var emptyNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))

    @BeforeEach
    fun setup() {
        learnKotlin = Note("Learning Kotlin", "Learn new Kotlin code","College", 5,"To-do", false, true)
        summerHoliday = Note("Summer Holiday to France","Go on holiday to france", "Holiday",1, "Doing", false, false)
        codeApp = Note("Code App", "This is a code app","Work",4, "Doing",  true, false)
        testApp = Note("Test App", "Test the app is working as expected","Work", 4, "To-do", false, false)
        swim = Note("Swim - Pool", "Go for a swim", "Hobby",3, "Doing",  true, false)

        // adding 5 Note to the notes api
        populatedNotes!!.add(learnKotlin!!)
        populatedNotes!!.add(summerHoliday!!)
        populatedNotes!!.add(codeApp!!)
        populatedNotes!!.add(testApp!!)
        populatedNotes!!.add(swim!!)
    }

    @AfterEach
    fun tearDown() {
        learnKotlin = null
        summerHoliday = null
        codeApp = null
        testApp = null
        swim = null
        populatedNotes = null
        emptyNotes = null
    }

    @Nested
    inner class AddNotes {
        @Test
        fun `adding a Note to a populated list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", "Study Lambdas notes","College", 1, "Done",  false, false)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertEquals(newNote, populatedNotes!!.findNote(populatedNotes!!.numberOfNotes() - 1))
        }

        @Test
        fun `adding a Note to an empty list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", "Study Lambdas notes","College",1, "To-do", false, false)
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.add(newNote))
            assertEquals(1, emptyNotes!!.numberOfNotes())
            assertEquals(newNote, emptyNotes!!.findNote(emptyNotes!!.numberOfNotes() - 1))
        }
    }

    @Nested
    inner class UpdateNotes {
        @Test
        fun `updating a note that does not exist returns false`() {
            assertFalse(populatedNotes!!.updateNote(6, Note("Updating Note", "Check if notes can be updated","Work",2, "Doing",  false, false)))
            assertFalse(populatedNotes!!.updateNote(-1, Note("Updating Note", "Check if notes can be updated","Work",2, "Done", false,false)))
            assertFalse(emptyNotes!!.updateNote(0, Note("Updating Note", "Check if notes can be updated","Work",2, "To-do", false,false)))
        }

        @Test
        fun `updating a note that exists returns true and updates`() {
            // check note 5 exists and check the contents
            assertEquals(swim, populatedNotes!!.findNote(4))
            assertEquals("Swim - Pool", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(3, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("Hobby", populatedNotes!!.findNote(4)!!.noteCategory)

            // update note 5 with new information and ensure contents updated successfully
            assertTrue(populatedNotes!!.updateNote(4, Note("Updating Note", "Check if notes can be updated","College", 2, "Done",  false, false)))
            assertEquals("Updating Note", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(2, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("College", populatedNotes!!.findNote(4)!!.noteCategory)
        }
    }

    @Nested
    inner class DeleteNotes {

        @Test
        fun `deleting a Note that does not exist, returns null`() {
            assertNull(emptyNotes!!.deleteNote(0))
            assertNull(populatedNotes!!.deleteNote(-1))
            assertNull(populatedNotes!!.deleteNote(5))
        }

        @Test
        fun `deleting a note that exists delete and returns deleted object`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(swim, populatedNotes!!.deleteNote(4))
            assertEquals(4, populatedNotes!!.numberOfNotes())
            assertEquals(learnKotlin, populatedNotes!!.deleteNote(0))
            assertEquals(3, populatedNotes!!.numberOfNotes())
        }
    }

    @Nested
    inner class ArchiveNotes {
        @Test
        fun `archiving a note that does not exist returns false`() {
            assertFalse(populatedNotes!!.archiveNote(6))
            assertFalse(populatedNotes!!.archiveNote(-1))
            assertFalse(emptyNotes!!.archiveNote(0))
        }

        @Test
        fun `archiving an already archived note returns false`() {
            assertTrue(populatedNotes!!.findNote(2)!!.isNoteArchived)
            assertFalse(populatedNotes!!.archiveNote(2))
        }

        @Test
        fun `archiving an active note that exists returns true and archives`() {
            assertFalse(populatedNotes!!.findNote(1)!!.isNoteArchived)
            assertTrue(populatedNotes!!.archiveNote(1))
            assertTrue(populatedNotes!!.findNote(1)!!.isNoteArchived)
        }
    }

    @Nested
    inner class CompleteNotes {
        @Test
        fun `Trying to mark a note as completed that does not exist returns false`() {
            assertFalse(populatedNotes!!.completedNote(6))
            assertFalse(populatedNotes!!.completedNote(-1))
            assertFalse(emptyNotes!!.completedNote(0))
        }

        @Test
        fun `Completing an already completed note returns false`() {
            assertTrue(populatedNotes!!.findNote(0)!!.isNoteCompleted)
            assertFalse(populatedNotes!!.completedNote(0))
        }

        @Test
        fun `Marking an active note as complete that exists returns true and completes`() {
            assertFalse(populatedNotes!!.findNote(1)!!.isNoteCompleted)
            assertTrue(populatedNotes!!.completedNote(1))
            assertTrue(populatedNotes!!.findNote(1)!!.isNoteCompleted)
        }
    }

    @Nested
    inner class ListNotes {

        @Test
        fun `listAllNotes returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listAllNotes().lowercase().contains("no notes"))
        }

        @Test
        fun `listAllNotes returns Notes when ArrayList has notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.listAllNotes().lowercase()
            assertTrue(notesString.contains("learning kotlin"))
            assertTrue(notesString.contains("code app"))
            assertTrue(notesString.contains("test app"))
            assertTrue(notesString.contains("swim"))
            assertTrue(notesString.contains("summer holiday"))
        }

        @Test
        fun `listActiveNotes returns no active notes stored when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfActiveNotes())
            assertTrue(
                emptyNotes!!.listActiveNotes().lowercase().contains("no active notes")
            )
        }

        @Test
        fun `listActiveNotes returns active notes when ArrayList has active notes stored`() {
            assertEquals(2, populatedNotes!!.numberOfActiveNotes())
            val activeNotesString = populatedNotes!!.listActiveNotes().lowercase()
            assertTrue(activeNotesString.contains("learning kotlin"))
            assertFalse(activeNotesString.contains("code app"))
            assertTrue(activeNotesString.contains("summer holiday"))
            assertTrue(activeNotesString.contains("test app"))
            assertFalse(activeNotesString.contains("swim"))
        }

        @Test
        fun `listActiveNotesInAlphabeticalOrderOfTitle returns no active notes stored when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertFalse(
                emptyNotes!!.listActiveNotesInAlphabeticalOrderOfTitle().lowercase().contains("No notes stored")
            )
        }

        @Test
        fun `listActiveNotesInAlphabeticalOrderOfTitle returns active notes when ArrayList has active notes stored`() {
            assertEquals(2, populatedNotes!!.numberOfActiveNotes())
            val alphaActiveNotesString = populatedNotes!!.listActiveNotesInAlphabeticalOrderOfTitle().lowercase()
            assertTrue(alphaActiveNotesString.contains("learning kotlin"))
            assertTrue(alphaActiveNotesString.contains("code app"))
            assertTrue(alphaActiveNotesString.contains("summer holiday"))
            assertTrue(alphaActiveNotesString.contains("test app"))
            assertTrue(alphaActiveNotesString.contains("swim"))
        }


        @Test
        fun `listArchivedNotes returns no archived notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfArchivedNotes())
            assertTrue(
                emptyNotes!!.listArchivedNotes().lowercase().contains("no archived notes")
            )
        }

        @Test
        fun `listArchivedNotes returns archived notes when ArrayList has archived notes stored`() {
            assertEquals(2, populatedNotes!!.numberOfArchivedNotes())
            val archivedNotesString = populatedNotes!!.listArchivedNotes().lowercase()
            assertFalse(archivedNotesString.contains("learning kotlin"))
            assertTrue(archivedNotesString.contains("code app"))
            assertFalse(archivedNotesString.contains("summer holiday"))
            assertFalse(archivedNotesString.contains("test app"))
            assertTrue(archivedNotesString.contains("swim"))
        }


        @Test
        fun `listCompletedNotes returns no completed notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfCompletedNotes())
            assertTrue(
                emptyNotes!!.listCompletedNotes().lowercase().contains("no completed notes stored")
            )
        }

        @Test
        fun `listCompletedNotes returns completed notes when ArrayList has completed notes stored`() {
            assertEquals(1, populatedNotes!!.numberOfCompletedNotes())
            val completedNotesString = populatedNotes!!.listCompletedNotes().lowercase()
            assertTrue(completedNotesString.contains("learning kotlin"))
            assertFalse(completedNotesString.contains("code app"))
            assertFalse(completedNotesString.contains("summer holiday"))
            assertFalse(completedNotesString.contains("test app"))
            assertFalse(completedNotesString.contains("swim"))
        }

        @Test
        fun `listNotesBySelectedPriority returns No Notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(
                emptyNotes!!.listNotesBySelectedPriority(1).lowercase().contains("no notes")
            )
        }

        @Test
        fun `listNotesBySelectedPriority returns no notes when no notes of that priority exist`() {
            // Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val priority2String = populatedNotes!!.listNotesBySelectedPriority(2).lowercase()
            assertTrue(priority2String.contains("no notes"))
            assertTrue(priority2String.contains("2"))
        }

        @Test
        fun `listNotesBySelectedPriority returns all notes that match that priority when notes of that priority exist`() {
            // Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val priority1String = populatedNotes!!.listNotesBySelectedPriority(1).lowercase()
            assertTrue(priority1String.contains("1 note"))
            assertTrue(priority1String.contains("priority 1"))
            assertTrue(priority1String.contains("summer holiday"))
            assertFalse(priority1String.contains("swim"))
            assertFalse(priority1String.contains("learning kotlin"))
            assertFalse(priority1String.contains("code app"))
            assertFalse(priority1String.contains("test app"))

            val priority4String = populatedNotes!!.listNotesBySelectedPriority(4).lowercase()
            assertTrue(priority4String.contains("2 note"))
            assertTrue(priority4String.contains("priority 4"))
            assertFalse(priority4String.contains("swim"))
            assertTrue(priority4String.contains("code app"))
            assertTrue(priority4String.contains("test app"))
            assertFalse(priority4String.contains("learning kotlin"))
            assertFalse(priority4String.contains("summer holiday"))
        }

        @Test
        fun `listNotesBySelectedProgress returns No Notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(
                emptyNotes!!.listNotesBySelectedProgress("To-do").lowercase().contains("no notes")
            )
        }

        @Test
        fun `listNotesBySelectedProgress returns no notes when no notes of that progress exist`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val progressDoneString = populatedNotes!!.listNotesBySelectedProgress("Done").lowercase()
            assertTrue(progressDoneString.contains("no notes"))

        }

        @Test
        fun `listNotesBySelectedProgress returns all notes that match that progress when notes of that progress exist`() {
            // Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val progressDoingString = populatedNotes!!.listNotesBySelectedProgress("Doing").lowercase()
            assertTrue(progressDoingString.contains("summer holiday"))
            assertTrue(progressDoingString.contains("code app"))
            assertFalse(progressDoingString.contains("test app"))


            val progressToDoString = populatedNotes!!.listNotesBySelectedProgress("To-do").lowercase()
            assertTrue(progressToDoString.contains("learning kotlin"))
            assertFalse(progressToDoString.contains("swim"))

        }
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.store()

            // Loading the empty notes.xml file into a new object
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            // Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 notes to the notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            // Loading notes.xml into a different collection
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            // Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.store()

            // Loading the empty notes.json file into a new object
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            // Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 notes to the notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            // Loading notes.json into a different collection
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            // Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }
    }

    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfNotesCalculatedCorrectly() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(0, emptyNotes!!.numberOfNotes())
        }

        @Test
        fun numberOfArchivedNotesCalculatedCorrectly() {
            assertEquals(2, populatedNotes!!.numberOfArchivedNotes())
            assertEquals(0, emptyNotes!!.numberOfArchivedNotes())
        }
        @Test
        fun numberOfCompletedNotesCalculatedCorrectly() {
            assertEquals(1, populatedNotes!!.numberOfCompletedNotes())
            assertEquals(0, emptyNotes!!.numberOfCompletedNotes())
        }

        @Test
        fun numberOfActiveNotesCalculatedCorrectly() {
            assertEquals(2, populatedNotes!!.numberOfActiveNotes())
            assertEquals(0, emptyNotes!!.numberOfActiveNotes())
        }

        @Test
        fun numberOfNotesByPriorityCalculatedCorrectly() {
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(1))
            assertEquals(0, populatedNotes!!.numberOfNotesByPriority(2))
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(3))
            assertEquals(2, populatedNotes!!.numberOfNotesByPriority(4))
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(5))
            assertEquals(0, emptyNotes!!.numberOfNotesByPriority(1))
        }

        @Test
        fun numberOfNotesByProgressCalculatedCorrectly() {
            assertEquals(2, populatedNotes!!.numberOfNotesByProgress("To-do"))
            assertEquals(3, populatedNotes!!.numberOfNotesByProgress("Doing"))
            assertEquals(0, populatedNotes!!.numberOfNotesByProgress("Done"))
            assertEquals(0, emptyNotes!!.numberOfNotesByProgress("To-do"))
        }
    }

    @Nested
    inner class SearchMethods {

        @Test
        fun `search notes by title returns no notes when no notes with that title exist`() {
            // Searching a populated collection for a title that doesn't exist.
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val searchResults = populatedNotes!!.searchByTitle("no results expected")
            assertTrue(searchResults.isEmpty())

            // Searching an empty collection
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.searchByTitle("").isEmpty())
        }

        @Test
        fun `search notes by title returns notes when notes with that title exist`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())

            // Searching a populated collection for a full title that exists (case matches exactly)
            var searchResults = populatedNotes!!.searchByTitle("Code App")
            assertTrue(searchResults.contains("Code App"))
            assertFalse(searchResults.contains("Test App"))

            // Searching a populated collection for a partial title that exists (case matches exactly)
            searchResults = populatedNotes!!.searchByTitle("App")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))

            // Searching a populated collection for a partial title that exists (case doesn't match)
            searchResults = populatedNotes!!.searchByTitle("aPp")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))
        }

        @Test
        fun `search notes by contents returns no notes when no notes with that content exist`() {
            // Searching a populated collection for contents that doesn't exist.
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val searchResults = populatedNotes!!.searchByContent("no results expected")
            assertTrue(searchResults.isEmpty())

            // Searching an empty collection
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.searchByTitle("").isEmpty())
        }

        @Test
        fun `search notes by contents returns notes when notes with that content exist`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())

            // Searching a populated collection for full contents that exists (case matches exactly)
            var searchResults = populatedNotes!!.searchByContent("Code App")
            assertTrue(searchResults.contains("Code App"))
            assertFalse(searchResults.contains("Test App"))

            // Searching a populated collection for partial contents that exists (case matches exactly)
            searchResults = populatedNotes!!.searchByContent("App")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))

            // Searching a populated collection for partial contents that exists (case doesn't match)
            searchResults = populatedNotes!!.searchByContent("aPp")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))
        }

        @Test
        fun `search notes by category returns no notes when no notes in that category exist`() {
            // Searching a populated collection for contents that doesn't exist.
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val searchResults = populatedNotes!!.searchByCategory("no results expected")
            assertTrue(searchResults.isEmpty())

            // Searching an empty collection
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.searchByTitle("").isEmpty())
        }

        @Test
        fun `search notes by category returns notes when notes in that category exist`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())

            // Searching a populated collection for a full category that exists (case matches exactly)
            var searchResults = populatedNotes!!.searchByCategory("Work")
              assertTrue(searchResults.contains("Test App"))
               assertTrue(searchResults.contains("Code App"))
              assertFalse(searchResults.contains("Swim - Pool"))

            // Searching a populated collection for a partial category that exists (case matches exactly)
            searchResults = populatedNotes!!.searchByCategory("Holi")
            assertTrue(searchResults.contains("Summer Holiday"))
            assertFalse(searchResults.contains("Swim - Pool"))

            // Searching a populated collection for a partial category that exists (case doesn't match)
            searchResults = populatedNotes!!.searchByCategory("cOllege")
            assertTrue(searchResults.contains("Learning Kotlin"))
            assertFalse(searchResults.contains("Swim - Pool"))
        }

    }

}