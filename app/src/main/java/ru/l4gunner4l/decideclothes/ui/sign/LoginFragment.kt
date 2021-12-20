package ru.l4gunner4l.decideclothes.ui.sign

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import ru.l4gunner4l.decideclothes.*
import ru.l4gunner4l.decideclothes.di.CLOTHES_QUALIFIER
import ru.l4gunner4l.decideclothes.ui.AdminMainScreen
import ru.l4gunner4l.decideclothes.ui.MainScreen
import ru.l4gunner4l.decideclothes.ui.RegisterScreen
import ru.terrakok.cicerone.Router

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val router: Router by inject(named(CLOTHES_QUALIFIER))
    private val viewModel: LoginViewModel by inject<LoginViewModel>()

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, Observer(::render))
        btnLogin.setOnClickListener {
            viewModel.processUiEvent(UiEvent.LoginUser(
                emailET.text.toString().trim(),
                passwordET.text.toString().trim()
            ))
        }
        tvRegister.setOnClickListener {
            router.navigateTo(RegisterScreen())
        }
    }

    private fun render(viewState: LoginViewState) {
        when(viewState.status) {
            LOGIN_STATUS.START -> {
            }
            LOGIN_STATUS.LOGINED -> {
                router.navigateTo(AdminMainScreen(viewState.user!!))
            }
            LOGIN_STATUS.ERROR -> {
                Toast.makeText(requireContext(), "Вход не удался!\nПопробуйте войти еще раз", Toast.LENGTH_LONG).show()
            }
        }
    }
}