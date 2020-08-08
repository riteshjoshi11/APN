package com.ANP.repository;

import com.ANP.bean.ListParametersBean;
import com.ANP.bean.PhonebookBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PhonebookDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    /*
         Returns PhonebookBean
         Please note this method mainly builds phonebookBean.phonebookContacts by reading the two backend tables.
     */
    public PhonebookBean listPhonebookPaged(ListParametersBean parametersBean) {
        return null;
    }

    /*
     * This method should create/update entry into phonebook table
     * Also it will iterate through list  'phonebookBean.phonebookContacts' and
     * Creates correponding phonebook_contact (Please note you will only receive contact_name,key=email,value,
     * set isGlobal=False default
     * Note this is not one time sync - user can sync multiple times.
     * So basically you need to look for
     *
    */
    public void syncPhonebook(PhonebookBean phonebookBean) {
    }
}
