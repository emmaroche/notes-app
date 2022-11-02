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

    //code reference for adding colour to improve UI: https://www.lihaoyi.com/post/BuildyourownCommandLinewithANSIescapecodes.html#deletion

    // displays the colour
    val magenta = "\u001b[35m"
    val cyan = "\u001b[36m"
    // displays the decoration
    val bold = "\u001b[1m"
    // resets colour and decoration back to what it previously was
    val reset = "\u001b[0m"

    return readNextInt(""" 
        >
$magenta  ---    -Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø-$reset      
$magenta /   \   $cyan|$reset                                             $cyan|$reset
$magenta|_____|$reset  $cyan|$reset $bold   Note Menu$reset                                $cyan|$reset
$cyan|     |$reset  $cyan|$reset                                             $cyan|$reset
$cyan|  $bold!$reset  $cyan|$reset  $cyan|$reset   $magenta 1)$reset Add a note                            $cyan|$reset
$cyan| $magenta$bold N$reset  $cyan|  |$reset   $magenta 2)$reset List all notes                        $cyan|$reset
$cyan| $magenta$bold O$reset  $cyan|  |$reset   $magenta 3)$reset Search notes                          $cyan|$reset 
$cyan| $magenta$bold T$reset  $cyan|  |$reset   $magenta 4)$reset Update a note                         $cyan|$reset
$cyan| $magenta$bold E$reset  $cyan|  |$reset   $magenta 5)$reset Delete a note                         $cyan|$reset
$cyan| $magenta$bold S$reset  $cyan|  |$reset   $magenta 6)$reset Archive a note                        $cyan|$reset
$cyan|     |  |$reset   $magenta 7)$reset Mark note as completed                $cyan|$reset
$cyan| $magenta$bold A$reset  $cyan|  |$reset   $magenta 8)$reset Save a note                           $cyan|$reset
$cyan| $magenta$bold P$reset  $cyan|  |$reset   $magenta 9)$reset Load a note                           $cyan|$reset
$cyan| $magenta$bold P$reset  $cyan|  |$reset                                             $cyan|$reset
$cyan|  $cyan$bold!  |$reset  $cyan|$reset   $magenta 0)$reset Exit                                  $cyan|$reset
$cyan|_____|$reset  $cyan|$reset                                             $cyan|$reset    
$magenta\     / $reset $cyan|_____________________________________________|$reset
$magenta \   / $reset  
 $magenta \ / $reset   $magenta⬇$reset Enter option below $magenta⬇$reset 
 >                 
         >         $magenta==>>$reset """.trimMargin(">"))

}
fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addNote()
            2  -> listNotes()
            3  -> searchNotes()
            4  -> updateNote()
            5  -> deleteNote()
            6  -> archiveNote()
            7  -> completeNote()
            8  -> save()
            9  -> load()
            0  -> exitApp()
            else -> println("\n         Invalid option entered: ${option}\n")
        }
    } while (true)
}

//Add note
fun addNote(){
    logger.info { "Adding a new note to the list\n" }

    val noteTitle = readNextLine("\n         Enter a title for the note: ")
    val noteContents = readNextLine("         Enter contents for the note: ")
    val noteCategory = readValidCategory("         Enter a category for the note from ${CategoryUtility.categories}: ")
    val notePriority = readValidPriority("         Enter note priority (1-low, 2, 3, 4, 5-high): ")
    val noteProgress = readValidProgress("         Enter note progress (To-do, doing, done): ")
    val isAdded = noteAPI.add(Note(noteTitle, noteContents, noteCategory, notePriority ,noteProgress, false, false))

    if (isAdded) {
        println("\n         Note Added Successfully!\n")
    } else {
        println("\n         Add Failed\n")
    }
}

