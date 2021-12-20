package ru.l4gunner4l.decideclothes.ui.sign

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import ru.l4gunner4l.decideclothes.di.CLOTHES_QUALIFIER
import ru.l4gunner4l.decideclothes.R
import ru.terrakok.cicerone.Router

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val router: Router by inject(named(CLOTHES_QUALIFIER))
    private val viewModel: RegisterViewModel by inject<RegisterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, Observer(::render))
        btnRegister.setOnClickListener {
            val email = emailET.text.toString().trim()
            val password = passwordET.text.toString().trim()
            viewModel.processUiEvent(UiEvent.CreateUser(email, password))
        }
        btnBack.setOnClickListener { back() }
    }

    private fun render(viewState: RegisterViewState) {
        when(viewState.status) {
            REGISTER_STATUS.START -> {}
            REGISTER_STATUS.REGISTERED -> {
                Toast.makeText(requireContext(), "Вы зарегистрированы!", Toast.LENGTH_LONG).show()
            }
            REGISTER_STATUS.ERROR -> {}
        }
    }

    private fun back() {
        router.exit()
    }

}