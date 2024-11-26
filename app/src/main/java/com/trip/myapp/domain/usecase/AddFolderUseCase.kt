package com.trip.myapp.domain.usecase

import com.trip.myapp.data.model.Folder
import com.trip.myapp.data.repository.FolderRepository
import javax.inject.Inject

class AddFolderUseCase @Inject constructor(
    private val folderRepository: FolderRepository
) {
    suspend operator fun invoke(
        id: String,
        title: String,
        author: String,
        image: Int?
    ) : Boolean {
        return folderRepository.addFolder(
            folder = Folder(
                id = id,
                title = title,
                author = author,
                image = image
            )
        )
    }
}