package controllers

import persistence.Serializer
import models.Note
import utils.Utilities.isValidListIndex

class NoteAPI (serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var notes = ArrayList<Note>()

    fun add(note: Note): Boolean {
        return notes.add(note)
    }
    fun numberOfNotes(): Int {
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }

    //Listing methods
    fun listAllNotes(): String =
        if  (notes.isEmpty()) "\n         No notes stored\n"
        else formatListString(notes)

    fun listActiveNotes(): String =
        if  (numberOfActiveNotes() == 0)  "\n         No active notes stored\n"
        else formatListString(notes.filter { note -> !note.isNoteArchived})

    fun listArchivedNotes(): String =
        if  (numberOfArchivedNotes() == 0) "\n         No archived notes stored\n"
        else formatListString(notes.filter { note -> note.isNoteArchived})

    fun listCompletedNotes(): String =
        if  (numberOfCompletedNotes() == 0) "\n        No notes stored\n"
        else formatListString(notes.filter { note -> !note.isNoteCompleted})

    fun listActiveNotesInAlphabeticalOrderOfTitle(): String =
        if  (notes.isEmpty()) "\n         No notes stored\n"
        else formatListString(notes.sortedBy { note -> note.noteTitle})

    fun listNotesBySelectedPriority(priority: Int): String =
        if (notes.isEmpty()) "\n         No notes stored"
        else {
            val listOfNotes = formatListString(notes.filter{ note -> note.notePriority == priority})
            if (listOfNotes.equals("")) "\n         No notes with priority: $priority\n"
            else "\n         ${numberOfNotesByPriority(priority)} note(s) with priority $priority\n: $listOfNotes"
        }

    fun listNotesBySelectedProgress(progress: String): String =
        if (notes.isEmpty()) "\n         No notes stored\n"
        else {
            val listOfNotes = formatListString(notes.filter{ note -> note.noteProgress == progress})
            if (listOfNotes.equals("")) "\n         No notes with progress: $progress\n"
            else "\n         ${numberOfNotesByProgress(progress)} note(s) with progress $progress\n: $listOfNotes"
        }


    //Counting methods
    fun numberOfActiveNotes(): Int = notes.count { note: Note -> !note.isNoteArchived }

    fun numberOfArchivedNotes(): Int = notes.count { note: Note -> note.isNoteArchived }

    fun numberOfCompletedNotes(): Int = notes.count { note: Note -> !note.isNoteCompleted }

    fun numberOfNotesByPriority(priority: Int): Int = notes.count { note: Note -> note.notePriority == priority }

    fun numberOfNotesByProgress(progress: String): Int = notes.count { note: Note -> note.noteProgress == progress }


    //update method
    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
        //find the note object by the index number
        val foundNote = findNote(indexToUpdate)

        //if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundNote != null) && (note != null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            return true
        }

        //if the note was not found, return false, indicating that the update was not successful
        return false
    }

    //delete method
    fun deleteNote(indexToDelete: Int): Note? {
        return if (isValidListIndex(indexToDelete, notes)) {
            notes.removeAt(indexToDelete)
        } else null
    }


    //archive note method
    fun archiveNote(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) {
            val noteToArchive = notes[indexToArchive]
            if (!noteToArchive.isNoteArchived) {
                noteToArchive.isNoteArchived = true
                return true
            }
        }
        return false
    }

    //completed note method
    fun completedNote(indexToComplete: Int): Boolean {
        if (isValidIndex(indexToComplete)) {
            val noteToComplete = notes[indexToComplete]
            if (noteToComplete.isNoteCompleted) {
                noteToComplete.isNoteCompleted = true
                return true
            }
        }
        return false
    }

    //search methods
    fun searchByTitle (searchString : String) =
        formatListString(
            notes.filter { note -> note.noteTitle.contains(searchString, ignoreCase = true) })

    fun searchByContent (searchString : String) =
        formatListString(
            notes.filter { note -> note.noteContents.contains(searchString, ignoreCase = true) })

    fun searchByCategory (searchString : String) =
        formatListString(
            notes.filter { note -> note.noteCategory.contains(searchString, ignoreCase = true) })

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, notes)
    }

    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }

    //helper function

    // displays the colour
    val magenta = "\u001b[35m"
    // displays the decoration
    val bold = "\u001b[1m"
    // resets colour back to what it previously was
    val reset = "\u001b[0m"
    private fun formatListString(notesToFormat : List<Note>) : String =

        notesToFormat
            .joinToString (separator = "\n") { note ->
            "\n        $magenta$bold ⬇ Note " + notes.indexOf(note).toString() + " ⬇$reset"+ note.toString()
            }

}