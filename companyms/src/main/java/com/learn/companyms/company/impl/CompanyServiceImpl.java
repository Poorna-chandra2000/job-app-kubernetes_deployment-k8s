package com.learn.companyms.company.impl;



import com.learn.companyms.company.Company;
import com.learn.companyms.company.CompanyRepository;
import com.learn.companyms.company.CompanyService;

import org.springframework.stereotype.Service;


import java.util.List;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public boolean updateCompany(Company company, Long id) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company companyToUpdate = companyOptional.get();
            companyToUpdate.setDescription(company.getDescription());
            companyToUpdate.setName(company.getName());
//            companyToUpdate.setJobsId(company.getJobsId());
            companyRepository.save(companyToUpdate);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void createCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public boolean deleteCompanyById(Long id) {
        if(companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public Company updateAvgRating(Long companyId, double rating) {
        Company company=companyRepository.findById(companyId).orElseThrow(()->new RuntimeException("Company not found"));
        Company Updaterating=Company.builder()
                .id(companyId)
                .name(company.getName())
                .description(company.getDescription())
                .avgRating(rating).build();

        return companyRepository.save(Updaterating);
    }


}