//List notes
fun listNotes() {
    logger.info { "List notes menu\n" }

    // displays the colour
    val magenta = "\u001b[35m"
    val cyan = "\u001b[36m"
    // displays the decoration
    val bold = "\u001b[1m"
    // resets colour back to what it previously was
    val reset = "\u001b[0m"

    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt("""
         >
         >         $magenta-Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø-$reset
         >         $cyan|$reset                                             $cyan|$reset
         >         $cyan|$reset $bold  List Menu$reset                                 $cyan|
         >         $cyan|$reset                                             $cyan|$reset
         >         $cyan|$reset  $magenta 1)$reset View ALL notes                         $cyan|$reset
         >         $cyan|$reset  $magenta 2)$reset View ACTIVE notes                      $cyan|$reset
         >         $cyan|$reset  $magenta 3)$reset View ARCHIVED notes                    $cyan|$reset
         >         $cyan|$reset  $magenta 4)$reset View COMPLETED notes                   $cyan|$reset
         >         $cyan|$reset  $magenta 5)$reset View notes in alphabetical order       $cyan|$reset
         >         $cyan|$reset  $magenta 6)$reset View notes of a specified priority     $cyan|$reset
         >         $cyan|$reset  $magenta 7)$reset View notes of a specified progress     $cyan|$reset
         >         $cyan|$reset                                             $cyan|$reset
         >         $cyan|$reset  $magenta 0)$reset Return to notes menu                   $cyan|$reset
         >         $cyan|$reset                                             $cyan|$reset
         >         $cyan|_____________________________________________|$reset
         >         
         >         $magenta⬇$reset Enter option below $magenta⬇$reset 
                
         >         $magenta==>>$reset """.trimMargin(">"))

        when (option) {
            1 -> listAllNotes()
            2 -> listActiveNotes()
            3 -> listArchivedNotes()
            4 -> listCompletedNotes()
            5 -> listActiveNotesInAlphabeticalOrder()
            6 -> listBySelectedPriority()
            7 -> listBySelectedProgress()
            0  -> runMenu()
            else -> println("\n         Invalid option entered: " + option)
        }
    } else {
        println("\n         Option Invalid - No notes stored")
    }
}

//Search notes
fun searchNotes() {
     logger.info { "Search notes menu\n" }

    // displays the colour
    val magenta = "\u001b[35m"
    val cyan = "\u001b[36m"
    // displays the decoration
    val bold = "\u001b[1m"
    // resets colour back to what it previously was
    val reset = "\u001b[0m"

    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt("""
         >
         >        $magenta-Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø - Ø-$reset
         >        $cyan|$reset                                             $cyan|$reset
         >        $cyan|$reset $bold  Search Menu$reset                               $cyan|$reset
         >        $cyan|$reset                                             $cyan|$reset
         >        $cyan|$reset  $magenta 1)$reset Search notes by title                  $cyan|$reset
         >        $cyan|$reset  $magenta 2)$reset Search notes by contents               $cyan|$reset
         >        $cyan|$reset  $magenta 3)$reset Search Notes by Category               $cyan|$reset
         >        $cyan|$reset                                             $cyan|$reset
         >        $cyan|$reset  $magenta 0)$reset Return to notes menu                   $cyan|$reset
         >        $cyan|$reset                                             $cyan|$reset
         >        $cyan|_____________________________________________|$reset
         >         
         >        $magenta⬇$reset Enter option below $magenta⬇$reset 
                
         >        $magenta==>>$reset """.trimMargin(">"))


        when (option) {

            1  -> searchNotesByTitle()
            2  -> searchNotesByContents()
            3  -> searchNotesByCategory()
            0  -> runMenu()
            else -> println("         Invalid option entered: \n" + option)
        }
    } else {
        println("\n         Option Invalid - No notes stored\n")
    }
}

//List ALL notes
fun listAllNotes() {
    logger.info { "Listing all notes \n" }

    println(noteAPI.listAllNotes())
}

//List active notes
fun listActiveNotes() {
    logger.info { "Listing active notes \n" }
    println(noteAPI.listActiveNotes())
}

//List archived notes
fun listArchivedNotes() {
    logger.info { "Listing archived notes \n" }

    println(noteAPI.listArchivedNotes())
}

//List completed notes
fun listCompletedNotes() {
    logger.info { "Listing completed notes \n" }

    println(noteAPI.listCompletedNotes())
}

//List notes in alphabetical order of the title
fun listActiveNotesInAlphabeticalOrder(){
    logger.info { "Listing note in Alphabetical Order of title \n" }

    println(noteAPI.listActiveNotesInAlphabeticalOrderOfTitle())
}

