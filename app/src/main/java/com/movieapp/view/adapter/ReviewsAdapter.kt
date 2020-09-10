package com.movieapp.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.movieapp.R
import com.movieapp.model.repository.NetworkState
import com.movieapp.model.vo.Reviews
import kotlinx.android.synthetic.main.item_movie_review.view.*
import kotlinx.android.synthetic.main.item_network_state.view.*

class ReviewsAdapter(public val context: Context) :
    PagedListAdapter<Reviews, RecyclerView.ViewHolder>(
        ReviewsAdapter.ReviewDiffCallback()
    ) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.item_movie_review, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.item_network_state, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }


    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }


    class ReviewDiffCallback : DiffUtil.ItemCallback<Reviews>() {
        override fun areItemsTheSame(oldItem: Reviews, newItem: Reviews): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Reviews, newItem: Reviews): Boolean {
            return oldItem == newItem
        }

    }


    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(review: Reviews?, context: Context) {
            itemView.author.text = review?.author
            itemView.review.text = review?.content
        }

    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE;
            } else {
                itemView.progress_bar_item.visibility = View.GONE;
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                Log.e("NetworkStateViewHolder", networkState.message)
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                Log.e("NetworkStateViewHolder", networkState.message)
            } else {
                Log.d("NetworkStateViewHolder", "all good")
            }
        }
    }


    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
            } else {                                       //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
            notifyItemChanged(itemCount - 1)       //add the network message at the end
        }

    }
}