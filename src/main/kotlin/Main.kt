import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistence.JSONSerializer
import java.lang.System.exit
import utils.CategoryUtility
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import utils.ValidateInput.readValidCategory
import utils.ValidateInput.readValidPriority
import utils.ValidateInput.readValidProgress
import java.io.File

//private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))
private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))
private val logger = KotlinLogging.logger {}
fun main(args: Array<String>) {
    runMenu()
}
fun mainMenu() : Int {
    return readNextInt(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU!!                    |
         > |   1) Add a note                |
         > |   2) List all notes            |
         > |   3) Update a note             |
         > |   4) Delete a note             |
         > |   5) Archive a note            |
         > |   6) Mark note as completed    |
         > |   7) Save a note               |
         > |   8) Load a note               |
         > |   9) Search for a note         |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">"))
}
fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addNote()
            2  -> listNotes()
            3  -> updateNote()
            4  -> deleteNote()
            5  -> archiveNote()
            6  -> completeNote()
            7  -> save()
            8  -> load()
            9  -> searchNotes()
            0  -> exitApp()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}
fun addNote(){
    //logger.info { "addNote() function invoked" }
    val noteTitle = readNextLine("Enter a title for the note: ")
    val noteContents = readNextLine("Enter contents for the note: ")
    val noteCategory = readValidCategory("Enter a category for the note from ${CategoryUtility.categories}: ")
    val notePriority = readValidPriority("Enter note priority (1-low, 2, 3, 4, 5-high): ")
    val noteProgress = readValidProgress("Enter note progress (To-do, doing, done): ")
    val isAdded = noteAPI.add(Note(noteTitle, noteContents, noteCategory, notePriority ,noteProgress, false, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}
fun listNotes() {
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL notes          |
                  > |   2) View ACTIVE notes       |
                  > |   3) View ARCHIVED notes     |
                  > --------------------------------
         > ==>> """.trimMargin(">"))


        when (option) {
            1 -> listAllNotes()
            2 -> listActiveNotes()
            3 -> listArchivedNotes()
            4 -> listBySelectedPriority()
            5 -> listBySelectedProgress()
            6 -> listActiveNotesInAlphabeticalOrder()
            else -> println("Invalid option entered: " + option)
        }
    } else {
        println("Option Invalid - No notes stored")
    }
}

//List notes of the priority that was inputted
fun listBySelectedPriority() {
    val searchSelectedPriority = readNextInt("Enter the priority to search by: ")
    val searchResults = noteAPI.listNotesBySelectedPriority(searchSelectedPriority)
    if (searchResults.isEmpty()) {
        println("No notes found")
    } else {
        println(searchResults)
    }
}

//List notes of the progress that was inputted
fun listBySelectedProgress() {
    val searchSelectedProgress = readNextLine("Enter the progress to search by: ")
    val searchResults = noteAPI.listNotesBySelectedProgress(searchSelectedProgress)
    if (searchResults.isEmpty()) {
        println("No notes found")
    } else {
        println(searchResults)
    }
}

fun listActiveNotesInAlphabeticalOrder(){
    println(noteAPI.listActiveNotesInAlphabeticalOrderOfTitle())
}
fun listAllNotes() {
    println(noteAPI.listAllNotes())
}

fun listActiveNotes() {
    println(noteAPI.listActiveNotes())
}

fun listArchivedNotes() {
    println(noteAPI.listArchivedNotes())
}

fun updateNote() {
    //logger.info { "updateNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val noteContents = readNextLine("Enter contents for the note: ")
            val noteCategory = readValidCategory("Enter a category for the note from ${CategoryUtility.categories}: ")
            val notePriority = readValidPriority("Enter note priority (1-low, 2, 3, 4, 5-high): ")
            val noteProgress = readValidProgress("Enter note progress (To-do, doing, done): ")

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, noteContents, noteCategory, notePriority, noteProgress, false, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

fun archiveNote() {
    listNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        // only ask the user to choose the note to archive if active notes exist
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        // pass the index of the note to NoteAPI for archiving and check for success.
        if (noteAPI.archiveNote(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

fun completeNote() {
    listNotes()
    if (noteAPI.numberOfCompletedNotes() > 0) {
        // only ask the user to choose the note to archive if active notes exist
        val indexToComplete = readNextInt("Enter the index of the note to mark as completed: ")
        // pass the index of the note to NoteAPI for archiving and check for success.
        if (noteAPI.completedNote(indexToComplete)) {
            println("Completion Successful!")
        } else {
            println("Completion NOT Successful")
        }
    }
}

fun deleteNote(){
    //logger.info { "deleteNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note to delete if notes exist
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        //pass the index of the note to NoteAPI for deleting and check for success.
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Delete Successful! Deleted note: ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun searchNotes() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = noteAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No notes found")
    } else {
        println(searchResults)
    }
}

fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun exitApp(){
    logger.info { "exitApp() function invoked" }
    exit(0)
}

