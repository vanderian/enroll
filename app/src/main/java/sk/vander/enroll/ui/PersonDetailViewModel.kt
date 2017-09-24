package sk.vander.enroll.ui

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.io.File
import javax.inject.Inject

/**
 * @author marian on 24.9.2017.
 */
class PersonDetailViewModel @Inject constructor() : ViewModel() {
  val state: BehaviorSubject<DetailState> = BehaviorSubject.createDefault(DetailState())
  val navigation: PublishSubject<Navigation> = PublishSubject.create()
  var photoState: PhotoState? = null

  fun bindIntents(intents: List<Observable<out ViewEvent>>): Disposable =
      Observable.merge(intents)
          .flatMapCompletable { handleEvent(it) }
          .subscribe()

  private fun handleEvent(event: ViewEvent): Completable {
    val newState = when {
      event is EventName -> state.value.copy(name = event.text)
      event is EventSurname -> state.value.copy(surname = event.text)
      event is EventDate -> state.value.copy(date = event.text)
      event is EventHasText -> state.value.copy(saveEnabled = event.has)
      event is ActivityResult && event.request == REQUEST_CODE_CROP ->
        photoState?.let { state.value.copy(photo = it.uri) } ?: state.value
      else -> state.value
    }
    if (state.value != newState) state.onNext(newState)

    when {
      event is EventFiles -> {
        photoState?.file?.delete()
        photoState = PhotoState(event.file, event.uri)
        navigation.onNext(ToActivityResult(
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply { putExtra(MediaStore.EXTRA_OUTPUT, event.uri) },
            REQUEST_CODE_CAMERA))
      }
      event is ActivityResult && event.result == Activity.RESULT_OK && event.request == REQUEST_CODE_CAMERA ->
        photoState?.let { navigation.onNext(ToActivityResult(cropIntent(it.uri), REQUEST_CODE_CROP)) }

      event is ActivityResult && event.result == Activity.RESULT_CANCELED && event.request == REQUEST_CODE_CAMERA ->
        photoState = photoState?.let { it.file.delete(); null }
    }
    return Completable.complete()
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
          putExtra("crop", true)
          putExtra("scale", true)
          putExtra("scaleUpIfNeeded", true)
          putExtra("aspectX", 4)
          putExtra("aspectY", 3)
          putExtra("outputX", size)
          putExtra("outputY", size)
          addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
          addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

    fun createFiles(context: Context): EventFiles {
      val file = File(File(context.getExternalFilesDir(null), PATH).apply { mkdirs() },
          "${System.currentTimeMillis()}.jpeg")
      val uri = FileProvider.getUriForFile(context, AUTHORITY, file)
      return EventFiles(file, uri)
    }
  }

  class PhotoFileProvider : FileProvider()

}
