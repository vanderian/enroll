package sk.vander.enroll

import autodagger.AutoComponent
import sk.vander.enroll.db.DataModule
import sk.vander.enroll.ui.ViewModelsModule

@AutoComponent(
    modules = arrayOf(
        InjectorFactoriesModules.Activities::class,
        ViewModelsModule::class,
        DataModule::class
    )
)
annotation class MainComponent