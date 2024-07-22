package com.tearas.myapplication.usecase.media

data class MediaUseCases(
    val scanAllImage: ScanAllImage,
    val scanAllDocument: ScanAllDocument,
    val scanAllIVideo: ScanAllIVideo,
    val scanAllAudio: ScanAllAudio,
    val getMediaFromFile: GetMediaFromFile,
    val scanFilesDuplicated: ScanFilesDuplicated,
    val deleteMedia: DeleteMedia,
    val recoveryMedia: RecoveryMedia,
    val getFilesRecoverSaved: GetFilesRecoverSaved,
    val sweepFilesJunk: SweepFilesJunk
)