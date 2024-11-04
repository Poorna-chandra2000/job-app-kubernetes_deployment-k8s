package com.learn.companyms.company;

import java.util.List;
import java.util.Map;

public interface CompanyService {
    List<Company> getAllCompanies();
    boolean updateCompany(Company company, Long id);
    void createCompany(Company company);
    boolean deleteCompanyById(Long id);
    Company getCompanyById(Long id);

    Company updateAvgRating(Long companyId, double rating);
}
