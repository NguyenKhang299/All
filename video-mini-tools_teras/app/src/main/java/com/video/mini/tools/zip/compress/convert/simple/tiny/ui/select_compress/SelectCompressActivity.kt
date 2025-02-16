package com.video.mini.tools.zip.compress.convert.simple.tiny.ui.select_compress

import android.annotation.SuppressLint
import android.content.Intent
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.video.mini.tools.zip.compress.convert.simple.tiny.core.BaseActivity
import com.video.mini.tools.zip.compress.convert.simple.tiny.databinding.ActivitySelectCompressBinding
import com.video.mini.tools.zip.compress.convert.simple.tiny.model.OptionCompressType
import com.video.mini.tools.zip.compress.convert.simple.tiny.model.OptionCompression
import com.video.mini.tools.zip.compress.convert.simple.tiny.model.OptionMedia
import com.video.mini.tools.zip.compress.convert.simple.tiny.model.Resolution
import com.video.mini.tools.zip.compress.convert.simple.tiny.ui.process.ProcessActivity
import com.video.mini.tools.zip.compress.convert.simple.tiny.utils.IntentUtils.getActionMedia
import com.video.mini.tools.zip.compress.convert.simple.tiny.utils.IntentUtils.getOptionMedia
import com.video.mini.tools.zip.compress.convert.simple.tiny.utils.IntentUtils.passActionMedia
import com.video.mini.tools.zip.compress.convert.simple.tiny.utils.IntentUtils.passOptionMedia
import com.video.mini.tools.zip.compress.convert.simple.tiny.utils.Utils

class SelectCompressActivity : BaseActivity<ActivitySelectCompressBinding>() {
    private val viewModel: SelectCompressViewModel by lazy { ViewModelProvider(this)[SelectCompressViewModel::class.java] }
    private lateinit var optionCompressions: ArrayList<OptionCompression>
    private lateinit var newResolution: Resolution
    private lateinit var mediaOption: OptionMedia
    private lateinit var optionSelected: OptionCompression
    private lateinit var itemClickBefore: OptionCompression
    private lateinit var optionAdapter: OptionsCompressionAdapter
    private lateinit var itemClick: OptionCompression

    private val spacingItem = 30
    private var positionItemClickBefore: Int = 0
    private var positionItemClick: Int = 0
    private var fileSize: Long? = 0
    override fun getViewBinding() = ActivitySelectCompressBinding.inflate(layoutInflater)

    override fun initData() {
        mediaOption = intent.getOptionMedia()!!
        val resolutions = mediaOption.dataOriginal.takeIf { it.size == 1 }?.get(0)?.resolution
        viewModel.postResolutionOrigin(resolutions ?: Resolution())
        optionCompressions = ArrayList(OptionCompression.getOptionsCompression(this, resolutions))
        optionSelected = optionCompressions[0]
        itemClickBefore = optionCompressions[0]
    }

    override fun initView() {
        setupUI()
    }

    @SuppressLint("SetTextI18n")
    override fun initObserver() {
        viewModel.resolutionLiveData.observe(this) {
            newResolution = it
            itemClick.detail = newResolution.toString()
            deselectAndSelectNew()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        binding.apply {
             setUpViewPager()
            setAdapterOptions()
            compress.setOnClickListener { startCompress() }
            toolbar2.setNavigationOnClickListener { finish() }
        }
    }

    private fun startCompress() {
        val intent = Intent(this, ProcessActivity::class.java).apply {
            passOptionMedia(createOptionMedia())
            passActionMedia(intent.getActionMedia()!!)
        }
        startActivity(intent)
        finish()
    }

    private fun setUpViewPager() {
        val videoCompressAdapter = VideoCompressAdapter(this).apply {
            submitData = mediaOption.dataOriginal
        }
        binding.viewPager.adapter = videoCompressAdapter
        binding.indicator.setViewPager(binding.viewPager)
    }


    private fun setAdapterOptions() {
        optionAdapter = OptionsCompressionAdapter()
        val itemSpacingDecoration = ItemSpacingDecoration(spacingItem)
        optionAdapter.setOnClickListener { optionCompress, position ->
            positionItemClick = position
            itemClick = optionCompress
            when (optionCompress.type) {
                is OptionCompressType.Custom -> showDialogCustomResolution()
                is OptionCompressType.CustomFileSize -> showCustomFileSizeDialog()
                is OptionCompressType.AdvanceTypeOption -> showAdvancedCompression()
                else -> deselectAndSelectNew()
            }
        }
        optionAdapter.submitData = optionCompressions

        binding.rcyOption.addItemDecoration(itemSpacingDecoration)
        binding.rcyOption.adapter = optionAdapter
    }

    private fun deselectAndSelectNew() {
        if (positionItemClick != positionItemClickBefore) {
            itemClickBefore.isSelected = false
            optionAdapter.notifyItemChanged(positionItemClickBefore, itemClickBefore)
        }

        itemClick.isSelected = true
        optionAdapter.notifyItemChanged(positionItemClick)
        optionSelected = itemClick
        positionItemClickBefore = positionItemClick
        itemClickBefore = itemClick
    }

    private fun showDialogCustomResolution() {
        val choseResolutionDialogFragment = ChoseResolutionDialogFragment.newInstance()
        choseResolutionDialogFragment.show(this)
    }

    private fun showCustomFileSizeDialog() {
        CustomFileSizeDialogFragment(::handleCustomFileSize).show(
            supportFragmentManager, CustomFileSizeDialogFragment::class.simpleName
        )
    }

    private fun showAdvancedCompression() {
        AdvanceCompressionBottomSheetFragment(mediaOption.dataOriginal[0].bitrate,
            object : ChoseAdvance {
                override fun onChoseAdvance(
                    optionCompressType: OptionCompressType, bitRate: Long, frameRate: Int
                ) {
                    mediaOption.optionCompressType = optionCompressType
                    mediaOption.bitrate = bitRate
                    mediaOption.frameRate = frameRate
                    deselectAndSelectNew()
                }
            }).show(supportFragmentManager, AdvanceCompressionBottomSheetFragment::class.simpleName)
    }

    private fun handleCustomFileSize(size: Long, unit: String) {
        fileSize = if (unit == "MB") Utils.convertMBtoBit(size) else Utils.convertKBtoBit(size)
        itemClick.detail = "$size $unit"
        deselectAndSelectNew()
    }

    private fun createOptionMedia() = mediaOption.copy(
        optionCompressType = optionSelected.type,
        newResolution = if (optionSelected.type == OptionCompressType.Custom) newResolution else Resolution(),
        fileSize = fileSize!!
    )
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}
