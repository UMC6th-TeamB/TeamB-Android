package com.smumc.smumc_6th_teamc_android.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smumc.smumc_6th_teamc_android.databinding.ItemTermsBinding

class TermsRVAdapter(private val termsList: ArrayList<Terms>): RecyclerView.Adapter<TermsRVAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TermsRVAdapter.ViewHolder {
        val binding: ItemTermsBinding = ItemTermsBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TermsRVAdapter.ViewHolder, position: Int) {
        holder.bind(termsList[position])
    }

    override fun getItemCount(): Int = termsList.size

    inner class ViewHolder(val binding: ItemTermsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(terms: Terms){
            binding.title.text = terms.title
            binding.content.text = terms.content
        }
    }
}