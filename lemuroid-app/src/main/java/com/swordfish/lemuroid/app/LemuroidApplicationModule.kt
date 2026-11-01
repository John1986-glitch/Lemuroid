        @Provides
        @PerApp
        @JvmStatic
        fun okHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build()

        @Provides
        @PerApp
        @JvmStatic
        fun retrofit(): Retrofit =
            Retrofit.Builder()
                .baseUrl("https://example.com")
                .addConverterFactory(
                    object : Converter.Factory() {
                        override fun responseBodyConverter(
                            type: Type,
                            annotations: Array<out Annotation>,
                            retrofit: Retrofit,
                        ): Converter<ResponseBody, *>? {
                            if (type == ZipInputStream::class.java) {
                                return Converter<ResponseBody, ZipInputStream> { responseBody ->
                                    ZipInputStream(responseBody.byteStream())
                                }
                            }
                            if (type == InputStream::class.java) {
                                return Converter<ResponseBody, InputStream> { responseBody ->
                                    responseBody.byteStream()
                                }
                            }
                            return null
                        }
                    },
                )
                .build()

        @Provides
        @PerApp
        @JvmStatic
        fun directoriesManager(context: Context) = DirectoriesManager(context)

        @Provides
        @PerApp
        @JvmStatic
        fun statesManager(directoriesManager: DirectoriesManager) = StatesManager(directoriesManager)

        @Provides
        @PerApp
        @JvmStatic
        fun savesManager(directoriesManager: DirectoriesManager) = SavesManager(directoriesManager)

        @Provides
        @PerApp
        @JvmStatic
        fun statesPreviewManager(directoriesManager: DirectoriesManager) = StatesPreviewManager(directoriesManager)

        @Provides
        @PerApp
        @JvmStatic
        fun coreManager(
            directoriesManager: DirectoriesManager,
            retrofit: Retrofit,
        ): CoreUpdater = CoreUpdaterImpl(directoriesManager, retrofit)

        @Provides
        @PerApp
        @JvmStatic
        fun coreVariablesManager(sharedPreferences: Lazy<SharedPreferences>) = CoreVariablesManager(sharedPreferences)

        @Provides
        @PerApp
        @JvmStatic
        fun gameLoader(
            lemuroidLibrary: LemuroidLibrary,
            statesManager: StatesManager,
            savesManager: SavesManager,
            coreVariablesManager: CoreVariablesManager,
            retrogradeDatabase: RetrogradeDatabase,
            savesCoherencyEngine: SavesCoherencyEngine,
            directoriesManager: DirectoriesManager,
            biosManager: BiosManager,
            desmumeMigrationHandler: DesmumeMigrationHandler,
        ) = GameLoader(
            lemuroidLibrary,
            statesManager,
            savesManager,
            coreVariablesManager,
            retrogradeDatabase,
            savesCoherencyEngine,
            directoriesManager,
            biosManager,
            desmumeMigrationHandler,
        )

        @Provides
        @PerApp
        @JvmStatic
        fun inputDeviceManager(
            context: Context,
            sharedPreferences: Lazy<SharedPreferences>,
        ) = InputDeviceManager(context, sharedPreferences)

        @Provides
        @PerApp
        @JvmStatic
        fun biosManager(directoriesManager: DirectoriesManager) = BiosManager(directoriesManager)

        @Provides
        @PerApp
        @JvmStatic
        fun biosPreferences(biosManager: BiosManager) = BiosPreferences(biosManager)

        @Provides
        @PerApp
        @JvmStatic
        fun coresSelection(
            sharedPreferences: Lazy<SharedPreferences>,
            desmumeMigrationHandler: DesmumeMigrationHandler,
        ) = CoresSelection(sharedPreferences, desmumeMigrationHandler)

        @Provides
        @PerApp
        @JvmStatic
        fun coreSelectionPreferences() = CoresSelectionPreferences()

        @Provides
        @PerApp
        @JvmStatic
        fun savesCoherencyEngine(
            savesManager: SavesManager,
            statesManager: StatesManager,
        ) = SavesCoherencyEngine(savesManager, statesManager)

        @Provides
        @PerApp
        @JvmStatic
        fun saveSyncManagerImpl(
            context: Context,
            directoriesManager: DirectoriesManager,
        ) = SaveSyncManagerImpl(context, directoriesManager)

        @Provides
        @PerApp
        @JvmStatic
        fun desmumeMigrationHandler(directoriesManager: DirectoriesManager) =
            DesmumeMigrationHandler(directoriesManager)

        @Provides
        @PerApp
        @JvmStatic
        fun postGameHandler(retrogradeDatabase: RetrogradeDatabase) =
            GameLaunchTaskHandler(ReviewManager(), retrogradeDatabase)

        @Provides
        @PerApp
        @JvmStatic
        fun shortcutsGenerator(
            context: Context,
            retrofit: Retrofit,
        ) = ShortcutsGenerator(context, retrofit)

        @Provides
        @PerApp
        @JvmStatic
        fun channelHandler(
            context: Context,
            retrogradeDatabase: RetrogradeDatabase,
            retrofit: Retrofit,
        ) = ChannelHandler(context, retrogradeDatabase, retrofit)

        @Provides
        @PerApp
        @JvmStatic
        fun retroControllerManager(sharedPreferences: Lazy<SharedPreferences>) =
            ControllerConfigsManager(sharedPreferences)

        @Provides
        @PerApp
        @JvmStatic
        fun settingsManager(
            context: Context,
            sharedPreferences: Lazy<SharedPreferences>,
        ) = SettingsManager(context, sharedPreferences)

        @Provides
        @PerApp
        @JvmStatic
        fun sharedPreferences(context: Context) = SharedPreferencesHelper.getSharedPreferences(context)

        @Provides
        @PerApp
        @JvmStatic
        fun gameLauncher(
            coresSelection: CoresSelection,
            gameLaunchTaskHandler: GameLaunchTaskHandler,
        ) = GameLauncher(coresSelection, gameLaunchTaskHandler)

        @Provides
        @PerApp
        @JvmStatic
        fun rumbleManager(
            context: Context,
            settingsManager: SettingsManager,
            inputDeviceManager: InputDeviceManager,
        ) = RumbleManager(context, settingsManager, inputDeviceManager)
    }
}
