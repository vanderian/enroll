package sk.vander.enroll.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import sk.vander.enroll.db.dao.PersonDao
import sk.vander.enroll.db.entity.Person
import sk.vander.lib.ui.viewmodel.ActivityResult
import sk.vander.lib.ui.viewmodel.BaseViewModel
import sk.vander.lib.ui.viewmodel.GoBack
import sk.vander.lib.ui.viewmodel.ToActivityResult
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author marian on 24.9.2017.
 */
class PersonCreateViewModel @Inject constructor(
    private val personDao: PersonDao,
    private val rxLocation: RxLocation,
    private val uuid: UUID
) : BaseViewModel<CreateState, CreateIntents>(CreateState()) {
  var photoState: PhotoState? = null

  private val locationRequest: LocationRequest = LocationRequest.create()
      .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
      .setNumUpdates(1)
      .setMaxWaitTime(TimeUnit.SECONDS.toMillis(5))

  @SuppressLint("MissingPermission")
  override fun collectIntents(intents: CreateIntents, activityResult: Observable<ActivityResult>): Disposable {
    val name = intents.name().share()
    val surname = intents.surname().share()

    val states = Observable.merge<CreateState>(listOf(
        name.map { state.value.copy(name = it.text) },
        surname.map { state.value.copy(surname = it.text) },
        intents.date().map { state.value.copy(date = it.text) },
        name.map { it.text.isNullOrEmpty() }
            .zipWith(surname.map { it.text.isNullOrEmpty() }, { n: Boolean, s: Boolean -> !n && !s })
            .map { state.value.copy(saveEnabled = it) },
        activityResult
            .filter { it.request == REQUEST_CODE_CROP }
            .map { photoState?.let { state.value.copy(photo = it.uri) } ?: state.value }
    )).doOnNext { if (state.value != it) state.onNext(it) }

    val navi = Observable.merge(
        intents.takePhoto().map {
          photoState?.file?.delete()
          photoState = PhotoState(it.first, it.second)
          ToActivityResult(
              Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply { putExtra(MediaStore.EXTRA_OUTPUT, it.second) },
              REQUEST_CODE_CAMERA)
        },
        activityResult.filter { it.result == Activity.RESULT_OK && it.request == REQUEST_CODE_CAMERA }
            .map { ToActivityResult(cropIntent(photoState!!.uri), REQUEST_CODE_CROP) },
        intents.save()
            .doOnNext { state.onNext(state.value.copy(loading = true)) }
            .flatMapSingle {
              rxLocation.settings().checkAndHandleResolution(locationRequest)
                  .flatMap { rxLocation.location().updates(locationRequest).firstOrError() }
                  .map { Person(state.value, it, uuid) }
                  .flatMap { Single.fromCallable { personDao.insert(it) }.subscribeOn(Schedulers.io()) }
                  .observeOn(AndroidSchedulers.mainThread())
                  .map { GoBack }
            }
    )

    return Observable.merge(
        states.doOnNext { if (state.value != it) state.onNext(it) },
        navi.doOnNext { navigation.onNext(it) },
        activityResult.filter { it.result == Activity.RESULT_CANCELED &&  it.request == REQUEST_CODE_CAMERA}
            .doOnNext { photoState = photoState?.let { it.file.delete(); null } }
    ).subscribe()
  }

  data class PhotoState(val file: File, val uri: Uri)

  companion object {
    private const val REQUEST_CODE_CAMERA = 1
    private const val REQUEST_CODE_CROP = 2
    private const val DEF_SIZE = 720
    private const val AUTHORITY = "sk.vander.enroll.take_photo"
    private const val PATH = "person_photo"

    private fun cropIntent(uri: Uri, size: Int = DEF_SIZE): Intent =
        Intent("com.android.camera.action.CROP").apply {
          setDataAndType(uri, "image/*")
          putExtra(MediaStore.EXTRA_OUTPUT, uri)
//          putExtra("crop", true)
//          putExtra("scale", true)
//          putExtra("scaleUpIfNeeded", true)
          putExtra("aspectX", 4)
          putExtra("aspectY", 3)
//          putExtra("outputX", size)
//          putExtra("outputY", size)
          addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
          addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

    fun createFiles(context: Context): Pair<File, Uri> {
      val file = File(File(context.getExternalFilesDir(null), PATH).apply { mkdirs() },
          "${System.currentTimeMillis()}.jpeg")
      val uri = FileProvider.getUriForFile(context, AUTHORITY, file)
      return Pair(file, uri)
    }
  }

  class PhotoFileProvider : FileProvider()

}
