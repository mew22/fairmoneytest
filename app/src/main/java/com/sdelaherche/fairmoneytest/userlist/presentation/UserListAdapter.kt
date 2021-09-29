package com.sdelaherche.fairmoneytest.userlist.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sdelaherche.fairmoneytest.R

class UserListAdapter :
    ListAdapter<UserModel, UserListAdapter.UserViewHolder>(DIFF_CALLBACK) {

    private companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserModel>() {
            override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean =
                oldItem == newItem
        }
    }

    var clickListener: ((user: UserModel) -> Unit)? = null

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { user -> holder.bind(user) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.tv_name)
        private val card: CardView = view.findViewById(R.id.card_user)
        private val picture: ImageView = view.findViewById(R.id.iv_picture)

        @SuppressLint("SetTextI18n")
        fun bind(user: UserModel) {
            name.text = "${user.title} ${user.firstName} ${user.lastName}"
            Glide.with(picture).load(user.picture).apply(
                RequestOptions()
                    .circleCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
            )
                .into(picture)
            card.setOnClickListener {
                clickListener?.invoke(user)
            }
        }
    }
}
