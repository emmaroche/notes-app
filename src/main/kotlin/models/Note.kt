package models

data class Note(var noteTitle: String,  var noteContents: String, var noteCategory: String, var notePriority: Int,
                var noteProgress: String, var isNoteArchived :Boolean, var isNoteCompleted :Boolean) {

    override fun toString(): String {
        
        // displays the colour
        val cyan = "\u001b[36m"
        // displays the decoration
        val bold = "\u001b[1m"
        // resets colour back to what it previously was
        val reset = "\u001b[0m"

        return  "\n" +
                "                                                  \n" +
                "        $cyan$bold Title: $reset${noteTitle}      \n" +
                "        $cyan$bold Contents: $reset$noteContents  \n" +
                "\n" +
                "        $cyan$bold Category: $reset$noteCategory   \n" +
                "        $cyan$bold Priority: $reset$notePriority   \n" +
                "        $cyan$bold Progress: $reset$noteProgress   "


    }

}