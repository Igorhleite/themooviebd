package com.igorleite.themooviebd.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.igorleite.themooviebd.R
import com.igorleite.themooviebd.databinding.CustomTvSearchBinding
import com.igorleite.themooviebd.utils.hideKeyboard
import com.igorleite.themooviebd.utils.onDone

class CustomInputField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var hintSearchText: String? = null
    private var customInputFieldClickListener: CiActionButtonListener? = null

    private val binding = CustomTvSearchBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        setLayout(attrs)
        setCustomInputFieldActions()
    }

    fun interface CiActionButtonListener {
        fun ciOnButtonClick()
    }

    private fun setLayout(attrs: AttributeSet?) {
        attrs?.let { attributeSet ->
            val attributes =
                context.obtainStyledAttributes(attributeSet, R.styleable.CustomInputField)
            setBackgroundResource(R.drawable.search_tv_background)

            val hintResId =
                attributes.getResourceId(R.styleable.CustomInputField_custom_search_hint, 0)
            if (hintResId != 0) {
                hintSearchText = context.getString(hintResId)
            }
            attributes.recycle()
        }
    }

    private fun setCustomInputFieldActions() {
        with(binding) {
            searchButton.setOnClickListener {
                customInputFieldClickListener?.ciOnButtonClick()
                hideKeyboard()
            }
            etSearch.onDone {
                customInputFieldClickListener?.ciOnButtonClick()
                hideKeyboard()
            }
        }
    }

    fun eiSetOnBtOutClickListener(listener: CiActionButtonListener) {
        customInputFieldClickListener = listener
    }

    fun eiGetEditTextField(): String {
        return binding.etSearch.text.toString()
    }
}