package ru.l4gunner4l.decideclothes.ui.admin.brands.details

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_admin_details_brand.*
import kotlinx.android.synthetic.main.fragment_admin_details_brand.btnBack
import kotlinx.android.synthetic.main.fragment_admin_details_brand.btnSave
import kotlinx.android.synthetic.main.fragment_admin_details_brand.imageIV
import kotlinx.android.synthetic.main.fragment_admin_details_brand.nameET
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import ru.l4gunner4l.decideclothes.R
import ru.l4gunner4l.decideclothes.base.STATUS
import ru.l4gunner4l.decideclothes.local.entity.BrandEntity
import ru.l4gunner4l.decideclothes.ui.admin.brands.UiEvent
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DetailsBrandFragment : Fragment(R.layout.fragment_admin_details_brand) {

    companion object {
        private const val KEY_BRAND = "KEY_BRAND"
        private const val REQUEST_TAKE_IMAGE_FROM_CAMERA = 123
        private const val REQUEST_TAKE_IMAGE_FROM_STORAGE = 124
        fun newInstance(brand: BrandEntity?) =
            DetailsBrandFragment().apply {
                arguments = bundleOf(KEY_BRAND to brand)
                isEditMode = brand != null
                photoPath = brand?.image ?: ""
            }
    }

    private val viewModel: DetailsBrandViewModel by inject() {
        parametersOf(requireArguments().getParcelable<BrandEntity>(KEY_BRAND))
    }
    private var isEditMode = false
    private lateinit var photoPath: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.viewState.observe(viewLifecycleOwner, ::render)
    }

    private fun render(viewState: BrandViewState) {
        when (viewState.status) {
            STATUS.CONTENT -> {
                if (isEditMode)
                    with(viewState.brand!!) {
                        nameET.setText(name)
                        Glide.with(requireView())
                            .load(image)
                            .error(R.drawable.ic_error_24)
                            .into(imageIV)
                    }
            }
        }
    }

    private fun initView() {
        labelBrandMode.text = if (isEditMode) "Изменение бренда" else "Добавляем бренд"
        btnSave.setOnClickListener {
            viewModel.processUiEvent(
                if (isEditMode)
                    UiEvent.SaveClick(
                        brand = BrandEntity(
                            id = requireArguments().getParcelable<BrandEntity>(KEY_BRAND)!!.id,
                            name = nameET.text.toString(),
                            image = photoPath
                        )
                    )
                else UiEvent.SaveClick(
                    brand = BrandEntity(
                        name = nameET.text.toString(),
                        image = photoPath
                    )
                )
            )
        }
        takeImageBtn.setOnClickListener { showTakePhotoDialog() }
        btnBack.setOnClickListener { viewModel.processUiEvent(UiEvent.ExitClick) }
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
                    startActivityForResult(
                        takePictureIntent,
                        REQUEST_TAKE_IMAGE_FROM_CAMERA
                    )
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