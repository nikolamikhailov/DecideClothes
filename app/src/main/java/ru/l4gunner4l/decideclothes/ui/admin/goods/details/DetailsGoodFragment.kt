package ru.l4gunner4l.decideclothes.ui.admin.goods.details

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_admin_details_good.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import ru.l4gunner4l.decideclothes.R
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.local.entity.Good
import ru.l4gunner4l.decideclothes.ui.admin.goods.DataEvent
import ru.l4gunner4l.decideclothes.ui.admin.goods.UiEvent
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DetailsGoodFragment : Fragment(R.layout.fragment_admin_details_good) {

    companion object {
        private const val KEY_GOOD = "KEY_GOOD"
        private const val REQUEST_TAKE_IMAGE_FROM_CAMERA = 123
        private const val REQUEST_TAKE_IMAGE_FROM_STORAGE = 124
        fun newInstance(good: Good?): DetailsGoodFragment {
            return DetailsGoodFragment().apply {
                arguments = bundleOf(KEY_GOOD to good)
                isEditMode = good != null
                photoPath = good?.image ?: ""
            }
        }
    }

    private val viewModel: DetailsGoodViewModel by inject() {
        parametersOf(requireArguments().getParcelable<Good>(KEY_GOOD))
    }
    private var isEditMode = false
    private lateinit var photoPath: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, ::render)
        viewModel.brands.observe(viewLifecycleOwner, { list ->
            brandSpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                list.map { it.name }
            ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            brandSpinner.setSelection(
                list.map { it.id }
                    .indexOf(viewModel.viewState.value?.good?.brand?.id ?: 0)
            )
        })
        viewModel.kinds.observe(viewLifecycleOwner, { list ->
            kindSpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                list.map { it.name }
            ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
            kindSpinner.setSelection(
                list.map { it.id }
                    .indexOf(viewModel.viewState.value?.good?.kind?.id ?: 0)
            )
        })
        viewModel.processDataEvent(DataEvent.InfoRequest)
        initView()
    }

    private fun render(viewState: GoodViewState) {
        when (viewState.status) {
            STATUS.CONTENT -> {
                if (isEditMode) {
                    with(viewState.good!!) {
                        nameET.setText(name)
                        priceET.setText(price.toString())
                        Glide.with(requireView())
                            .load(image)
                            .error(R.drawable.ic_error_24)
                            .into(imageIV)
                    }
                }
            }
        }
    }

    private fun initView() {
        labelGoodMode.text = if (isEditMode) "Изменение товара" else "Добавление товара"
        btnSave.setOnClickListener {
            viewModel.processUiEvent(
                UiEvent.SaveClick(
                    isEdit = isEditMode,
                    name = nameET.text.toString(),
                    kindPos = kindSpinner.selectedItemPosition,
                    brandPos = brandSpinner.selectedItemPosition,
                    price = priceET.text.toString().toInt(),
                    image = photoPath
                )
            )
        }
        takeImageBtn.setOnClickListener { showTakePhotoDialog() }
        btnBack.setOnClickListener {
            viewModel.processUiEvent(UiEvent.ExitClick)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode != REQUEST_TAKE_IMAGE_FROM_CAMERA && requestCode != REQUEST_TAKE_IMAGE_FROM_STORAGE) || resultCode != Activity.RESULT_OK || data == null) return
        if (requestCode == REQUEST_TAKE_IMAGE_FROM_CAMERA) updatePhoto()
        if (requestCode == REQUEST_TAKE_IMAGE_FROM_STORAGE) updatePhoto(data.data)
    }

    private fun showTakePhotoDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Where from you want to take photo?")
            setPositiveButton("Camera") { _, _ -> takePhotoFromCamera() }
            setNeutralButton("Storage") { _, _ -> takePhotoFromStorage() }
        }.show()
    }

    private fun takePhotoFromCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(), "ru.l4gunner4l.decideclothes.fileprovider", it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_IMAGE_FROM_CAMERA)
                }
            }
        }
    }

    private fun takePhotoFromStorage() {
        Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
            .let { intent ->
                intent.resolveActivity(requireActivity().packageManager)?.let {
                    startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        REQUEST_TAKE_IMAGE_FROM_STORAGE
                    )
                }
            }
    }

    private fun updatePhoto() {
        Glide.with(requireContext())
            .load(photoPath)
            .error(R.drawable.ic_error_24)
            .into(imageIV)
    }

    private fun updatePhoto(image: Uri?) {
        photoPath = image?.toString() ?: ""
        Glide.with(requireContext())
            .load(image)
            .error(R.drawable.ic_error_24)
            .into(imageIV)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            photoPath = absolutePath
        }
    }
}