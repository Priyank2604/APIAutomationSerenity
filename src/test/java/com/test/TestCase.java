package com.test;

import com.methods.ApiMethods;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //include the @FixMethodOrder annotation.
@RunWith(SerenityRunner.class)
public class TestCase {

    @Steps
    ApiMethods apiMethods;


    @Test
    public void A_generatingAccessToken(){
        apiMethods.generatetoken();
    }

    @Test
    public void B_signup(){
        apiMethods.signup();
    }

    @Test
    public void C1_otpVerify(){
        apiMethods.otpVerify();
    }


//    @Test
//    public void test4_subscription(){
//        apiMethods.subscription();
//    }

    @Test
    public void D1_payment(){
        apiMethods.payment();
    }
    @Test
    public void D2_otpPhoneVerify(){
        apiMethods.otpPhoneVerify();
    }

    @Test
    public void D3_loginAccess(){
        apiMethods.loginAccess();
    }

    @Test
    public void E1_company_details(){
        apiMethods.company_details();
    }
    @Test
    public void E2_addCompany(){
        apiMethods.addCompany();
    }

    @Test
    public void E3_company_details1(){
        apiMethods.company_details1();
    }

    @Test
    public void F_addClinic(){
        apiMethods.addClinic();
    }

    @Test
    public void G_clinicSpeciality(){
        apiMethods.clinicSpeciality();
    }

    @Test
    public void H_clinicSchedule(){
        apiMethods.clinicSchedule();
    }

    @Test
    public void I_addUser(){
        apiMethods.addUser();
    }

    @Test
    public void J_addstaff(){
        apiMethods.addstaff();
    }

    @Test
    public void K_addInventory(){
        apiMethods.addInventory();
    }

    @Test
    public void L_addServiceCode(){
        apiMethods.addServiceCode();
    }

    @Test
    public void M_addOwner(){
        apiMethods.addOwner();
    }

    @Test
    public void N_addPet(){
        apiMethods.addPet();
    }

    @Test
    public void O_addAppointment(){
        apiMethods.addAppointment();
    }

    @Test
    public void P_addEvent(){
        apiMethods.addEvent();
    }

    @Test
    public void Q_getEvent(){
        apiMethods.getEvent();
    }
}
