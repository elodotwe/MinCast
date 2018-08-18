package com.jacobarau.mincast.activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jacobarau.mincast.R
import com.jacobarau.mincast.subscription.Subscription

class SubscriptionsFragment : Fragment() {
    lateinit var model: SharedViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MyAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_subscriptions, container, false)
        viewManager = GridLayoutManager(activity, 3)
        viewAdapter = MyAdapter(model.getSubscriptions().value)
        model.getSubscriptions().observe(this, Observer {
            subscriptions -> viewAdapter.setData(subscriptions)
        })

        recyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.adapter = viewAdapter

        recyclerView.layoutManager = viewManager
        recyclerView.addItemDecoration(GridSpacing(20))

        // Inflate the layout for this fragment
        return root
    }

    class MyAdapter(private var myDataset: List<Subscription>?) :
            RecyclerView.Adapter<MyAdapter.ViewHolder>() {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        fun setData(newData: List<Subscription>?) {
            myDataset = newData
            notifyDataSetChanged()
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): MyAdapter.ViewHolder {
            // create a new view
            val subscriptionView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.subscription_item, parent, false)
            return ViewHolder(subscriptionView)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.view.findViewById<TextView>(R.id.itemName).text = myDataset!![position].title
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset?.size ?: 0
    }

    class GridSpacing(private val padding: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = padding / 2
            outRect.right = padding / 2
            outRect.top = padding / 2
            outRect.bottom = padding / 2
        }
    }
}
