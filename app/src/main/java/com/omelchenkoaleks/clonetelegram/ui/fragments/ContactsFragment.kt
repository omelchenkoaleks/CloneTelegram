package com.omelchenkoaleks.clonetelegram.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.omelchenkoaleks.clonetelegram.R
import com.omelchenkoaleks.clonetelegram.models.CommonModel
import com.omelchenkoaleks.clonetelegram.utils.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.contact_item.view.*
import kotlinx.android.synthetic.main.fragment_contacts.*

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var mRefContacts: DatabaseReference // ссылка откуда мы будем скачивать наши данные
    private lateinit var mRefUsers: DatabaseReference // ссылка на users в базе данных (что можно было по id получить данные)


    // ЧТОБЫ НЕ БЫЛО УТЕЧКИ ПАМЯТИ:
    private lateinit var mRefUsersListener: AppValueEventListener

    // Для того, чтобы закрыть все слушатели, нужно собрать их и потом в массиве их все закрыть.
    private var mapListeners = hashMapOf<DatabaseReference, AppValueEventListener>()


    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Контакты"
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = contacts_recycler_view
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts, CommonModel::class.java)
            .build()

        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.contact_item, parent, false)
                return ContactsHolder(view)
            }

            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUsers =
                    REF_DATABASE_ROOT.child(NODE_USERS).child(model.id) // получаем ссылку на юзера

                mRefUsersListener = AppValueEventListener {
                    val contact = it.getCommonModel() // создали и получили контакт
                    holder.name.text = contact.fullName
                    holder.status.text = contact.state
                    holder.photo.downloadAndSetImage(contact.photoUrl)
                }

                mRefUsers.addValueEventListener(mRefUsersListener)
                mapListeners[mRefUsers] = mRefUsersListener
            }
        }

        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()

    }

    class ContactsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.contact_fullname
        val status: TextView = view.contact_status
        val photo: CircleImageView = view.contact_photo
    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
        mapListeners.forEach() {
            it.key.removeEventListener(it.value)
        }
    }

}