import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import java.lang.System.exit
import utils.ScannerInput

private val noteAPI = NoteAPI()
private val logger = KotlinLogging.logger {}
fun main(args: Array<String>) {
    runMenu()
}
fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a note                |
         > |   2) List all notes            |
         > |   3) Update a note             |
         > |   4) Delete a note             |
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
            0  -> exitApp()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}
fun addNote(){
    //logger.info { "addNote() function invoked" }
    val noteTitle = ScannerInput.readNextLine("Enter a title for the note: ")
    val notePriority = ScannerInput.readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = ScannerInput.readNextLine("Enter a category for the note: ")
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}
fun listNotes(){
    //logger.info { "listNotes() function invoked" }
    println(noteAPI.listAllNotes())
}

fun updateNote(){
    logger.info { "updateNote() function invoked" }
}

fun deleteNote(){
    logger.info { "deleteNote() function invoked" }
}

fun exitApp(){
    logger.info { "exitApp() function invoked" }
    exit(0)
}