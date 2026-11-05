package com.example.catalogone.data.repository

import com.example.catalogone.data.mapper.toDto
import com.example.catalogone.data.mapper.toDomain
import com.example.catalogone.data.remote.CatalogApiService
import com.example.catalogone.domain.model.Enquiry
import com.example.catalogone.domain.model.EnquiryResult
import com.example.catalogone.domain.repository.EnquiryRepository
import retrofit2.HttpException
import javax.inject.Inject
import org.json.JSONObject

class EnquiryRepositoryImpl @Inject constructor(
    private val apiService: CatalogApiService
) : EnquiryRepository {

    override suspend fun submitEnquiry(enquiry: Enquiry): Result<EnquiryResult> {
        return runCatching {
            apiService.submitEnquiry(enquiry.toDto()).toDomain()
        }.recoverCatching { throwable ->
            if (throwable is HttpException) {
                val errorBody = throwable.response()?.errorBody()?.string()
                if (!errorBody.isNullOrBlank()) {
                    val message = runCatching { JSONObject(errorBody).getString("error") }.getOrNull()
                    if (!message.isNullOrBlank()) {
                        throw Exception(message)
                    }
                }
            }
            throw throwable
        }
    }
}
