package com.example.catalogone.domain.repository

import com.example.catalogone.domain.model.Enquiry
import com.example.catalogone.domain.model.EnquiryResult

interface EnquiryRepository {
    suspend fun submitEnquiry(enquiry: Enquiry): Result<EnquiryResult>
}
