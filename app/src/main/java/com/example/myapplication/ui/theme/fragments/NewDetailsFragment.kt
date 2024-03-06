package com.example.myapplication.ui.theme.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.adapters.NewsAdapter
import com.example.myapplication.databinding.FragmentNewDetailsBinding
import com.example.myapplication.models.Article
import com.example.myapplication.ui.theme.mvvm.NewsActivity
import com.example.myapplication.ui.theme.mvvm.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class NewDetailsFragment : Fragment() {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentNewDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNewDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        newsViewModel = (requireActivity() as NewsActivity).newsViewModel


        val article = arguments?.getSerializable("article") as Article


        newsAdapter = NewsAdapter()


        newsAdapter.setOnItemClickListener { selectedArticle ->
            val bundle = Bundle().apply {
                putSerializable("article", selectedArticle)
            }
            findNavController().navigate(R.id.action_newsDetailsFragment_to_articleFragment, bundle)
        }


        binding.apply {
            Glide.with(requireContext()).load(article.urlToImage).into(articleImage)
            articleTitle.text = article.title
            articleDescription.text = article.description
            articleDateTime.text = article.publishedAt


            SourceButton.setOnClickListener {
                article.url?.let { url ->
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }


            fabFavorite.setOnClickListener {
                newsViewModel.addToFavourites(article)
                Snackbar.make(view, "Added to favourites", Snackbar.LENGTH_SHORT).show()
            }


            fabShare.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, article.url)
                startActivity(Intent.createChooser(shareIntent, "Share Article"))
            }
        }
    }


}