//List notes of the priority that was inputted
fun listBySelectedPriority() {
    logger.info { "Listing note by selected priority\n" }

    val searchSelectedPriority = readNextInt("\n         Enter the note priority number to list: ")
    val searchResults = noteAPI.listNotesBySelectedPriority(searchSelectedPriority)
    if (searchResults.isEmpty()) {
        println("\n         No notes found\n")
    } else {
        println(searchResults)
    }
}

//List notes of the progress that was inputted
fun listBySelectedProgress() {
    logger.info { "Listing note by selected progress\n" }

    val searchSelectedProgress = readNextLine("\n        Enter the note progress to list: ")
    val searchResults = noteAPI.listNotesBySelectedProgress(searchSelectedProgress)
    if (searchResults.isEmpty()) {
        println("\n         No notes found\n")
    } else {
        println(searchResults)
    }
}

//Update a note
fun updateNote() {
    logger.info { "Updating note\n" }

    listAllNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("\n         Enter the number of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("         Enter a title for the note: ")
            val noteContents = readNextLine("         Enter contents for the note: ")
            val noteCategory = readValidCategory("         Enter a category for the note from ${CategoryUtility.categories}: ")
            val notePriority = readValidPriority("         Enter note priority (1-low, 2, 3, 4, 5-high): ")
            val noteProgress = readValidProgress("         Enter note progress (To-do, doing, done): ")

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, noteContents, noteCategory, notePriority, noteProgress, false, false))){
                println("\n         Update Successful!\n")
            } else {
                println("\n         Update Failed\n")
            }
        } else {
            println("\n         There are no notes for this number\n")
        }
    }
}

//Delete a note
fun deleteNote(){
    logger.info { "Deleting note\n" }

    listAllNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note to delete if notes exist
        val indexToDelete = readNextInt("\n         Enter the number of the note to delete: ")
        //pass the index of the note to NoteAPI for deleting and check for success.
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("\n         Delete Successful! Deleted note: ${noteToDelete.noteTitle}\n")
        } else {
            println("\n         Delete NOT Successful\n")
        }
    }
}

//Archive a note
fun archiveNote() {
    logger.info { "Archiving note\n" }

    listActiveNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        // only ask the user to choose the note to archive if active notes exist
        val indexToArchive = readNextInt("\n         Enter the number of the note to archive: ")
        // pass the index of the note to NoteAPI for archiving and check for success.
        if (noteAPI.archiveNote(indexToArchive)) {
            println("\n         Archive Successful!\n")
        } else {
            println("\n         Archive NOT Successful\n")
        }
    }
}

//Mark a note as completed
fun completeNote() {
    logger.info { "Marking note as completed\n" }

    listActiveNotes()
    if (noteAPI.numberOfCompletedNotes() > 0) {
        // only ask the user to choose the note to archive if active notes exist
        val indexToComplete = readNextInt("\n         Enter the number of the note to mark as completed: ")
        // pass the index of the note to NoteAPI for archiving and check for success.
        if (noteAPI.completedNote(indexToComplete)) {
            println("\n         Completion Successful!\n")
        } else {
            println("\n         Completion NOT Successful\n")
        }
    }
}

//Search notes by title
fun searchNotesByTitle() {
    logger.info { "Searching note by title\n" }

    val searchTitle = readNextLine("\n         Enter the title to search by: ")
    val searchResults = noteAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("\n         No notes found\n")
    } else {
        println(searchResults)
    }
}

//Search notes by its contents
fun searchNotesByContents() {
    logger.info { "Searching note by Contents\n" }

    val searchTitle = readNextLine("\n         Enter contents to search by: ")
    val searchResults = noteAPI.searchByContent(searchTitle)
    if (searchResults.isEmpty()) {
        println("\n         No notes found\n")
    } else {
        println(searchResults)
    }
}

//Search notes by specific category
fun searchNotesByCategory() {
    logger.info { "Searching note by category\n" }

    val searchTitle = readNextLine("\n         Enter the category to search by: ")
    val searchResults = noteAPI.searchByCategory(searchTitle)
    if (searchResults.isEmpty()) {
        println("\n         No notes found\n")
    } else {
        println(searchResults)
    }
}

fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("\n         Error writing to file: $e")
    }
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("\n         Error reading from file: $e")
    }
}

fun exitApp(){
    logger.info { "Exiting App, thank you for using!\n" }
    exit(0)
}

