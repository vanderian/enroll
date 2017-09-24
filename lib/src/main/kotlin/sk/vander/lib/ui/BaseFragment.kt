package sk.vander.lib.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.Unbinder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import sk.vander.lib.Injectable
import sk.vander.lib.R
import sk.vander.lib.ui.viewmodel.*
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * @author marian on 20.9.2017.
 */
abstract class BaseFragment<T : BaseViewModel<S>, S: ViewState>(private val clazz: KClass<T>) : Fragment(), Injectable {
  private val activityResult = BehaviorSubject.create<ActivityResult>()
  private lateinit var unbinder: Unbinder
  protected val disposable = CompositeDisposable()
  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
  lateinit var viewModel: T

  @LayoutRes abstract fun layout(): Int
  abstract fun intents(): List<Observable<out ViewEvent>>
  abstract fun render(state: S)

  private fun navigate(navigation: Navigation) {
    when (navigation) {
      is GoBack -> activity.onBackPressed()
      is ToFragment -> activity.supportFragmentManager.beginTransaction()
          .replace(R.id.container_id, navigation.fragment)
          .addToBackStack("")
          .commit()
      is ToActivityResult ->
        if (navigation.intent.resolveActivity(context.packageManager) != null) {
          startActivityForResult(navigation.intent, navigation.requestCode)
        } else {
          Toast.makeText(context, context.getString(R.string.no_app_error, navigation.intent), Toast.LENGTH_SHORT).show()
          activityResult.onNext(ActivityResult(navigation.requestCode))
        }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory)[clazz.java]
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
      inflater.inflate(layout(), container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    unbinder = ButterKnife.bind(this, view)
  }

  override fun onStart() {
    super.onStart()
    disposable.addAll(
        viewModel.bindIntents(intents().plus(activityResult)),
        viewModel.state.subscribe { render(it) },
        viewModel.navigation.subscribe { navigate(it) }
    )
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    activityResult.onNext(ActivityResult(requestCode, resultCode, data))
  }

  override fun onStop() {
    disposable.clear()
    super.onStop()
  }
}