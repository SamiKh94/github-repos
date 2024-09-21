package com.githubrepos.app.utils.views


import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import coil.load
import coil.transform.CircleCropTransformation
import com.githubrepos.app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Avatar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var imageUrl: String? = null
    private var placeholderRes: Int = R.drawable.ic_avatar_placeholder
    private var errorRes: Int = R.drawable.ic_avatar_placeholder

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.Avatar, 0, 0).apply {
            try {
                imageUrl = getString(R.styleable.Avatar_imageUrl)
                placeholderRes =
                    getResourceId(R.styleable.Avatar_placeholder, placeholderRes)
                errorRes = getResourceId(R.styleable.Avatar_errorImage, errorRes)
            } finally {
                recycle()
            }
        }

        // Load the image when the view is created
        loadImage()
    }

    private fun loadImage() {
        if (!imageUrl.isNullOrEmpty()) {
            this.load(imageUrl) {
                placeholder(placeholderRes)
                crossfade(true)
                transformations(CircleCropTransformation())
                error(errorRes)
            }
        } else {
            // If the URL is null or empty, load the placeholder
            setImageResource(placeholderRes)
        }
    }

    fun setImageUrl(url: String?) {
        imageUrl = url
        loadImage()  // Re-load the image when the URL is set
    }
}