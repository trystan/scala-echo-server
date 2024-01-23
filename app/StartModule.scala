import scala.concurrent.Future
import javax.inject._
import play.api.inject.ApplicationLifecycle
import services.HistoryRepository
import scala.concurrent.ExecutionContext
import com.google.inject.AbstractModule

// From https://stackoverflow.com/questions/14631827/how-to-execute-on-start-code-in-scala-play-framework-application

@Singleton
class ApplicationStart @Inject() (val historyRepo: HistoryRepository, lifecycle: ApplicationLifecycle)(implicit ec: ExecutionContext) {
  lifecycle.addStopHook(() => {
    Future { historyRepo.stop() }
  })

  historyRepo.start()
}

class StartModule extends AbstractModule {
  override def configure() = {
    throw new Exception("Is this getting called?")
    bind(classOf[ApplicationStart]).asEagerSingleton()
  }
}