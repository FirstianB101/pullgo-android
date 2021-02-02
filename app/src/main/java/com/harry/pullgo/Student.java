package com.harry.pullgo;

public class Student {
    private Account account;
    private String parentPhone;
    private String schoolName;
    private int schoolYear;
    //private Set<Academy> academies;
    //private Set<Classroom> classrooms;
    //private Set<Academy> appliedAcademies;
    //private Set<Classroom> appliedClassrooms;
    //private Set<AttenderState> attendingStates;

    public void setAccount(Account account){
        this.account=new Account(account);
    }
    /*
    public void applyAcademy(Academy academy){
    }

    public void removeAppliedAcademy(Academy academy){
    }

    public void applyClassroom(Classroom classroom){
    }

    public void removeAppliedClassroom(Classroom classroom){
    }

    public void attend(AttenderState state){
    }
    */
}
