package com.stargazer.recipeapp.adapter


//class NoteRVAdapter(
//    val context: Context,
//    val noteClickDeleteInterface: NoteClickDeleteInterface,
//    val noteClickInterface: NoteClickInterface
//) :
//    RecyclerView.Adapter<NoteRVAdapter.ViewHolder>() {
//
//    private val allNotes = ArrayList<Note>()
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val noteTV: TextView = itemView.findViewById(R.id.idTVNote)
//        val dateTV: TextView = itemView.findViewById(R.id.idTVDate)
//        val deleteIV: ImageView = itemView.findViewById(R.id.idIVDelete)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(
//            R.layout.note_rv_item,
//            parent, false
//        )
//        return ViewHolder(itemView)
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.noteTV.text = allNotes[position].noteTitle
//        holder.dateTV.text = "Last Updated : ${allNotes[position].timeStamp}"
//
//        holder.deleteIV.setOnClickListener {
//            noteClickDeleteInterface.onDeleteIconClick(allNotes[position])
//        }
//
//        holder.itemView.setOnClickListener {
//            noteClickInterface.onNoteClick(allNotes[position])
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return allNotes.size
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun updateList(newList: List<Note>) {
//        allNotes.clear()
//        allNotes.addAll(newList)
//        notifyDataSetChanged()
//    }
//}
//
//interface NoteClickDeleteInterface {
//    fun onDeleteIconClick(note: Note)
//}
//
//interface NoteClickInterface {
//    fun onNoteClick(note: Note)
//}
