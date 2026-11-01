    @ContributesAndroidInjector(modules = [GamePadBindingActivity.Module::class])
    abstract fun gamepadBindingActivity(): GamePadBindingActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [GamePadShortcutBindingActivity.Module::class])
    abstract fun gamepadShortcutBindingActivity(): GamePadShortcutBindingActivity

    @Module
    companion object {
        @Provides
        @PerApp
        @JvmStatic
        fun customCoverManager(context: Context) = CustomCoverManager(context)

        @Provides
        @PerApp
        @JvmStatic
        fun libretroDBManager(app: LemuroidApplication) = LibretroDBManager(app)

        @Provides
        @PerApp
        @JvmStatic
        fun retrogradeDb(app: LemuroidApplication) =
            Room.databaseBuilder(app, RetrogradeDatabase::class.java, RetrogradeDatabase.DB_NAME)
                .addCallback(GameSearchDao.CALLBACK)
                .addMigrations(GameSearchDao.MIGRATION, Migrations.VERSION_8_9, Migrations.VERSION_9_10)
                .fallbackToDestructiveMigration()
                .build()

        @Provides
        @PerApp
        @JvmStatic
        fun gameMetadataProvider(libretroDBManager: LibretroDBManager): GameMetadataProvider =
            LibretroDBMetadataProvider(libretroDBManager)

        @Provides
        @PerApp
        @IntoSet
        @JvmStatic
        fun localSAFStorageProvider(context: Context): StorageProvider = StorageAccessFrameworkProvider(context)

        @Provides
        @PerApp
        @IntoSet
        @JvmStatic
        fun localGameStorageProvider(
            context: Context,
            directoriesManager: DirectoriesManager,
        ): StorageProvider = LocalStorageProvider(context, directoriesManager)

        @Provides
        @PerApp
        @JvmStatic
        fun gameStorageProviderRegistry(
            context: Context,
            providers: Set<@JvmSuppressWildcards StorageProvider>,
        ) = StorageProviderRegistry(context, providers)

        @Provides
        @PerApp
        @JvmStatic
        fun lemuroidLibrary(
            db: RetrogradeDatabase,
            storageProviderRegistry: Lazy<StorageProviderRegistry>,
            gameMetadataProvider: Lazy<GameMetadataProvider>,
            biosManager: BiosManager,
            customCoverManager: CustomCoverManager,
        ) = LemuroidLibrary(db, storageProviderRegistry, gameMetadataProvider, biosManager, customCoverManager)
